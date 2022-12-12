package com.example.smartbox_dup.screen.function.socket;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPClient {
    Socket socket;
    String address;
    byte[] bytes;
    String message;
    int port;
    OutputStream os;
    DataOutputStream outStream;
    InputStream is;
    DataInputStream inStream;

    public TCPClient(String _address, int _port) {
        socket = new Socket();
        this.address = _address;
        this.port = _port;
    }

    public void connect() {
        Thread socketThread = new Thread(){
            @Override
            public void run() {
                try {
                    socket.connect(new InetSocketAddress(address, port));
                    os = socket.getOutputStream();
                    outStream = new DataOutputStream(os);
                    is = socket.getInputStream();
                    inStream = new DataInputStream(is);


                    String msg = "hello";
                    outStream.writeInt(msg.getBytes().length);
                    outStream.write(msg.getBytes());
                    outStream.flush();
                    Log.i("this", "endendend");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        socketThread.start();
    }


    public void close() {
        try {
            os.close();
            is.close();
            socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendMessage(String msg) {
        try {
            outStream.writeInt(msg.getBytes().length);
            outStream.write(msg.getBytes());
            outStream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}