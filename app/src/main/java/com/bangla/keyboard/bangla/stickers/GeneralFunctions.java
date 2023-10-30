package com.bangla.keyboard.bangla.stickers;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

public class GeneralFunctions {

    public static boolean checkKeyboard(Activity activity){
        String packageLocal = activity.getPackageName();
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        List<InputMethodInfo> list = inputMethodManager.getEnabledInputMethodList();
        // check if our keyboard is enabled as input method
        for (InputMethodInfo inputMethod : list) {
            String packageName = inputMethod.getPackageName();
            if (packageName.equals(packageLocal)) {
                return true;
            }
        }
        return false;
    }
}
