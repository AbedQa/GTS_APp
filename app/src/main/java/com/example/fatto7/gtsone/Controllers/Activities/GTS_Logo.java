package com.example.fatto7.gtsone.Controllers.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fatto7.gtsone.ConnectionManager.ApiClient;
import com.example.fatto7.gtsone.ConnectionManager.CheckInternetConnection;
import com.example.fatto7.gtsone.ConnectionManager.JSONParser;
import com.example.fatto7.gtsone.R;
import com.example.fatto7.gtsone.SharedAndConstants.Constants;
import com.example.fatto7.gtsone.SharedAndConstants.PreferenceUtils;
import com.google.android.gms.common.api.Api;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.fatto7.gtsone.Controllers.Activities.Login.Loginsuccess;

import static com.example.fatto7.gtsone.Controllers.Activities.Login.mypreference;
import static com.example.fatto7.gtsone.Controllers.Activities.Login.uCompanyId;
import static com.example.fatto7.gtsone.Controllers.Activities.Login.uEstablishmentID;
import static com.example.fatto7.gtsone.Controllers.Activities.Login.uNamekey;
import static com.example.fatto7.gtsone.Controllers.Activities.Login.uPassword;
import static com.example.fatto7.gtsone.SharedAndConstants.Constants.BASE_URL;

public class GTS_Logo extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static int SPLASH_TIME_OUT = 3000;
    ProgressDialog progressDialog;
    JSONParser jParser = new JSONParser();
    public static String device_id = "";
    public static String CoID = "";
    public static String ESTID = "";
    public static String CurrencyID = "";
    public static String StationID = "";

    public static String sharedFolder = "";
    public static String UserId = "", EmployeeId = "", Uname = "";
    PreferenceUtils preferenceUtils;
    ImageView mainLogo;

    SharedPreferences sharedpreferences;
    //String mypreference = "mypref";
    String user = "", pass = "", coID = "", estID = "";

    String[] appPermissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MEDIA_CONTENT_CONTROL
            , Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET};

    private static final int PERMISSIONS_REQUEST_CODE = 1240;
    String success = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gts_logo);
        preferenceUtils = new
                PreferenceUtils(getApplicationContext());

        checkAndRequestPermissions();
        checkConnection();

        mainLogo = findViewById(R.id.main_logo);
        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        BASE_URL = preferenceUtils.getBASEURL();
        if (BASE_URL == null || BASE_URL.isEmpty()) {
            startActivity(new Intent(GTS_Logo.this,SettingsActivity.class));
            return;
        }
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation fadeInAnimation = AnimationUtils.loadAnimation(GTS_Logo.this, R.anim.fade_in);
                mainLogo.startAnimation(fadeInAnimation);
                mainLogo.setVisibility(View.VISIBLE);


            }
        }, 1200);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getStationCpu(device_id);

            }
        }, SPLASH_TIME_OUT);

    }

    private void getStationCpu(String device_id) {
        String url = BASE_URL + "GetStationCPU";
        ApiClient.getApiManager(getApplicationContext()).GetStationCPU(url, device_id, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("RESPONSE_CALLED", "ON_RESPONSE_CALLED");
                String didItWork = String.valueOf(response.isSuccessful());
                Log.v("SUCCESS?", didItWork);
                Log.v("RESPONSE_CODE", String.valueOf(response.code()));
                if (response.code() >= 500) {
                    Toast.makeText(getApplicationContext(), "There is somthing error on server", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject json = jParser.convertResponsToJSON(response.body());
                try {
                    JSONArray jsonArr = json.getJSONArray("StationCPU");
                    if (jsonArr.length() > 0) {
                        success = "true";
                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);
                            CoID = json.getString("COID");
                            ESTID = json.getString("ESTID");
                            CurrencyID = json.getString("CurrencyId");
                            StationID = json.getString("STATID");
                            //    STATCPU = json.getString("STATCPU");
                        }

                        user = sharedpreferences.getString(uNamekey, "");
                        pass = sharedpreferences.getString(uPassword, "");
                        coID = sharedpreferences.getString(uCompanyId, "");
                        estID = sharedpreferences.getString(uEstablishmentID, "");
                    } else {

                        success = "false";
                    }
                    LoginServer(user,pass,coID,estID);
                } catch (Exception e) {

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("",t.toString());
            }
        });
    }

    private void LoginServer(String user, String pass, String coID, String estID) {
        String url = BASE_URL + "Login";

        ApiClient.getApiManager(getApplicationContext()).Login(url, user, pass, coID, estID, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.v("RESPONSE_CALLED", "ON_RESPONSE_CALLED");
                String didItWork = String.valueOf(response.isSuccessful());
                Log.v("SUCCESS?", didItWork);
                Log.v("RESPONSE_CODE", String.valueOf(response.code()));
                if (response.code() >= 500) {
                    Toast.makeText(getApplicationContext(), "There is somthing error on server", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject json = jParser.convertResponsToJSON(response.body());

                try {
                    JSONArray jsonArr = json.getJSONArray("Login");
                    if (jsonArr.length() > 0) {
                        Loginsuccess = "true";
                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);
                            UserId = json.getString("UserId");
                            Uname = json.getString("Uname");
                            EmployeeId = json.getString("EmployeeId");
                            sharedFolder = json.getString("sharedfolder");
                        }
                    } else {
                        Loginsuccess = "false";
                    }

                    if (success.contains("true") && !sharedpreferences.contains(uNamekey) && !Loginsuccess.contains("true")) {
                        Intent i = new Intent(GTS_Logo.this, Login.class);
                        startActivity(i);
                    } else if (success.contains("true") && !sharedpreferences.contains(uNamekey) && !Loginsuccess.contains("false")) {

                        Intent i = new Intent(GTS_Logo.this, Login.class);
                        startActivity(i);

                    } else if (success.contains("true") && sharedpreferences.contains(uNamekey) && Loginsuccess.contains("true")) {

                        Intent i = new Intent(GTS_Logo.this, VisitSheet.class);
                        startActivity(i);

                    } else {
                        Intent i = new Intent(GTS_Logo.this, Registration.class);
                        startActivity(i);
                    }
                    finish();
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("",t.toString());

            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected == false) {
            Intent intent = new Intent(this, NoNetwork.class);
            startActivity(intent);
        } else {

        }
    }

    public void checkConnection() {

        boolean isConnected = ConnectivityReceiver.isConnected();

        if (isConnected == false) {
            Intent intent = new Intent(this, NoNetwork.class);
            startActivity(intent);
        } else {

        }
    }

    public Boolean checkAndRequestPermissions() {
        //Check which permissions are granted
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String perm : appPermissions) {

            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(perm);
            }

        }

        //Ask for Non-Granted Permissions
        if (!listPermissionsNeeded.isEmpty()) {

            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSIONS_REQUEST_CODE);

            return false;

        }

        // App has all permissions
        return true;
    }

}



