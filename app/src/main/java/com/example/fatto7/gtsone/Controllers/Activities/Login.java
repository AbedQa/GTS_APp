package com.example.fatto7.gtsone.Controllers.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fatto7.gtsone.BusinessManager.Models.clsCompanyLogin;
import com.example.fatto7.gtsone.BusinessManager.Models.clsCompanyRegistration;
import com.example.fatto7.gtsone.ConnectionManager.ApiClient;
import com.example.fatto7.gtsone.ConnectionManager.JSONParser;
import com.example.fatto7.gtsone.Controllers.Adapters.SpinnerAdapterCompany;
import com.example.fatto7.gtsone.Controllers.Adapters.SpinnerAdapterCompanyLogin;
import com.example.fatto7.gtsone.R;
import com.example.fatto7.gtsone.SharedAndConstants.GTSApplication;

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

import static com.example.fatto7.gtsone.SharedAndConstants.Constants.BASE_URL;

public class Login extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {


    public static String Loginsuccess = "";
    ProgressDialog progressDialog;
    JSONParser jParser = new JSONParser();
    Spinner LoginCompanySp;
    EditText LoginUserNameET, LoginPasswordET;
    Button LoginConnectBtn;
    Typeface gillsansFont;
    public static SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static String uNamekey = "key", uPassword = "pass", uCompanyId = "compid", uEstablishmentID = "estabid";

    ArrayList<clsCompanyLogin> CompanyList;

    public static String CoID = "", ESTID = "", m_username = "", m_password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        //   this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        checkConnection();

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        gillsansFont = Typeface.createFromAsset(this.getAssets(), "fonts/gillsans.ttf");
        LoginCompanySp = findViewById(R.id.LoginCompanySp);

        LoginUserNameET = findViewById(R.id.LoginUserNameET);
        LoginPasswordET = findViewById(R.id.LoginPasswordET);

        LoginConnectBtn = findViewById(R.id.LoginConnectBtn);
        CompanyList = new ArrayList<clsCompanyLogin>();

        LoginPasswordET.setTypeface(gillsansFont);
        LoginUserNameET.setTypeface(gillsansFont);
        LoginConnectBtn.setTypeface(gillsansFont);

        GetCompanyAPi();



        LoginCompanySp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GTS_Logo.CoID = CompanyList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


     /*   final string m_username = ""
                            , final string m_password = ""
                            , string m_doc_source_company_id = ""
                            , string m_mas_source_establishment_id =*/
        LoginConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CoID = GTS_Logo.CoID;
                ESTID = GTS_Logo.ESTID;
                m_username = LoginUserNameET.getText().toString();
                m_password = LoginPasswordET.getText().toString();
                LoginServer(m_username,m_password,CoID,ESTID);

            }
        });


    }


    private void GetCompanyAPi() {
        CompanyList.clear();

        String url = BASE_URL + "GetCompany";

        ApiClient.getApiManager(getApplicationContext()).GetCompany(url, new Callback<ResponseBody>() {
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
                    JSONArray jsonArr = json.getJSONArray("Company");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        json = jsonArr.getJSONObject(i);
                        clsCompanyLogin objCompany = new clsCompanyLogin();
                        objCompany.setId(json.getString("id"));
                        objCompany.setName(json.getString("Name"));
                        CompanyList.add(objCompany);
                    }
                    SpinnerAdapterCompanyLogin CompanyAdapter = new SpinnerAdapterCompanyLogin(Login.this, 0,
                            CompanyList);
                    LoginCompanySp.setAdapter(CompanyAdapter);
                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void LoginServer(String user, final String pass, String coID, String estID) {
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
                            GTS_Logo.UserId = json.getString("UserId");
                            GTS_Logo.Uname = json.getString("Uname");
                            GTS_Logo.EmployeeId = json.getString("EmployeeId");

                            //save username in shared pref.
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(uNamekey, GTS_Logo.Uname);
                            editor.putString(uPassword, String.valueOf(pass));
                            editor.putString(uCompanyId, String.valueOf(CoID));
                            editor.putString(uEstablishmentID, String.valueOf(ESTID));
                            editor.commit();

                        }
                    } else {
                        Loginsuccess = "false";
                    }
                    if (Loginsuccess.contains("true")) {

                        Intent intent = new Intent(Login.this, VisitSheet.class);
                        startActivity(intent);
                        finish();
                    } else {

                        Toast.makeText(Login.this, "Login Failed", Toast.LENGTH_LONG).show();

                    }
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

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        GTSApplication.getInstance().setConnectivityListener(this);
    }
}


