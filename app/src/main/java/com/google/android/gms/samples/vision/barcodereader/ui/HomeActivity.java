package com.google.android.gms.samples.vision.barcodereader.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.samples.vision.barcodereader.BarcodeCaptureActivity;
import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.samples.vision.barcodereader.databinding.HomeActivityBinding;
import com.google.android.gms.vision.barcode.Barcode;
import com.opencsv.CSVReader;

import org.apache.commons.codec.binary.Hex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "BarcodeMain";
    HomeActivityBinding binding;
    private static final int FILE_SELECT_CODE = 0;
    final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    InputStream input;
    private static final int RC_BARCODE_CAPTURE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        if (shouldAskPermissions()) {
            askPermissions();
        }
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity);
        binding.fileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        binding.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

                startActivityForResult(intent, RC_BARCODE_CAPTURE);
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
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        if (VersionUtils.isAfter26()) {
                            path = PathUtil.getPath(this, uri);
                        } else {
                            path = getPath(this, uri);
                        }
                        printCVSContent(readCVSFromAssetFolder(path));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "File Path: " + path);
                    binding.textView.setText(path);
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
            case RC_BARCODE_CAPTURE:
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    if (data != null) {
                        Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
//                        statusMessage.setText(R.string.barcode_success);
//                        barcodeValue.setText(barcode.displayValue);
                        for (int i = 0; i < barcode.displayValue.length(); i++) {
                            if (convertStringToHex(barcode.displayValue.charAt(i) + "").equals("1d")){
                                Toast.makeText(this, "FNC1 Found", Toast.LENGTH_SHORT).show();
                            }
                        }
                        Toast.makeText(this, barcode.displayValue, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Barcode read: " + barcode.displayValue);
                    } else {
//                        statusMessage.setText(R.string.barcode_failure);
                        Log.d(TAG, "No barcode captured, intent data is null");
                    }
                } else {
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static String convertStringToHex(String str) {

        // display in uppercase
        //char[] chars = Hex.encodeHex(str.getBytes(StandardCharsets.UTF_8), false);

        // display in lowercase, default
        char[] chars = Hex.encodeHex(str.getBytes(StandardCharsets.UTF_8));
        Log.e("Hex", String.valueOf(chars));
        return String.valueOf(chars);
    }

    public String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    protected boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
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
    private List<String[]> readCVSFromAssetFolder(String path) {
        List<String[]> csvLine = new ArrayList<>();
        try {
            File file = new File(path.replace("product_list.csv", ""), "product_list.csv");
            input = new FileInputStream(file);
            ReadCSV csv = new ReadCSV(input);
            csvLine = csv.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvLine;
    }

    private void printCVSContent(List<String[]> result) {
        String cvsColumn = "";
        for (int i = 0; i < result.size(); i++) {
            String[] rows = result.get(i);
            cvsColumn += rows[0] +" " +rows[1] +" "   +rows[2] +" "   +rows[3]  + "\n";
            Log.e("Row "+i,rows[0] +" " +rows[1] +" "   +rows[2] +" "   +rows[3]);
        }
        Toast.makeText(this, cvsColumn, Toast.LENGTH_SHORT).show();
    }
}
