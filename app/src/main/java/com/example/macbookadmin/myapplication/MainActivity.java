package com.example.macbookadmin.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.socket_lib.sdk.ConnectionInfo;
import com.example.socket_lib.sdk.OkSocket;
import com.example.socket_lib.sdk.SocketActionAdapter;
import com.example.socket_lib.sdk.bean.ISendable;
import com.example.socket_lib.sdk.bean.OriginalData;
import com.example.socket_lib.sdk.connection.IConnectionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectionInfo connectionInfo = new ConnectionInfo("126.168.5.4", 3001);
        connectionInfo.setBackupInfo(new ConnectionInfo("10.16.6.18" , 3001));
        IConnectionManager manager = OkSocket.open(connectionInfo);
        manager.registerReceiver(new SocketActionAdapter() {
            @Override
            public void onSocketConnectionSuccess(Context context, ConnectionInfo info, String action) {
                Log.d("yog", "connect success");
            }

            @Override
            public void onSocketConnectionFailed(Context context, ConnectionInfo info, String action, Exception e) {
                Log.d("yog", "connect fail");

            }

        });
        manager.connect();
    }
}
