package com.google.android.gms.samples.vision.barcodereader.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.google.android.gms.samples.vision.barcodereader.R;

public class Home2Activity extends AppCompatActivity  {
    HomeViewModel homeViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

    }


}
