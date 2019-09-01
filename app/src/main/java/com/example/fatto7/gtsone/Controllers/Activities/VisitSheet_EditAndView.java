package com.example.fatto7.gtsone.Controllers.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fatto7.gtsone.BusinessManager.Models.PriceLevel;
import com.example.fatto7.gtsone.BusinessManager.Models.UploadFile;
import com.example.fatto7.gtsone.BusinessManager.Models.clsAttachment;
import com.example.fatto7.gtsone.BusinessManager.Models.clsNotes;
import com.example.fatto7.gtsone.BusinessManager.Models.clsPersonalActivity;
import com.example.fatto7.gtsone.BusinessManager.Models.clsSparePartsDashboard;
import com.example.fatto7.gtsone.BusinessManager.Models.clsVisitSheetStatus;
import com.example.fatto7.gtsone.BusinessManager.Models.clsVisitSheetStatusEdit;
import com.example.fatto7.gtsone.BusinessManager.Models.clsVisitSheet_EditAndViewDashboard;
import com.example.fatto7.gtsone.ConnectionManager.ApiClient;
import com.example.fatto7.gtsone.ConnectionManager.ApiInterface;
import com.example.fatto7.gtsone.ConnectionManager.JSONParser;
import com.example.fatto7.gtsone.Controllers.Adapters.NotesActivityListAdapter;
import com.example.fatto7.gtsone.Controllers.Adapters.PersonalActivityListAdaper;
import com.example.fatto7.gtsone.Controllers.Adapters.SpinnerAdapterVisitSheetStatusEdit;
import com.example.fatto7.gtsone.Controllers.Adapters.SpinnerAdapterVisitsheetStatus;
import com.example.fatto7.gtsone.Controllers.Fragments.AddSpareParts;
import com.example.fatto7.gtsone.R;
import com.example.fatto7.gtsone.SharedAndConstants.Constants;
import com.example.fatto7.gtsone.SharedAndConstants.GTSApplication;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.example.fatto7.gtsone.Controllers.Activities.GTS_Logo.CoID;
import static com.example.fatto7.gtsone.Controllers.Activities.GTS_Logo.sharedFolder;
import static com.example.fatto7.gtsone.Controllers.Activities.Login.mypreference;
import static com.example.fatto7.gtsone.Controllers.Activities.Login.uCompanyId;
import static com.example.fatto7.gtsone.Controllers.Activities.Login.uEstablishmentID;
import static com.example.fatto7.gtsone.Controllers.Activities.Login.uNamekey;
import static com.example.fatto7.gtsone.Controllers.Activities.Login.uPassword;
import static com.example.fatto7.gtsone.Controllers.Fragments.VisitSheetFragment.VesitSheetId;
import static com.example.fatto7.gtsone.SharedAndConstants.Constants.BASE_URL;

public class VisitSheet_EditAndView extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    public static SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    Typeface gillsansFont;
    Integer Partsquantity = 0;
    private FusedLocationProviderClient Startclient, Travelclient, Endclient;
    Button logOut;
    TextView visitsheetTitle;
    ProgressDialog progressDialog;
    JSONParser jParser = new JSONParser();

    TabHost Visitsheet_EditAndView_Tabhost;

    Spinner VS_E_A_V_StatusSp;
    String statusItem = "";
    TextView VS_E_A_V_CustomerTV, VS_E_A_V_NumberingTV, VS_E_A_V_ContactPersonTV, VS_E_A_V_ItemNameTV,
            VS_E_A_V_VisitDateTV, VS_E_A_V_TravelTimeTV, VS_E_A_V_StartTimeTV, VS_E_A_V_EndTimeTV, VS_E_A_V_VisitTypeTV,
            VS_E_A_V_CurrencyTV, VS_E_A_V_LogisticsOwner, VS_E_A_V_LogisticsAddressTV, VS_E_A_V_LogisticsContactPersonTV,
            VS_E_A_V_LogisticAssignSecTV, VS_E_A_V_AdressTV, VS_E_A_V_TechnicalTV, VS_E_A_V_DiscountTV, VS_E_A_V_TotalLinesTV,
            VS_E_A_V_TotalTaxTV, VS_E_A_V_GrossTotalLCTV, VS_E_A_V_GrossTotalfCTV, VS_E_A_V_SerialNoTV;


    EditText VS_E_A_V_RemarksET, VS_E_A_V_MeterReadingTV;

    String VisitSheetId = "";
    String itemmaster = "";
    CheckBox VS_E_A_V_ContractCB, VS_E_A_V_CopyToDeliveryCB, VS_E_A_V_CopyToSalesInvoiceCB;

    Button VS_E_A_V_UploadFileBtn, VS_E_A_V_StartTimeBtn, VS_E_A_V_TravelTimeBtn, VS_E_A_V_EndTimeBtn;

    Button VS_E_A_V_SaveIV, VS_E_A_V_PostToDeliveryIV, VS_E_A_V_SaveandFinishIV, VS_E_A_V_CloseIV, VS_E_A_V_AddSpareParts;

    String currentTap = "";

    TableLayout EditAndViewtb1;

    ArrayList<clsVisitSheetStatus> VisitSheetStatusList;
    ArrayList<clsVisitSheetStatusEdit> VisitSheetStatusListEdit;

    List<clsPersonalActivity> clsPersonalActivities;
    ArrayList<com.example.fatto7.gtsone.BusinessManager.Models.clsNotes> clsNotes;
    ArrayList<clsAttachment> clsAttachments;
    ArrayList<clsVisitSheet_EditAndViewDashboard> allexistingdatalist;
    ArrayList<clsSparePartsDashboard> allexistingSpareParts;

    public PersonalActivityListAdaper PALAdapter;
    public NotesActivityListAdapter NoteAdapter;
    ListView Personal_Activity_list, Notes_List;

    String Editable;
    String Status;
    String reportno, reportdate, owner, technician;

    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;
    int CAMERA_REQUEST = 10;
    int GALLERY_REQUEST = 2;
    public static int MY_PERMISSIONS_REQUEST_CAMERA = 110;
    public static int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 000;
    Bitmap bitmap;
    ImageView imageView;
    String photoPath = "";
    File file;
    TextView imagePath;
    String fileExt;
    byte[] byteArray = null;
    String bitmapstring1;
    String fileName;
    public static String DocVSID = "";
    String SparePartsQuantity = "";
    String ItemsQuantity = "";
    String NoteQuantity = "";
    String success = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_visit_sheet__edit_and_view);
        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());

        checkConnection();
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        DocVSID = sharedpreferences.getString(uEstablishmentID, "");
        Log.d("", DocVSID);
        final Intent intent = getIntent();
        VisitSheetId = intent.getStringExtra("VisitSheetId");

        Editable = intent.getStringExtra("Editable");
        VS_E_A_V_AddSpareParts = findViewById(R.id.addSpearpart);
        gillsansFont = Typeface.createFromAsset(this.getAssets(), "fonts/gillsans.ttf");
        VS_E_A_V_MeterReadingTV = (EditText) findViewById(R.id.VS_E_A_V_MeterReadingTV);
        VS_E_A_V_SerialNoTV = findViewById(R.id.VS_E_A_V_SerialNoTV);

        VS_E_A_V_CustomerTV = findViewById(R.id.VS_E_A_V_CustomerTV);
        VS_E_A_V_NumberingTV = findViewById(R.id.VS_E_A_V_NumberingTV);
        VS_E_A_V_VisitDateTV = findViewById(R.id.VS_E_A_V_VisitDateTV);
        VS_E_A_V_TravelTimeTV = findViewById(R.id.VS_E_A_V_TravelTimeTV);
        VS_E_A_V_StartTimeTV = findViewById(R.id.VS_E_A_V_StartTimeTV);
        VS_E_A_V_EndTimeTV = findViewById(R.id.VS_E_A_V_EndTimeTV);
        VS_E_A_V_VisitTypeTV = findViewById(R.id.VS_E_A_V_VisitTypeTV);
        VS_E_A_V_CurrencyTV = findViewById(R.id.VS_E_A_V_CurrencyTV);
        VS_E_A_V_AdressTV = findViewById(R.id.VS_E_A_V_AdressTV);
        VS_E_A_V_ContactPersonTV = findViewById(R.id.VS_E_A_V_ContactPersonTV);
        VS_E_A_V_ItemNameTV = findViewById(R.id.VS_E_A_V_ItemNameTV);
        VS_E_A_V_TechnicalTV = findViewById(R.id.VS_E_A_V_TechnicalTV);
        VS_E_A_V_RemarksET = findViewById(R.id.VS_E_A_V_RemarksET);
        VS_E_A_V_DiscountTV = findViewById(R.id.VS_E_A_V_DiscountTV);
        VS_E_A_V_TotalLinesTV = findViewById(R.id.VS_E_A_V_TotalLinesTV);
        //   VS_E_A_V_AmountTV = findViewById(R.id.VS_E_A_V_AmountTV);
        VS_E_A_V_TotalTaxTV = findViewById(R.id.VS_E_A_V_TotalTaxTV);
        VS_E_A_V_GrossTotalLCTV = findViewById(R.id.VS_E_A_V_GrossTotalLCTV);
        VS_E_A_V_GrossTotalfCTV = findViewById(R.id.VS_E_A_V_GrossTotalfCTV);
        imageView = findViewById(R.id.image_place_holder);
        imagePath = findViewById(R.id.image_path_txt);
        VS_E_A_V_ContractCB = findViewById(R.id.VS_E_A_V_ContractCB);
        VS_E_A_V_CopyToDeliveryCB = findViewById(R.id.VS_E_A_V_CopyToDeliveryCB);
        VS_E_A_V_CopyToSalesInvoiceCB = findViewById(R.id.VS_E_A_V_CopyToSalesInvoiceCB);

        VS_E_A_V_UploadFileBtn = findViewById(R.id.VS_E_A_V_UploadFileBtn);
        VS_E_A_V_StartTimeBtn = findViewById(R.id.VS_E_A_V_StartTimeBtn);
        VS_E_A_V_TravelTimeBtn = findViewById(R.id.VS_E_A_V_TravelTimeBtn);
        VS_E_A_V_EndTimeBtn = findViewById(R.id.VS_E_A_V_EndTimeBtn);

        VS_E_A_V_StatusSp = findViewById(R.id.VS_E_A_V_StatusSp);
        VS_E_A_V_UploadFileBtn.setVisibility(View.INVISIBLE);

        VS_E_A_V_SaveIV = findViewById(R.id.VS_E_A_V_SaveIV);
        VS_E_A_V_PostToDeliveryIV = findViewById(R.id.VS_E_A_V_PostToDeliveryIV);
        VS_E_A_V_SaveandFinishIV = findViewById(R.id.VS_E_A_V_SaveandFinishIV);
        VS_E_A_V_CloseIV = findViewById(R.id.VS_E_A_V_CloseIV);

        VS_E_A_V_CustomerTV.setTypeface(gillsansFont);
        VS_E_A_V_NumberingTV.setTypeface(gillsansFont);
        VS_E_A_V_VisitDateTV.setTypeface(gillsansFont);
        VS_E_A_V_TravelTimeTV.setTypeface(gillsansFont);
        VS_E_A_V_StartTimeTV.setTypeface(gillsansFont);
        VS_E_A_V_EndTimeTV.setTypeface(gillsansFont);
        VS_E_A_V_VisitTypeTV.setTypeface(gillsansFont);
        VS_E_A_V_CurrencyTV.setTypeface(gillsansFont);
        VS_E_A_V_AdressTV.setTypeface(gillsansFont);
        VS_E_A_V_ContactPersonTV.setTypeface(gillsansFont);
        VS_E_A_V_ItemNameTV.setTypeface(gillsansFont);
        VS_E_A_V_TechnicalTV.setTypeface(gillsansFont);
        VS_E_A_V_RemarksET.setTypeface(gillsansFont);
        VS_E_A_V_DiscountTV.setTypeface(gillsansFont);
        VS_E_A_V_TotalLinesTV.setTypeface(gillsansFont);
        // VS_E_A_V_AmountTV.setTypeface(gillsansFont);
        VS_E_A_V_TotalTaxTV.setTypeface(gillsansFont);
        VS_E_A_V_GrossTotalLCTV.setTypeface(gillsansFont);
        VS_E_A_V_GrossTotalfCTV.setTypeface(gillsansFont);

        VS_E_A_V_SaveIV.setTypeface(gillsansFont);
        VS_E_A_V_PostToDeliveryIV.setTypeface(gillsansFont);
        VS_E_A_V_SaveandFinishIV.setTypeface(gillsansFont);
        VS_E_A_V_CloseIV.setTypeface(gillsansFont);
        imagePath.setTypeface(gillsansFont);
        VS_E_A_V_UploadFileBtn.setTypeface(gillsansFont);
        VS_E_A_V_StartTimeBtn.setTypeface(gillsansFont);
        VS_E_A_V_TravelTimeBtn.setTypeface(gillsansFont);
        VS_E_A_V_EndTimeBtn.setTypeface(gillsansFont);


        VS_E_A_V_DiscountTV.setTypeface(gillsansFont);
        VS_E_A_V_TotalLinesTV.setTypeface(gillsansFont);
        //VS_E_A_V_AmountTV.setTypeface(gillsansFont);
        VS_E_A_V_TotalTaxTV.setTypeface(gillsansFont);
        VS_E_A_V_GrossTotalLCTV.setTypeface(gillsansFont);
        VS_E_A_V_GrossTotalfCTV.setTypeface(gillsansFont);

        Visitsheet_EditAndView_Tabhost = findViewById(R.id.Visitsheet_EditAndView_Tabhost);
        Visitsheet_EditAndView_Tabhost.setup();

        EditAndViewtb1 = findViewById(R.id.EditAndViewtb1);

        VisitSheetStatusList = new ArrayList<>();
        VisitSheetStatusListEdit = new ArrayList<>();

        allexistingdatalist = new ArrayList<>();
        allexistingSpareParts = new ArrayList<>();
        clsAttachments = new ArrayList<>();


        TabHost.TabSpec spec = Visitsheet_EditAndView_Tabhost.newTabSpec("General");
        spec.setContent(R.id.General);
        spec.setIndicator("General");
        Visitsheet_EditAndView_Tabhost.addTab(spec);

        ////////////////////////////////////////////////////////////////////////////////////////////

        VS_E_A_V_ContractCB.setEnabled(false);
        VS_E_A_V_CopyToDeliveryCB.setEnabled(false);
        VS_E_A_V_CopyToSalesInvoiceCB.setEnabled(false);

        ////////////////////////Consumable Parts tab////////////////////////////////////

        spec = Visitsheet_EditAndView_Tabhost.newTabSpec("Spare Parts");
        spec.setContent(R.id.Spare_Parts);
        spec.setIndicator("S.Parts");
        Visitsheet_EditAndView_Tabhost.addTab(spec);

        ////////////////////////Personal activity tab////////////////////////////////////

        spec = Visitsheet_EditAndView_Tabhost.newTabSpec("Personal Activity");
        spec.setContent(R.id.Personal_Activity);
        spec.setIndicator("Activity");
        Visitsheet_EditAndView_Tabhost.addTab(spec);

        clsPersonalActivities = new ArrayList<>();
        Personal_Activity_list = findViewById(R.id.Personal_Activity_list);
        GetSlipPersonalActivity(VisitSheetId,CoID);
        VS_E_A_V_AddSpareParts.setVisibility(View.INVISIBLE);
        VS_E_A_V_AddSpareParts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ParammWithURL = Constants.BASE_URL + "GetPriceLevel?doc_sourced_company_id=" + CoID + "&mas_sourced_establishment_id=" + DocVSID;
                Log.d("", ParammWithURL);

                ApiClient.getApiManager(getApplicationContext()).GetPriceLevel(ParammWithURL, new Callback<com.example.fatto7.gtsone.BusinessManager.Models.PriceLevelResponse<PriceLevel>>() {
                    @Override
                    public void onResponse(Call<com.example.fatto7.gtsone.BusinessManager.Models.PriceLevelResponse<PriceLevel>> call, Response<com.example.fatto7.gtsone.BusinessManager.Models.PriceLevelResponse<PriceLevel>> response) {
                        try {
                            if (response.isSuccessful()) {
                                Log.v("RESPONSE_CALLED", "ON_RESPONSE_CALLED");
                                String didItWork = String.valueOf(response.isSuccessful());
                                Log.v("SUCCESS?", didItWork);
                                Log.v("RESPONSE_CODE", String.valueOf(response.code()));
                                if (response.code() >= 500) {
                                    Toast.makeText(getApplicationContext(), "There is somthing error on server", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (response.body().getData().size() == 0) {
                                    return;
                                }
                                Intent i = new Intent(VisitSheet_EditAndView.this, AddSpareParts.class);
                                i.putExtra("mas_sourced_pricelevel_id", response.body().getData().get(0).getPriceLevelId());
                                i.putExtra("mas_sourced_itemmaster_id", itemmaster);
                                i.putExtra("VisitSheetId", VisitSheetId);
                                i.putExtra("VisitSheetId", VisitSheetId);
                                startActivity(i);

                            }
                        } catch (Exception e) {
                            Log.d("", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<com.example.fatto7.gtsone.BusinessManager.Models.PriceLevelResponse<PriceLevel>> call, Throwable t) {

                    }
                });
            }
        });
        Visitsheet_EditAndView_Tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                currentTap = tabId;
                if (tabId.contains("Spare Parts")) {
                    VS_E_A_V_AddSpareParts.setVisibility(View.VISIBLE);
                } else {
                    VS_E_A_V_AddSpareParts.setVisibility(View.INVISIBLE);

                }
            }
        });

        ////////////////////////Logistics tab////////////////////////////////////

        spec = Visitsheet_EditAndView_Tabhost.newTabSpec("logitics");
        spec.setContent(R.id.logitics);
        spec.setIndicator("logistic");
        Visitsheet_EditAndView_Tabhost.addTab(spec);
        VS_E_A_V_LogisticsOwner = findViewById(R.id.VS_E_A_V_LogisticsOwner);
        VS_E_A_V_LogisticsAddressTV = findViewById(R.id.VS_E_A_V_LogisticsAddressTV);
        VS_E_A_V_LogisticsContactPersonTV = findViewById(R.id.VS_E_A_V_LogisticsContactPersonTV);
        VS_E_A_V_LogisticAssignSecTV = findViewById(R.id.VS_E_A_V_LogisticAssignSecTV);

        ////////////////////////Notes tab////////////////////////////////////

        spec = Visitsheet_EditAndView_Tabhost.newTabSpec("Notes");
        spec.setContent(R.id.Notes);
        spec.setIndicator("Notes");
        Visitsheet_EditAndView_Tabhost.addTab(spec);
        clsNotes = new ArrayList<>();
        Notes_List = findViewById(R.id.Notes_List);
        GetSlipNote(VisitSheetId);
        ////////////////////////////////////////////////////////
        spec = Visitsheet_EditAndView_Tabhost.newTabSpec("Total");
        spec.setContent(R.id.Total);
        spec.setIndicator("Total");
        Visitsheet_EditAndView_Tabhost.addTab(spec);

        GetDocVisitSheetApi(VisitSheetId,CoID);


        if (Editable.contains("0")) {


            GetVisitSheetStatusApi();
            VS_E_A_V_TravelTimeBtn.setEnabled(false);
            VS_E_A_V_StartTimeBtn.setEnabled(false);
            VS_E_A_V_EndTimeBtn.setEnabled(false);
            VS_E_A_V_StatusSp.setEnabled(false);
            VS_E_A_V_VisitDateTV.setEnabled(false);
            VS_E_A_V_SaveIV.setEnabled(false);
            VS_E_A_V_SaveandFinishIV.setEnabled(false);
            VS_E_A_V_PostToDeliveryIV.setEnabled(false);
            VS_E_A_V_RemarksET.setEnabled(false);

            VS_E_A_V_MeterReadingTV.setEnabled(false);

        }

        if (Editable.contains("1")) {
            GetVisitSheetStatusEdit();

            VS_E_A_V_RemarksET.setEnabled(true);
            VS_E_A_V_TravelTimeBtn.setEnabled(true);
            VS_E_A_V_StartTimeBtn.setEnabled(true);
            VS_E_A_V_EndTimeBtn.setEnabled(true);
            VS_E_A_V_StatusSp.setEnabled(true);
            VS_E_A_V_MeterReadingTV.setEnabled(true);

        }


        for (int i = 0; i < Visitsheet_EditAndView_Tabhost.getTabWidget().getChildCount(); i++) {

            final TextView tv = Visitsheet_EditAndView_Tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTypeface(gillsansFont);
            tv.setTextColor(Color.WHITE);

        }


        logOut = findViewById(R.id.logout);
        visitsheetTitle = findViewById(R.id.visit_sheet_title);

        logOut.setTypeface(gillsansFont);
        visitsheetTitle.setTypeface(gillsansFont);


        // logout button
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builderSingle = new AlertDialog.Builder(VisitSheet_EditAndView.this);

                builderSingle.setTitle("Select An Action");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(VisitSheet_EditAndView.this, android.R.layout.select_dialog_item);
                arrayAdapter.add("   Log Out");


                builderSingle.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(VisitSheet_EditAndView.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Are you sure you want to logout?");
                        builderInner.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences preferences = getSharedPreferences(mypreference, 0);
                                preferences.edit().remove(uNamekey).commit();
                                preferences.edit().remove(uPassword).commit();
                                preferences.edit().remove(uCompanyId).commit();
                                preferences.edit().remove(uEstablishmentID).commit();
                                Intent intent = new Intent(VisitSheet_EditAndView.this, GTS_Logo.class);
                                startActivity(intent);
                                finish();
                            }

                        });
                        builderInner.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }

                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();

            }
        });




        Calendar calendar = Calendar.getInstance();
        String CurrentDate = DateFormat.getDateInstance().format(calendar.getTime());

        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        VS_E_A_V_VisitDateTV.setText(date);

        /////////////////////////////////////////////////////////////////////////////////

        VS_E_A_V_StatusSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Editable.contains("0")) {
                    Status = String.valueOf(VisitSheetStatusList.get(position).getId());
                } else {
                    Status = String.valueOf(VisitSheetStatusListEdit.get(position).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        VS_E_A_V_SaveIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("current Tab", currentTap);

                //                //  new saveAttachementMetaData().execute(CoID, ESTID);
                if (Editable.contains("0")) {
                    Intent intent = new Intent(VisitSheet_EditAndView.this, VisitSheet.class);
                    startActivity(intent);
                } else {

                     UpdateDocVisitSheet_Save_Only(VisitSheetId, Status, VS_E_A_V_MeterReadingTV.getText().toString(),
                            reportno, reportdate, VS_E_A_V_RemarksET.getText().toString());

                }


            }
        });


        VS_E_A_V_PostToDeliveryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int x = allexistingSpareParts.size();
                String ok = "";
                if (x <= 0) {
                    ok = "0";
                    Toast.makeText(VisitSheet_EditAndView.this, "No Spare Parts to be Delivered", Toast.LENGTH_LONG);
                } else {
                    ok = "0";
                    for (int i = 0; i < x; i++) {
                        if (allexistingSpareParts.get(i).getStatus().contains("Not Delivered")) {
                            ok = "1";
                        }
                    }
                }

                if (ok.contains("0")) {
                    Toast.makeText(VisitSheet_EditAndView.this, "No Spare Parts to be Delivered", Toast.LENGTH_SHORT).show();
                } else {
                     UpdateDocVisitSheet_Post_To_Delivery(VisitSheetId, VS_E_A_V_MeterReadingTV.getText().toString(),
                            reportno, reportdate, VS_E_A_V_RemarksET.getText().toString());
                }


            }
        });


        VS_E_A_V_SaveandFinishIV.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {


                int x = allexistingSpareParts.size();
                String ok = "";
                for (int i = 0; i < x; i++) {
                    String zw = allexistingSpareParts.get(i).getStatus();
                    if (zw.contains("Not Delivered")) {
                        String Type = allexistingSpareParts.get(i).getType();
                        if (Type.contains("1")) {
                            ok = "1";
                        } else if (Type.contains("2")) {
                            float unitprice = Float.valueOf(allexistingSpareParts.get(i).getUnit_Price());
                            if (unitprice > 0)
                                ok = "1";
                            else
                                ok = "2";
                        }

                    }else {
                        ok = "2";

                    }
                }
                String travelTime = VS_E_A_V_TravelTimeTV.getText() == null ? "" : VS_E_A_V_TravelTimeTV.getText().toString();
                String startTime = VS_E_A_V_StartTimeTV.getText() == null ? "" : VS_E_A_V_StartTimeTV.getText().toString();
                if (travelTime.isEmpty() || startTime.isEmpty()) {
                    Toast.makeText(VisitSheet_EditAndView.this, "Not Allowed to Finish Job", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ok.equals("1") || ok.isEmpty()) {
                    Toast.makeText(VisitSheet_EditAndView.this, "Not Allowed to Finish Job", Toast.LENGTH_SHORT).show();
                } else {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm aa");
                    String EndTime = format.format(calendar.getTime());
                    VS_E_A_V_EndTimeTV.setText(EndTime);


                    if (ActivityCompat.checkSelfPermission(VisitSheet_EditAndView.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }

                    Endclient.getLastLocation().addOnSuccessListener(VisitSheet_EditAndView.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            String EndLongitude, EndLatitude, EndLocation = null;
                            if (location != null) {


                                EndLongitude = String.valueOf(location.getLongitude());
                                EndLatitude = String.valueOf(location.getLatitude());
                                EndLocation = EndLongitude + "#" + EndLatitude;
                                //Toast.makeText(VisitSheet_EditAndView.this,EndLocation,Toast.LENGTH_SHORT).show();
                            }
                            SetEndTimeAndLocation(VisitSheetId, VS_E_A_V_EndTimeTV.getText().toString(), EndLocation);
                        }
                    });


                }


            }
        });


        VS_E_A_V_CloseIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Editable.contains("1")) {
                    //new UpdateDocVisitSheet_ToOpened().execute(VisitSheetId, "1");
                    finish();
                } else {
//                    Intent intent = new Intent(VisitSheet_EditAndView.this, VisitSheet.class);
//                    startActivity(intent);
                    finish();

                }


            }
        });


        Requestpermision();
        Startclient = LocationServices.getFusedLocationProviderClient(VisitSheet_EditAndView.this);

        VS_E_A_V_StartTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm aa");
                String StartTime = format.format(calendar.getTime());
                VS_E_A_V_StartTimeTV.setText(StartTime);

                if (ActivityCompat.checkSelfPermission(VisitSheet_EditAndView.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                Startclient.getLastLocation().addOnSuccessListener(VisitSheet_EditAndView.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        String StartLongitude, StartLatitude, StartLocation = null;
                        if (location != null) {

                            StartLongitude = String.valueOf(location.getLongitude());
                            StartLatitude = String.valueOf(location.getLatitude());
                            StartLocation = StartLongitude + "#" + StartLatitude;
                            SetStartTimeAndLocation(VisitSheetId, VS_E_A_V_StartTimeTV.getText().toString(), StartLocation);
                            // Toast.makeText(VisitSheet_EditAndView.this,StartLongitude+"##"+StartLatitude,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        Travelclient = LocationServices.getFusedLocationProviderClient(VisitSheet_EditAndView.this);
        VS_E_A_V_TravelTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm aa");
                String TravelTime = format.format(calendar.getTime());
                VS_E_A_V_TravelTimeTV.setText(TravelTime);


                if (ActivityCompat.checkSelfPermission(VisitSheet_EditAndView.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                Travelclient.getLastLocation().addOnSuccessListener(VisitSheet_EditAndView.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        String TravelLongitude, TravelLatitude, TravelLocation = null;
                        if (location != null) {

                            TravelLongitude = String.valueOf(location.getLongitude());
                            TravelLatitude = String.valueOf(location.getLatitude());
                            TravelLocation = TravelLongitude + "#" + TravelLatitude;
                            SetTravelTimeAndLocation(VisitSheetId,VS_E_A_V_TravelTimeTV.getText().toString(), TravelLocation);
                            // Toast.makeText(VisitSheet_EditAndView.this,TravelLongitude+"##"+TravelLatitude,Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


        Endclient = LocationServices.getFusedLocationProviderClient(VisitSheet_EditAndView.this);
        VS_E_A_V_EndTimeBtn.setVisibility(View.GONE);


        VS_E_A_V_VisitDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                final int year = c.get(Calendar.YEAR);
                final int month = c.get(Calendar.MONTH);
                final int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(VisitSheet_EditAndView.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthofyear, int dayofmonth) {
                        VS_E_A_V_VisitDateTV.setText(dayofmonth + "/" + monthofyear + "/" + year);
                    }
                }, year, month, day);
                datePicker.setTitle("select date");
                datePicker.show();

            }
        });


    }
    private void GetVisitSheetStatusApi() {
        String url = BASE_URL + "GetVisitSheetStatus";

        ApiClient.getApiManager(getApplicationContext()).GetVisitSheetStatus(url, new Callback<ResponseBody>() {
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
                    JSONArray jsonArr = json.getJSONArray("VisitSheetStatus");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        json = jsonArr.getJSONObject(i);
                        clsVisitSheetStatus objStatus = new clsVisitSheetStatus();
                        objStatus.setId(json.getString("id"));
                        objStatus.setName(json.getString("Name"));
                        VisitSheetStatusList.add(objStatus);
                    }
                    SpinnerAdapterVisitsheetStatus StatusAdapter = new SpinnerAdapterVisitsheetStatus(VisitSheet_EditAndView.this, 0, VisitSheetStatusList);
                    VS_E_A_V_StatusSp.setAdapter(StatusAdapter);
                    if (statusItem.isEmpty()) {
                        return;
                    }
                    for (int i = 0; i < VisitSheetStatusList.size(); i++) {
                        clsVisitSheetStatus item = VisitSheetStatusList.get(i);
                        if (item.getId().isEmpty()) {
                            continue;
                        }
                        if (item.getId().equals(statusItem)) {
                            VS_E_A_V_StatusSp.setSelection(i);
                            break;
                        }
                    }

                } catch (Exception e) {

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("",t.toString());

            }
        });
    }


    private void GetDocVisitSheetApi(String visitSheetId, String coID) {
                String url = BASE_URL + "GetDocVisitSheet";

        ApiClient.getApiManager(getApplicationContext()).GetDocVisitSheet(url,coID, visitSheetId,   new Callback<ResponseBody>() {
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
                    JSONArray jsonArr = json.getJSONArray("DocVisitSheet");

                    for (int i = 0; i < jsonArr.length(); i++) {
                        json = jsonArr.getJSONObject(i);

                        clsVisitSheet_EditAndViewDashboard VisitSheetEditAndViewDashboard = new clsVisitSheet_EditAndViewDashboard();

                        VisitSheetEditAndViewDashboard.setId(json.getString("id"));
                        VisitSheetEditAndViewDashboard.setNum(json.getString("num"));
                        VisitSheetEditAndViewDashboard.setMap_sourced_visitsheet_status_id(json.getString("map_sourced_visitsheet_status_id"));
                        VisitSheetEditAndViewDashboard.setMethod(json.getString("method"));
                        VisitSheetEditAndViewDashboard.setMethod_info(json.getString("method info"));
                        VisitSheetEditAndViewDashboard.setCustomer_name(json.getString("customer name"));
                        VisitSheetEditAndViewDashboard.setAddress(json.getString("address"));
                        VisitSheetEditAndViewDashboard.setAddress_details(json.getString("address details"));
                        VisitSheetEditAndViewDashboard.setContact_person(json.getString("contact person"));
                        VisitSheetEditAndViewDashboard.setContact_details(json.getString("contact details"));
                        VisitSheetEditAndViewDashboard.setContract(json.getString("contract"));
                        VisitSheetEditAndViewDashboard.setCurrency(json.getString("currency"));
                        VisitSheetEditAndViewDashboard.setAssigned_to(json.getString("assigned to"));
                        VisitSheetEditAndViewDashboard.setOwner(json.getString("owner"));
                        VisitSheetEditAndViewDashboard.setTechnician(json.getString("technician"));
                        VisitSheetEditAndViewDashboard.setVisit_type(json.getString("visit type"));
                        VisitSheetEditAndViewDashboard.setVisit_status(json.getString("visit status"));
                        VisitSheetEditAndViewDashboard.setPriority(json.getString("priority"));
                        VisitSheetEditAndViewDashboard.setRefno(json.getString("refno"));
                        VisitSheetEditAndViewDashboard.setOrderdate(json.getString("orderdate"));
                        VisitSheetEditAndViewDashboard.setOrdertime(json.getString("ordertime"));
                        VisitSheetEditAndViewDashboard.setOrderday(json.getString("orderday"));
                        VisitSheetEditAndViewDashboard.setDuedate(json.getString("duedate"));
                        VisitSheetEditAndViewDashboard.setDocdate(json.getString("docdate"));
                        VisitSheetEditAndViewDashboard.setEnddate(json.getString("enddate"));
                        VisitSheetEditAndViewDashboard.setCanceldate(json.getString("canceldate"));
                        VisitSheetEditAndViewDashboard.setTraveltime(json.getString("traveltime"));
                        VisitSheetEditAndViewDashboard.setTravellocation(json.getString("travellocation"));
                        VisitSheetEditAndViewDashboard.setStarttime(json.getString("starttime"));
                        VisitSheetEditAndViewDashboard.setStartloacation(json.getString("startloacation"));
                        VisitSheetEditAndViewDashboard.setEndtime(json.getString("endtime"));
                        VisitSheetEditAndViewDashboard.setEndloacation(json.getString("endloacation"));
                        VisitSheetEditAndViewDashboard.setMas_sourced_itemmaster_id(json.getString("mas_sourced_itemmaster_id"));
                        VisitSheetEditAndViewDashboard.setItemcode(json.getString("itemcode"));
                        VisitSheetEditAndViewDashboard.setItemname(json.getString("itemname"));
                        VisitSheetEditAndViewDashboard.setItemgroup(json.getString("itemgroup"));
                        VisitSheetEditAndViewDashboard.setItemstatus(json.getString("itemstatus"));
                        VisitSheetEditAndViewDashboard.setItemtype(json.getString("itemtype"));
                        VisitSheetEditAndViewDashboard.setSerialno(json.getString("serialno"));
                        VisitSheetEditAndViewDashboard.setMeterreading(json.getString("meterreading"));
                        VisitSheetEditAndViewDashboard.setReportno(json.getString("reportno"));
                        VisitSheetEditAndViewDashboard.setReportdate(json.getString("reportdate"));
                        VisitSheetEditAndViewDashboard.setRemarks(json.getString("remarks"));
                        VisitSheetEditAndViewDashboard.setDiscountamount(json.getString("discountamount"));
                        VisitSheetEditAndViewDashboard.setDiscountpercent(json.getString("discountpercent"));
                        VisitSheetEditAndViewDashboard.setTotaldownpayment(json.getString("totaldownpayment"));
                        VisitSheetEditAndViewDashboard.setTotalbeforediscount(json.getString("totalbeforediscount"));
                        VisitSheetEditAndViewDashboard.setFreighttotal(json.getString("freighttotal"));
                        VisitSheetEditAndViewDashboard.setLinetotaltax(json.getString("linetotaltax"));
                        VisitSheetEditAndViewDashboard.setFreighttax(json.getString("freighttax"));
                        VisitSheetEditAndViewDashboard.setGrandtax(json.getString("grandtax"));
                        VisitSheetEditAndViewDashboard.setGrandtotal(json.getString("grandtotal"));
                        VisitSheetEditAndViewDashboard.setGrandtotallc(json.getString("grandtotallc"));
                        VisitSheetEditAndViewDashboard.setGrandtotalfc(json.getString("grandtotalfc"));
                        statusItem = VisitSheetEditAndViewDashboard.getMap_sourced_visitsheet_status_id();
                        //VisitSheetEditAndViewDashboard.setReturnedfromvisitsheet(json.getString("returnedfromvisitsheet"));
                        // VisitSheetEditAndViewDashboard.setCopytodelivery(json.getString("copytodelivery"));
                        // VisitSheetEditAndViewDashboard.setCopytosalesinvoice(json.getString("copytosalesinvoice"));
                        allexistingdatalist.add(VisitSheetEditAndViewDashboard);

                    }

                    int allinputdata = allexistingdatalist.size();
                    for (int i = 0; i < allinputdata; i++) {
                        VS_E_A_V_CustomerTV.setText(allexistingdatalist.get(i).getCustomer_name());
                        VS_E_A_V_AdressTV.setText(allexistingdatalist.get(i).getAddress());
                        VS_E_A_V_ContactPersonTV.setText(allexistingdatalist.get(i).getContact_person());
                        VS_E_A_V_CurrencyTV.setText(allexistingdatalist.get(i).getCurrency());
                        VS_E_A_V_ItemNameTV.setText(allexistingdatalist.get(i).getItemname());
                        VS_E_A_V_SerialNoTV.setText(allexistingdatalist.get(i).getSerialno());
                        VS_E_A_V_MeterReadingTV.setText(allexistingdatalist.get(i).getMeterreading());
                        VS_E_A_V_VisitTypeTV.setText(allexistingdatalist.get(i).getVisit_type());
                        VS_E_A_V_NumberingTV.setText(allexistingdatalist.get(i).getNum());
                        VS_E_A_V_TravelTimeTV.setText(allexistingdatalist.get(i).getTraveltime());
                        VS_E_A_V_StartTimeTV.setText(allexistingdatalist.get(i).getStarttime());
                        VS_E_A_V_EndTimeTV.setText(allexistingdatalist.get(i).getEndtime());

                        VS_E_A_V_TechnicalTV.setText(allexistingdatalist.get(i).getTechnician());
                        VS_E_A_V_RemarksET.setText(allexistingdatalist.get(i).getRemarks());
                        VS_E_A_V_TotalLinesTV.setText(String.format(Float.valueOf(allexistingdatalist.get(i).getTotalbeforediscount()).toString()));
                        VS_E_A_V_DiscountTV.setText(String.format(Float.valueOf(allexistingdatalist.get(i).getDiscountpercent()).toString()));
                        // VS_E_A_V_AmountTV.setText(String.format(Float.valueOf(allexistingdatalist.get(i).getDiscountamount()).toString()));
                        VS_E_A_V_TotalTaxTV.setText(String.format(Float.valueOf(allexistingdatalist.get(i).getLinetotaltax()).toString()));
                        VS_E_A_V_GrossTotalfCTV.setText(String.format(Float.valueOf(allexistingdatalist.get(i).getGrandtotalfc()).toString()));
                        VS_E_A_V_GrossTotalLCTV.setText(String.format(Float.valueOf(allexistingdatalist.get(i).getGrandtotallc()).toString()));

                        VS_E_A_V_LogisticsOwner.setText(allexistingdatalist.get(i).getOwner());
                        VS_E_A_V_LogisticsAddressTV.setText(allexistingdatalist.get(i).getAddress());
                        VS_E_A_V_LogisticAssignSecTV.setText(allexistingdatalist.get(i).getAssigned_to());
                        VS_E_A_V_LogisticsContactPersonTV.setText(allexistingdatalist.get(i).getContact_person());
                        reportno = allexistingdatalist.get(i).getReportno();
                        reportdate = allexistingdatalist.get(i).getReportdate();
                        owner = allexistingdatalist.get(i).getOwner();
                        technician = allexistingdatalist.get(i).getTechnician();
                        itemmaster = allexistingdatalist.get(i).getMas_sourced_itemmaster_id();
                        //check Time if already set or not
                        if (TextUtils.isEmpty(VS_E_A_V_TravelTimeTV.getText())) {

                        } else {
                            VS_E_A_V_TravelTimeBtn.setEnabled(false);
                        }
                        if (TextUtils.isEmpty(VS_E_A_V_StartTimeTV.getText())) {

                        } else {
                            VS_E_A_V_StartTimeBtn.setEnabled(false);
                        }
                        if (TextUtils.isEmpty(VS_E_A_V_EndTimeTV.getText())) {

                        } else {
                            VS_E_A_V_EndTimeBtn.setEnabled(false);
                        }

                        //set Check Boxes Enabled OR Disabled
                        String copyToDelivery = allexistingdatalist.get(i).getCopytodelivery();
                        String copyToSalesInvoice = allexistingdatalist.get(i).getCopytosalesinvoice();
                        String contract = allexistingdatalist.get(i).getContract();
                        String returnedFromVisitsheet = allexistingdatalist.get(i).getReturnedfromvisitsheet();

                        if (copyToDelivery != null && copyToDelivery.contains("True")) {

                            VS_E_A_V_CopyToDeliveryCB.setChecked(true);

                        } else {

                            VS_E_A_V_CopyToDeliveryCB.setChecked(false);

                        }
                        if (contract != null && contract.contains("True")) {

                            VS_E_A_V_ContractCB.setChecked(true);

                        } else {

                            VS_E_A_V_ContractCB.setChecked(false);

                        }
                        if (copyToSalesInvoice != null && copyToSalesInvoice.contains("True")) {

                            VS_E_A_V_CopyToSalesInvoiceCB.setChecked(true);

                        } else {

                            VS_E_A_V_CopyToSalesInvoiceCB.setChecked(false);

                        }


                    }


                } catch (Exception e) {

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("",t.toString());

            }
        });
    }


    //////////////////////////after on create///////////////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {

    }


    private void Requestpermision() {
        ActivityCompat.requestPermissions(VisitSheet_EditAndView.this, new String[]{ACCESS_FINE_LOCATION}, 1);

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
    protected void onStart() {
        super.onStart();
        EditAndViewtb1.removeAllViews();
        GetSlipSpareParts(VisitSheetId);
    }

    private void  GetSlipSpareParts(String doc_sourced_visitsheet_id){
        String url = BASE_URL + "GetSlipSpareParts";
        allexistingSpareParts.clear();
        ApiClient.getApiManager(getApplicationContext()).GetSlipSpareParts(url, doc_sourced_visitsheet_id, new Callback<ResponseBody>() {
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
                    JSONArray jsonArr = json.getJSONArray("SlipSpareParts");
                    if (jsonArr.length() > 0) {
                        SparePartsQuantity = "true";

                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);

                            clsSparePartsDashboard EditAndViewSpareParts = new clsSparePartsDashboard();
                            EditAndViewSpareParts.setId(json.getString("id"));
                            EditAndViewSpareParts.setVoid(json.getString("Void"));
                            EditAndViewSpareParts.setEmployee_Sales(json.getString("Employee Sales"));
                            EditAndViewSpareParts.setEmployee_Owner(json.getString("Employee Owner"));
                            EditAndViewSpareParts.setTax(json.getString("Tax"));
                            EditAndViewSpareParts.setWarehouseId(json.getString("WarehouseId"));
                            EditAndViewSpareParts.setBinId(json.getString("BinId"));
                            EditAndViewSpareParts.setItemId(json.getString("ItemId"));
                            EditAndViewSpareParts.setAccount_Code(json.getString("Account Code"));
                            EditAndViewSpareParts.setAccount_Code_COGS(json.getString("Account Code COGS"));
                            EditAndViewSpareParts.setLine_of_Business_COGS(json.getString("Line of Business COGS"));
                            EditAndViewSpareParts.setProject(json.getString("Project"));
                            EditAndViewSpareParts.setPrice_Level(json.getString("Price Level"));
                            EditAndViewSpareParts.setShipping(json.getString("Shipping"));
                            EditAndViewSpareParts.setValuationMethod(json.getString("ValuationMethod"));
                            EditAndViewSpareParts.setDepartmentCOGS(json.getString("DepartmentCOGS"));
                            EditAndViewSpareParts.setLine_Of_Business(json.getString("Line Of Business"));
                            // EditAndViewSpareParts.setDepartment(json.getString("Department"));
                            EditAndViewSpareParts.setUOMId(json.getString("UOMId"));
                            EditAndViewSpareParts.setSlipBlanketAgreementId(json.getString("SlipBlanketAgreementId"));
                            EditAndViewSpareParts.setSlipQuotationId(json.getString("SlipQuotationId"));
                            EditAndViewSpareParts.setSlipOrderId(json.getString("SlipOrderId"));
                            EditAndViewSpareParts.setSlipDeliveryId(json.getString("SlipDeliveryId"));
                            EditAndViewSpareParts.setSerialId(json.getString("SerialId"));
                            EditAndViewSpareParts.setLotId(json.getString("LotId"));
                            EditAndViewSpareParts.setItem_No(json.getString("Item No."));
                            EditAndViewSpareParts.setItem_Name(json.getString("Item Name"));
                            EditAndViewSpareParts.setBarcode(json.getString("Barcode"));
                            EditAndViewSpareParts.setMfg_No(json.getString("Mfg No."));
                            EditAndViewSpareParts.setSerial_No(json.getString("Serial No"));
                            EditAndViewSpareParts.setWarehouse(json.getString("Warehouse"));
                            EditAndViewSpareParts.setBin(json.getString("Bin"));
                            EditAndViewSpareParts.setDelivery_Date(json.getString("Delivery Date"));
                            EditAndViewSpareParts.setActual_Delivery_Date(json.getString("Actual Delivery Date"));
                            EditAndViewSpareParts.setExpiration_Date(json.getString("Expiration Date"));
                            EditAndViewSpareParts.setAccount(json.getString("Account"));
                            EditAndViewSpareParts.setAccount_COGS(json.getString("Account COGS"));
                            EditAndViewSpareParts.setRemarks(json.getString("Remarks"));
                            EditAndViewSpareParts.setUOM(json.getString("UOM"));
                            EditAndViewSpareParts.setLot_No(json.getString("Lot No"));
                            EditAndViewSpareParts.setMultiUOM(json.getString("MultiUOM"));
                            EditAndViewSpareParts.setConsumable_Sales_Forecast(json.getString("Consumable Sales Forecast"));
                            EditAndViewSpareParts.setNo_of_Package(json.getString("No of Package"));
                            EditAndViewSpareParts.setCommission(json.getString("Commission %"));
                            EditAndViewSpareParts.setTax_Only(json.getString("Tax Only"));
                            EditAndViewSpareParts.setQuantity(json.getString("Quantity"));
                            EditAndViewSpareParts.setQuantity_To_Ship(json.getString("Quantity To Ship"));
                            EditAndViewSpareParts.setOrdered_Quantity(json.getString("Ordered Quantity"));
                            EditAndViewSpareParts.setQuantity_In_Stock(json.getString("Quantity In Stock"));
                            EditAndViewSpareParts.setQuantity_In_Stock_UOM(json.getString("Quantity In Stock UOM"));
                            EditAndViewSpareParts.setItem_Per_Unit(json.getString("Item Per Unit"));
                            EditAndViewSpareParts.setUnit_Price(json.getString("Unit Price"));
                            EditAndViewSpareParts.setUnit_Price_Fc(json.getString("Unit Price Fc"));
                            EditAndViewSpareParts.setOriginal_Price(json.getString("Original Price"));
                            EditAndViewSpareParts.setDiscount_Amount(json.getString("Discount Amount"));
                            EditAndViewSpareParts.setDiscount_Percent(json.getString("Discount Percent"));
                            EditAndViewSpareParts.setTax_Percent(json.getString("Tax Percent"));
                            EditAndViewSpareParts.setPrice_After_Discount(json.getString("Price After Discount"));
                            EditAndViewSpareParts.setTax_Amount(json.getString("Tax Amount"));
                            EditAndViewSpareParts.setTotal_Price(json.getString("Total Price"));
                            EditAndViewSpareParts.setGross_Price(json.getString("Gross Price"));
                            EditAndViewSpareParts.setTax_Amount_Doc(json.getString("Tax Amount Doc"));
                            EditAndViewSpareParts.setTotal_Doc(json.getString("Total Doc"));
                            EditAndViewSpareParts.setGross_Doc(json.getString("Gross Doc"));
                            EditAndViewSpareParts.setGross_Doc_Lc(json.getString("Gross Doc Lc"));
                            EditAndViewSpareParts.setGross_Doc_Fc(json.getString("Gross Doc Fc"));
                            EditAndViewSpareParts.setItem_Stat(json.getString("Item Stat"));
                            EditAndViewSpareParts.setCost(json.getString("Cost"));
                            // EditAndViewSpareParts.setStatus(json.getString("Status"));
                            EditAndViewSpareParts.setStatus(json.getString("Status Name"));
                            EditAndViewSpareParts.setType(json.getString("Type"));
                            EditAndViewSpareParts.setSeq(json.getString("Seq"));
                            EditAndViewSpareParts.setWarranty(json.getString("Warranty"));
                            EditAndViewSpareParts.setBOM_Type(json.getString("BOM Type"));

                            allexistingSpareParts.add(EditAndViewSpareParts);

                        }
                    } else {
                        SparePartsQuantity = "false";
                    }
                    if (SparePartsQuantity.contains("true")) {
                        addHeaders();
                        int allinputdata = allexistingSpareParts.size();
                        Partsquantity = allexistingSpareParts.size();
                        for (int i = 0; i < allinputdata; i++) {

                            TableRow SparePartsrow = new TableRow(VisitSheet_EditAndView.this);
                            final int ii = i;
                            SparePartsrow.setLayoutParams(getLayoutparams());
                            SparePartsrow.addView(getRowTextView(0, allexistingSpareParts.get(i).getSeq(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(VisitSheet_EditAndView.this, R.color.cell_background_coloe)));
                            SparePartsrow.addView(getRowTextView(0, allexistingSpareParts.get(i).getBarcode(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(VisitSheet_EditAndView.this, R.color.cell_background_coloe)));
                            SparePartsrow.addView(getRowTextView(2, allexistingSpareParts.get(i).getItem_No(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(VisitSheet_EditAndView.this, R.color.cell_background_coloe)));
                            SparePartsrow.addView(getRowTextView(3, allexistingSpareParts.get(i).getItem_Name(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(VisitSheet_EditAndView.this, R.color.cell_background_coloe)));
                            SparePartsrow.addView(getRowTextView(5, String.format("%.3f", Float.valueOf(allexistingSpareParts.get(i).getUnit_Price())), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(VisitSheet_EditAndView.this, R.color.cell_background_coloe)));
                            SparePartsrow.addView(getRowTextView(2, String.format("%.3f", Float.valueOf(allexistingSpareParts.get(i).getGross_Doc())), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(VisitSheet_EditAndView.this, R.color.cell_background_coloe)));
                            SparePartsrow.addView(getRowTextView(2, allexistingSpareParts.get(i).getStatus(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(VisitSheet_EditAndView.this, R.color.cell_background_coloe)));
                            SparePartsrow.addView(getRowTextView(2, String.format("%.3f", Float.valueOf(allexistingSpareParts.get(i).getQuantity())), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(VisitSheet_EditAndView.this, R.color.cell_background_coloe)));
                            SparePartsrow.addView(getRowTextView(2, String.format("%.3f", Float.valueOf(allexistingSpareParts.get(i).getTax_Amount())), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(VisitSheet_EditAndView.this, R.color.cell_background_coloe)));
                            EditAndViewtb1.addView(SparePartsrow, gettblLayoutParams());


                        }
                    } else {
                        //  Toast.makeText(VisitSheet_EditAndView.this,"NO spare parts",Toast.LENGTH_LONG).show();

                    }
                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
    private void  GetVisitSheetStatusEdit(){
        VisitSheetStatusListEdit.clear();
        String url = BASE_URL + "GetVisitSheetStatusForEdit";
        ApiClient.getApiManager(getApplicationContext()).GetVisitSheetStatusEdit(url, new Callback<ResponseBody>() {
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
               try {
                   JSONObject json = jParser.convertResponsToJSON(response.body());
                   JSONArray jsonArr = json.getJSONArray("VisitSheetStatusForEdit");

                   for (int i = 0; i < jsonArr.length(); i++) {
                       json = jsonArr.getJSONObject(i);
                       clsVisitSheetStatusEdit objStatusEdit = new clsVisitSheetStatusEdit();
                       objStatusEdit.setId(json.getString("id"));
                       objStatusEdit.setName(json.getString("Name"));
                       VisitSheetStatusListEdit.add(objStatusEdit);
                   }
                   SpinnerAdapterVisitSheetStatusEdit StatusEditAdapter = new SpinnerAdapterVisitSheetStatusEdit(VisitSheet_EditAndView.this, 0, VisitSheetStatusListEdit);
                   VS_E_A_V_StatusSp.setAdapter(StatusEditAdapter);
                   if (statusItem.isEmpty()) {
                       return;
                   }
                   for (int i = 0; i < VisitSheetStatusListEdit.size(); i++) {
                       clsVisitSheetStatusEdit item = VisitSheetStatusListEdit.get(i);
                       if (item.getId().isEmpty()) {
                           continue;
                       }
                       if (item.getId().equals(statusItem)) {
                           VS_E_A_V_StatusSp.setSelection(i);
                           break;
                       }
                   }
                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
    private void  GetSlipPersonalActivity(String doc_sourced_visitsheet_id ,String doc_sourced_company_id ){
        clsPersonalActivities.clear();
        String url = BASE_URL + "GetSlipPersonalActivity";
        ApiClient.getApiManager(getApplicationContext()).GetSlipPersonalActivity(url,doc_sourced_visitsheet_id,doc_sourced_company_id, new Callback<ResponseBody>() {
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
                try {
                    JSONObject json = jParser.convertResponsToJSON(response.body());
                    JSONArray jsonArr = json.getJSONArray("SlipPersonalActivity");

                    if (jsonArr.length() > 0) {
                        ItemsQuantity = "true";
                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);
                            clsPersonalActivity objPersonalActivity = new clsPersonalActivity();
                            objPersonalActivity.setId(json.getString("id"));
                            objPersonalActivity.setSeq(json.getString("Seq"));
                            objPersonalActivity.setVoid(json.getString("void"));
                            objPersonalActivity.setDoc_sourced_joborder_id(json.getString("doc_sourced_joborder_id"));
                            objPersonalActivity.setMas_sourced_personalactivity_id(json.getString("mas_sourced_personalactivity_id"));
                            objPersonalActivity.setMas_sourced_group_id(json.getString("mas_sourced_group_id"));
                            objPersonalActivity.setItemGroup(json.getString("ItemGroup"));
                            objPersonalActivity.setProcedure(json.getString("Procedure"));
                            objPersonalActivity.setPerformed(json.getString("Performed"));
                            objPersonalActivity.setRemarks(json.getString("Remarks"));
                            clsPersonalActivities.add(objPersonalActivity);
                        }
                    }else {
                        ItemsQuantity = "false";
                    }
                    if (ItemsQuantity.contains("true")) {
                        PALAdapter = new PersonalActivityListAdaper(VisitSheet_EditAndView.this, clsPersonalActivities);
                        Personal_Activity_list.setAdapter(PALAdapter);

                    }
                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
    private void  GetSlipNote(String doc_sourced_visitsheet_id  ){
        clsNotes.clear();
        String url = BASE_URL + "GetSlipNote";
        ApiClient.getApiManager(getApplicationContext()).GetSlipNote(url,doc_sourced_visitsheet_id, new Callback<ResponseBody>() {
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
                try {
                    JSONObject json = jParser.convertResponsToJSON(response.body());
                    JSONArray jsonArr = json.getJSONArray("SlipNote");
                    try {
                        if (jsonArr.length() > 0) {
                            NoteQuantity = "true";
                            for (int i = 0; i < jsonArr.length(); i++) {
                                json = jsonArr.getJSONObject(i);

                                clsNotes objNotes = new clsNotes();

                                objNotes.setId(json.getString("id"));
                                objNotes.setDeleted(json.getString("deleted"));
                                objNotes.setVoid(json.getString("void"));
                                objNotes.setTransmit(json.getString("transmit"));
                                objNotes.setDoc_source_company_id(json.getString("doc_source_company_id"));
                                objNotes.setMas_source_establishment_id(json.getString("mas_source_establishment_id"));
                                objNotes.setMas_source_station_id(json.getString("mas_source_station_id"));
                                objNotes.setMas_source_function_id(json.getString("mas_source_function_id"));
                                objNotes.setDoc_source_userid_id(json.getString("doc_source_userid_id"));
                                objNotes.setDatecreate(json.getString("datecreate"));
                                objNotes.setNotedate(json.getString("notedate"));
                                objNotes.setMessage(json.getString("message"));
                                objNotes.setMap_sourced_entryprefix_id(json.getString("map_sourced_entryprefix_id"));
                                objNotes.setDoc_sourced_id(json.getString("doc_sourced_id"));
                                objNotes.setTransdesc(json.getString("transdesc"));
                                objNotes.setUname(json.getString("uname"));
                                objNotes.setFunction(json.getString("function"));
                                objNotes.setStation(json.getString("station"));
                                objNotes.setMas_sourced_notetype_id(json.getString("mas_sourced_notetype_id"));
                                objNotes.setNotetype(json.getString("notetype"));
                                objNotes.setSeq(json.getString("Seq"));
                                objNotes.setIncludesprint(json.getString("includesprint"));
                                clsNotes.add(objNotes);
                            }
                        } else {
                            NoteQuantity = "false";
                        }

                    } catch (JSONException e) {

                    }
                    if (NoteQuantity.contains("true")) {
                        NoteAdapter = new NotesActivityListAdapter(VisitSheet_EditAndView.this, Editable, clsNotes);
                        Notes_List.setAdapter(NoteAdapter);

                    }
                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
    private void  SetTravelTimeAndLocation(String doc_sourced_visitsheet_id ,String traveltime,String travellocation) {
     String url = BASE_URL + "SetTravelTimeAndLocation";
        ApiClient.getApiManager(getApplicationContext()).SetTravelTimeAndLocation(url,doc_sourced_visitsheet_id,traveltime,travellocation, new Callback<ResponseBody>() {
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
                try {
                    JSONObject json = jParser.convertResponsToJSON(response.body());
                    JSONArray jsonArr = json.getJSONArray("SetTravelTimeAndLocation");
                    if (jsonArr.length() > 0) {
                        success = "true";
                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);


                        }
                    } else {
                        success = "false";
                    }
                    if (success.contains("true")) {
                        Toast.makeText(VisitSheet_EditAndView.this, "Travel Time set correctly", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(VisitSheet_EditAndView.this, "Error with Set Travel Time ", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void  SetStartTimeAndLocation(String doc_sourced_visitsheet_id ,String starttime,String startlocation) {
        String url = BASE_URL + "SetStartTimeAndLocation";
        ApiClient.getApiManager(getApplicationContext()).SetStartTimeAndLocation(url,doc_sourced_visitsheet_id,starttime,startlocation, new Callback<ResponseBody>() {
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
                try {
                    JSONObject json = jParser.convertResponsToJSON(response.body());
                    JSONArray jsonArr = json.getJSONArray("SetStartTimeAndLocation");
                    if (jsonArr.length() > 0) {
                        success = "true";
                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);


                        }
                    } else {
                        success = "false";
                    }
                    if (success.contains("true")) {
                        Toast.makeText(VisitSheet_EditAndView.this, "Travel Time set correctly", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(VisitSheet_EditAndView.this, "Error with Set Travel Time ", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void  SetEndTimeAndLocation(String doc_sourced_visitsheet_id ,String endtime,String endlocation) {
        String url = BASE_URL + "SetEndTimeAndLocation";
        ApiClient.getApiManager(getApplicationContext()).SetEndTimeAndLocation(url,doc_sourced_visitsheet_id,endtime,endlocation, new Callback<ResponseBody>() {
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
                try {
                    JSONObject json = jParser.convertResponsToJSON(response.body());
                    JSONArray jsonArr = json.getJSONArray("SetEndTimeAndLocation");
                    if (jsonArr.length() > 0) {
                        success = "true";
                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);


                        }
                    } else {
                        success = "false";
                    }
//                    if (success.contains("true")) {
//                        Toast.makeText(VisitSheet_EditAndView.this, "End Time set correctly", Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(VisitSheet_EditAndView.this, "Error with Set End Time ", Toast.LENGTH_LONG).show();
//                    }
                    UpdateDocVisitSheet_Save_And_Finish(VisitSheetId, owner, technician, VS_E_A_V_MeterReadingTV.getText().toString(),
                            reportno, reportdate, VS_E_A_V_RemarksET.getText().toString());

                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void  UpdateSlipPersonalActivity(String id ,String performed,String remarks) {
        String url = BASE_URL + "UpdateSlipPersonalActivity";
        ApiClient.getApiManager(getApplicationContext()).UpdateSlipPersonalActivity(url,id,performed,remarks, new Callback<ResponseBody>() {
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
                try {
                    JSONObject json = jParser.convertResponsToJSON(response.body());
                    JSONArray jsonArr = json.getJSONArray("UpdateSlipPersonalActivity");
                    if (jsonArr.length() > 0) {
                        success = "true";
                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);


                        }
                    } else {
                        success = "false";
                    }
                    if (success.contains("true")) {
                        //  Toast.makeText(VisitSheet_EditAndView.this, "Update Personal Activity successfully", Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(VisitSheet_EditAndView.this, "Update Personal Activity Unsuccessfully", Toast.LENGTH_LONG).show();
                    }


                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void  UpdateSlipNote(String id ,String message,String includesprint) {
        String url = BASE_URL + "UpdateSlipNote";
        ApiClient.getApiManager(getApplicationContext()).UpdateSlipNote(url,id,message,includesprint, new Callback<ResponseBody>() {
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
                try {
                    JSONObject json = jParser.convertResponsToJSON(response.body());
                    JSONArray jsonArr = json.getJSONArray("UpdateSlipNote");
                    if (jsonArr.length() > 0) {
                        success = "true";
                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);


                        }
                    } else {
                        success = "false";
                    }
                    if (success.contains("true")) {
                        //  Toast.makeText(VisitSheet_EditAndView.this, "Update Personal Activity successfully", Toast.LENGTH_LONG).show();
                    } else {
                        //Toast.makeText(VisitSheet_EditAndView.this, "Update Personal Activity Unsuccessfully", Toast.LENGTH_LONG).show();
                    }


                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void  UpdateDocVisitSheet_Save_Only(String doc_sourced_visitsheet_id ,String map_sourced_visitsheet_status_id,String meterreading
                                                  ,String reportno,String reportdate  ,  String remarks   ) {
        String url = BASE_URL + "UpdateDocVisitSheet_Save_Only";
        ApiClient.getApiManager(getApplicationContext()).UpdateDocVisitSheet_Save_Only(url,doc_sourced_visitsheet_id,map_sourced_visitsheet_status_id,meterreading,reportno,reportdate,remarks, new Callback<ResponseBody>() {
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
                try {
                    JSONObject json = jParser.convertResponsToJSON(response.body());
                    JSONArray jsonArr = json.getJSONArray("UpdateDocVisitSheet_Save_Only");
                    if (jsonArr.length() > 0) {
                        success = "true";
                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);
                        }
                    } else {
                        success = "false";
                    }
                    if (success.contains("true")) {
                        if (PALAdapter == null) {
                            PALAdapter = new PersonalActivityListAdaper(getBaseContext(), clsPersonalActivities);
                        }

                        clsPersonalActivities = PALAdapter.personalactivityList;
                        if (clsPersonalActivities.size() > 0) {
                            for (int i = 0; i < clsPersonalActivities.size(); i++) {
                                String id, performed, remarks;
                                id = clsPersonalActivities.get(i).getId();
                                performed = clsPersonalActivities.get(i).getPerformed();
                                remarks = clsPersonalActivities.get(i).getRemarks();
                                //remarks
                                UpdateSlipPersonalActivity(id, performed, remarks);
                            }

                        }

                        int notescount, personalactivitycount = 0;
                        if (NoteAdapter == null) {
                            NoteAdapter = new NotesActivityListAdapter(getBaseContext(), Editable, clsNotes);
                        }
                        notescount = NoteAdapter.NotesList.size();

                        if (notescount > 0) {
                            for (int i = 0; i < NoteAdapter.NotesList.size(); i++) {
                                String id, message, includeprint;
                                id = NoteAdapter.NotesList.get(i).getId();
                                message = NoteAdapter.NotesList.get(i).getMessage();
                                includeprint = NoteAdapter.NotesList.get(i).getIncludesprint();
                                UpdateSlipNote(id, message, includeprint);
                            }
                        }
                        Toast.makeText(VisitSheet_EditAndView.this, "Saved", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(VisitSheet_EditAndView.this, VisitSheet.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                        //finish();
                    } else {
                        //  Toast.makeText(VisitSheet_EditAndView.this, "Save Failed", Toast.LENGTH_LONG).show();


                    }


                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void  UpdateDocVisitSheet_Post_To_Delivery(String doc_sourced_visitsheet_id ,String meterreading
            ,String reportno,String reportdate  ,  String remarks   ) {
        String url = BASE_URL + "UpdateDocVisitSheet_Post_To_Delivery";
        ApiClient.getApiManager(getApplicationContext()).UpdateDocVisitSheet_Post_To_Delivery(url,doc_sourced_visitsheet_id,meterreading,reportno,reportdate,remarks, new Callback<ResponseBody>() {
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
                try {
                    JSONObject json = jParser.convertResponsToJSON(response.body());
                    JSONArray jsonArr = json.getJSONArray("UpdateDocVisitSheet_Post_To_Delivery");
                    if (jsonArr.length() > 0) {
                        success = "true";
                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);
                        }
                    } else {
                        success = "false";
                    }
                    if (success.contains("true")) {
                        Toast.makeText(VisitSheet_EditAndView.this, "Posted to Delivery succeed", Toast.LENGTH_LONG).show();
                        finish();

                    } else {
                        Toast.makeText(VisitSheet_EditAndView.this, "Post to Delivery Failed", Toast.LENGTH_LONG).show();


                    }


                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }




    private void  UpdateDocVisitSheet_Save_And_Finish(String doc_sourced_visitsheet_id ,String doc_sourced_employee_id_owner
            ,String doc_sourced_employee_id_technician,String meterreading
            ,String reportno,String reportdate  ,  String remarks   ) {
        String url = BASE_URL + "UpdateDocVisitSheet_Save_And_Finish";
        ApiClient.getApiManager(getApplicationContext()).UpdateDocVisitSheet_Save_And_Finish(url,doc_sourced_visitsheet_id,doc_sourced_employee_id_owner,doc_sourced_employee_id_technician,meterreading,reportno,reportdate,remarks, new Callback<ResponseBody>() {
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
                try {
                    JSONObject json = jParser.convertResponsToJSON(response.body());
                    JSONArray jsonArr = json.getJSONArray("UpdateDocVisitSheet_Save_And_Finish");
                    if (jsonArr.length() > 0) {
                        success = "true";
                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);
                        }
                    } else {
                        success = "false";
                    }
                    if (success.contains("true")) {
                        if (PALAdapter == null) {
                            PALAdapter = new PersonalActivityListAdaper(getBaseContext(), clsPersonalActivities);
                        }
                        clsPersonalActivities = PALAdapter.personalactivityList;
                        if (clsPersonalActivities.size() > 0) {
                            for (int i = 0; i < clsPersonalActivities.size(); i++) {
                                String id, performed, remarks;
                                id = clsPersonalActivities.get(i).getId();
                                performed = clsPersonalActivities.get(i).getPerformed();
                                remarks = clsPersonalActivities.get(i).getRemarks();
                                //remarks
                                UpdateSlipPersonalActivity(id, performed, remarks);
                            }

                        }

                        int notescount, personalactivitycount = 0;
                        if (NoteAdapter == null) {
                            NoteAdapter = new NotesActivityListAdapter(getBaseContext(), Editable, clsNotes);
                        }
                        notescount = NoteAdapter.NotesList.size();
                        if (notescount > 0) {
                            for (int i = 0; i < NoteAdapter.NotesList.size(); i++) {
                                String id, message, includeprint;
                                id = NoteAdapter.NotesList.get(i).getId();
                                message = NoteAdapter.NotesList.get(i).getMessage();
                                includeprint = NoteAdapter.NotesList.get(i).getIncludesprint();
                                UpdateSlipNote(id, message, includeprint);
                            }
                        }
                        finish();
                    } else {


                    }


                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    public void addHeaders() {

        TableRow tr = new TableRow(VisitSheet_EditAndView.this);
        tr.setLayoutParams(getLayoutparams());
        tr.addView(getTextView(0, "Seq", Color.WHITE, Typeface.BOLD, R.color.colorAccent));
        tr.addView(getTextView(0, "Barcode", Color.WHITE, Typeface.BOLD, R.color.colorAccent));
        tr.addView(getTextView(0, "Item No", Color.WHITE, Typeface.BOLD, R.color.colorAccent));
        tr.addView(getTextView(0, "Item Name", Color.WHITE, Typeface.BOLD, R.color.colorAccent));
        tr.addView(getTextView(0, "Unit Price", Color.WHITE, Typeface.BOLD, R.color.colorAccent));
        tr.addView(getTextView(0, "Gross Doc", Color.WHITE, Typeface.BOLD, R.color.colorAccent));
        tr.addView(getTextView(0, "Status", Color.WHITE, Typeface.BOLD, R.color.colorAccent));
        tr.addView(getTextView(0, "Qantity", Color.WHITE, Typeface.BOLD, R.color.colorAccent));
        tr.addView(getTextView(0, "Tax", Color.WHITE, Typeface.BOLD, R.color.colorAccent));
        EditAndViewtb1.addView(tr, gettblLayoutParams());

    }

    private TextView getTextView(int id, String title, int color, int typeface, int bycolor) {

        TextView tv = new TextView(VisitSheet_EditAndView.this);
        tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
        tv.setPadding(10, 10, 10, 10);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bycolor);
        tv.setLayoutParams(getLayoutparams());
        // tv.setOnClickListener(this);
        return tv;
    }

    private TextView getRowTextView(int id, String title, int color, int typeface, int bycolor) {

        TextView tr = new TextView(VisitSheet_EditAndView.this);
        tr.setId(id);
        tr.setText(title.toUpperCase());
        tr.setTextColor(color);
        tr.setPadding(40, 40, 40, 40);
        tr.setTypeface(Typeface.DEFAULT, typeface);
        tr.setBackgroundColor(bycolor);
        tr.setTypeface(gillsansFont);
        tr.setLayoutParams(getLayoutparams());
        //  tr.setOnClickListener(this);
        return tr;
    }

    private TableRow.LayoutParams getLayoutparams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;

    }

    private TableLayout.LayoutParams gettblLayoutParams() {

        return new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkConnection();
        // register connection status listener
        GTSApplication.getInstance().setConnectivityListener(this);
    }



}





