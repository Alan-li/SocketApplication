package com.example.socket_lib.sdk.connection.abilities;


import com.example.socket_lib.sdk.bean.ISendable;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface ISender<T> {
    /**
     * 在当前的连接上发送数据
     *
     * @param sendable 具有发送能力的Bean {@link ISendable}
     * @return T
     */
    T send(ISendable sendable);
}
