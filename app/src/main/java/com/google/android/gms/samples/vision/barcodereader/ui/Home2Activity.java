package com.google.android.gms.samples.vision.barcodereader.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphicTracker;
import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.samples.vision.barcodereader.model.accept_service_request_model.Product;
import com.google.android.gms.vision.barcode.Barcode;

import org.greenrobot.eventbus.EventBus;

public class Home2Activity extends AppCompatActivity  {
    HomeViewModel homeViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

    }


}
