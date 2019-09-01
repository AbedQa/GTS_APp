package com.example.fatto7.gtsone.BusinessManager.Models;

import com.google.gson.annotations.SerializedName;

public class ResponeInsertItemSpartParts {
    @SerializedName("NewPK")
    private String newPK;

    public String getNewPK() {
        return newPK;
    }

    public void setNewPK(String newPK) {
        this.newPK = newPK;
    }
}
