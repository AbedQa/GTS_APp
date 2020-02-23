package com.example.fatto7.gtsone.Controllers.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fatto7.gtsone.BusinessManager.Models.clsPersonalActivity;
import com.example.fatto7.gtsone.R;

import java.util.ArrayList;
import java.util.List;

public class PersonalActivityListAdaper extends ArrayAdapter<clsPersonalActivity> {
    public Context mContext;

    public List<clsPersonalActivity> personalactivityList = new ArrayList<>();
    Typeface gillsansFont;

    public PersonalActivityListAdaper(Context context, List<clsPersonalActivity> list) {
        super(context, 0, list);
        mContext = context;
        personalactivityList = list;
    }

    @Override
    public int getCount() {
        return personalactivityList.size();
    }

    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.personalactivity, parent, false);
        clsPersonalActivity PAList = personalactivityList.get(position);
        gillsansFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/gillsans.ttf");
        TextView PersonalActivityProcedureTV = listItem.findViewById(R.id.PersonalActivityProcedureTV);
        CheckBox PersonalActivityPerforemedTV = listItem.findViewById(R.id.PersonalActivityPerforemedTV);
        EditText PersonalActivityRemarksTV = listItem.findViewById(R.id.PersonalActivityRemarksTV);
        PersonalActivityProcedureTV.setText(PAList.getProcedure());
        PersonalActivityProcedureTV.setTypeface(gillsansFont);
        if (PAList.getPerformed() != null) {
            PersonalActivityPerforemedTV.setChecked(PAList.getPerformed().contains("True") ? true : false);
        }
        if (PAList.getRemarks() != null) {
            PersonalActivityRemarksTV.setText(PAList.getRemarks());
        }
        PersonalActivityPerforemedTV.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (position >= personalactivityList.size()) {
                    return;
                }
                personalactivityList.get(position).setPerformed(isChecked ? "true" : "false");
            }
        });
        PersonalActivityRemarksTV.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (position >= personalactivityList.size()) {
                    return false;
                }
                personalactivityList.get(position).setRemarks(String.valueOf(v.getText() == null ? "" : v.getText()));

                return true;
            }
        });


        return listItem;
    }

    public interface OnDataChangeListener {
        public void onDataChanged(int size);
    }
}
