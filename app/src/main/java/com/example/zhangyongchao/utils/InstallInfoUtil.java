package com.example.zhangyongchao.utils;

import com.example.zhangyongchao.basic.ApplicationBasic;
import com.example.zhangyongchao.myapplication.R;

import java.io.File;
import java.util.Random;

/**
 * Created by zhangyongchao on 2015/12/17.
 */
public class InstallInfoUtil {
    private static String installId = null;
    private static class Holder{
        static InstallInfoUtil instance = new InstallInfoUtil();
    }
    public static InstallInfoUtil getInstance(){
        return Holder.instance;
    }
    private InstallInfoUtil(){
        init();
    }
    private void init(){

    }
    public String getInstall(){
        synchronized (this){
            if (installId != null){
                return installId;
            }
            installId = System.currentTimeMillis() + getRand();
            ApplicationBasic.getSharedPreferences()
                    .edit()
                    .putString("INSTALL_ID", installId)
                    .commit();
            return installId;
        }
    }

    private static String getRand(){
        int rand = new Random().nextInt(1000);
        if (rand < 10){
            return "00" + rand;
        }
        if (rand < 100){
            return "0" + rand;
        }
        return "" + rand;
    }

    public static String getUserAgent(){
        return "android,5.1.1,1450411604293293,1.20.1210,google,Nexus 5,LMY48M," +
                "MOBILE__DISCONNECTED_WIFI_CONNECTED," +
                "Debug Communication,Dalvik,2.1.0,afanti,4.9E-324,4.9E-324,cn_afanti_debug";
    }

    public static String getVersion(){
        return ApplicationBasic.getGlobalContext().getResources().getString(R.string.version);
    }

}
