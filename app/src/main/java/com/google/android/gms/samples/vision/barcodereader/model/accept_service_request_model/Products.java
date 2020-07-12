package com.google.android.gms.samples.vision.barcodereader.model.accept_service_request_model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Products implements Parcelable {
    private ArrayList<Product> products = new ArrayList<>();


    protected Products(Parcel in) {
        products = in.createTypedArrayList(Product.CREATOR);
    }

    public static final Creator<Products> CREATOR = new Creator<Products>() {
        @Override
        public Products createFromParcel(Parcel in) {
            return new Products(in);
        }

        @Override
        public Products[] newArray(int size) {
            return new Products[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(products);
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
