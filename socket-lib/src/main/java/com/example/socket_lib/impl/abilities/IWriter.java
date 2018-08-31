package com.example.socket_lib.impl.abilities;

import com.example.socket_lib.sdk.OkSocketOptions;
import com.example.socket_lib.sdk.bean.ISendable;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface IWriter {
    boolean write() throws RuntimeException;

    void setOption(OkSocketOptions option);

    void offer(ISendable sendable);

    void close();

}
