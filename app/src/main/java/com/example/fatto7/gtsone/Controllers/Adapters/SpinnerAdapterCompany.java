package com.example.fatto7.gtsone.Controllers.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.fatto7.gtsone.BusinessManager.Models.clsCompanyRegistration;
import com.example.fatto7.gtsone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a.byyari on 8/29/2017.
 */


public class SpinnerAdapterCompany extends ArrayAdapter<clsCompanyRegistration> {
    private Context mContext;
    private ArrayList<clsCompanyRegistration> listCompany;
    private SpinnerAdapterCompany SpinnerAdapterCompany;
    Typeface gillsansFont ;

    public SpinnerAdapterCompany(Context context, int resource, List<clsCompanyRegistration> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listCompany = (ArrayList<clsCompanyRegistration>) objects;
        this.SpinnerAdapterCompany = this;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        gillsansFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/gillsans.ttf");
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item_company, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.textCompany);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(listCompany.get(position).getName());

        holder.mTextView.setTypeface(gillsansFont);
        // To check weather checked event fire from getview() or user input

        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
    }
}
