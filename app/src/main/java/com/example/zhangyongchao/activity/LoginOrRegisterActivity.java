package com.example.zhangyongchao.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.zhangyongchao.basic.ActivityBasic;
import com.example.zhangyongchao.myapplication.R;
import com.example.zhangyongchao.network.ConnectionManager;
import com.example.zhangyongchao.utils.Log;

/**
 * Created by zhangyongchao on 2015/12/16.
 */
public class LoginOrRegisterActivity extends ActivityBasic {

    private EditText userNameEt;
    private EditText passWordEt;
    private Button submitBt;

    private static final String TAG = LoginOrRegisterActivity.class.getSimpleName();
    private static final String LOG_LOGIN_REPONSE = "Login response ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        userNameEt = (EditText)findViewById(R.id.name_et);
        passWordEt = (EditText)findViewById(R.id.password_et);
        submitBt = (Button)findViewById(R.id.submit_bt);

        setListener();
    }

    private void setListener() {

        userNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setSubmitBtStatus();
            }
        });
        passWordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setSubmitBtStatus();
            }
        });
        submitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( ! inputIsEmpty()){
                    String[] params  = {userNameEt.getText().toString().trim(), passWordEt.getText().toString().trim()};
                    new LoginTask().execute(params);
                }
            }
        });
    }

    private boolean inputIsEmpty(){
        return TextUtils.isEmpty(userNameEt.getText()) || TextUtils.isEmpty(passWordEt.getText());
    }

    private void setSubmitBtStatus(){
        if (inputIsEmpty()){
            submitBt.setClickable(false);
            submitBt.setBackgroundResource(R.drawable.button_disable);
        }else{
            submitBt.setClickable(true);
            submitBt.setBackgroundResource(R.drawable.selector_login);
        }
    }

    public class LoginTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            return ConnectionManager.getInstance().login(params[0], params[1]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, LOG_LOGIN_REPONSE + s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }



}
