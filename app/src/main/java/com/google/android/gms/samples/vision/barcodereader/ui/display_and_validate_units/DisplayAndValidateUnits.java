package com.google.android.gms.samples.vision.barcodereader.ui.display_and_validate_units;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.samples.vision.barcodereader.databinding.FragmentDisplayAndValidateUnitsBinding;
import com.google.android.gms.samples.vision.barcodereader.model.accept_service_request_model.Product;
import com.google.android.gms.samples.vision.barcodereader.ui.ShowUnitsList;
import com.google.android.gms.samples.vision.barcodereader.utils.ReadCSV;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.plus.PlusOneDummyView.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayAndValidateUnits extends Fragment {
    private FragmentDisplayAndValidateUnitsBinding binding;
    private static final int FILE_SELECT_CODE = 0;
    private ParcelFileDescriptor inputPFD = null;
    private DisplayAndValidateUnitsViewModel viewModel;
    private static final String KEY_PRODUCTS_LIST = "productList";
    private NavController navController;
    private ArrayList<Product> productArrayList  = null;
    FileDescriptor fd;
    public DisplayAndValidateUnits() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_display_and_validate_units, container, false);
        viewModel = ViewModelProviders.of(this).get(DisplayAndValidateUnitsViewModel.class);
        showAndHideViews();
        getProductList(fd);
        binding.chooseFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        return binding.getRoot();
    }

    public void showAndHideViews() {
        binding.typeOfSource.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.batchFileRadioButton:
                        binding.notificationListViewInputLayout.setVisibility(View.GONE);
                        binding.filePathTxtView.setVisibility(View.VISIBLE);
                        binding.chooseFileBtn.setVisibility(View.VISIBLE);
                        break;
                    case R.id.notifyIdRadioButton:
                        binding.notificationListViewInputLayout.setVisibility(View.VISIBLE);
                        binding.filePathTxtView.setVisibility(View.GONE);
                        binding.chooseFileBtn.setVisibility(View.GONE);
                        break;

                }
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        inputPFD = requireActivity().getContentResolver().openFileDescriptor(uri, "r");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Log.e("MainActivity", "File not found.");
                        Toast.makeText(getContext(), "file not found.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                     fd = inputPFD.getFileDescriptor();
                    viewModel.readDataFromFileAndGetProductList(fd);
                    Log.d(TAG, "File Path: " + path);
                    binding.filePathTxtView.setText(uri.getPath());

                }
                break;


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getProductList(FileDescriptor fd){
        // initiate the observer for the first time to get the onchanged updates data
        viewModel.readDataFromFileAndGetProductList(fd).observe(getViewLifecycleOwner(), new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                if(products.size()>0) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(KEY_PRODUCTS_LIST, (ArrayList<? extends Parcelable>) products);
                    navController = Navigation.findNavController(getActivity(), R.id.show_units_list_fragment);
                    navController.setGraph(navController.getGraph(), bundle);
                    if(binding.productListLinearLayout.getVisibility() == View.GONE)
                    binding.productListLinearLayout.setVisibility(View.VISIBLE);
                }

            }
        });
    }



}
