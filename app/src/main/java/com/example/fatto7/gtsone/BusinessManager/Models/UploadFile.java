package com.example.fatto7.gtsone.BusinessManager.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadFile {



    @SerializedName("m_sharefolder")
    String shareFolder;

    @SerializedName("m_doc_sourced_visitsheet_id")
    String visitsheetID;

    @SerializedName("m_seq")
    String seq;

    @SerializedName("m_type")
    String type;

    public String getShareFolder() {
        return shareFolder;
    }

    public void setShareFolder(String shareFolder) {
        this.shareFolder = shareFolder;
    }

    public String getVisitsheetID() {
        return visitsheetID;
    }

    public void setVisitsheetID(String visitsheetID) {
        this.visitsheetID = visitsheetID;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Expose
    byte[] file;

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

}
