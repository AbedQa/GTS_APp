package com.example.fatto7.gtsone.Controllers.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fatto7.gtsone.ConnectionManager.ApiClient;
import com.example.fatto7.gtsone.R;
import com.example.fatto7.gtsone.SharedAndConstants.Constants;
import com.example.fatto7.gtsone.SharedAndConstants.PreferenceUtils;
import com.google.android.gms.common.api.Api;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {


    Button submit;
    EditText editText;
    PreferenceUtils preferenceUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        editText = (EditText) findViewById(R.id.url);
        submit = (Button) findViewById(R.id.submit);
        preferenceUtils = new PreferenceUtils(getApplicationContext());
        if (preferenceUtils.getBASEURL() == null || preferenceUtils.getBASEURL().isEmpty()) {
            editText.setText("");
        } else {
            editText.setText(preferenceUtils.getBASEURL());
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText() == null || editText.getText().toString().isEmpty()) {
                    Toast.makeText(SettingsActivity.this, "BASE URL Shouldn't be empty", Toast.LENGTH_SHORT).show();

                    return;
                }
               if (isServerReachable(editText.getText().toString()) ) {
                   String BASEURL = editText.getText().toString();
                   preferenceUtils.setBASEURL(BASEURL);
                   Constants.BASE_URL =  BASEURL;
                   startActivity(new Intent(SettingsActivity.this,GTS_Logo.class));
                   }else {

                   Toast.makeText(SettingsActivity.this, "Please Enter Valid BASE URL", Toast.LENGTH_SHORT).show();
               }


            }
        });

    }

     public boolean isServerReachable( String url) {
         if (android.os.Build.VERSION.SDK_INT > 9) {
             StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                     .permitAll().build();

             StrictMode.setThreadPolicy(policy);
         }
         try {
             URL diachi = new URL(url);
             HttpURLConnection huc = (HttpURLConnection) diachi.openConnection();
             huc.setRequestMethod("HEAD");
             return (huc.getResponseCode() != 401
             );
         } catch (MalformedURLException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }
        return false;
    }
    private void CheckValid(final String url) {
        ApiClient.getApiManager(getApplicationContext()).fakeService(url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        Log.v("RESPONSE_CALLED", "ON_RESPONSE_CALLED");
                        String didItWork = String.valueOf(response.isSuccessful());
                        Log.v("SUCCESS?", didItWork);
                        Log.v("RESPONSE_CODE", String.valueOf(response.code()));
                        if (response.code() >= 500) {
                            Toast.makeText(SettingsActivity.this, "There is somthing error on server", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (response.code() >= 200 && response.code() <= 299) {
                            preferenceUtils.setBASEURL(url);
                            Constants.BASE_URL = url;
                            startActivity(new Intent(SettingsActivity.this, GTS_Logo.class));
                        } else {
                            Toast.makeText(SettingsActivity.this, "Please Enter Valid BASE URL", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }else {
                        Toast.makeText(SettingsActivity.this, "Please Enter Valid BASE URL", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    Log.v("SUCCESS?", e.getLocalizedMessage());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private boolean isValid(String urlString) {
        try {
            URL url = new URL(urlString);
            return URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches();
        } catch (MalformedURLException e) {

        }

        return false;
    }
}
