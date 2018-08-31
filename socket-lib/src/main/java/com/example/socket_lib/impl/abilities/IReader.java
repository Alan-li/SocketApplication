package com.example.socket_lib.impl.abilities;

import android.support.annotation.MainThread;
import android.support.annotation.WorkerThread;

import com.example.socket_lib.sdk.OkSocketOptions;

/**
 * Created by xuhao on 2017/5/16.
 */

public interface IReader {

    @WorkerThread
    void read() throws RuntimeException;

    @MainThread
    void setOption(OkSocketOptions option);

    void close();

    @WorkerThread
    void login() throws RuntimeException;

}
