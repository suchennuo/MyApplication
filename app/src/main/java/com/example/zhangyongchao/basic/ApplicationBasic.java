package com.example.zhangyongchao.basic;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.zhangyongchao.utils.ApplicationUtil;

/**
 * Created by zhangyongchao on 2015/12/17.
 */
public class ApplicationBasic extends Application {
    static ApplicationBasic applicationBasic;
    static SharedPreferences sp = null;
    private static final String TAG = ApplicationBasic.class.getSimpleName();

    @Override
    public void onCreate(){
        super.onCreate();
        applicationBasic = this;
        if (sp == null){
            sp = getApplicationContext().getSharedPreferences(ApplicationUtil.SP_FIRST, Context.MODE_PRIVATE);
        }
        initAppConfig();
    }
    public void initAppConfig(){
        //TODO:
    }

    public static ApplicationBasic getGlobalContext(){
        return applicationBasic;
    }

    public static SharedPreferences getSharedPreferences(){
        return sp;
    }

}
