package com.example.fatto7.gtsone.Controllers.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fatto7.gtsone.BusinessManager.Models.clsVisitSheetStatusEdit;
import com.example.fatto7.gtsone.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapterVisitSheetStatusEdit  extends ArrayAdapter<clsVisitSheetStatusEdit> {
    private Context mContext;
    private ArrayList<clsVisitSheetStatusEdit> listStatusEdit;
    private SpinnerAdapterVisitSheetStatusEdit spinnerAdapterVisitSheetStatusEdit;
    Typeface gillsansFont;

    public SpinnerAdapterVisitSheetStatusEdit(Context context, int resource, List<clsVisitSheetStatusEdit> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listStatusEdit = (ArrayList<clsVisitSheetStatusEdit>) objects;
        this.spinnerAdapterVisitSheetStatusEdit = this;
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

        final SpinnerAdapterVisitSheetStatusEdit.ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item_status_edit, null);
            holder = new SpinnerAdapterVisitSheetStatusEdit.ViewHolder();
            holder.mTextView = (TextView) convertView.findViewById(R.id.textStatusEdit);

            convertView.setTag(holder);
        } else {
            holder = (SpinnerAdapterVisitSheetStatusEdit.ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(listStatusEdit.get(position).getName());
        gillsansFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/gillsans.ttf");
        holder.mTextView.setTypeface(gillsansFont);

        // To check weather checked event fire from getview() or user input


        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
    }
}
