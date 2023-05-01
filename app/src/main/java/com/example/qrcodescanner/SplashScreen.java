package com.example.qrcodescanner;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrcodescanner.databinding.ActivitySplashScreenBinding;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


public class SplashScreen extends AppCompatActivity {
    ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        getSupportActionBar().hide();
//
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanCode();
            }
        });

    }

    private void ScanCode() {
        ScanOptions options = new ScanOptions();
        options.setOrientationLocked(true);
        options.setPrompt("Volume up Button for flash");
        options.setBeepEnabled(true);
        options.setCaptureActivity(CaptureAct.class);
        barcodeLauncher.launch(options);
    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() != null) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(SplashScreen.this);
                    builder.setTitle("Result");
                    builder.setMessage(result.getContents());
                    builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                } else {
                    Toast.makeText(SplashScreen.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
            });



}