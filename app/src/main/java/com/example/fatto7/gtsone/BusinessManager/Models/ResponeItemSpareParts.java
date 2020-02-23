package com.example.fatto7.gtsone.BusinessManager.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponeItemSpareParts<E> {
    @SerializedName("ItemSpareParts")
    private List<E> data;


    public void setData(List<E> data) {
        this.data = data;
    }

    public List<E> getData() {
        return data;
    }


    @Override
    public String toString() {
        return
                "Response{" +
                        "data = '" + data + '\'' +
                        "" +
                        "}";
    }
}
