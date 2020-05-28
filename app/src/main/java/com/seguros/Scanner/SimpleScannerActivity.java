package com.seguros.Scanner;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import com.google.zxing.Result;

/* Implement Activity with ZXingScannerView.ResultHandler */
public class SimpleScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView scannerView;
    String resultText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this); /* Initialize object */
        setContentView(scannerView); /* Set the ScannerView as a content of current activity */

    }

    @Override
    public void onResume() {
        super.onResume();
        /* Asking user to allow access of camera */
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            scannerView.setResultHandler(this); /* Set handler for ZXingScannerView */
            scannerView.startCamera(); /* Start camera */
        } else {
            ActivityCompat.requestPermissions(SimpleScannerActivity.this, new
                    String[]{Manifest.permission.CAMERA}, 1024);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera(); /* Stop camera */
    }

    @Override
    public void handleResult(Result scanResult) {

        ToneGenerator toneNotification = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100); /* Setting beep sound */
        toneNotification.startTone(ToneGenerator.TONE_PROP_BEEP, 150);

        resultText = scanResult.getText(); /* Retrieving text from QR Code */

        Intent resultData = new Intent();
        resultData.putExtra("ResultText", resultText);
        setResult(200,resultData);
        finish();
   //     Intent intent = new Intent(SimpleScannerActivity.this, BaseScannerActivity.class);
   //     intent.putExtra("ResultText", resultText); /* Sending text to next activity to display */
   //     startActivity(intent);

        /* scannerView.resumeCameraPreview(this); */ /* If you want resume scanning, call this method */
    }
}