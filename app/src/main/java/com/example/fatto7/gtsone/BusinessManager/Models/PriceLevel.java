package com.example.fatto7.gtsone.BusinessManager.Models;

import com.google.gson.annotations.SerializedName;

public class PriceLevel {
    @SerializedName("Price Level Id")

    private String priceLevelId;

    public String getPriceLevelId() {
        return priceLevelId;
    }

    public void setPriceLevelId(String priceLevelId) {
        this.priceLevelId = priceLevelId;
    }
}
