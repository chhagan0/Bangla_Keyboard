package com.bangla.keyboard.bangla.stickers;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.inputmethodservice.InputMethodService;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;

public class Keyboard extends InputMethodService implements CustomkeyboardView.CityButtonClickListener {
    MyReceiver myReceiver;
    private CustomkeyboardView keyboardViView;
    Boolean first = true;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateInputView() {
        keyboardViView = (CustomkeyboardView) getLayoutInflater().inflate(R.layout.keyboard,null);
        return keyboardViView;
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        switch (info.inputType & InputType.TYPE_MASK_CLASS) {
            case InputType.TYPE_CLASS_NUMBER:
            case InputType.TYPE_CLASS_PHONE:
                getSharedPreferences("inputType", MODE_PRIVATE).edit().putBoolean("inputType", false).apply();
                break;
            default:
                getSharedPreferences("inputType", MODE_PRIVATE).edit().putBoolean("inputType", true).apply();
        }
        setInputView(onCreateInputView());
    }

    @Override
    public void onCityButtonClick(int primaryCode) {
        keyboardViView.setInputConnection(getCurrentInputConnection());
    }

    @Override
    public void onSpeakButton(Boolean serviceRunning) {
        registerReceiver();
        Log.e("Sdfsdf", serviceRunning + "");
        if (!serviceRunning) {
            first = true;
            getApplicationContext().startService(new Intent(getApplicationContext(), Speech.class));
        }
        else {
            Log.e("stopped", "stopped");
            getApplicationContext().stopService(new Intent(getApplicationContext(), Speech.class));
        }
    }

    private void registerReceiver() {
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SENDMESSAGE");
        registerReceiver(myReceiver, intentFilter);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (first) {
                first = false;
                if (intent.getStringExtra("message") != null) {
                    Log.e("message", intent.getStringExtra("message"));
                    getCurrentInputConnection().commitText(intent.getStringExtra("message"), 1);
                }
            }
            if (intent.getBooleanExtra("stop", false)) {
                Log.e("sdfsdfsdf", "Stopped");
                keyboardViView.stop();
            }
        }
    }

}

