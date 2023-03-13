package com.example.smartbox_dup.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends StringRequest {
    private static final String BOUNDARY = "----Boundary" + System.currentTimeMillis();
    private static final String LINE_FEED = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data; boundary=" + BOUNDARY;
    private final Map<String, Object> mParams;
    private final Map<String, File> mFileParams;
    private final Context mContext;

    public MultipartRequest(Context context, int method, String url, Map<String, Object> params,
                            Map<String, File> fileParams, Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        mContext = context;
        mParams = params;
        mFileParams = fileParams;
    }

    @Override
    public String getBodyContentType() {
        return CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // Add params
            for (Map.Entry<String, Object> entry : mParams.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                outputStream.write(getParamString(key, value).getBytes());
            }

            // Add files
            for (Map.Entry<String, File> entry : mFileParams.entrySet()) {
                String key = entry.getKey();
                File file = entry.getValue();
                String mimeType = mContext.getContentResolver().getType(Uri.fromFile(file));
                byte[] fileBytes = getFileBytes(file);
                outputStream.write(getFileString(key, file.getName(), mimeType).getBytes());
                outputStream.write(fileBytes);
                outputStream.write(LINE_FEED.getBytes());
            }

            // Add boundary end
            outputStream.write(("--" + BOUNDARY + "--").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(json, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    private byte[] getFileBytes(File file) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private String getParamString(String key, Object value) {
        return "--" + BOUNDARY + LINE_FEED +
                "Content-Disposition: form-data; name=\"" + key + "\"" + LINE_FEED + LINE_FEED +
                value + LINE_FEED;
    }

    private String getFileString(String key, String fileName, String mimeType) {
        return "--" + BOUNDARY + LINE_FEED +
                "Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"" + LINE_FEED +
                "Content-Type: " + mimeType + LINE_FEED + LINE_FEED;
    }
}