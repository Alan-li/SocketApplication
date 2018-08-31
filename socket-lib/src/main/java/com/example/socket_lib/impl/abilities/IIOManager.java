package com.example.socket_lib.impl.abilities;


import com.example.socket_lib.sdk.OkSocketOptions;
import com.example.socket_lib.sdk.bean.ISendable;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface IIOManager {
    void resolve();

    void setOkOptions(OkSocketOptions options);

    void send(ISendable sendable);

    void close();

    void close(Exception e);

}
