package com.example.fatto7.gtsone.Controllers.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fatto7.gtsone.BusinessManager.Models.clsTechnicianV_S;
import com.example.fatto7.gtsone.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapterTechnicianV_S extends ArrayAdapter<clsTechnicianV_S> {
    private Context mContext;
    private ArrayList<clsTechnicianV_S> listTechnician;
    private SpinnerAdapterTechnicianV_S spinnerAdapterTechnicianV_s;
    Typeface gillsansFont;

    public SpinnerAdapterTechnicianV_S(Context context, int resource, List<clsTechnicianV_S> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listTechnician = (ArrayList<clsTechnicianV_S>) objects;
        this.spinnerAdapterTechnicianV_s = this;
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

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item_technician_v_s, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.textTechnician);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(listTechnician.get(position).getName());
        gillsansFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/gillsans.ttf");
        holder.mTextView.setTypeface(gillsansFont);

        // To check weather checked event fire from getview() or user input


        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
    }
}