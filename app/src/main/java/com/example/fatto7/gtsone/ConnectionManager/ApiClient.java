package com.example.fatto7.gtsone.ConnectionManager;

import android.content.Context;

import com.example.fatto7.gtsone.BusinessManager.Models.ItemSpareParts;
import com.example.fatto7.gtsone.BusinessManager.Models.PriceLevel;
import com.example.fatto7.gtsone.BusinessManager.Models.PriceLevelResponse;
import com.example.fatto7.gtsone.BusinessManager.Models.ResponeInsertItemSpartParts;
import com.example.fatto7.gtsone.BusinessManager.Models.ResponeItemSpareParts;
import com.example.fatto7.gtsone.BusinessManager.Models.Response;
import com.example.fatto7.gtsone.BusinessManager.Models.VisitSheet;
import com.example.fatto7.gtsone.BusinessManager.Models.clsVisitSheetDashboard;
import com.example.fatto7.gtsone.SharedAndConstants.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

import static com.example.fatto7.gtsone.Controllers.Activities.GTS_Logo.CoID;
import static com.example.fatto7.gtsone.Controllers.Activities.GTS_Logo.ESTID;
import static com.example.fatto7.gtsone.Controllers.Activities.GTS_Logo.UserId;

public class ApiClient {

    private static Retrofit retrofit;
    private static ApiInterface service;
    private static Context context;
    private static ApiClient apiManager;

    public static ApiClient getApiManager(Context context) {
        if (apiManager == null) {
            apiManager = new ApiClient();
        }
        getBaseRequest();
        return apiManager;
    }

    private static OkHttpClient getRequestHeader() {
        OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(600, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .build();

        return httpClient;
    }

    public void fakeService(String url, Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.fakeService(url);
        userCall.enqueue(callback);
    }

    public void GetVisitSheetDashboard(String url
            , Callback<Response<VisitSheet>> callback) {
        Call<Response<VisitSheet>> userCall = service.GetVisitSheetDashboard(url);
        userCall.enqueue(callback);
    }

    public void GetPriceLevel(String url
            , Callback<PriceLevelResponse<PriceLevel>> callback) {
        Call<PriceLevelResponse<PriceLevel>> userCall = service.GetPriceLevel(url);
        userCall.enqueue(callback);
    }

    public void GetItemSpareParts(String url
            , Callback<ResponeItemSpareParts<ItemSpareParts>> callback) {
        Call<ResponeItemSpareParts<ItemSpareParts>> userCall = service.GetItemSpareParts(url);
        userCall.enqueue(callback);
    }
    public void GetStationCPU(String url ,
           String m_cpu , Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.GetStationCPU(url,m_cpu);
        userCall.enqueue(callback);
    }

    public void Login(String url ,
                         String m_username,
                         String m_password,
                         String m_doc_source_company_id,
                         String m_mas_source_establishment_id , Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.Login(url,m_username,m_password,m_doc_source_company_id,m_mas_source_establishment_id);
        userCall.enqueue(callback);
    }
    public void GetCompany(String url ,
                      Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.GetCompany(url);
        userCall.enqueue(callback);
    }
    public void GetEstablishment(String url ,
                           Callback<ResponseBody> callback,String doc_sourced_company_id) {
        Call<ResponseBody> userCall = service.GetEstablishment(url,doc_sourced_company_id);
        userCall.enqueue(callback);
    }
    public void GetDocVisitSheet(String url ,String doc_sourced_company_id,String id,
                                   Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.GetDocVisitSheet(url,doc_sourced_company_id,id);
        userCall.enqueue(callback);
    }



    public void GetVisitSheetStatus(String url ,
                                 Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.GetVisitSheetStatus(url);
        userCall.enqueue(callback);
    }

    public void UpdateDocVisitSheet_Void(String url ,String doc_sourced_visitsheet_id,
                                    Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.UpdateDocVisitSheet_Void(url,doc_sourced_visitsheet_id);
        userCall.enqueue(callback);
    }



    public void GetTechnician(String url ,String doc_sourced_employee_id,String doc_sourced_company_id,
                                         Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.GetTechnician(url,doc_sourced_employee_id,doc_sourced_company_id);
        userCall.enqueue(callback);
    }




    public void CheckAllowToEdit(String url ,String id,String doc_sourced_company_id,
                              Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.CheckAllowToEdit(url,id,doc_sourced_company_id);
        userCall.enqueue(callback);
    }


    public void CheckAllowToView(String url ,String id,String doc_sourced_company_id,
                                 Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.CheckAllowToView(url,id,doc_sourced_company_id);
        userCall.enqueue(callback);
    }


    public void CheckAllowToVoid(String url ,String id,String doc_sourced_company_id,
                                 Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.CheckAllowToVoid(url,id,doc_sourced_company_id);
        userCall.enqueue(callback);
    }

    public void InsertStaion(String url ,
                             String doc_sourced_company_id, String m_mas_source_establishment_id,
                             String m_name ,
                             String m_shortname,
                             String m_cpu,Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.InsertStaion(url,doc_sourced_company_id,m_mas_source_establishment_id,m_name,m_shortname,m_cpu);
        userCall.enqueue(callback);
    }



    public void SetItemSpareParts(String url,
                                  String doc_source_company_id , String mas_source_establishment_id , String mas_source_station_id ,
            String doc_source_userid_id , String doc_sourced_joborder_id , String mas_sourced_tax_id ,
            String mas_sourced_warehouse_id , String mas_sourced_bin_id , String mas_sourced_itemmaster_id ,
            String mas_sourced_pricelevel_id , String mas_sourced_shippingtype_id , String map_sourced_valuationmethod_id ,
            String slip_sourced_unitofmeasure_id , String itemcode ,String itemname , String barcode ,
            String warehouse , String bin , String unitofmeasure , String multiuom,String quantity , String quantityinstock,
            String itemperunit , String taxpercent , String itemstatus , String cost , String type , String map_sourced_warranty_id
            , Callback<ResponeItemSpareParts<ResponeInsertItemSpartParts>> callback) {

        Call<ResponeItemSpareParts<ResponeInsertItemSpartParts>> userCall = service.SetItemSpareParts(url, doc_source_company_id,mas_source_establishment_id,mas_source_station_id,
                doc_source_userid_id,doc_sourced_joborder_id,mas_sourced_tax_id,mas_sourced_warehouse_id,mas_sourced_bin_id
        ,mas_sourced_itemmaster_id,mas_sourced_pricelevel_id,mas_sourced_shippingtype_id,map_sourced_valuationmethod_id,
                slip_sourced_unitofmeasure_id,itemcode,itemname,barcode,warehouse,bin,unitofmeasure
        ,multiuom,quantity,quantityinstock,itemperunit,taxpercent,itemstatus,cost,type,map_sourced_warranty_id);
        userCall.enqueue(callback);
    }


    public void GetSlipSpareParts(String url,String doc_sourced_visitsheet_id, Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.GetSlipSpareParts(url,doc_sourced_visitsheet_id);
        userCall.enqueue(callback);
    }



    public void GetVisitSheetStatusEdit(String url , Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.GetVisitSheetStatusEdit(url);
        userCall.enqueue(callback);
    }



    public void GetSlipPersonalActivity( String url, String doc_sourced_visitsheet_id
            , String doc_sourced_company_id, Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.GetSlipPersonalActivity(url,doc_sourced_visitsheet_id,doc_sourced_company_id);
        userCall.enqueue(callback);
    }


    public void GetSlipNote(String url,String doc_sourced_visitsheet_id, Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.GetSlipNote(url,doc_sourced_visitsheet_id);
        userCall.enqueue(callback);
    }



    public void SetStartTimeAndLocation( String url, String doc_sourced_visitsheet_id
            , String starttime
            , String startlocation, Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.SetStartTimeAndLocation(url,doc_sourced_visitsheet_id,starttime,startlocation);
        userCall.enqueue(callback);
    }


    public void SetTravelTimeAndLocation( String url, String doc_sourced_visitsheet_id
            , String traveltime
            , String travellocation, Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.SetTravelTimeAndLocation(url,doc_sourced_visitsheet_id,traveltime,travellocation);
        userCall.enqueue(callback);
    }



    public void SetEndTimeAndLocation( String url, String doc_sourced_visitsheet_id
            , String endtime
            , String endlocation, Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.SetEndTimeAndLocation(url,doc_sourced_visitsheet_id,endtime,endlocation);
        userCall.enqueue(callback);
    }




    public void UpdateSlipPersonalActivity(String url, String id
            , String performed
            , String remarks, Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.UpdateSlipPersonalActivity(url,id,performed,remarks);
        userCall.enqueue(callback);
    }



    public void UpdateSlipNote( String url,String id
            , String message
            , String includesprint, Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.UpdateSlipNote(url,id,message,includesprint);
        userCall.enqueue(callback);
    }





    public void UpdateDocVisitSheet_Save_Only( String url, String doc_sourced_visitsheet_id
            , String map_sourced_visitsheet_status_id
            , String meterreading
            ,  String reportno
            ,  String reportdate
            ,  String remarks, Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.UpdateDocVisitSheet_Save_Only(url,doc_sourced_visitsheet_id,map_sourced_visitsheet_status_id,meterreading,reportno
        ,reportdate,remarks);
        userCall.enqueue(callback);
    }


    public void UpdateDocVisitSheet_Post_To_Delivery( String url, String doc_sourced_visitsheet_id
            , String meterreading
            ,  String reportno
            ,  String reportdate
            ,  String remarks, Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.UpdateDocVisitSheet_Post_To_Delivery(url,doc_sourced_visitsheet_id,meterreading,reportno
                ,reportdate,remarks);
        userCall.enqueue(callback);
    }





    public void UpdateDocVisitSheet_Save_And_Finish( String url, String doc_sourced_visitsheet_id
            , String doc_sourced_employee_id_owner
            , String doc_sourced_employee_id_technician
            , String meterreading
            ,  String reportno
            ,  String reportdate
            ,  String remarks, Callback<ResponseBody> callback) {
        Call<ResponseBody> userCall = service.UpdateDocVisitSheet_Save_And_Finish(url,doc_sourced_visitsheet_id,doc_sourced_employee_id_owner,doc_sourced_employee_id_technician,meterreading,reportno
                ,reportdate,remarks);
        userCall.enqueue(callback);
    }


    public static Retrofit getBaseRequest() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getRequestHeader())
                    .build();
        }
        service = retrofit.create(ApiInterface.class);

        return retrofit;
    }
}
