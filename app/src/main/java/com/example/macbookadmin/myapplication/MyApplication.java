package com.example.macbookadmin.myapplication;

import android.app.Application;

import com.example.socket_lib.sdk.OkSocket;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OkSocket.initialize(this , true);
    }
}
