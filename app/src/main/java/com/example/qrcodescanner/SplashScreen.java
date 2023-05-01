package com.example.qrcodescanner;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.qrcodescanner.databinding.ActivitySplashScreenBinding;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.IOException;


public class SplashScreen extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    Bitmap bitmap;
    private static final int CAMERA_REQ_CODE = 101;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                Uri resultUri=result.getUri();
                try {
                    bitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(),resultUri);
                    getTextFromImage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    private void getTextFromImage(){
        TextRecognizer recognizer=new TextRecognizer.Builder(this).build();

        if(!recognizer.isOperational()){
            Toast.makeText(this, "No text Found!!", Toast.LENGTH_SHORT).show();
        }else{
            Frame frame=new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray=recognizer.detect(frame);
            StringBuilder stringBuilder=new StringBuilder();
            for(int i=0;i<textBlockSparseArray.size();i++){
                TextBlock textBlock=textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            binding.text.setText(stringBuilder.toString());

        }
    }

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

        if (ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashScreen.this, new String[]{
                    Manifest.permission.CAMERA}, CAMERA_REQ_CODE);
        }
        binding.textscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(SplashScreen.this);

            }
        });



        binding.scanCode.setOnClickListener(new View.OnClickListener() {
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
                if (result.getContents() != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                    builder.setTitle("Result");
                    builder.setMessage(result.getContents());
                    builder.setPositiveButton("Visit website", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String url = result.getContents();
                            if(url!=null){
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }else{
                            dialog.dismiss();}
                        }
                    }).show();
                } else {
                    Toast.makeText(SplashScreen.this, "No content found ", Toast.LENGTH_LONG).show();
                }
            });


}