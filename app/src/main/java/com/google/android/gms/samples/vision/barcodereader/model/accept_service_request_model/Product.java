package com.google.android.gms.samples.vision.barcodereader.model.accept_service_request_model;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.Date;

@Root(name = "PRODUCT")
public class Product implements Parcelable {
    @Element(name = "GTIN")
    private String GTIN ;
    @Element(name = "SN")
    private String SerialNumber;
    @Element(name = "XD",required = false)
    private String expireDate;
    @Element (name = "BN",required = false)
    private String batchNumber;


    public Product(Parcel in) {
        GTIN = in.readString();
        SerialNumber = in.readString();
        batchNumber = in.readString();
    }
    public Product(){

    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getGTIN() {
        return GTIN;
    }

    public void setGTIN(String GTIN) {
        this.GTIN = GTIN;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(GTIN);
        dest.writeString(SerialNumber);
        dest.writeString(batchNumber);
    }
}
