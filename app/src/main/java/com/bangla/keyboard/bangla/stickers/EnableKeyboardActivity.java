package com.bangla.keyboard.bangla.stickers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class EnableKeyboardActivity extends AppCompatActivity {

    RelativeLayout enableKeyboardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_keyboard);

        enableKeyboardBtn = findViewById(R.id.enableKeyboard);

        enableKeyboardBtn.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), " লিস্ট থেকে \uD83D\uDC49 বাংলা কিবোর্ড \uD83D\uDC48 চালু করুন", Toast.LENGTH_LONG).show();
            Intent enableIntent = new Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS);
            enableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(enableIntent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GeneralFunctions.checkKeyboard(this)) {
//            if (android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.DEFAULT_INPUT_METHOD).contains(getPackageName())) {
//                startActivity(new Intent(EnableKeyboardActivity.this, MainActivity.class));
//            }else{
                startActivity(new Intent(EnableKeyboardActivity.this, ChooseKeyboardActivity.class));
//            }
            finish();
        }
    }
}