package com.bangla.keyboard.bangla.stickers;

import android.app.Application;

import com.onesignal.OneSignal;

public class ApplicationClass extends Application {
    private static final String ONESIGNAL_APP_ID = "9474bb57-f2c9-464f-a493-84ab2f92a749";

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }
}
