package com.bangla.keyboard.bangla.stickers;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ChooseKeyboardActivity extends AppCompatActivity {

    Dialog dialog;
    RelativeLayout chooseKeyboardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_keyboard);

        chooseKeyboardBtn = findViewById(R.id.chooseKeyboard);
        dialog = new Dialog(ChooseKeyboardActivity.this);

        chooseKeyboardBtn.setOnClickListener(v -> {
            Toast.makeText(getApplicationContext(), " লিস্ট থেকে \uD83D\uDC49 বাংলা কিবোর্ড \uD83D\uDC48 বাছুন", Toast.LENGTH_LONG).show();
            InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
            imeManager.showInputMethodPicker();
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (GeneralFunctions.checkKeyboard(this)) {
            if (android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.DEFAULT_INPUT_METHOD).contains(getPackageName())) {
                if(dialog!=null && !dialog.isShowing())
               showWelcomeDialog();
            }
        }else{
            startActivity(new Intent(ChooseKeyboardActivity.this, EnableKeyboardActivity.class));
            finishAffinity();
        }
    }

    private void showWelcomeDialog() {
        dialog.setContentView(R.layout.welcome_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);

        Handler handler = new Handler();

        handler.postDelayed((Runnable) () -> {
            try {
                if ((this.dialog != null) && this.dialog.isShowing()) {
                    this.dialog.dismiss();
                }
            } catch (final IllegalArgumentException e) {
                // Handle or log or ignore
            } catch (final Exception e) {
                // Handle or log or ignore
            } finally {
                this.dialog = null;
            }
            startActivity(new Intent(ChooseKeyboardActivity.this, MainActivity.class));
            finishAffinity();
        }, 500);
        dialog.show();

    }

}