package com.example.socket_lib.impl.abilities;

import com.example.socket_lib.sdk.ConnectionInfo;
import com.example.socket_lib.sdk.connection.IConnectionManager;


/**
 * Created by xuhao on 2017/6/30.
 */

public interface IConnectionSwitchListener {
    void onSwitchConnectionInfo(IConnectionManager manager, ConnectionInfo oldInfo, ConnectionInfo newInfo);
}
