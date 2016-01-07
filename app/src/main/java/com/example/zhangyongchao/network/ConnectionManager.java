package com.example.zhangyongchao.network;

import com.example.zhangyongchao.basic.ApplicationBasic;
import com.example.zhangyongchao.utils.ApplicationUtil;
import com.example.zhangyongchao.utils.InstallInfoUtil;
import com.example.zhangyongchao.utils.Log;
import com.example.zhangyongchao.utils.MD5;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;


/**
 * Created by zhangyongchao on 2015/12/16.
 */
public class ConnectionManager {

    private static final String TAG = ConnectionManager.class.getSimpleName();
    private static final String ERROR_GET_RESPONSE = "Get response failure ";
    private static final String LOG_RESPONSE_CONTENT = "Response content ";

    private static class Holder{
        static ConnectionManager instance = new ConnectionManager();
    }
    public static ConnectionManager getInstance(){
        return Holder.instance;
    }
    private ConnectionManager(){
        init();
    }

    private void init(){

    }

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();

    static {
        mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);

    }

    public Response execute(Request request) throws IOException{
        return mOkHttpClient.newCall(request).execute();
    }
/*
    public void enqueue(Request request, Callback responseCallback){
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

    public void enqueue(Request request){
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

*/
    // Calling response.body.close() will release all resources held by the response.
    // The connection pool will keep the connection open,
    // but that'll get closed automatically after a timeout if it goes unused.

    public String getResponse(Request request) throws IOException {
        String result = null;
        Response response = execute(request);
        response.body().close(); //
        if (response.isSuccessful()){
            result = response.body().string();
            Log.i(TAG, LOG_RESPONSE_CONTENT + response);
        } else {
            Log.e(TAG, ERROR_GET_RESPONSE + response);
        }
        return result;
    }

    private void setInstallId(FormEncodingBuilder formBuilder){
        formBuilder.add("install_id", InstallInfoUtil.getInstance().getInstall());
    }

    public String login(String phoneNum, String password){
        mOkHttpClient.interceptors().add(new ReceivedCookiesInterceptor());
//        mOkHttpClient.interceptors().add(new AddCookiesInterceptor());
        mOkHttpClient.interceptors().add(new UserAgentInterceptor());
        password = MD5.calculateMD5(password);
        String response = null;
        FormEncodingBuilder formBuilder = new FormEncodingBuilder();
        formBuilder.add("telephone_num", phoneNum);
        formBuilder.add("password", password);
        formBuilder.add("source", "");
        setInstallId(formBuilder);
        RequestBody formBody = formBuilder.build();
        Request request = new Request.Builder()
                .url(ApplicationUtil.REQUEST_HEAD + "do_login/")
                .post(formBody)
                .build();
        try {
            response = getResponse(request);
            Log.d(TAG, "login params " + phoneNum + " " + password);
        } catch (Exception e) {
            Log.d(TAG, "login exception: " + e);
        }
        return response;
    }

    public class UserAgentInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request requestWithUserAgent = originalRequest.newBuilder()
                    .header("User-Agent", InstallInfoUtil.getUserAgent())
                    .build();
            return chain.proceed(requestWithUserAgent);
        }
    }
    public class ReceivedCookiesInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if ( ! originalResponse.headers("Set-Cookie").isEmpty()){
                HashSet<String> cookies = new HashSet<>();

                for (String header : originalResponse.headers("Set-Cookie")){
                    cookies.add(header);
                }

                //TODO：序列化cookie存储，过期更新
                ApplicationBasic.getSharedPreferences()
                        .edit().putStringSet("PREF_COOKIES", cookies).commit();
            }
            return null;
        }
    }

    public class AddCookiesInterceptor implements Interceptor{


        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            HashSet<String> preferences = (HashSet) ApplicationBasic.getSharedPreferences()
                    .getStringSet("PREF_COOKIES", new HashSet<String>());
            for (String cookie : preferences){
                builder.addHeader("Cookie", cookie);
                Log.d(TAG, "Add Header: " + cookie);
            }
            return chain.proceed(builder.build());
        }
    }

    public String getPostList(int pushType, int postDistrict){
        String response = null;
        FormEncodingBuilder formBuilder = new FormEncodingBuilder();
        setInstallId(formBuilder);
        setVersion(formBuilder);
        setPlatform(formBuilder);
        formBuilder.add("type", pushType+"");
        formBuilder.add("post_district", postDistrict+"");

        RequestBody formBody = formBuilder.build();
        Request request = new Request.Builder()
                .url(ApplicationUtil.REQUEST_HEAD + "get_posts/")
                .post(formBody)
                .build();
        try {
            response = getResponse(request);
        } catch (Exception e) {
            Log.d(TAG, "getPostList exception: " + e);
        }
        return response;
    }

    public void setVersion(FormEncodingBuilder formBuilder){
        formBuilder.add("version", InstallInfoUtil.getVersion());
    }

    public void setPlatform(FormEncodingBuilder formBuilder){
        formBuilder.add("platform", "a");
    }

}
