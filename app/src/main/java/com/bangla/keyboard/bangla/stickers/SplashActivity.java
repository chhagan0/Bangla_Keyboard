package com.bangla.keyboard.bangla.stickers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(() -> {
            if (GeneralFunctions.checkKeyboard(this)) {
                if (android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.DEFAULT_INPUT_METHOD).contains(getPackageName())) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this, ChooseKeyboardActivity.class));
                }
            }else{
                startActivity(new Intent(SplashActivity.this, EnableKeyboardActivity.class));
            }
            finish();

        },2000);

    }
}