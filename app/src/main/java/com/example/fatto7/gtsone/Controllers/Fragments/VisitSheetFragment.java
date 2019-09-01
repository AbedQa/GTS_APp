package com.example.fatto7.gtsone.Controllers.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fatto7.gtsone.BusinessManager.Models.Response;
import com.example.fatto7.gtsone.BusinessManager.Models.VisitSheet;
import com.example.fatto7.gtsone.BusinessManager.Models.clsTechnicianV_S;
import com.example.fatto7.gtsone.BusinessManager.Models.clsVisitSheetDashboard;
import com.example.fatto7.gtsone.ConnectionManager.ApiClient;
import com.example.fatto7.gtsone.ConnectionManager.JSONParser;
import com.example.fatto7.gtsone.Controllers.Activities.GTS_Logo;
import com.example.fatto7.gtsone.Controllers.Activities.Login;
import com.example.fatto7.gtsone.Controllers.Activities.SettingsActivity;
import com.example.fatto7.gtsone.Controllers.Activities.VisitSheet_EditAndView;
import com.example.fatto7.gtsone.Controllers.Adapters.SpinnerAdapterTechnicianV_S;
import com.example.fatto7.gtsone.R;
import com.example.fatto7.gtsone.SharedAndConstants.Constants;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.codecrafters.tableview.TableView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.POWER_SERVICE;
import static com.example.fatto7.gtsone.Controllers.Activities.VisitSheet.pageIndex;
import static com.example.fatto7.gtsone.SharedAndConstants.Constants.BASE_URL;

public class VisitSheetFragment extends Fragment implements View.OnClickListener {

    Typeface gillsansFont;
    String EditFlag, VoidFlag = "", ViewFlag = "";

    ProgressDialog progressDialog;
    JSONParser jParser = new JSONParser();
    ArrayList<clsTechnicianV_S> TechnicianList;

    RadioGroup radioGroup;

    Spinner VisitSheetReordsDateSp, VisitSheetTechnicalSp;

    RadioButton VisitSheet_RB;

    CheckBox VisitSheetNotStartedCB, VisitSheetInProgressCB, VisitSheetOpenedCB, VisitSheetCompletedCB,
            VisitSheetDefferdCB, VisitSheetCancelledCB, VisitSheetWaitingForDeliverCB;

    String m_datefilter, m_datebased, TechnicianID;

    Button VisitSheetSearchIV;

    String Numbering, Active, status, Customer_Name, Item_Code, Item_Name, Owner, Technician;

    String fromDate, toDate;
    PopupMenu popup;
    TableLayout visitsheettb1;
    static ArrayList<clsVisitSheetDashboard> allexistingdatalist;
    static List<VisitSheet> allexistingdatalist2 = new ArrayList<>();

    TableView<String[]> tableView;
    String Not_Started, Opened, Deferred, In_Progress, Finished, Cancelled, Waiting_For_Delivery;
    public static String DocVSID = "";
    public static String VesitSheetId = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_visit_sheet, null);

        gillsansFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/gillsans.ttf");
        CreatePOPUP();

        VisitSheetReordsDateSp = view.findViewById(R.id.VisitSheetReordsDateSp);
        VisitSheetTechnicalSp = view.findViewById(R.id.VisitSheetTechnicalSp);

        VisitSheetNotStartedCB = view.findViewById(R.id.VisitSheetNotStartedCB);
        VisitSheetInProgressCB = view.findViewById(R.id.VisitSheetInProgressCB);
        VisitSheetOpenedCB = view.findViewById(R.id.VisitSheetOpenedCB);
        VisitSheetCompletedCB = view.findViewById(R.id.VisitSheetCompletedCB);
        VisitSheetDefferdCB = view.findViewById(R.id.VisitSheetDefferdCB);
        VisitSheetCancelledCB = view.findViewById(R.id.VisitSheetCancelledCB);
        VisitSheetWaitingForDeliverCB = view.findViewById(R.id.VisitSheetWaitingForDeliverCB);
        visitsheettb1 = view.findViewById(R.id.tb1_allexisting_data);

        VisitSheetSearchIV = view.findViewById(R.id.VisitSheetSearchIV);

        final Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);


        // setTypeface
        VisitSheetNotStartedCB.setTypeface(gillsansFont);
        VisitSheetInProgressCB.setTypeface(gillsansFont);
        VisitSheetOpenedCB.setTypeface(gillsansFont);
        VisitSheetCompletedCB.setTypeface(gillsansFont);
        VisitSheetDefferdCB.setTypeface(gillsansFont);
        VisitSheetCancelledCB.setTypeface(gillsansFont);
        VisitSheetWaitingForDeliverCB.setTypeface(gillsansFont);
        VisitSheetSearchIV.setTypeface(gillsansFont);

        // set Click listeners
        VisitSheetNotStartedCB.setOnClickListener(this);
        VisitSheetInProgressCB.setOnClickListener(this);
        VisitSheetOpenedCB.setOnClickListener(this);
        VisitSheetCompletedCB.setOnClickListener(this);
        VisitSheetDefferdCB.setOnClickListener(this);
        VisitSheetCancelledCB.setOnClickListener(this);
        VisitSheetWaitingForDeliverCB.setOnClickListener(this);

        // set checkboxes checked
        VisitSheetNotStartedCB.setChecked(true);
        VisitSheetInProgressCB.setChecked(true);
        VisitSheetOpenedCB.setChecked(true);
        VisitSheetCompletedCB.setChecked(false);
        VisitSheetDefferdCB.setChecked(true);
        VisitSheetCancelledCB.setChecked(false);
        VisitSheetWaitingForDeliverCB.setChecked(true);

        // set default values
        Not_Started = "1";
        In_Progress = "4";
        Opened = "2";
        Finished = "5";
        Deferred = "3";
        Cancelled = "6";
        Waiting_For_Delivery = "7";

        TechnicianList = new ArrayList<>();
        allexistingdatalist = new ArrayList<>();
        GetTechnician(GTS_Logo.EmployeeId, GTS_Logo.CoID);
        VisitSheetSearchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pageIndex == 0) {

                    m_datefilter = "0";
                    fromDate = year + "/" + month + "/" + day;
                    toDate = year + "/" + month + "/" + day;

                } else if (pageIndex == 1) {

                    m_datefilter = "1";

                } else if (pageIndex == 2) {

                    m_datefilter = "2";

                } else {

                    m_datefilter = "3";

                }

                String m_visitstatus = "";
                if (VisitSheetNotStartedCB.isChecked()) {
                    m_visitstatus = m_visitstatus + Not_Started + ",";
                }
                if (VisitSheetOpenedCB.isChecked()) {
                    m_visitstatus = m_visitstatus + Opened + ",";
                }
                if (VisitSheetDefferdCB.isChecked()) {
                    m_visitstatus = m_visitstatus + Deferred + ",";
                }
                if (VisitSheetInProgressCB.isChecked()) {
                    m_visitstatus = m_visitstatus + In_Progress + ",";
                }
                if (VisitSheetCompletedCB.isChecked()) {
                    m_visitstatus = m_visitstatus + Finished + ",";
                }
                if (VisitSheetCancelledCB.isChecked()) {
                    m_visitstatus = m_visitstatus + Cancelled + ",";
                }
                if (VisitSheetWaitingForDeliverCB.isChecked()) {
                    m_visitstatus = m_visitstatus + Waiting_For_Delivery + ",";
                }
                m_visitstatus = m_visitstatus.substring(0, m_visitstatus.length() - 1);
                visitsheettb1.removeAllViews();
                addHeaders();
//                new GetVisitSheetDashboard().execute(GTS_Logo.CoID, m_datefilter, "",
//                        "", m_datebased, m_visitstatus, TechnicianID);

                FetchVisitSheetDashboard(GTS_Logo.CoID, m_datefilter, "",
                        "", m_datebased, m_visitstatus, TechnicianID);

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Records_Filter, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        VisitSheetReordsDateSp.setAdapter(adapter);

        VisitSheetReordsDateSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                m_datebased = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        VisitSheetTechnicalSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TechnicianID = TechnicianList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;

    }

    private void CreatePOPUP() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            popup = new PopupMenu(getActivity(), getView(), Gravity.CENTER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                popup.setGravity(Gravity.END);
            }

        } else {
            popup = new PopupMenu(getContext(), visitsheettb1);
        }


        popup.getMenuInflater().inflate(R.menu.visitsheet_popmenu, popup.getMenu());
    }

    private void FetchVisitSheetDashboard(String coID, String m_datefilter, String dateFrom, String dateTo, String m_datebased, String m_visitstatus, String technicianID) {
        allexistingdatalist2.clear();
        StringBuffer urlStringBuffer = new StringBuffer();
        urlStringBuffer.append(Constants.BASE_URL);
        urlStringBuffer.append("GetVisitSheetDashboard");
        urlStringBuffer.append("?m_doc_source_company_id=");
        urlStringBuffer.append(coID);
        urlStringBuffer.append("&");
        urlStringBuffer.append("m_datefilter=");
        urlStringBuffer.append(m_datefilter);
        urlStringBuffer.append("&");
        urlStringBuffer.append("m_visitstatus=");
        urlStringBuffer.append(m_visitstatus);
        urlStringBuffer.append("&");
        urlStringBuffer.append("m_doc_sourced_employee_technician_id=");
        urlStringBuffer.append(technicianID);
        urlStringBuffer.append("&");
        urlStringBuffer.append("m_datebased=");
        urlStringBuffer.append(m_datebased);
        urlStringBuffer.append("&");
        urlStringBuffer.append("m_datefrom=");
        urlStringBuffer.append(dateFrom);
        urlStringBuffer.append("&");
        urlStringBuffer.append("m_dateto=");
        urlStringBuffer.append(dateTo);
        String url = urlStringBuffer.toString();
        progressDialog = ProgressDialog.show(getContext(), "Please Wait", "Please Wait");
        ApiClient.getApiManager(getContext()).GetVisitSheetDashboard(url, new Callback<Response<VisitSheet>>() {
            @Override
            public void onResponse(Call<Response<VisitSheet>> call, retrofit2.Response<Response<VisitSheet>> response) {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        Log.v("RESPONSE_CALLED", "ON_RESPONSE_CALLED");
                        String didItWork = String.valueOf(response.isSuccessful());
                        Log.v("SUCCESS?", didItWork);
                        Log.v("RESPONSE_CODE", String.valueOf(response.code()));
                        if (response.code() >= 500) {
                            Toast.makeText(getContext(), "There is somthing error on server", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        allexistingdatalist2 = response.body().getData();
                        ReloadView();
                    }
                } catch (Exception e) {
                    progressDialog.dismiss();
                    //  Log.v("SUCCESS?", e.getLocalizedMessage());

                }
            }

            @Override
            public void onFailure(Call<Response<VisitSheet>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });


    }

    private void ReloadView() {

        progressDialog.dismiss();

        int allinputdata = allexistingdatalist2.size();
        for (int i = 0; i < allinputdata; i++) {


            final TableRow visitrow = new TableRow(getActivity());
            final int ii = i;

            visitrow.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    popup = new PopupMenu(getActivity(), visitrow);
                    popup.getMenuInflater().inflate(R.menu.visitsheet_popmenu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {


                                case R.id.Visitshet_menu_Edit:


                                    if (allexistingdatalist2.size() < ii) {
                                        return false;
                                    }
                                    CheckAllowToEdit(allexistingdatalist2.get(ii).getId(),GTS_Logo.CoID);
                                    return true;


                                case R.id.Visitshet_menu_void:


                                    if (allexistingdatalist2.size() < ii) {
                                        return false;
                                    }
                                    CheckAllowToVoid(allexistingdatalist2.get(ii).getId(), GTS_Logo.CoID);

                                    return true;

                                case R.id.Visitshet_menu_View:

                                    if (allexistingdatalist2.size() < ii) {
                                        return false;
                                    }
                                    CheckAllowToView(allexistingdatalist2.get(ii).getId(), GTS_Logo.CoID);
                                    return true;
                            }
                            return true;
                        }
                    });
                    popup.show();
                    // Toast.makeText(VisitSheet.this,allexistingdatalist.get(ii).getId(),Toast.LENGTH_LONG).show();


                }
            });
            visitrow.setLayoutParams(getLayoutparams());
            visitrow.addView(getRowTextView(0, String.valueOf(i + 1), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(getActivity(), R.color.cell_background_coloe)));
            visitrow.addView(getRowTextView(0, allexistingdatalist2.get(i).getNumbering() == null ? "" : allexistingdatalist2.get(i).getNumbering(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(getActivity(), R.color.cell_background_coloe)));
            visitrow.addView(getRowTextView(0, allexistingdatalist2.get(i).getActive() == null ? "" : allexistingdatalist2.get(i).getActive(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(getActivity(), R.color.cell_background_coloe)));
            visitrow.addView(getRowTextView(2, allexistingdatalist2.get(i).getStatus() == null ? "" : allexistingdatalist2.get(i).getStatus(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(getActivity(), R.color.cell_background_coloe)));
            visitrow.addView(getRowTextView(3, allexistingdatalist2.get(i).getCustomerName() == null ? "" : allexistingdatalist2.get(i).getCustomerName(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(getActivity(), R.color.cell_background_coloe)));
            visitrow.addView(getRowTextView(4, allexistingdatalist2.get(i).getItemCode() == null ? "" : allexistingdatalist2.get(i).getItemCode(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(getActivity(), R.color.cell_background_coloe)));
            visitrow.addView(getRowTextView(5, allexistingdatalist2.get(i).getItemName() == null ? "" : allexistingdatalist2.get(i).getItemName(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(getActivity(), R.color.cell_background_coloe)));
            visitrow.addView(getRowTextView(2, allexistingdatalist2.get(i).getOwner() == null ? "" : allexistingdatalist2.get(i).getOwner(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(getActivity(), R.color.cell_background_coloe)));
            visitrow.addView(getRowTextView(2, allexistingdatalist2.get(i).getTechnician() == null ? "" : allexistingdatalist2.get(i).getTechnician(), Color.BLACK, Typeface.NORMAL, ContextCompat.getColor(getActivity(), R.color.cell_background_coloe)));
            visitsheettb1.addView(visitrow, gettblLayoutParams());
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.VisitSheetNotStartedCB:

                if (VisitSheetNotStartedCB.isChecked()) {
                    Not_Started = "1";
                } else {
                    Not_Started = "";
                }

                break;

            case R.id.VisitSheetInProgressCB:

                if (VisitSheetInProgressCB.isChecked()) {
                    In_Progress = "4";
                } else {
                    In_Progress = "";


                }

                break;

            case R.id.VisitSheetWaitingForDeliverCB:

                if (VisitSheetWaitingForDeliverCB.isChecked()) {
                    Waiting_For_Delivery = "7";
                } else {
                    Waiting_For_Delivery = "";
                }

                break;

            case R.id.VisitSheetCompletedCB:

                if (VisitSheetCompletedCB.isChecked()) {
                    Finished = "5";
                } else {
                    Finished = "";
                }

                break;

            case R.id.VisitSheetDefferdCB:

                if (VisitSheetDefferdCB.isChecked()) {
                    Deferred = "3";
                } else {
                    Deferred = "0";
                }

                break;

            case R.id.VisitSheetCancelledCB:

                if (VisitSheetCancelledCB.isChecked()) {
                    Cancelled = "6";
                } else {
                    Cancelled = "";
                }

                break;

            case R.id.VisitSheetOpenedCB:

                if (VisitSheetOpenedCB.isChecked()) {
                    Opened = "2";
                } else {
                    Opened = "";
                }

                break;

        }

    }

    private void  UpdateDocVisitSheet_Void(String doc_sourced_visitsheet_id) {
        String url = BASE_URL + "UpdateDocVisitSheet_Void";

        ApiClient.getApiManager(getContext()).UpdateDocVisitSheet_Void(url, doc_sourced_visitsheet_id, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String success = "";

                Log.v("RESPONSE_CALLED", "ON_RESPONSE_CALLED");
                String didItWork = String.valueOf(response.isSuccessful());
                Log.v("SUCCESS?", didItWork);
                Log.v("RESPONSE_CODE", String.valueOf(response.code()));
                if (response.code() >= 500) {
                    Toast.makeText(getContext(), "There is somthing error on server", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject json = jParser.convertResponsToJSON(response.body());
                try {
                    JSONArray jsonArr = json.getJSONArray("UpdateDocVisitSheet_Void");
                    if (jsonArr.length() > 0) {
                        success = "true";
                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);


                        }
                    } else {
                        success = "false";
                    }
                    if (success.contains("true")) {

                        Toast.makeText(getActivity(), "Void Done", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Void Failed", Toast.LENGTH_LONG).show();


                    }
                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    private void  CheckAllowToEdit(final String id , String doc_sourced_company_id) {
        String url = BASE_URL + "CheckAllowToEditVisitSheet";

        ApiClient.getApiManager(getContext()).CheckAllowToEdit(url,id, doc_sourced_company_id, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String success = "";
                VesitSheetId = id;
                Log.v("RESPONSE_CALLED", "ON_RESPONSE_CALLED");
                String didItWork = String.valueOf(response.isSuccessful());
                Log.v("SUCCESS?", didItWork);
                Log.v("RESPONSE_CODE", String.valueOf(response.code()));
                if (response.code() >= 500) {
                    Toast.makeText(getContext(), "There is somthing error on server", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject json = jParser.convertResponsToJSON(response.body());
                try {
                    JSONArray jsonArr = json.getJSONArray("AllowToEditVisitSheet");
                    try {

                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);

                            EditFlag = json.getString("id");


                        }
                        if (EditFlag.contains("0")) {
                            EditFlag = "1";
                            Intent i = new Intent(getActivity(), VisitSheet_EditAndView.class);
                            i.putExtra("VisitSheetId", VesitSheetId);
                            i.putExtra("Editable", "1");
                            startActivity(i);
                        }



                    } catch (JSONException e) {

                    }
                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }



    private void  CheckAllowToView(final String id , String doc_sourced_company_id) {
        String url = BASE_URL + "CheckAllowToEditVisitSheet";

        ApiClient.getApiManager(getContext()).CheckAllowToView(url,id, doc_sourced_company_id, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String success = "";
                VesitSheetId = id;
                Log.v("RESPONSE_CALLED", "ON_RESPONSE_CALLED");
                String didItWork = String.valueOf(response.isSuccessful());
                Log.v("SUCCESS?", didItWork);
                Log.v("RESPONSE_CODE", String.valueOf(response.code()));
                if (response.code() >= 500) {
                    Toast.makeText(getContext(), "There is somthing error on server", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject json = jParser.convertResponsToJSON(response.body());
                try {
                    JSONArray jsonArr = json.getJSONArray("AllowToEditVisitSheet");
                    try {
                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);

                            EditFlag = json.getString("id");


                        }
                        EditFlag = "0";
                        Intent i = new Intent(getActivity(), VisitSheet_EditAndView.class);

                        i.putExtra("Editable", "0");
                        i.putExtra("VisitSheetId", VesitSheetId);
                        startActivity(i);



                    } catch (JSONException e) {

                    }
                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    private void  CheckAllowToVoid(final String id , String doc_sourced_company_id) {
        String url = BASE_URL + "CheckAllowToVoidVisitSheet";

        ApiClient.getApiManager(getContext()).CheckAllowToVoid(url,id, doc_sourced_company_id, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String success = "";
                VesitSheetId = id;
                Log.v("RESPONSE_CALLED", "ON_RESPONSE_CALLED");
                String didItWork = String.valueOf(response.isSuccessful());
                Log.v("SUCCESS?", didItWork);
                Log.v("RESPONSE_CODE", String.valueOf(response.code()));
                if (response.code() >= 500) {
                    Toast.makeText(getContext(), "There is somthing error on server", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject json = jParser.convertResponsToJSON(response.body());
                try {
                    JSONArray jsonArr = json.getJSONArray("AllowToVoidVisitSheet");
                    try {
                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);

                            VoidFlag = json.getString("id");


                        }
                        if (VoidFlag.contains("0")) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Are You sure to void order").setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            UpdateDocVisitSheet_Void(DocVSID);
                                            Toast t = Toast.makeText(getActivity(), "Void success ", Toast.LENGTH_SHORT);
                                            t.show();

                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.setTitle("Void");
                            alertDialog.show();

                        } else {


                            Toast t = Toast.makeText(getActivity(), "You dont have access to void ", Toast.LENGTH_SHORT);
                            t.show();

                        }



                    } catch (JSONException e) {

                    }
                }catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    private void GetTechnician(String doc_sourced_employee_id , String doc_sourced_company_id) {
        String url = BASE_URL + "GetTechnician";
        ApiClient.getApiManager(getContext()).GetTechnician(url, doc_sourced_employee_id, doc_sourced_company_id, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                Log.v("RESPONSE_CALLED", "ON_RESPONSE_CALLED");
                String didItWork = String.valueOf(response.isSuccessful());
                Log.v("SUCCESS?", didItWork);
                Log.v("RESPONSE_CODE", String.valueOf(response.code()));
                if (response.code() >= 500) {
                    Toast.makeText(getContext(), "There is somthing error on server", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject json = jParser.convertResponsToJSON(response.body());
                try {
                    JSONArray jsonArr = json.getJSONArray("Technician");
                    try {

                        for (int i = 0; i < jsonArr.length(); i++) {
                            json = jsonArr.getJSONObject(i);
                            clsTechnicianV_S objTechnician = new clsTechnicianV_S();
                            objTechnician.setId(json.getString("id"));
                            objTechnician.setName(json.getString("Name"));
                            TechnicianList.add(objTechnician);
                        }
                        if (getActivity() == null) {
                            return;
                        }
                        SpinnerAdapterTechnicianV_S TechnicianAdapter = new SpinnerAdapterTechnicianV_S(getActivity(), 0, TechnicianList);
                        VisitSheetTechnicalSp.setAdapter(TechnicianAdapter);
                    } catch (JSONException e) {
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

        TableRow tr = new TableRow(getActivity());
        tr.setLayoutParams(getLayoutparams());
        tr.addView(getTextView(0, "Seq", Color.WHITE, Typeface.BOLD, R.color.colorPrimary));
        tr.addView(getTextView(0, "Numbering", Color.WHITE, Typeface.BOLD, R.color.colorPrimary));
        tr.addView(getTextView(0, "Active", Color.WHITE, Typeface.BOLD, R.color.colorPrimary));
        tr.addView(getTextView(0, "Status", Color.WHITE, Typeface.BOLD, R.color.colorPrimary));
        tr.addView(getTextView(0, "Customer Name", Color.WHITE, Typeface.BOLD, R.color.colorPrimary));
        tr.addView(getTextView(0, "Item Code", Color.WHITE, Typeface.BOLD, R.color.colorPrimary));
        tr.addView(getTextView(0, "Item Name", Color.WHITE, Typeface.BOLD, R.color.colorPrimary));
        tr.addView(getTextView(0, "Owner", Color.WHITE, Typeface.BOLD, R.color.colorPrimary));
        tr.addView(getTextView(0, "Technician", Color.WHITE, Typeface.BOLD, R.color.colorPrimary));
        visitsheettb1.addView(tr, gettblLayoutParams());

    }

    private TextView getTextView(int id, String title, int color, int typeface, int bycolor) {

        TextView tv = new TextView(getActivity());
        tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
        tv.setTextSize(18.0f);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tv.setPadding(20, 20, 20, 20);
        tv.setTypeface(gillsansFont);
        tv.setBackgroundColor(bycolor);
        tv.setLayoutParams(getLayoutparams());
        // tv.setOnClickListener(this);
        return tv;
    }


    private TextView getRowTextView(int id, String title, int color, int typeface,
                                    int bycolor) {

        TextView tr = new TextView(getActivity());
        tr.setId(id);
        tr.setText(title.toUpperCase());
        tr.setTextColor(color);
        tr.setTypeface(tr.getTypeface(), Typeface.BOLD);

        tr.setPadding(20, 20, 20, 20);
        tr.setTypeface(gillsansFont);
        tr.setBackgroundColor(bycolor);
        tr.setLayoutParams(getLayoutparams());
        return tr;
    }

    private TableRow.LayoutParams getLayoutparams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(1, 0, 0, 1);
        return params;

    }

    private TableLayout.LayoutParams gettblLayoutParams() {

        return new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
    }

}

