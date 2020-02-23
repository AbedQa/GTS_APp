package com.example.fatto7.gtsone.Controllers.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.fatto7.gtsone.R;

public class NoNetwork extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_no_network);

        TextView txt = findViewById(R.id.no_internet_txt);
        Button btn = findViewById(R.id.btn);
        Button refresh = findViewById(R.id.btn_refresh);

        Typeface gillsansFontReg = Typeface.createFromAsset(getAssets(), "fonts/gillsans.ttf");
        txt.setTypeface(gillsansFontReg);
        btn.setTypeface(gillsansFontReg);
        refresh.setTypeface(gillsansFontReg);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));

            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoNetwork.this, GTS_Logo.class);
                finish();
                startActivity(intent);
            }
        });

    }
}
