package com.example.smartbox_dup.utils;

import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class LogUpload {

    String host;
    int port;
    String id;
    String pw;

    public LogUpload(String host, int port, String id, String pw) {
        this.host = host;
        this.port = port;
        this.id = id;
        this.pw = pw;
    }
    public LogUpload() {
        host = "ftp-pcms.kworks.co.kr";
        port = 9999;
        id = "pcms2022";
        pw = "pcms2022!5000";
    }

    private void fileUpload(String localFilePath, String destinationFileName) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(host, port);
            ftpClient.login(id, pw);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.changeWorkingDirectory("/ROOT/log_file/vcms2/");
            ftpClient.enterLocalPassiveMode();

            File localFile = new File(localFilePath);
            String remoteFile = destinationFileName + ".zip";
            InputStream inputStream = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                inputStream = Files.newInputStream(localFile.toPath());
            }

            System.out.println("Start uploading first file");
            boolean done = ftpClient.storeFile(remoteFile, inputStream);
            inputStream.close();
            if (done) {
                // 업로드 완료
                Log.i("this", "end Upload");
                localFile.delete(); // zip파일 삭제
            }
        } catch (IOException ex) {
            Log.e("this", "LogUpload - IOException", ex);
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    public void zipAndUpload(String folderParentPath, String folderName, String destinationFileName) {
        try {
            String srcPath = folderParentPath + File.separator + folderName;


            File targetDir = new File(srcPath);
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            File[] files = targetDir.listFiles();
            boolean fileExist = false;
            for (File file : files) {
                Log.i("this", "file: " + file.getName());
                //txt 파일 제외 파일은 모두 삭제
                if (file.getName().endsWith("txt")) {
                    fileExist = true;
                } else {
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }


            String outZipPath = "";

            if (fileExist) {
                outZipPath = folderParentPath + File.separator + "local_log_zip" + ".zip";

                // zip folder
                File sourceFile = new File(srcPath);
                if (!sourceFile.isFile() && !sourceFile.isDirectory()) {
                    throw new Exception("압축 대상의 파일을 찾을 수가 없습니다.");
                }
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                ZipOutputStream zos = null;

                try {
                    fos = new FileOutputStream(outZipPath); // FileOutputStream
                    bos = new BufferedOutputStream(fos); // BufferedStream
                    zos = new ZipOutputStream(bos); // ZipOutputStream
                    zos.setLevel(8); // 압축 레벨 - 최대 압축률은 9, 디폴트 8

                    zipEntry(sourceFile, srcPath, zos); // Zip 파일 생성
                    zos.finish(); // ZipOutputStream finish
                } finally {
                    if (zos != null) {
                        zos.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                }

                // 압축 된 파일 업로드
                String finalOutZipPath = outZipPath;
                new Thread() {
                    @Override
                    public void run() {
                        fileUpload(finalOutZipPath, destinationFileName);
                        deleteFolder(srcPath);
                    }}.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void zipEntry(File sourceFile, String sourcePath, ZipOutputStream zos) throws Exception {
        int BUFFER_SIZE = 1024*2;
        // sourceFile 이 디렉토리인 경우 하위 파일 리스트 가져와 재귀호출
        if (sourceFile.isDirectory()) {
            if (sourceFile.getName().equalsIgnoreCase(".metadata")) { // .metadata 디렉토리 return
                return;
            }
            File[] fileArray = sourceFile.listFiles(); // sourceFile 의 하위 파일 리스트
            Log.i("this", "file count: " + fileArray.length);
            for (int i = 0; i < fileArray.length; i++) {
                zipEntry(fileArray[i], sourcePath, zos); // 재귀 호출
            }
        } else { // sourcehFile 이 디렉토리가 아닌 경우
            BufferedInputStream bis = null;
            try {
                String sFilePath = sourceFile.getPath();
                StringTokenizer tok = new StringTokenizer(sFilePath, "/");

                int tok_len = tok.countTokens();
                String zipEntryName = tok.toString();
                while (tok_len != 0) {
                    tok_len--;
                    zipEntryName = tok.nextToken();
                }
                bis = new BufferedInputStream(new FileInputStream(sourceFile));

                ZipEntry zentry = new ZipEntry(zipEntryName);
                zentry.setTime(sourceFile.lastModified());
                zos.putNextEntry(zentry);

                byte[] buffer = new byte[BUFFER_SIZE];
                int cnt = 0;

                while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                    zos.write(buffer, 0, cnt);
                }
                zos.closeEntry();
            } finally {
                if (bis != null) {
                    bis.close();
                }
            }
        }
    }

    private void deleteFolder(String path) {
        try {
            File dir = new File(path);
            if (dir.exists()) {
                File[] deleteFolderList = dir.listFiles();
                for (File childFile : deleteFolderList) {
                    if (childFile.isFile()) {
                        childFile.delete();
                    } else {
                        deleteFolder(childFile.getAbsolutePath());
                    }
                    childFile.delete();
                }
                dir.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}