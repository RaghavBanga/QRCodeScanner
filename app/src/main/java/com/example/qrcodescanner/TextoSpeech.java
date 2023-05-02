package com.example.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;

import com.example.qrcodescanner.databinding.ActivitySplashScreenBinding;
import com.example.qrcodescanner.databinding.ActivityTextoSpeechBinding;

import org.w3c.dom.Text;

import java.util.Locale;

public class TextoSpeech extends AppCompatActivity {
    ActivityTextoSpeechBinding binding;
    TextToSpeech textToSpeech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTextoSpeechBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

                // if No error is found then only it will run
                if(i!=TextToSpeech.ERROR){
                    // To Choose language of speech
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });

        // Adding OnClickListener
        binding.Speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech.speak(binding.text.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
            }
        });


    }
}