package com.bangla.keyboard.bangla.stickers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.Toast;

import com.bangla.keyboard.bangla.stickers.setting.SettingActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Dialog dialog;
    private AppUpdateManager appUpdateManager;
    private ActionBar actionBar;
    private Toolbar toolbar;
    Boolean chk;
    boolean doubleBackToExitPressedOnce = false;
    boolean drawerState = false;
    LinearLayout homeCardBottom,llBoardSetting,llBoardFont,llSpeechText,llPrivacyPolicy,llShare,llOurWebsite,llOurOtherApps,llRating,llsetting;
    Switch switchVibration;
    NavigationView navView;
    MaterialCardView homeCardCloseBtn, themeCard;
    DrawerLayout drawer;
    CardView translator;
    ImageView drawerImage,ivShare,sharebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initToolbar();
        initClicks();
        initDrawer();

        dialog = new Dialog(MainActivity.this);


        appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);

        // Don't need to do this here anymore
        // Returns an intent object that you use to check for an update.
        //Task<AppUpdateInfo> appUpdateInfo = appUpdateManager.getAppUpdateInfo();

        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {

                            // Checks that the platform will allow the specified type of update.
                            if ((appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                                // Request the update.
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            AppUpdateType.IMMEDIATE,
                                            MainActivity.this,
                                            1001);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
        if(this.getSharedPreferences("options", MODE_PRIVATE).getBoolean("home_show", false)){
            homeCardBottom.setVisibility(View.GONE);
        }
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
    }

    private void initClicks() {
        homeCardCloseBtn.setOnClickListener(v -> {
            this.getSharedPreferences("options", MODE_PRIVATE).edit().putBoolean("home_show", true).commit();
            homeCardBottom.setVisibility(View.GONE);
        });


        themeCard.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, ThemeActivity.class));
        });
translator.setOnClickListener(view->{
    String packageName = "com.banglatranslation.banglatranslator"; // Replace with the package name of the app you want to check

    // Check if the app is installed
    if (isAppInstalled(packageName)) {
        // Open the app
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            startActivity(intent);
        }
    } else {
        // Open the Play Store
        openPlayStore(packageName);
    }

    });

        switchVibration.setOnClickListener(view->{
            if(switchVibration.isChecked()){
                this.getSharedPreferences("options", MODE_PRIVATE).edit().putBoolean("vibration_on", true).commit();
            }else{
                this.getSharedPreferences("options", MODE_PRIVATE).edit().putBoolean("vibration_on", false).commit();
            }
        });
        llSpeechText.setOnClickListener(view -> {
            Toast.makeText(this,"Feature will come in next update",Toast.LENGTH_SHORT).show();
        });
        llBoardFont.setOnClickListener(view -> {
            Toast.makeText(this,"Feature will come in next update",Toast.LENGTH_SHORT).show();
        });
        llBoardSetting.setOnClickListener(view -> {
            showWelcomeDialog();
//            Toast.makeText(this,"Feature will come in next update",Toast.LENGTH_SHORT).show();
        });
        llPrivacyPolicy.setOnClickListener(view -> {
            try {
                String url = getResources().getString(R.string.privacyPolicyUrl);
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(MainActivity.this,R.color.green));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.intent.setPackage("com.android.chrome");
                customTabsIntent.launchUrl(MainActivity.this, Uri.parse(url));
            } catch (Exception ex) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.privacyPolicyUrl))));
            }
        });
        llShare.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    getString(R.string.share_message) +getString(R.string.play_store_url)+ getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });
        llOurWebsite.setOnClickListener(view -> {
            try {
                String url = getResources().getString(R.string.websiteUrl);
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(MainActivity.this, R.color.green));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.intent.setPackage("com.android.chrome");
                customTabsIntent.launchUrl(MainActivity.this, Uri.parse(url));
            } catch (Exception ex) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.websiteUrl))));
            }
        });
        llOurOtherApps.setOnClickListener(view -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.moreAppUrl))));
        });
        llsetting.setOnClickListener(view->{
            startActivity(new Intent(this, SettingActivity.class));
        });
        llRating.setOnClickListener(view -> {
            drawer.closeDrawers();
            showBottomSheetDialogRate(false);
        });
        ivShare.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    getString(R.string.share_message) +getString(R.string.play_store_url)+ getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });
        sharebtn.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    getString(R.string.share_message) +getString(R.string.play_store_url)+ getPackageName());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });

    }

    private void initUI() {
        homeCardBottom = findViewById(R.id.home_bottom_card);
        homeCardCloseBtn = findViewById(R.id.home_card_close);
        drawer = findViewById(R.id.drawer_layout);
        drawerImage = findViewById(R.id.drawerIcon);
        themeCard = findViewById(R.id.themeCard);
        llBoardSetting = findViewById(R.id.ll_board_setting);
        llBoardFont = findViewById(R.id.ll_board_font);
        llSpeechText = findViewById(R.id.ll_speech_text);
        switchVibration=findViewById(R.id.switch_vibration);
        llPrivacyPolicy=findViewById(R.id.ll_privacy_policy);
        llShare=findViewById(R.id.ll_share);
        llOurOtherApps=findViewById(R.id.ll_our_other_apps);
        llOurWebsite=findViewById(R.id.ll_our_website);
        llRating=findViewById(R.id.ll_rating);
        llsetting=findViewById(R.id.ll_setting);
        ivShare=findViewById(R.id.iv_share);
        sharebtn=findViewById(R.id.sharebtn);
        translator=findViewById(R.id.translator);
        if(this.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on",true)){
            this.getSharedPreferences("options", MODE_PRIVATE).edit().putBoolean("vibration_on", true).commit();
            switchVibration.setChecked(true);
        }else{
            switchVibration.setChecked(false);
        }


    }

    private void initDrawer() {

        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                View view=MainActivity.this.getCurrentFocus();
                if(view!=null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                drawerState = true;
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                drawerState = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        drawerImage.setOnClickListener(v -> {
            drawerState = !drawerState;
            if (drawerState) {

                drawer.openDrawer(GravityCompat.START);
            } else {
                drawer.closeDrawers();
            }
        });

    }

    @Override
    protected void onPostResume() {
        showAlert();
        super.onPostResume();
    }

    public void showAlert() {
        final String[] perms = {Manifest.permission.RECORD_AUDIO};
        String rationale = "Bangla keyboard can not work without microphone permission. Please allow it.";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Allow Microphone Permission")
                .setSettingsDialogTitle("Allow Microphone Permission");
        Permissions.check(MainActivity.this, perms, rationale, options , new PermissionHandler() {
            @Override
            public void onGranted() {
                Log.e("GRANTED", "GRANTED");
                // do your task.
            }

            @Override
            public boolean onBlocked(Context context, ArrayList<String> blockedList) {
                Log.e("BLOCKED", "BLOCKED");
                return super.onBlocked(context, blockedList);
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                Log.e("DENIED", "DENIED");
                super.onDenied(context, deniedPermissions);
            }

            @Override
            public void onJustBlocked(Context context, ArrayList<String> justBlockedList, ArrayList<String> deniedPermissions) {
                Log.e("JUSTBLOCKED", "JUSTBLOCKED");
                super.onJustBlocked(context, justBlockedList, deniedPermissions);
            }
        });
    }
    private void showBottomSheetDialogRate(final Boolean chkk) {
        final Dialog dialog = new Dialog(this);
        chk = false;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.ratesheet);
        dialog.setCancelable(true);
        try {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        } catch (Exception ex) {
        }

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.TOP;

        final AppCompatRatingBar rating_bar = (AppCompatRatingBar) dialog.findViewById(R.id.rating_bar);

        ImageButton send = dialog.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText feedback = dialog.findViewById(R.id.feedback);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.email)});
                intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name) + " Feedback");
                intent.putExtra(Intent.EXTRA_TEXT, feedback.getText().toString());
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));
            }
        });

        final LinearLayout item = dialog.findViewById(R.id.item);
        item.setVisibility(View.GONE);

        ((AppCompatButton) dialog.findViewById(R.id.rate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (chk) {
                    if (ratingBar.getRating() < 4) {
                        item.setVisibility(View.VISIBLE);
                    } else {
                        item.setVisibility(View.GONE);
                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                        }
                    }
                }
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (!chkk) {

            ((AppCompatButton) dialog.findViewById(R.id.rate)).setVisibility(View.GONE);

            ((AppCompatButton) dialog.findViewById(R.id.exit)).setText("Rate Us");
            ((AppCompatButton) dialog.findViewById(R.id.exit)).setTextColor(Color.parseColor("#512DA8"));
            ((AppCompatButton) dialog.findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                }
            });
        } else {

            ((AppCompatButton) dialog.findViewById(R.id.exit)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishAffinity();
                }
            });
        }

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (doubleBackToExitPressedOnce) {
                        finishAffinity();
                    }

                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(getApplicationContext(), "অ্যাপ বন্ধ করার জন্য আরেকবার  ব্যাক বাটন ক্লিক করুন", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 1000);
                }
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

        new Thread(new Runnable() {
            @Override
            public void run() {
                rating_bar.setRating(1);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rating_bar.setRating(2);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rating_bar.setRating(3);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rating_bar.setRating(4);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rating_bar.setRating(5);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rating_bar.setRating(0);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rating_bar.setRating(1);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rating_bar.setRating(2);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rating_bar.setRating(3);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rating_bar.setRating(4);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rating_bar.setRating(5);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                rating_bar.setRating(0);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                chk = true;
            }
        }).start();
    }
    private void showWelcomeDialog() {
        dialog.setContentView(R.layout.welcome_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);

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
                this.dialog =new Dialog(MainActivity.this);;
            }
        }, 3000);
        dialog.show();

    }
    private boolean isAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void openPlayStore(String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // If the Play Store app is not available, open the Play Store website
            Uri playStoreUri = Uri.parse("https://play.google.com/store/apps/details?id=" + packageName);
            Intent webIntent = new Intent(Intent.ACTION_VIEW, playStoreUri);
            startActivity(webIntent);
        }
    }
}