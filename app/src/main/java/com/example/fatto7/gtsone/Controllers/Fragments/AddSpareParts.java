package com.example.fatto7.gtsone.Controllers.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fatto7.gtsone.BusinessManager.Models.ItemSpareParts;
import com.example.fatto7.gtsone.BusinessManager.Models.PriceLevel;
import com.example.fatto7.gtsone.BusinessManager.Models.PriceLevelResponse;
import com.example.fatto7.gtsone.BusinessManager.Models.ResponeInsertItemSpartParts;
import com.example.fatto7.gtsone.BusinessManager.Models.ResponeItemSpareParts;
import com.example.fatto7.gtsone.ConnectionManager.ApiClient;
import com.example.fatto7.gtsone.Controllers.Activities.GTS_Logo;
import com.example.fatto7.gtsone.Controllers.Activities.VisitSheet_EditAndView;
import com.example.fatto7.gtsone.Controllers.Adapters.SparePartsAdapter;
import com.example.fatto7.gtsone.R;
import com.example.fatto7.gtsone.SharedAndConstants.Constants;
import com.google.gson.Gson;

import net.projectmonkey.object.mapper.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.fatto7.gtsone.Controllers.Activities.GTS_Logo.CoID;
import static com.example.fatto7.gtsone.Controllers.Activities.GTS_Logo.ESTID;
import static com.example.fatto7.gtsone.Controllers.Activities.GTS_Logo.UserId;

public class AddSpareParts extends AppCompatActivity implements SparePartsAdapter.ItemListener, View.OnClickListener, TextWatcher {
    private String priceLevel = "";
    private String CID = "";
    private String CurrancyId = "";
    private String StationId = "";
    private String itemmasterId = "";
    private String VisitSheetId = "";
    private EditText editTextSearch;
    private List<ItemSpareParts> itemSparePartsList = new ArrayList<>();
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;
    SparePartsAdapter sparePartsAdapter;
    private ImageView back;
    private Button SaveandFinishIV;
    public List<ItemSpareParts> itemSparePartsListSelection = new ArrayList<>();
    public List<Integer> servecesDone = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_spare_parts);
        recyclerView = (RecyclerView) findViewById(R.id.myList);
        editTextSearch = (EditText) findViewById(R.id.editTextSearch);
        SaveandFinishIV = (Button) findViewById(R.id.SaveandFinishIV);
        back = (ImageView) findViewById(R.id.back);
        priceLevel = getIntent().getStringExtra("mas_sourced_pricelevel_id");
        VisitSheetId = getIntent().getStringExtra("VisitSheetId");
        CID = GTS_Logo.CoID;
        CurrancyId = GTS_Logo.CurrencyID;
        StationId = GTS_Logo.StationID;
        itemmasterId = getIntent().getStringExtra("mas_sourced_itemmaster_id");
        progressDialog = ProgressDialog.show(AddSpareParts.this, "Please Wait", "Please Wait");
        back.setOnClickListener(this);
        editTextSearch.addTextChangedListener(this);
        SaveandFinishIV.setOnClickListener(this);
        fetchItems();
    }

    private void fetchItems() {
        String ParammWithURL = Constants.BASE_URL + "GetItemSpareParts?doc_sourced_company_id=" + CoID + "&mas_sourced_pricelevel_id=" + priceLevel + "&mas_sourced_currency_id=" + CurrancyId + "&mas_sourced_itemmaster_id=" + itemmasterId;
        ApiClient.getApiManager(getApplicationContext()).GetItemSpareParts(ParammWithURL, new Callback<ResponeItemSpareParts<ItemSpareParts>>() {
            @Override
            public void onResponse(Call<ResponeItemSpareParts<ItemSpareParts>> call, Response<ResponeItemSpareParts<ItemSpareParts>> response) {
                progressDialog.dismiss();
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
                        itemSparePartsList.addAll(response.body().getData());
                        ConfigureAdapter();

                    }
                } catch (Exception e) {
                    Log.d("", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<com.example.fatto7.gtsone.BusinessManager.Models.ResponeItemSpareParts<ItemSpareParts>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void ConfigureAdapter() {
        LinearLayoutManager lLayout = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lLayout);
        sparePartsAdapter = new SparePartsAdapter(getApplicationContext(), itemSparePartsList, this);
        recyclerView.setAdapter(sparePartsAdapter);
    }

    @Override
    public void onItemClick(ItemSpareParts item) {

    }

    @Override
    public void onItemClickAddRemove(boolean isAdded, ItemSpareParts spareParts, int position) {
        if (isAdded) {
            itemSparePartsListSelection.add(spareParts);
        } else {
            if (itemSparePartsListSelection.size() > position)
                itemSparePartsListSelection.remove(position);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == back) {
            finish();
        } else if (v == SaveandFinishIV) {
            CallServer(0);
        }
    }

    private void CallServer(final int position) {
        if (itemSparePartsListSelection.size() == 0)
            return;
        if (itemSparePartsListSelection.size() <= position) {
            return;
        }
        List<HashMap<String, String>> hashMapArrayList = new ArrayList<>();
        ItemSpareParts itemSpareParts = itemSparePartsListSelection.get(position);
        String ParammWithURL = Constants.BASE_URL + "InsertItemSpareParts";
        ApiClient.getApiManager(getApplicationContext()).SetItemSpareParts(ParammWithURL, CoID, ESTID, StationId, UserId, VisitSheetId, itemSpareParts.getMasSourcedTaxoutputId()
                , itemSpareParts.getMasSourcedDefwarehouseId(), itemSpareParts.getMasSourcedDefbinId(),
                itemSpareParts.getId(), priceLevel, itemSpareParts.getMasSourcedShippingtypeId(),
                itemSpareParts.getMapSourcedValuationmethodId(), itemSpareParts.getUOMId(), itemSpareParts.getNum(),
                itemSpareParts.getName(), itemSpareParts.getNum(), itemSpareParts.getDefaultWarehouse(), itemSpareParts.getDefaultBin(),
                itemSpareParts.getUnitOfMeasure(), itemSpareParts.getMultiUOM(), itemSpareParts.getQuantity(), itemSpareParts.getQuantityInStock(),
                itemSpareParts.getUOMMultiplier(), itemSpareParts.getTAXPercentage(), itemSpareParts.getStatus(), itemSpareParts.getCost(),
                itemSpareParts.getType(), itemSpareParts.getWarrantyId(), new Callback<ResponeItemSpareParts<ResponeInsertItemSpartParts>>() {
                    @Override
                    public void onResponse(Call<ResponeItemSpareParts<ResponeInsertItemSpartParts>> call, Response<ResponeItemSpareParts<ResponeInsertItemSpartParts>> response) {
                        progressDialog.dismiss();
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
                                int nextPosition = position + 1;

                                servecesDone.add(nextPosition);

                                if (servecesDone.size() == itemSparePartsListSelection.size()) {
                                    finish();
                                } else {
                                    CallServer(nextPosition);
                                }

                            }
                        } catch (Exception e) {
                            Log.d("", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponeItemSpareParts<ResponeInsertItemSpartParts>> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        filter(editable.toString());
    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        List<ItemSpareParts> filterdNames = new ArrayList<>();
        if (itemSparePartsList.size() == 0) {
            return;
        }
        //looping through existing elements
        for (ItemSpareParts s : itemSparePartsList) {
            //if the existing elements contains the search input
            if (s.getName().toLowerCase().contains(text.toLowerCase()) || s.getId().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }
        if (sparePartsAdapter == null) {
            return;
        }
        //calling a method of the adapter class and passing the filtered list
        sparePartsAdapter.filterList(filterdNames);
    }
}


