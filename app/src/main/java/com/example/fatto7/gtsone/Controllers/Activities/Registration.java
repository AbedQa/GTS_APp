package com.example.fatto7.gtsone.Controllers.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
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

import com.example.fatto7.gtsone.BusinessManager.Models.clsCompanyRegistration;
import com.example.fatto7.gtsone.BusinessManager.Models.clsEstablishmentRegistration;
import com.example.fatto7.gtsone.ConnectionManager.ApiClient;
import com.example.fatto7.gtsone.ConnectionManager.JSONParser;
import com.example.fatto7.gtsone.Controllers.Adapters.SpinnerAdapterCompany;
import com.example.fatto7.gtsone.Controllers.Adapters.SpinnerAdapterEstableshment;
import com.example.fatto7.gtsone.R;

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

public class Registration extends AppCompatActivity {
    ProgressDialog progressDialog;
    JSONParser jParser = new JSONParser();
    EditText RegistrationNameET;
    Button RegistrationRegBtn;
    String EstablishmentID, EstablishmentName, CompanyID, CompanyName, m_name;
    Spinner RegistrationCompanySp, RegistrationEstaplishmentSp;
    ArrayList<clsCompanyRegistration> CompanyList;
    ArrayList<clsEstablishmentRegistration> EstableshmentList;
    Typeface gillsansFont;
    String NewStationID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registration);
        // this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        RegistrationNameET = findViewById(R.id.RegistrationNameET);
        RegistrationCompanySp = findViewById(R.id.RegistrationCompanySp);
        RegistrationEstaplishmentSp = findViewById(R.id.RegistrationEstaplishmentSp);
        RegistrationRegBtn = findViewById(R.id.RegistrationRegBtn);
        RegistrationCompanySp = findViewById(R.id.RegistrationCompanySp);
        CompanyList = new ArrayList<clsCompanyRegistration>();
        EstableshmentList = new ArrayList<clsEstablishmentRegistration>();

        gillsansFont = Typeface.createFromAsset(this.getAssets(), "fonts/gillsans.ttf");
        RegistrationRegBtn.setTypeface(gillsansFont);
        RegistrationNameET.setTypeface(gillsansFont);
        GetCompanyAPi();
        RegistrationCompanySp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                CompanyID = String.valueOf(CompanyList.get(position).getId());
                CompanyName = String.valueOf(CompanyList.get(position).getName());

                GetEstablishmentApi(String.valueOf(CompanyID));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        RegistrationEstaplishmentSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                EstablishmentID = String.valueOf(EstableshmentList.get(position).getId());
                EstablishmentName = String.valueOf(EstableshmentList.get(position).getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ///////////////////////////////////////////////////////////////////////


        RegistrationRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String m_cpu = GTS_Logo.device_id;
                m_name = RegistrationNameET.getText().toString();
                InsertStaionApi(String.valueOf(CompanyID), String.valueOf(EstablishmentID), m_name, "", m_cpu);

            }
        });


    }

    private void InsertStaionApi(String CompanyID, String EstablishmentID, String m_name, String s, String m_cpu) {

        String url = BASE_URL + "InsertStation";

        ApiClient.getApiManager(getApplicationContext()).InsertStaion(url,CompanyID,EstablishmentID,m_name,s,m_cpu, new Callback<ResponseBody>() {
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
                    JSONArray jsonArr = json.getJSONArray("Station");
                    if (jsonArr.length() > 0) {
                        NewStationID = "true";
                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);
                        }
                    } else {
                        NewStationID = "false";
                    }
                    if (NewStationID.contains("true")) {
                        Intent intent = new Intent(Registration.this, GTS_Logo.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Registration.this, "Registration Failed", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void GetEstablishmentApi(String CompanyID) {
        String url = BASE_URL + "GetEstablishment";
        EstableshmentList.clear();
        ApiClient.getApiManager(getApplicationContext()).GetEstablishment(url, new Callback<ResponseBody>() {
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
                    JSONArray jsonArr = json.getJSONArray("Establishment");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        json = jsonArr.getJSONObject(i);
                        clsEstablishmentRegistration objEstableshment = new clsEstablishmentRegistration();
                        objEstableshment.setId(json.getString("id"));
                        objEstableshment.setName(json.getString("Name"));
                        EstableshmentList.add(objEstableshment);
                    }
                    SpinnerAdapterEstableshment EstableshmentAdapter = new SpinnerAdapterEstableshment(Registration.this, 0, EstableshmentList);
                    RegistrationEstaplishmentSp.setAdapter(EstableshmentAdapter);
                }catch (Exception e) {

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        },CompanyID);
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
                        clsCompanyRegistration objCompany = new clsCompanyRegistration();
                        objCompany.setId(json.getString("id"));
                        objCompany.setName(json.getString("Name"));
                        CompanyList.add(objCompany);
                    }
                    SpinnerAdapterCompany CompanyAdapter = new SpinnerAdapterCompany(Registration.this, 0, CompanyList);
                    RegistrationCompanySp.setAdapter(CompanyAdapter);
                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }




}
