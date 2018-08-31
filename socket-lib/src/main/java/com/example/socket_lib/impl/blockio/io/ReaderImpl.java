package com.example.socket_lib.impl.blockio.io;

import android.util.Log;

import com.example.socket_lib.impl.exceptions.ReadException;
import com.example.socket_lib.sdk.OkSocketOptions;
import com.example.socket_lib.sdk.bean.OriginalData;
import com.example.socket_lib.sdk.connection.abilities.IStateSender;
import com.example.socket_lib.sdk.connection.interfacies.IAction;
import com.example.socket_lib.sdk.protocol.IHeaderProtocol;
import com.example.socket_lib.utils.BytesUtils;
import com.example.socket_lib.utils.ConvertUtil;
import com.example.socket_lib.utils.SL;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by xuhao on 2017/5/31.
 */

public class ReaderImpl extends AbsReader {


    public ReaderImpl(InputStream inputStream, IStateSender stateSender) {
        super(inputStream, stateSender);
    }

    @Override
    public void read() throws RuntimeException {

    }

    @Override
    public void login() throws RuntimeException {
        try {
            byte[] bytes = new byte[1024];
            int read = mInputStream.read(bytes);
            String loginResult = new String(bytes, "UTF-16LE");
            Log.d("yog", "result: " + loginResult.trim());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
