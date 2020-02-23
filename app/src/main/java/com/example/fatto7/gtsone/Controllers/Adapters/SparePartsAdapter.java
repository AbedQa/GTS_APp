package com.example.fatto7.gtsone.Controllers.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fatto7.gtsone.BusinessManager.Models.ItemSpareParts;
import com.example.fatto7.gtsone.R;
import com.example.fatto7.gtsone.SharedAndConstants.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public class SparePartsAdapter extends RecyclerView.Adapter<SparePartsAdapter.MyViewHolder> {

    private Context mContext;
    private List<ItemSpareParts> mData;
    protected SparePartsAdapter.ItemListener mListener;

    public SparePartsAdapter(Context mContext, List<ItemSpareParts> mData, SparePartsAdapter.ItemListener itemListener) {
        this.mContext = mContext;
        this.mData = mData;
        mListener = itemListener;

    }

    public void filterList(List<ItemSpareParts> filterdNames) {
        this.mData = filterdNames;
        notifyDataSetChanged();
    }


    @Override
    public SparePartsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.spear_parts_card, parent, false);

        return new SparePartsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SparePartsAdapter.MyViewHolder holder, final int position) {
        holder.setData(mData.get(position));
        holder.mListener = mListener;
        holder.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle, Num, Quantity, Group;
        Button min, max;
        TextView txNumber;
        private PreferenceUtils preferenceUtils;
        ItemSpareParts item;
        ImageView addRemoveImageView;
        protected SparePartsAdapter.ItemListener mListener;
        private Context mContext;
        private boolean isAdd = false;
        private double maxQuantity = 0;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvTitle = (TextView) itemView.findViewById(R.id.title);
            min = (Button) itemView.findViewById(R.id.MinBtn);
            max = (Button) itemView.findViewById(R.id.PluBtn);
            txNumber = (TextView) itemView.findViewById(R.id.txNumber);
            Num = (TextView) itemView.findViewById(R.id.Num);
            Quantity = (TextView) itemView.findViewById(R.id.Quantity);
            Group = (TextView) itemView.findViewById(R.id.Group);
            addRemoveImageView = (ImageView) itemView.findViewById(R.id.AddRemove);
            preferenceUtils = new PreferenceUtils(itemView.getContext());
            addRemoveImageView.setOnClickListener(this);
            min.setOnClickListener(this);
            max.setOnClickListener(this);

        }


        public void setData(ItemSpareParts item) {
            this.item = item;
            tvTitle.setText(item.getName() == null ? "" : item.getName());
            Num.setText(item.getNum() == null ? "" : item.getNum());
            String qty = (item.getQuantityInStock() == null ? "" : item.getQuantityInStock());
            if (qty.isEmpty()) {
                qty = "0";
            } else if (qty.length() > 5) {
                qty = qty.substring(0, 4);
            }
            maxQuantity = Double.parseDouble(qty);
            Quantity.setText("Quantity In Stock : " + (qty));
            Group.setText(item.getGroup() == null ? "" : item.getGroup());

        }

        @Override
        public void onClick(View v) {
            if (v == addRemoveImageView) {
                if (item.getType().contains("1")) {
                    if ((int) maxQuantity == 0) {
                        Toast.makeText(mContext, "Can't Added because you don't have Quantity In Stock", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (txNumber.getText() == null || txNumber.getText().toString().equals("0")) {
                        Toast.makeText(mContext, "Please add quantity to add on your cart", Toast.LENGTH_SHORT).show();

                        return;
                    }

                    item.setQuantity(txNumber.getText() == null ? "0" : txNumber.getText().toString());
                } else {
                    item.setQuantity("1");
                }
                isAdd = !isAdd;
                if (isAdd) {
                    addRemoveImageView.setImageResource(R.drawable.ic_close_black_24dp);
                } else {
                    addRemoveImageView.setImageResource(R.drawable.ic_add_black_24dp);
                }
                mListener.onItemClickAddRemove(isAdd, item, getAdapterPosition());
            } else if (v == min) {
                int quantity = Integer.parseInt(txNumber.getText() == null ? "0" : txNumber.getText().toString());
                if (quantity == 0) {
                    quantity = 0;
                } else {
                    quantity--;
                }
                txNumber.setText("" + quantity);
            } else if (v == max) {
                int quantity = Integer.parseInt(txNumber.getText() == null ? "0" : txNumber.getText().toString());

                quantity++;
                double vlue = new Double(quantity);
                if (maxQuantity <= vlue) {
                    quantity = (int) maxQuantity;
                }
                txNumber.setText("" + quantity);
            } else {
                if (mListener != null) {
                    mListener.onItemClick(item);
                }
            }

        }

    }

    public interface ItemListener {
        void onItemClick(ItemSpareParts item);

        void onItemClickAddRemove(boolean isAdded, ItemSpareParts spareParts, int position);
    }

}


