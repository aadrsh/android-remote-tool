package com.android.gyroscope.classes;

import android.app.Activity;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.List;

public class MySocket {
    ServerSocket serverSocket;
    Socket socket;
    PrintWriter writer;
    static String STATUS;
    static boolean SocketConnected = false;
    private TextView status_tv;
    Activity activity;

    public MySocket(Activity activity, TextView status_tv){
        this.activity=activity;
        this.status_tv=status_tv;
    }

    public void connect(){
        new Thread(createNconnect).start();
        new Thread(receiveData).start();
    }

    Runnable changeStatus = new Runnable() {
        @Override
        public void run() {
            status_tv.setText(STATUS);
        }
    };

    Runnable createNconnect = new Runnable() {
        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(12345);

                STATUS = "Server Started";
                activity.runOnUiThread(changeStatus);
                while (true) {
                    socket = serverSocket.accept();

                    SocketConnected = true;
                    STATUS = "Connected to " + socket.getInetAddress();
                    activity.runOnUiThread(changeStatus);

                    OutputStream outputStream = socket.getOutputStream();
                    writer = new PrintWriter(outputStream, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    Runnable receiveData = new Runnable() {
        @Override
        public void run() {

            if (SocketConnected)
                try {
                    InputStream inputStream = socket.getInputStream();
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    if (bufferedReader.readLine().equals("Close")){
                        SocketConnected = false;
                        STATUS="Disconnected";
                        activity.runOnUiThread(changeStatus)

                        ;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            receiveData.run();
        }
    };


    public void sendData(String data){
        Log.w("DATA",data);
            if (SocketConnected) {
               new SendThread(data,writer).start();
            } else {
                STATUS = "Disconnected";
                activity.runOnUiThread(changeStatus);
            }
        }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    public void close(){
        try {
            writer.close();
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
