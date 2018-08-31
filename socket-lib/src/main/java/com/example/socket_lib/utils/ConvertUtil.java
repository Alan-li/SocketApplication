package com.example.socket_lib.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/8 0008.
 */

public class ConvertUtil {

    /**
     * 截取属性值
     *
     * @param strSrc
     * @param strName
     * @return
     */
    public static String getValue(String strSrc, String strName) {
        String strLowerSrc = strSrc.toLowerCase();
        String strLowerName = strName.toLowerCase();

        int nIndexBegin = strLowerSrc.indexOf("<" + strLowerName + ">");
        int nIndexEnd = strLowerSrc.indexOf("</" + strLowerName + ">");
        if (nIndexEnd - nIndexBegin <= 0) {
            return null;
        }
        String strRect = strSrc.substring(nIndexBegin + strName.length() + 2, nIndexEnd);

        return strRect;
    }

    /**
     * 128位md5加密
     *
     * @param str
     * @return
     */
    public static String cal128MD5(String str) {
        String standardMD5_32 = "";
//        String finalMD5_128 = "";
        StringBuilder stringBuilder = new StringBuilder();

        for (int seed = 0; seed < 4; seed++) {
            MD5Code code = new MD5Code(seed);
            standardMD5_32 = code.getMD5ofStr(str, seed);
//            finalMD5_128 += standardMD5_32;
            stringBuilder.append(standardMD5_32);
        }

        return stringBuilder.toString();
    }

    /**
     * 32位md5加密
     *
     * @param input
     * @return
     */
    public static String getMD5(String input) {
        byte[] source = null;
        try {
            // Get byte according by specified coding.
            source = input.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                source = input.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }
        String result = null;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(source);
            // The result should be one 128 integer
            byte temp[] = md.digest();
            char str[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = temp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            result = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
