package com.example.fatto7.gtsone.Controllers.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.TextView;

import com.example.fatto7.gtsone.Controllers.Fragments.VisitSheetDateFragment;
import com.example.fatto7.gtsone.Controllers.Fragments.VisitSheetFragment;
import com.example.fatto7.gtsone.R;
import com.example.fatto7.gtsone.SharedAndConstants.GTSApplication;

import static com.example.fatto7.gtsone.Controllers.Activities.Login.mypreference;
import static com.example.fatto7.gtsone.Controllers.Activities.Login.uCompanyId;
import static com.example.fatto7.gtsone.Controllers.Activities.Login.uEstablishmentID;
import static com.example.fatto7.gtsone.Controllers.Activities.Login.uNamekey;
import static com.example.fatto7.gtsone.Controllers.Activities.Login.uPassword;

public class VisitSheet extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    Typeface gillsansFont;
    Button logOut, settings;
    TextView visitsheetTitle;
    ViewPager tabsPager;
    TabLayout tabLayout;
    public static int pageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_visit_sheet);


        tabLayout = findViewById(R.id.tabs);
        settings = (Button) findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VisitSheet.this, SettingsActivity.class));
            }
        });
        tabsPager = findViewById(R.id.tabs_viewpager);

        tabLayout.addTab(tabLayout.newTab().setText("TODAY"));
        tabLayout.addTab(tabLayout.newTab().setText("THIS MONTH"));
        tabLayout.addTab(tabLayout.newTab().setText("THIS YEAR"));
        tabLayout.addTab(tabLayout.newTab().setText("DATE"));

        tabsPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount()));
        tabsPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(tabsPager);
        tabLayout.getTabAt(0).setText("TODAY");
        tabLayout.getTabAt(1).setText("THIS MONTH");
        tabLayout.getTabAt(2).setText("THIS YEAR");
        tabLayout.getTabAt(3).setText("DATE");
        tabLayout.setTabMode(TabLayout.MODE_FIXED);


        gillsansFont = Typeface.createFromAsset(this.getAssets(), "fonts/gillsans.ttf");

        checkConnection();

        logOut = findViewById(R.id.logout);
        visitsheetTitle = findViewById(R.id.visit_sheet_title);

        logOut.setTypeface(gillsansFont);
        visitsheetTitle.setTypeface(gillsansFont);


        // logout button
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builderSingle = new AlertDialog.Builder(VisitSheet.this);

                builderSingle.setTitle("Select An Action");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(VisitSheet.this, android.R.layout.select_dialog_item);
                arrayAdapter.add("   Log Out");


                builderSingle.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(VisitSheet.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("Are you sure you want to logout?");
                        builderInner.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences preferences = getSharedPreferences(mypreference, 0);
                                preferences.edit().remove(uNamekey).commit();
                                preferences.edit().remove(uPassword).commit();
                                preferences.edit().remove(uCompanyId).commit();
                                preferences.edit().remove(uEstablishmentID).commit();
                                Intent intent = new Intent(VisitSheet.this, GTS_Logo.class);
                                startActivity(intent);
                                finish();
                            }

                        });
                        builderInner.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }

                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();

            }
        });

        changeTabsFont();


        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(tabsPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);

                pageIndex = tab.getPosition();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected == false) {
            Intent intent = new Intent(this, NoNetwork.class);
            startActivity(intent);
        } else {

        }
    }

    public void checkConnection() {

        boolean isConnected = ConnectivityReceiver.isConnected();

        if (isConnected == false) {
            Intent intent = new Intent(this, NoNetwork.class);
            startActivity(intent);
        } else {

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkConnection();
        // register connection status listener
        GTSApplication.getInstance().setConnectivityListener(this);
    }

    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(gillsansFont, Typeface.NORMAL);
                }
            }
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return new VisitSheetFragment();
                case 1:
                    return new VisitSheetFragment();
                case 2:
                    return new VisitSheetFragment();
                case 3:
                    return new VisitSheetDateFragment();
                default:
                    return null;

            }

        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}







