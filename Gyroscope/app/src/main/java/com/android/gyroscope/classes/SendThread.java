package com.android.gyroscope.classes;


import java.io.PrintWriter;

public class SendThread extends Thread implements Runnable {

    private String data="";
    private PrintWriter writer;
    SendThread(String data, PrintWriter writer){
        this.data=data;
        this.writer=writer;
    }

    @Override
    public void run() {
        writer.println("@"+data+"#");
    }
}
