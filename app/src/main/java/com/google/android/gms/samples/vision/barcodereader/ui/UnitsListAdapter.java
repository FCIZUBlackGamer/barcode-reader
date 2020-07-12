package com.google.android.gms.samples.vision.barcodereader.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.samples.vision.barcodereader.databinding.UnitListItemBinding;
import com.google.android.gms.samples.vision.barcodereader.model.accept_service_request_model.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class UnitsListAdapter extends RecyclerView.Adapter<UnitsListAdapter.UnitListViewHolder> {
    private Context mContext;
    private ArrayList<Product> productList = new ArrayList<>();
    private String TAG = UnitsListAdapter.class.getSimpleName();
    UnitListItemBinding unitListItemBinding;

    public UnitsListAdapter(Context mContext, ArrayList<Product> productList) {
        this.mContext = mContext;
        this.productList = productList;
    }

    @NonNull
    @Override
    public UnitsListAdapter.UnitListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        unitListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.unit_list_item, parent, false);
        return new UnitListViewHolder(unitListItemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull UnitsListAdapter.UnitListViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.unitListItemBinding.GTINTxt.setText(product.getGTIN());
        holder.unitListItemBinding.SerialNumberTxt.setText(product.getSerialNumber());
        holder.unitListItemBinding.BatchNumberTxt.setText(product.getBatchNumber());
        holder.unitListItemBinding.expiredDateTxt.setText(product.getExpireDate());

    }

    @Override
    public int getItemCount() {
        if(productList!=null)
        return productList.size();
        else return 0;
    }


    class UnitListViewHolder extends RecyclerView.ViewHolder {

        UnitListItemBinding unitListItemBinding;

        public UnitListViewHolder(UnitListItemBinding unitListItemBinding) {
            super(unitListItemBinding.getRoot());
            this.unitListItemBinding = unitListItemBinding;

        }
    }
}
