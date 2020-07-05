package com.google.android.gms.samples.vision.barcodereader.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
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
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "BarcodeMain";
    HomeActivityBinding binding;
    private static final int FILE_SELECT_CODE = 0;
    final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
    InputStream input;
    List<String[]> csvContentData;
    List<Barcode> barcodeList = new ArrayList<>();
    private static final int RC_BARCODE_CAPTURE = 9001;
    private HashMap<String, String> barcodeData = new HashMap<>();
    int aiStart = 0;

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
//                    try {
//                        if (VersionUtils.isAfter26()) {
//                            path = PathUtil.getPath(this, uri);
//                        } else {
//                            path = getPath(this, uri);
//                        }
//                        csvContentData = readCVSFromAssetFolder(path);
//                        printCVSContent(csvContentData);
//                    } catch (URISyntaxException e) {
//                        e.printStackTrace();
//                    }

//TODO: to solve problem of android 10
                    /*
                     * Try to open the file for "read" access using the
                     * returned URI. If the file isn't found, write to the
                     * error log and return.
                     */
                    ParcelFileDescriptor inputPFD = null;
                    try {
                        /*
                         * Get the content resolver instance for this context, and use it
                         * to get a ParcelFileDescriptor for the file.
                         */
                       inputPFD = getContentResolver().openFileDescriptor(uri, "r");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Log.e("MainActivity", "File not found.");
                        Toast.makeText(this,"file not found.",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Get a regular file descriptor for the file
                    FileDescriptor fd = inputPFD.getFileDescriptor();
                    csvContentData = readCVSFromAssetFolder(fd);
                     printCVSContent(csvContentData);

                    Log.d(TAG, "File Path: " + path);
                    binding.textView.setText(uri.getPath());
                    // Get the file instance
                    // File file = new File(path);
                    // Initiate the upload
                }
                break;
            case RC_BARCODE_CAPTURE:
                if (resultCode == CommonStatusCodes.SUCCESS) {
                    if (data != null) {

                        Barcode barcodeObject = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

                        barcodeList.add(barcodeObject);
                        barcode = barcodeObject.displayValue;
                        if (convertStringToHex(barcode.charAt(0) + "").equals("1d")) {
                            for (int i = 0; i < 4; i++) {
                                //  if (convertStringToHex(barcode.charAt(aiStart + 1) + "").equals("1d")) {
////                                    switch (barcode.substring(aiStart + , aiStart + 4)) {
//                                        case "10":
//                                            barcode = barcode.substring(1);
//                                            String bn = getBN();
//                                            barcodeData.put(KEY_BN, bn.substring(2));
//                                            break;
//                                        case "21":
//                                            String sn = getSerialNumber();
//                                            barcodeData.put(KEY_SN, sn.substring(2));
//                                            break;
//
//                                    }
//                                } else
                                switch (barcode.substring(aiStart + 1, aiStart + 3)) {
                                    case "01":
                                        String gtin = getGTIN();
                                        barcodeData.put(KEY_GTIN, gtin.substring(2));
                                        aiStart += 16;
                                        break;
                                    case "17":
                                        String xd = getXD(barcode);
                                        barcodeData.put(KEY_XD, xd.substring(2));
                                        aiStart += 8;
                                        break;
                                    case "21":
                                        String sn = getSerialNumber();
                                        barcodeData.put(KEY_SN, sn.substring(2));
                                        break;
                                    case "10":
                                        String bn = getBN();
                                        barcodeData.put(KEY_BN, bn.substring(2));
                                        break;

                                }
                            }
                        }
                        aiStart = 0;
                        if (csvContentData != null)
                        {
                            if(checkBarCodeExistInCvsFile(csvContentData))
                            {
                                if(barcodeList.size()==1)
                                    binding.firstStateBtn.setBackgroundTintList(getResources().getColorStateList(R.color.state_ok_tint_background));
                                if(barcodeList.size()==2)
                                    binding.secondStateBtn.setBackgroundTintList(getResources().getColorStateList(R.color.state_ok_tint_background));
                                if(barcodeList.size()==3)
                                    binding.thirdStateBtn.setBackgroundTintList(getResources().getColorStateList(R.color.state_ok_tint_background));
                                if(barcodeList.size()==4)
                                    binding.fourStateBtn.setBackgroundTintList(getResources().getColorStateList(R.color.state_ok_tint_background));
                            }
                            else {
                                if(barcodeList.size()==1)
                                    binding.firstStateBtn.setBackgroundTintList(getResources().getColorStateList(R.color.state_false_tint_background));
                                if(barcodeList.size()==2)
                                    binding.secondStateBtn.setBackgroundTintList(getResources().getColorStateList(R.color.state_false_tint_background));
                                if(barcodeList.size()==3)
                                    binding.thirdStateBtn.setBackgroundTintList(getResources().getColorStateList(R.color.state_false_tint_background));
                                if(barcodeList.size()==4)
                                    binding.fourStateBtn.setBackgroundTintList(getResources().getColorStateList(R.color.state_false_tint_background));
                            }
                        }
                            Toast.makeText(this, "check barcode " + checkBarCodeExistInCvsFile(csvContentData), Toast.LENGTH_SHORT).show();


                        Log.i(TAG, barcodeData.values().toString());
                        Toast.makeText(this, barcodeObject.displayValue, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Barcode read: " + barcodeObject.displayValue);
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
    private List<String[]> readCVSFromAssetFolder(FileDescriptor path) {
        List<String[]> csvLine = new ArrayList<>();
        input = new FileInputStream(path);
        ReadCSV csv = new ReadCSV(input);
        csvLine = csv.read();
        return csvLine;
    }
    private boolean checkBarCodeExistInCvsFile(List<String[]> result) {

        for (int i = 0; i < result.size(); i++) {
            String[] rows = result.get(i);
            if (barcodeData.get(KEY_GTIN).equals(rows[0]) &&barcodeData.get(KEY_SN).equals(rows[1]) && barcodeData.get(KEY_BN).equals(rows[2])) {
                if (checkExpiredDate(rows[3]))
                    return true;
            }


        }
        return false;
    }

    private static String KEY_GTIN = "GTIN";
    private static String KEY_SN = "SN";
    private static String KEY_BN = "BN";
    private static String KEY_XD = "XD";

    private void printCVSContent(List<String[]> result) {
        String cvsColumn = "";
        for (int i = 0; i < result.size(); i++) {
            String[] rows = result.get(i);
            cvsColumn += rows[0] + " " + rows[1] + " " + rows[2] + " " + rows[3] + "\n";
            Log.e("Row " + i, rows[0] + " " + rows[1] + " " + rows[2] + " " + rows[3]);
        }
        Toast.makeText(this, cvsColumn, Toast.LENGTH_SHORT).show();
    }


    String barcode;

    private String getGTIN() {

        return barcode.substring(aiStart + 1, aiStart + 17);
    }

    private String getXD(String barCode) {
        return barCode.substring(aiStart + 1, aiStart + 9);
    }

    private String getSerialNumber() {
        String xd = "";
        for (int i = aiStart; i < barcode.length(); i++) {
            if (convertStringToHex(barcode.charAt(i) + "").equals("1d") && i > aiStart) {
                xd = barcode.substring(aiStart + 1, i);
                aiStart += xd.length() + 1;
                return xd;
            } else if (i == barcode.length() - 1) {
                xd = barcode.substring(aiStart + 1);
                aiStart += xd.length();
            }

        }
        return xd;
    }

    private String getBN() {
        String bn = "";
        for (int i = aiStart; i < barcode.length(); i++) {
            if (convertStringToHex(barcode.charAt(i) + "").equals("1d") && i > aiStart) {
                bn = barcode.substring(aiStart + 1, i);
                aiStart += bn.length() + 1;
                break;
            }
            if (i == barcode.length() - 1) {
                bn = barcode.substring(aiStart + 1);
                aiStart += bn.length();

            }
        }

        return bn;
    }

    private boolean checkExpiredDate(String expireDateFromFile) {
        String barcodeExpireDate = barcodeData.get(KEY_XD);
        String[] dateInArray = expireDateFromFile.split("/");
        String day = barcodeExpireDate.substring(4, 6);
        String month = barcodeExpireDate.substring(2, 4);
        String year = String.format("20%s", barcodeExpireDate.substring(0, 2));

        if (dateInArray[0].equals(day) && dateInArray[1].equals(month) && dateInArray[2].equals(year))
            return true;

        return false;
    }
}
