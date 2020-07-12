package com.google.android.gms.samples.vision.barcodereader.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPagerAdapter  extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return AddUnitManually.newInstance();
            case 1:
                return ScanBarcode.newInstance();

        }
        return null;

    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
