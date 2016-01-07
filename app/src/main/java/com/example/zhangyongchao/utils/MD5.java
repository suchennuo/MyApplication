package com.example.zhangyongchao.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by zhangyongchao on 2015/12/17.
 */
public class MD5 {
    private static final String TAG = "MD5";


    public static String calculateMD5(String str){
        String output = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = str.getBytes("UTF-8");
            digest.update(bytes);
            byte[] md5sum = digest.digest();

            BigInteger bigInt = new BigInteger(1, md5sum);
            output = bigInt.toString(16);
            output = String.format("%32s", output).replace(' ', '0');
        } catch (Exception e) {
            Log.e(TAG, "Exception while getting digest", e);
            return null;
        }

        return output;
    }
}
