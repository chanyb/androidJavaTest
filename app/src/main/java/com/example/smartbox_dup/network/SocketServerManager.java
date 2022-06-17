package com.example.smartbox_dup.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class SocketServerManager {
    private static SocketServerManager instance = new SocketServerManager();
    private String serverIp = "192.168.0.215";
    private int portNo = 8000;
    private Socket server = new Socket();
    private SocketAddress socketAddress = new InetSocketAddress(serverIp, portNo);
    private PrintWriter out;
    private BufferedReader in;
    private SocketServerManager() {
        connect();
    }

    public void connect() {
        if(server.isClosed()) Log.i("this", "server is closed.");
        if(!server.isConnected()) {
            try {
                server = new Socket();
                server.connect(socketAddress, 5000);
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(server.getOutputStream())), true);
                in = new BufferedReader(new InputStreamReader(server.getInputStream()));

                Log.i("this", in.readLine());
            } catch (IOException e) {
                Log.i("this", "error", e);
                e.printStackTrace();
            }
        }
    }


    public static SocketServerManager getInstance() {
        return instance;
    }

    public void sendMessage(String _msg) {
        Log.i("this", String.valueOf(server.isClosed()));
        Log.i("this", String.valueOf(server.isConnected()));
        if(!server.isConnected()) {
            Log.i("this", "disconnected with server");
        } else {
            out.println(_msg);
        }
    }

    public String getMessage() {
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "getMessage error";
    }
}
