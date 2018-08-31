package com.example.socket_lib.sdk.connection.abilities;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface IConnectable {
    /**
     * 将当前连接管理器发起连接
     */
    void connect();

    /**
     * 将当前连接管理器重新发起连接
     */
    void reconnect();

}
