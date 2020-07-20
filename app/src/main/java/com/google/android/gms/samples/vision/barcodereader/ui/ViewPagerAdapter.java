package com.google.android.gms.samples.vision.barcodereader.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.gms.samples.vision.barcodereader.ui.add_unit_manually.AddUnitManually;
import com.google.android.gms.samples.vision.barcodereader.ui.scan_barcode_to_add.ScanBarcode;

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
