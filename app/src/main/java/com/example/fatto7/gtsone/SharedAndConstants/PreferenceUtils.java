package com.example.fatto7.gtsone.SharedAndConstants;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;



public class PreferenceUtils {
    private static final String BASEURL_KEY = "USER";


    private SharedPreferences sharedPreferences;

    public PreferenceUtils(Context mContext) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }




    public void setBASEURL(String baseurl) {
        sharedPreferences.edit().putString(BASEURL_KEY, baseurl).commit();
    }



    public String getBASEURL() {
        return sharedPreferences.getString(BASEURL_KEY, "");
    }
}
