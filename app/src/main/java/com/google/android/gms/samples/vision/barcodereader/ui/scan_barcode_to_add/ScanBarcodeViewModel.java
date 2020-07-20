package com.google.android.gms.samples.vision.barcodereader.ui.scan_barcode_to_add;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.samples.vision.barcodereader.model.accept_service_request_model.Product;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;

public class ScanBarcodeViewModel extends ViewModel {
   private int aiStart = 0;
    private String barcode;
    private String gtin,serialNumber,batchNumber,expireDate;

    public Product getBarcodeData(String barcode){
        this.barcode = barcode;
        Product product = new Product();
        if (convertStringToHex(barcode.charAt(0) + "").equals("1d")) {
            for (int i = 0; i < 4; i++) {
                switch (barcode.substring(aiStart + 1, aiStart + 3)) {
                    case "01":
                      getGTIN();
                        product.setGTIN(gtin);
                        aiStart += 16;
                        break;
                    case "17":
                       product.setExpireDate(getXD(barcode));
                        aiStart += 8;
                        break;
                    case "21":
                        product.setSerialNumber(getSerialNumber());
                        break;
                    case "10":
                       product.setBatchNumber(getBN());
                        break;

                }
            }
        }
        aiStart = 0;
        return product;
    }

    private String getGTIN() {
        gtin = barcode.substring(aiStart + 1, aiStart + 17);
        return gtin;
    }

    private String getXD(String barCode) {

        expireDate = barCode.substring(aiStart + 1, aiStart + 9);
        return expireDate;
    }

    private String getSerialNumber() {

        for (int i = aiStart; i < barcode.length(); i++) {
            if (convertStringToHex(barcode.charAt(i) + "").equals("1d") && i > aiStart) {
                serialNumber = barcode.substring(aiStart + 1, i);
                aiStart += serialNumber.length() + 1;
                return serialNumber;
            } else if (i == barcode.length() - 1) {
                serialNumber = barcode.substring(aiStart + 1);
                aiStart += serialNumber.length();
            }
        }
        return serialNumber;
    }

    private String getBN() {
        for (int i = aiStart; i < barcode.length(); i++) {
            if (convertStringToHex(barcode.charAt(i) + "").equals("1d") && i > aiStart) {
                batchNumber = barcode.substring(aiStart + 1, i);
                aiStart += batchNumber.length() + 1;
                break;
            }
            if (i == barcode.length() - 1) {
                batchNumber = barcode.substring(aiStart + 1);
                aiStart += batchNumber.length();

            }
        }

        return batchNumber;
    }
    public static String convertStringToHex(String str) {
        char[] chars = Hex.encodeHex(str.getBytes(StandardCharsets.UTF_8));
        Log.e("Hex", String.valueOf(chars));
        return String.valueOf(chars);
    }
}
