package com.google.android.gms.samples.vision.barcodereader.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphicTracker;
import com.google.android.gms.samples.vision.barcodereader.BarcodeTrackerFactory;
import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.samples.vision.barcodereader.databinding.FragmentScanCodeToAddBinding;
import com.google.android.gms.samples.vision.barcodereader.model.accept_service_request_model.Product;
import com.google.android.gms.samples.vision.barcodereader.ui.camera.GraphicOverlay;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

public class ScanBarcode extends Fragment implements  View.OnClickListener {
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barcodeDetector =
                new BarcodeDetector.Builder(getContext())
                        .setBarcodeFormats(Barcode.DATA_MATRIX)
                        .build();

        cameraSource = new CameraSource
                .Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(15.0f)
                .build();
        binding.cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    int rc = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
                    if (rc == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(binding.cameraView.getHolder());
                    } else {
                        requestCameraPermission();
                    }

                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }

        });
//        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(binding.graphicOverlay, getContext());
//        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());
        binding.addNewProductBtn.setOnClickListener(this);
        binding.cancelBtn.setOnClickListener(this);
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    product = scanBarcodeViewModel.getBarcodeData(barcodes.valueAt(0).displayValue);
                    binding.GTINTxt.post(new Runnable() {    // Use the post method of the TextView
                        public void run() {
                           binding.GTINTxt.setText(product.getGTIN());
                           binding.batchNumberTxt.setText(product.getBatchNumber());
                           binding.expiredDateTxt.setText(product.getExpireDate());
                           binding.SerialNumberTxt.setText(product.getSerialNumber());
                        }
                    });
                }


                }
        });
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNewProductBtn:
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

}
