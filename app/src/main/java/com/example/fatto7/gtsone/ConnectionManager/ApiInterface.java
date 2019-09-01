package com.example.fatto7.gtsone.ConnectionManager;

import com.example.fatto7.gtsone.BusinessManager.Models.ItemSpareParts;
import com.example.fatto7.gtsone.BusinessManager.Models.PriceLevel;
import com.example.fatto7.gtsone.BusinessManager.Models.PriceLevelResponse;
import com.example.fatto7.gtsone.BusinessManager.Models.ResponeInsertItemSpartParts;
import com.example.fatto7.gtsone.BusinessManager.Models.ResponeItemSpareParts;
import com.example.fatto7.gtsone.BusinessManager.Models.Response;
import com.example.fatto7.gtsone.BusinessManager.Models.UploadFile;
import com.example.fatto7.gtsone.BusinessManager.Models.VisitSheet;
import com.example.fatto7.gtsone.BusinessManager.Models.clsVisitSheetDashboard;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {


    @POST("Post")
    Call<Void> uploadImage(@Query("m_sharefolder") String shareFolder, @Query("m_doc_sourced_visitsheet_id") String visitsheetID,
                           @Query("m_seq") String seq, @Query("m_type") String type, @Body byte[] file);

    @POST
    Call<ResponseBody> fakeService(@Url String url);

    @Headers({
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;application/json",
            "Accept-Encoding: gzip, de0flate0",
            "Accept-Language: en-US,en;q=0.9"})
    @GET
    Call<Response<VisitSheet>> GetVisitSheetDashboard(@Url String url);

    @Headers({
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;application/json",
            "Accept-Encoding: gzip, de0flate0",
            "Accept-Language: en-US,en;q=0.9"})
    @GET
    Call<PriceLevelResponse<PriceLevel>> GetPriceLevel(@Url String url);

    @Headers({
            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;application/json",
            "Accept-Encoding: gzip, de0flate0",
            "Accept-Language: en-US,en;q=0.9"})
    @GET
    Call<ResponeItemSpareParts<ItemSpareParts>> GetItemSpareParts(@Url String url);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> GetStationCPU(@Url String url,@Field("m_cpu") String Mcpu);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> Login(@Url String url,@Field("m_username") String m_username ,
                             @Field("m_password") String m_password,
                             @Field("m_doc_source_company_id") String m_doc_source_company_id,
                             @Field("m_mas_source_establishment_id") String m_mas_source_establishment_id);



    @POST
    Call<ResponseBody> GetCompany(@Url String url);



    @FormUrlEncoded
    @POST
    Call<ResponseBody> GetEstablishment(@Url String url,@Field("doc_sourced_company_id") String doc_sourced_company_id);


    @FormUrlEncoded
    @POST
    Call<ResponseBody> InsertStaion(@Url String url,@Field("m_doc_source_company_id") String doc_sourced_company_id
            ,@Field("m_mas_source_establishment_id") String m_mas_source_establishment_id
            ,@Field("m_name") String m_name
            ,@Field("m_shortname") String m_shortname
            ,@Field("m_cpu") String m_cpu);



    @FormUrlEncoded
    @POST
    Call<ResponseBody> GetDocVisitSheet(@Url String url,@Field("doc_sourced_company_id") String doc_sourced_company_id
            ,@Field("id") String id);



    @POST
    Call<ResponseBody> GetVisitSheetStatus(@Url String url);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> UpdateDocVisitSheet_Void(@Url String url,@Field("doc_sourced_visitsheet_id") String doc_sourced_visitsheet_id);

    @FormUrlEncoded
    @POST
    Call<ResponseBody> GetTechnician(@Url String url,@Field("doc_sourced_employee_id") String doc_sourced_employee_id , @Field("doc_sourced_company_id") String doc_sourced_company_id);


    @FormUrlEncoded
    @POST
    Call<ResponseBody> CheckAllowToEdit(@Url String url,@Field("id") String id , @Field("doc_sourced_company_id") String doc_sourced_company_id);


    @FormUrlEncoded
    @POST
    Call<ResponseBody> CheckAllowToView(@Url String url,@Field("id") String id , @Field("doc_sourced_company_id") String doc_sourced_company_id);



    @FormUrlEncoded
    @POST
    Call<ResponseBody> CheckAllowToVoid(@Url String url,@Field("id") String id , @Field("doc_sourced_company_id") String doc_sourced_company_id);




    @FormUrlEncoded
    @POST
    Call<ResponseBody> GetSlipSpareParts(@Url String url,@Field("doc_sourced_visitsheet_id") String doc_sourced_visitsheet_id);


    @POST
    Call<ResponseBody> GetVisitSheetStatusEdit(@Url String url);



    @FormUrlEncoded
    @POST
    Call<ResponseBody> GetSlipPersonalActivity(@Url String url,@Field("doc_sourced_visitsheet_id") String doc_sourced_visitsheet_id
            ,@Field("doc_sourced_company_id") String doc_sourced_company_id);


    @FormUrlEncoded
    @POST
    Call<ResponseBody> GetSlipNote(@Url String url,@Field("doc_sourced_visitsheet_id") String doc_sourced_visitsheet_id);


    @FormUrlEncoded
    @POST
    Call<ResponseBody> SetStartTimeAndLocation(@Url String url,@Field("doc_sourced_visitsheet_id") String doc_sourced_visitsheet_id
            ,@Field("starttime") String starttime
            ,@Field("startlocation") String startlocation);



    @FormUrlEncoded
    @POST
    Call<ResponseBody> SetTravelTimeAndLocation(@Url String url,@Field("doc_sourced_visitsheet_id") String doc_sourced_visitsheet_id
            ,@Field("traveltime") String traveltime
            ,@Field("travellocation") String travellocation);


    @FormUrlEncoded
    @POST
    Call<ResponseBody> SetEndTimeAndLocation(@Url String url,@Field("doc_sourced_visitsheet_id") String doc_sourced_visitsheet_id
            ,@Field("endtime") String traveltime
            ,@Field("endlocation") String travellocation);



    @FormUrlEncoded
    @POST
    Call<ResponseBody> UpdateSlipPersonalActivity(@Url String url,@Field("id") String id
            ,@Field("performed") String performed
            ,@Field("remarks") String remarks);


    @FormUrlEncoded
    @POST
    Call<ResponseBody> UpdateSlipNote(@Url String url,@Field("id") String id
            ,@Field("message") String message
            ,@Field("includesprint") String includesprint);



    @FormUrlEncoded
    @POST
    Call<ResponseBody> UpdateDocVisitSheet_Save_Only(@Url String url,@Field("doc_sourced_visitsheet_id") String doc_sourced_visitsheet_id
            ,@Field("map_sourced_visitsheet_status_id") String map_sourced_visitsheet_status_id
            ,@Field("meterreading") String meterreading
            ,@Field("reportno") String reportno
            ,@Field("reportdate") String reportdate
            ,@Field("remarks") String remarks);



    @FormUrlEncoded
    @POST
    Call<ResponseBody> UpdateDocVisitSheet_Post_To_Delivery(@Url String url,@Field("doc_sourced_visitsheet_id") String doc_sourced_visitsheet_id
            ,@Field("meterreading") String meterreading
            ,@Field("reportno") String reportno
            ,@Field("reportdate") String reportdate
            ,@Field("remarks") String remarks);



    @FormUrlEncoded
    @POST
    Call<ResponseBody> UpdateDocVisitSheet_Save_And_Finish(@Url String url,@Field("doc_sourced_visitsheet_id") String doc_sourced_visitsheet_id
            ,@Field("doc_sourced_employee_id_owner") String doc_sourced_employee_id_owner
            ,@Field("doc_sourced_employee_id_technician") String doc_sourced_employee_id_technician
            ,@Field("meterreading") String meterreading
            ,@Field("reportno") String reportno
            ,@Field("reportdate") String reportdate
            ,@Field("remarks") String remarks);


    @FormUrlEncoded
    @POST
    Call<ResponeItemSpareParts<ResponeInsertItemSpartParts>> SetItemSpareParts(@Url String url, @Field("doc_source_company_id") String doc_source_company_id, @Field("mas_source_establishment_id") String mas_source_establishment_id, @Field("mas_source_station_id") String mas_source_station_id,
                                                                               @Field("doc_source_userid_id") String doc_source_userid_id, @Field("doc_sourced_joborder_id") String doc_sourced_joborder_id, @Field("mas_sourced_tax_id") String mas_sourced_tax_id,
                                                                               @Field("mas_sourced_warehouse_id") String mas_sourced_warehouse_id, @Field("mas_sourced_bin_id") String mas_sourced_bin_id, @Field("mas_sourced_itemmaster_id") String mas_sourced_itemmaster_id,
                                                                               @Field("mas_sourced_pricelevel_id") String mas_sourced_pricelevel_id, @Field("mas_sourced_shippingtype_id") String mas_sourced_shippingtype_id, @Field("map_sourced_valuationmethod_id") String map_sourced_valuationmethod_id,
                                                                               @Field("slip_sourced_unitofmeasure_id") String slip_sourced_unitofmeasure_id, @Field("itemcode") String itemcode, @Field("itemname") String itemname, @Field("barcode") String barcode,
                                                                               @Field("warehouse") String warehouse, @Field("bin") String bin, @Field("unitofmeasure") String unitofmeasure, @Field("multiuom") String multiuom, @Field("quantity") String quantity, @Field("quantityinstock") String quantityinstock,
                                                                               @Field("itemperunit") String itemperunit, @Field("taxpercent") String taxpercent, @Field("itemstatus") String itemstatus, @Field("cost") String cost, @Field("type") String type, @Field("map_sourced_warranty_id") String map_sourced_warranty_id);

}
