package com.google.android.gms.samples.vision.barcodereader.ui.display_and_validate_units;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.samples.vision.barcodereader.model.accept_service_request_model.Product;
import com.google.android.gms.samples.vision.barcodereader.utils.ReadCSV;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DisplayAndValidateUnitsViewModel extends ViewModel {
    ArrayList<Product> productsList = new ArrayList<>();
    MutableLiveData<List<Product>> productsListData = new MutableLiveData<>();

    public MutableLiveData<List<Product>> readDataFromFileAndGetProductList (FileDescriptor path) {
        if(path !=null) {
            List<String[]> csvLine = new ArrayList<>();
            InputStream input = new FileInputStream(path);
            ReadCSV csv = new ReadCSV(input);
            csvLine = csv.read();
            return getProductList(csvLine);
        }
        else {
            productsListData.setValue(productsList);
            return productsListData;
        }
    }

    private MutableLiveData getProductList (List<String[]> result) {
        for (int i = 0; i < result.size(); i++) {
            String[] rows = result.get(i);
            if(i != 0) {
                Product product = new Product();
                product.setGTIN(rows[0]);
                product.setSerialNumber(rows[1]);
                product.setBatchNumber(rows[2]);
                product.setExpireDate(rows[3]);
                productsList.add(product);
            }
        }
        productsListData.setValue(productsList);
        return productsListData;
    }


}
