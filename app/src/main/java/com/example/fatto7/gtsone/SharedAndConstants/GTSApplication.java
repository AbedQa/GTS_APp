package com.example.fatto7.gtsone.SharedAndConstants;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.example.fatto7.gtsone.Controllers.Activities.ConnectivityReceiver;

public class GTSApplication extends MultiDexApplication {


    private static GTSApplication mInstance;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized GTSApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

}
