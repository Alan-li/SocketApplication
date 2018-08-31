package com.example.socket_lib.impl.blockio.io;


import com.example.socket_lib.impl.abilities.IWriter;
import com.example.socket_lib.impl.exceptions.WriteException;
import com.example.socket_lib.sdk.OkSocketOptions;
import com.example.socket_lib.sdk.bean.IPulseSendable;
import com.example.socket_lib.sdk.bean.ISendable;
import com.example.socket_lib.sdk.connection.abilities.IStateSender;
import com.example.socket_lib.sdk.connection.interfacies.IAction;
import com.example.socket_lib.utils.BytesUtils;
import com.example.socket_lib.utils.SL;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by xuhao on 2017/5/31.
 */

public class WriterImpl implements IWriter {

    private OkSocketOptions mOkOptions;

    private IStateSender mStateSender;

    private OutputStream mOutputStream;

    private LinkedBlockingQueue<ISendable> mQueue = new LinkedBlockingQueue<>();

    public WriterImpl(OutputStream outputStream, IStateSender stateSender) {
        mStateSender = stateSender;
        mOutputStream = outputStream;
    }

    @Override
    public boolean write() throws RuntimeException {
        return false;
    }

    @Override
    public void setOption(OkSocketOptions option) {
        mOkOptions = option;
    }

    @Override
    public void offer(ISendable sendable) {
        mQueue.offer(sendable);
    }

    @Override
    public void close() {
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }


}
