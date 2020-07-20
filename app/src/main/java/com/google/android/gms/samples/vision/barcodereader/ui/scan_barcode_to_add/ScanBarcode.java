package com.google.android.gms.samples.vision.barcodereader.ui.scan_barcode_to_add;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.samples.vision.barcodereader.databinding.FragmentScanCodeToAddBinding;
import com.google.android.gms.samples.vision.barcodereader.model.accept_service_request_model.Product;
import com.google.android.gms.samples.vision.barcodereader.ui.customviews.RectOverlay;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class ScanBarcode extends Fragment implements View.OnClickListener {

    ScanBarcodeViewModel scanBarcodeViewModel;
    FragmentScanCodeToAddBinding binding;
    Product product;
    NavController navController;
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    public ScanBarcode() {
        // Required empty public constructor
    }

    public static ScanBarcode newInstance() {
        ScanBarcode fragment = new ScanBarcode();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scan_code_to_add, container, false);
        scanBarcodeViewModel = ViewModelProviders.of(this).get(ScanBarcodeViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.show_units_list_fragment);
        if (shouldAskPermissions()) {
            askPermissions();
        }
        int rc = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            initiateCameraView();
        } else {
            requestCameraPermission();
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.addNewProductBtn.setOnClickListener(this);
        binding.cancelBtn.setOnClickListener(this);
    }

    private void initiateCameraView() {
        binding.cameraView.setAutoFocus(true)
                .setPreviewSize(750, 750)
                .setBeepSound(true)
                .setVibration(200)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .drawOverlay(new RectOverlay(getContext()));
        Disposable disposable = binding.cameraView.getObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(barcode -> {
            product = scanBarcodeViewModel.getBarcodeData(barcode.displayValue);
            binding.GTINTxt.setText(product.getGTIN());
            binding.batchNumberTxt.setText(product.getBatchNumber());
            binding.expiredDateTxt.setText(product.getExpireDate());
            binding.SerialNumberTxt.setText(product.getSerialNumber());
        }, Throwable::printStackTrace);
    }

    private void closeDialog() {
        navController.popBackStack();
    }

    private static final String TAG = ScanBarcode.class.getSimpleName();

    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(requireActivity(), permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = requireActivity();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };
        Snackbar.make(binding.addressInputLayout, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNewProductBtn:
                if(product!=null)
                EventBus.getDefault().post(product);

            case R.id.cancel_btn:
                closeDialog();
                break;
        }
    }

    @TargetApi(23)
    protected void askPermissions() {
        String[] permissions = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions, requestCode);
    }

    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");

            initiateCameraView();
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getActivity().finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Multitracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

}
