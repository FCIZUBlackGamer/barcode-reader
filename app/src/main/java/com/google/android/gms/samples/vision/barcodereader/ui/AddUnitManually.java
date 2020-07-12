package com.google.android.gms.samples.vision.barcodereader.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.samples.vision.barcodereader.databinding.FragmentAddUnitManuallyBinding;
import com.google.android.gms.samples.vision.barcodereader.model.accept_service_request_model.Product;
import com.google.android.material.textfield.TextInputEditText;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;


public class AddUnitManually extends Fragment implements View.OnClickListener {
    FragmentAddUnitManuallyBinding binding;
    NavController navController;

    public AddUnitManually() {
        // Required empty public constructor
    }

    public static AddUnitManually newInstance() {
        AddUnitManually fragment = new AddUnitManually();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_unit_manually, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.show_units_list_fragment);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addNewProductBtn.setOnClickListener(this::onClick);
        binding.cancelBtn.setOnClickListener(this::onClick);
        binding.expiredDateTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker(binding.expiredDateTxt);
                }
            }
        });
    }

    private Product getProductData() {
        Product product = new Product();
        product.setGTIN(binding.gtinEditTxt.getText().toString());
        product.setExpireDate(binding.expiredDateTxt.getText().toString());
        product.setSerialNumber(binding.SerialNumberTxt.getText().toString());
        product.setBatchNumber(binding.batchNumberEditTxt.getText().toString());
        return product;
    }

    private void showDatePicker(TextInputEditText date) {
        final Calendar newCalendar = Calendar.getInstance();
        @SuppressLint("SetTextI18n")
        DatePickerDialog StartTime = new DatePickerDialog(getContext(), R.style.DialogTheme, (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        StartTime.show();
    }

    private void closeDialog() {
        navController.popBackStack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addNewProductBtn:
                EventBus.getDefault().post(getProductData());
            case R.id.cancel_btn:
                closeDialog();
                break;
        }
    }
}
