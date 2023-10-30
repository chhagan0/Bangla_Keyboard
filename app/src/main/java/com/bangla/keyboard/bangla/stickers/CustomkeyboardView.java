package com.bangla.keyboard.bangla.stickers;

import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.provider.Settings.System.getString;

import static androidx.browser.customtabs.CustomTabsClient.getPackageName;
import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bangla.keyboard.bangla.stickers.setting.SettingActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class CustomkeyboardView extends LinearLayout implements View.OnClickListener, KeyboardView.OnKeyboardActionListener {
    private Keyboard keyboard;
    MyReceiver myReceiver;
    Boolean isCaps = false;
    Context context;
    KeyboardView keyboardView, keyboardkey;
    InputConnection inputConnection;
    Integer option = 0;
    LinearLayout linearLayout, llTop, llBengla, llEnglish, numericKeyboard, llTopBangla, llsign, llmic, lloption,lldone,llll;
    ImageView text, cancel, ivoption;
    TextView question, comma, dot, btn_hash, btn_dollar, btn_and, number, settings, tvSpeakedText, tvAbc, text1, text2, text3, text4, text5, text6, text7, text8, text9, text10, tvNum0, tvNum1, tvNum2, tvNum3, tvNum4, tvNum5, tvNum6, tvNum7, tvNum8, tvNum9, tvPercent, tvAdd, tvMinus, tvStar, tvDivide, tvNumEquals, tvNumDot, tvNum, tvCopiedText, tvSign;
    CardView cvEnglish, cvBengali, cvLanguage;
    ImageView done, ivMic, settingsTop, ivSpace, ivClear, ivDone, ivSpeak;
    String current = "ENGLISH";
    boolean twoSeconds = false;
    boolean needToSmall = false;
    Boolean serviceRunning = false;
    Button speak;
    TextView listening, space, tvEnglish, tvBengali, language;
    FrameLayout frameLayout;
    TextView nointernet;
    RecyclerView recyclerView;
    View viewBengali, viewEnglish;
    Boolean inputType = true; //true for text

    public CustomkeyboardView(Context context) {
        this(context, null, 0);
    }

    public CustomkeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomkeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @SuppressLint({"ClickableViewAccessibility", "ResourceType"})
    private void init(final Context context, AttributeSet attrs) {
        this.context = context;
        registerReceiverCustomKeyBoardView();
        int nightModeFlags =
                getContext().getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            if (!context.getSharedPreferences("color", MODE_PRIVATE).getBoolean("change", false)) {
                context.getSharedPreferences("color", MODE_PRIVATE).edit().putBoolean("light", false).apply();
                context.getSharedPreferences("color", MODE_PRIVATE).edit().putBoolean("dark", true).apply();
                context.getSharedPreferences("color", MODE_PRIVATE).edit().putString("color", "color2").apply();
            }
        } else {
            if (!context.getSharedPreferences("color", MODE_PRIVATE).getBoolean("light", false)) {
                context.getSharedPreferences("color", MODE_PRIVATE).edit().putBoolean("dark", false).apply();
                context.getSharedPreferences("color", MODE_PRIVATE).edit().putBoolean("change", false).apply();
                context.getSharedPreferences("color", MODE_PRIVATE).edit().putString("color", "color1").apply();
            }
        }

        // initialize buttons
        if (context.getSharedPreferences("color", MODE_PRIVATE).getString("color", "color1").equalsIgnoreCase("color1"))
            LayoutInflater.from(context).inflate(R.layout.keyboard_view2, this, true);
        else if (context.getSharedPreferences("color", MODE_PRIVATE).getString("color", "color2").equalsIgnoreCase("color2"))
            LayoutInflater.from(context).inflate(R.layout.keyboard_view3, this, true);
        else
            LayoutInflater.from(context).inflate(R.layout.keyboard_view, this, true);

        recyclerView = findViewById(R.id.recyclerView);
        keyboardView = findViewById(R.id.keyy);
        keyboardkey = findViewById(R.xml.qwerty4);
        linearLayout = findViewById(R.id.linear);
        llTop = findViewById(R.id.ll_top);
        llBengla = findViewById(R.id.ll_bengla);
        llEnglish = findViewById(R.id.ll_english);
        viewBengali = findViewById(R.id.view_bengali);
        viewEnglish = findViewById(R.id.view_english);
        settingsTop = findViewById(R.id.settings_top);
        numericKeyboard = findViewById(R.id.numeric_keyboard);
        tvAbc = findViewById(R.id.tv_abc);
        llTopBangla = findViewById(R.id.ll_top_bangla);
        llsign = findViewById(R.id.ll_normal_sign);
        llmic = findViewById(R.id.ll_circle_mic);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        text5 = findViewById(R.id.text5);
        text6 = findViewById(R.id.text6);
        text7 = findViewById(R.id.text7);
        text8 = findViewById(R.id.text8);
        text9 = findViewById(R.id.text9);
        text10 = findViewById(R.id.text10);
        ivSpace = findViewById(R.id.iv_space);
        ivClear = findViewById(R.id.iv_clear);
        ivDone = findViewById(R.id.iv_done);
        tvNum0 = findViewById(R.id.tv_num0);
        tvNum1 = findViewById(R.id.tv_num1);
        tvNum2 = findViewById(R.id.tv_num2);
        tvNum3 = findViewById(R.id.tv_num3);
        tvNum4 = findViewById(R.id.tv_num4);
        tvNum5 = findViewById(R.id.tv_num5);
        tvNum6 = findViewById(R.id.tv_num6);
        tvNum7 = findViewById(R.id.tv_num7);
        tvNum8 = findViewById(R.id.tv_num8);
        tvNum9 = findViewById(R.id.tv_num9);
        tvAdd = findViewById(R.id.tv_add);
        tvMinus = findViewById(R.id.tv_minus);
        tvStar = findViewById(R.id.tv_star);
        tvDivide = findViewById(R.id.tv_divide);
        tvPercent = findViewById(R.id.tv_num_percent);
        tvNumEquals = findViewById(R.id.tv_num_equals);
        tvNumDot = findViewById(R.id.tv_num_dot);
        tvNum = findViewById(R.id.tv_num);
        tvCopiedText = findViewById(R.id.tv_copied_text);
        cvLanguage = findViewById(R.id.cv_language);
        ivSpeak = findViewById(R.id.iv_speak);
        tvSign = findViewById(R.id.tv_sign);
        ivoption = findViewById(R.id.iv_option);
        lloption = findViewById(R.id.ll_option);
        lldone = findViewById(R.id.ll_done);
        llll = findViewById(R.id.ll_ll);
        ImageView ivoptionleft = findViewById(R.id.iv_option_left);
        ImageView ivemoji = findViewById(R.id.iv_emoji);
        ImageView ivsetting = findViewById(R.id.iv_setting);
        ImageView ivtheme = findViewById(R.id.iv_theme);
        ImageView ivshare = findViewById(R.id.iv_share);
        ivtheme.setOnClickListener(view -> {
            context.startActivity(new Intent(context, ThemeActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK));

        });
        ivshare.setOnClickListener(view -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    getResources().getString(R.string.share_message) + getResources().getString(R.string.play_store_url) +"com.bangla.keyboard.bangla.stickers"
            );
            sendIntent.setType("text/plain");
            context.startActivity(sendIntent);

        });
        ivsetting.setOnClickListener(view -> {
            context.startActivity(new Intent(context, SettingActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK));

        });

        lloption.setOnClickListener(view -> {
            if (option == 0) {
                option = 1;
                ivemoji.setVisibility(VISIBLE);
                ivsetting.setVisibility(VISIBLE);
                ivtheme.setVisibility(VISIBLE);
                ivoption.setVisibility(GONE);
                ivoptionleft.setVisibility(VISIBLE);
                ivshare.setVisibility(VISIBLE);
                cvLanguage.setVisibility(VISIBLE);
                llll.setVisibility(VISIBLE);

            } else {
                option = 0;
                ivoption.setVisibility(VISIBLE);
                ivoptionleft.setVisibility(GONE);
                ivemoji.setVisibility(GONE);
                ivsetting.setVisibility(GONE);
                ivtheme.setVisibility(GONE);
                ivshare.setVisibility(GONE);
                cvLanguage.setVisibility(GONE);
                llll.setVisibility(GONE);
                ivoption.setBackgroundResource(R.drawable.baseline_chevron_right_24);
            }
        });
        if (!context.getSharedPreferences("option", MODE_PRIVATE).getBoolean("sign", true)) {
            tvSign.setVisibility(GONE);
            tvCopiedText.setVisibility(INVISIBLE);
        }

        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm.hasPrimaryClip()) {
            ClipData data = cm.getPrimaryClip();
            if (data.getItemCount() > 0) {
                CharSequence text = data.getItemAt(0).coerceToText(context);
                if (text != null && !text.toString().isEmpty()) {
                    // Put your paste-handling code here
                    tvCopiedText.setVisibility(VISIBLE);
                    tvSign.setVisibility(GONE);
                    tvCopiedText.setText(text);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!tvCopiedText.getText().toString().isEmpty()) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    cm.clearPrimaryClip();
                                } else {
                                    cm.setText("");
                                }
                                tvCopiedText.setVisibility(INVISIBLE);
                                tvCopiedText.setText("");
//                                tvCopiedText.setHint("copied text");
                            }
                        }
                    }, 300000);
                }
            }
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        ArrayList<Emoji> emojisAll = new ArrayList(EmojiManager.getAll());
        ArrayList<String> emojis = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i == 9) {
                emojis.add("0");
            } else {
                emojis.add(String.valueOf(i + 1));
            }
        }
        for (Emoji e : emojisAll) {
            emojis.add(e.getHtmlDecimal());
        }
        AdapterListEmoji mAdapter = new AdapterListEmoji(context, emojis);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterListEmoji.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String obj, int position) {
                setCityButtonClickListener((CityButtonClickListener) context);
                buttonClickListener.onCityButtonClick(0);
                inputConnection.commitText(Html.fromHtml(obj), 1);
            }
        });
        if (context.getSharedPreferences("options", MODE_PRIVATE).getInt("options", 1) == 3) {
            setBanglaBackground();
        } else {
            llTopBangla.setVisibility(View.GONE);
            setEnglishBackground();
        }
        tvCopiedText.setOnClickListener(view -> {
            if (!tvCopiedText.getText().toString().isEmpty()) {
                setCityButtonClickListener((CityButtonClickListener) context);
                buttonClickListener.onCityButtonClick(0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    cm.clearPrimaryClip();
                } else {
                    cm.setText("");
                }
                inputConnection.commitText(tvCopiedText.getText().toString(), 1);
                tvCopiedText.setVisibility(INVISIBLE);
                tvCopiedText.setText("");
//                tvCopiedText.setHint("copied text");
            }
        });
        text1.setOnClickListener(view -> {
            vibrationOnKeyPress();
            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            if (text1.getText().toString().equals("অ")) {
                inputConnection.commitText(text1.getText().toString(), 1);
            }
//            inputConnection.commitText(text1.getText().toString(), 1);

        });
        text2.setOnClickListener(view -> {
            vibrationOnKeyPress();
            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            if (text2.getText().toString().equals("আ")) {
                inputConnection.commitText(text2.getText().toString(), 1);
            } else {
                setTopRowBanglaText();
                inputConnection.commitText("া", 1);
            }

        });
        text3.setOnClickListener(view -> {
            vibrationOnKeyPress();

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            if (text3.getText().toString().equals("ই")) {
                inputConnection.commitText(text3.getText().toString(), 1);
            } else {
                setTopRowBanglaText();
                inputConnection.commitText("ি", 1);
            }

        });
        text4.setOnClickListener(view -> {
            vibrationOnKeyPress();

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            if (text4.getText().toString().equals("ঈ")) {
                inputConnection.commitText(text4.getText().toString(), 1);
            } else {
                setTopRowBanglaText();
                inputConnection.commitText("ী", 1);
            }

        });
        text5.setOnClickListener(view -> {
            vibrationOnKeyPress();

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            if (text5.getText().toString().equals("উ")) {
                inputConnection.commitText(text5.getText().toString(), 1);
            } else {
                setTopRowBanglaText();
                inputConnection.commitText("ু", 1);
            }

        });
        text6.setOnClickListener(view -> {
            vibrationOnKeyPress();
            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            if (text6.getText().toString().equals("ঊ")) {
                inputConnection.commitText(text6.getText().toString(), 1);
            } else {
                setTopRowBanglaText();
                inputConnection.commitText("ু", 1);
            }

        });
        text7.setOnClickListener(view -> {
            vibrationOnKeyPress();
            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            if (text7.getText().toString().equals("এ")) {
                inputConnection.commitText(text7.getText().toString(), 1);
            } else {
                setTopRowBanglaText();
                inputConnection.commitText("ে", 1);
            }

        });
        text8.setOnClickListener(view -> {
            vibrationOnKeyPress();

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            if (text8.getText().toString().equals("ঐ")) {
                inputConnection.commitText(text8.getText().toString(), 1);
            } else {
                setTopRowBanglaText();
                inputConnection.commitText("ৈ", 1);
            }

        });
        text9.setOnClickListener(view -> {
            vibrationOnKeyPress();

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            if (text9.getText().toString().equals("ও")) {
                inputConnection.commitText(text9.getText().toString(), 1);
            } else {
                setTopRowBanglaText();
                inputConnection.commitText("ে" + "া", 1);
            }

        });
        text10.setOnClickListener(view -> {
            vibrationOnKeyPress();
            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            if (text10.getText().toString().equals("ঔ")) {
                inputConnection.commitText(text10.getText().toString(), 1);
            } else {
                setTopRowBanglaText();
                inputConnection.commitText("ে" + "ৗ", 1);
            }

        });
        ivClear.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }
            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            if (TextUtils.isEmpty(inputConnection.getSelectedText(0)))
                inputConnection.deleteSurroundingText(1, 0);
            else inputConnection.commitText("", 1);
        });
        ivSpace.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            setText(32);
        });
        ivDone.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
            inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
        });

        tvNum0.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.commitText(tvNum0.getText().toString(), 1);
        });
        tvNum1.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.commitText(tvNum1.getText().toString(), 1);
        });
        tvNum2.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.commitText(tvNum2.getText().toString(), 1);
        });
        tvNum3.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.commitText(tvNum3.getText().toString(), 1);
        });
        tvNum4.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.commitText(tvNum4.getText().toString(), 1);
        });
        tvNum5.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.commitText(tvNum5.getText().toString(), 1);
        });
        tvNum6.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.commitText(tvNum6.getText().toString(), 1);
        });
        tvNum7.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.commitText(tvNum7.getText().toString(), 1);
        });
        tvNum8.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.commitText(tvNum8.getText().toString(), 1);
        });
        tvNum9.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.commitText(tvNum9.getText().toString(), 1);
        });
        tvAdd.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.commitText(tvAdd.getText().toString(), 1);
        });
        tvMinus.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.commitText(tvMinus.getText().toString(), 1);
        });
        tvStar.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.commitText(tvStar.getText().toString(), 1);
        });
        tvDivide.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.commitText(tvDivide.getText().toString(), 1);
        });
        tvPercent.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.commitText(tvPercent.getText().toString(), 1);
        });
        tvNumEquals.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.commitText(tvNumEquals.getText().toString(), 1);
        });
        tvNumDot.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }

            setCityButtonClickListener((CityButtonClickListener) context);
            buttonClickListener.onCityButtonClick(0);
            inputConnection.commitText(tvNumDot.getText().toString(), 1);
        });
        llBengla.setOnClickListener(view -> {
                    context.getSharedPreferences("option", MODE_PRIVATE).edit().putBoolean("sign", false).apply();
                    tvSign.setVisibility(GONE);
                    tvCopiedText.setVisibility(INVISIBLE);
                    setBanglaBackground();
                }
        );
        llEnglish.setOnClickListener(view -> {
            llTopBangla.setVisibility(View.GONE);
            setEnglishBackground();
        });
        settingsTop.setOnClickListener(view -> {
            if (serviceRunning) {
                setCityButtonClickListener((CityButtonClickListener) context);
                buttonClickListener.onSpeakButton(serviceRunning);
                serviceRunning = false;
                stopp();
            }
            context.startActivity(new Intent(context, ThemeActivity.class).addFlags(FLAG_ACTIVITY_NEW_TASK));
        });
        String color = (context.getSharedPreferences("color", MODE_PRIVATE).getString("color", "color1"));
        switch (color) {
            case "color1":


                keyboardView.setBackgroundColor(Color.parseColor("#EBEFF2"));
                linearLayout.setBackgroundColor(Color.parseColor("#EBEFF2"));
                llTop.setBackgroundColor(Color.parseColor("#EBEFF2"));
//                recyclerView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                ThemeAdapter.selectedPosition = 0;
                break;
            case "color2":
                keyboardView.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
                linearLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
                llTop.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
//                recyclerView.setBackgroundColor(ContextCompat.getColor(context, R.color.black));
                ThemeAdapter.selectedPosition = 1;
                break;
            case "color3":
                settingsTop.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                text1.setBackgroundResource(R.drawable.btn_keyboard_purpal);
                text2.setBackgroundResource(R.drawable.btn_keyboard_purpal);
                text3.setBackgroundResource(R.drawable.btn_keyboard_purpal);
                text4.setBackgroundResource(R.drawable.btn_keyboard_purpal);
                text5.setBackgroundResource(R.drawable.btn_keyboard_purpal);
                text6.setBackgroundResource(R.drawable.btn_keyboard_purpal);
                text7.setBackgroundResource(R.drawable.btn_keyboard_purpal);
                text8.setBackgroundResource(R.drawable.btn_keyboard_purpal);
                text9.setBackgroundResource(R.drawable.btn_keyboard_purpal);
                text10.setBackgroundResource(R.drawable.btn_keyboard_purpal);
                llsign.setBackgroundColor(Color.parseColor("#DBE0FF"));
                tvAbc.setBackgroundColor(Color.parseColor("#DBE0FF"));
                tvPercent.setBackgroundColor(Color.parseColor("#DBE0FF"));
                ivSpace.setBackgroundColor(Color.parseColor("#DBE0FF"));
                ivClear.setBackgroundColor(Color.parseColor("#DBE0FF"));
                tvNumDot.setBackgroundColor(Color.parseColor("#DBE0FF"));
                llmic.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DBE0FF")));
                lldone.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#DBE0FF")));
                Keyboard keyboard = new Keyboard(this.getContext(), R.xml.qwerty4);
                keyboardView.setKeyboard(keyboard);



                keyboardView.setBackgroundColor(getResources().getColor(R.color.color_3));
                numericKeyboard.setBackgroundColor(getResources().getColor(R.color.color_3));
                linearLayout.setBackgroundColor(getResources().getColor(R.color.color_3));
                llTop.setBackgroundColor(getResources().getColor(R.color.color_3));
                llTopBangla.setBackgroundColor(getResources().getColor(R.color.color_3));
                numericKeyboard.setBackgroundColor(getResources().getColor(R.color.color_3));
                recyclerView.setBackgroundColor(getResources().getColor(R.color.color_3));
                ThemeAdapter.selectedPosition = 2;
                break;
            case "color4":
                text1.setBackgroundResource(R.drawable.btn_keyboard_green);
                text2.setBackgroundResource(R.drawable.btn_keyboard_green);
                text3.setBackgroundResource(R.drawable.btn_keyboard_green);
                text4.setBackgroundResource(R.drawable.btn_keyboard_green);
                text5.setBackgroundResource(R.drawable.btn_keyboard_green);
                text6.setBackgroundResource(R.drawable.btn_keyboard_green);
                text7.setBackgroundResource(R.drawable.btn_keyboard_green);
                text8.setBackgroundResource(R.drawable.btn_keyboard_green);
                text9.setBackgroundResource(R.drawable.btn_keyboard_green);
                text10.setBackgroundResource(R.drawable.btn_keyboard_green);
                llsign.setBackgroundColor(Color.parseColor("#E2FFE7"));
                tvAbc.setBackgroundColor(Color.parseColor("#E2FFE7"));
                tvPercent.setBackgroundColor(Color.parseColor("#E2FFE7"));
                ivSpace.setBackgroundColor(Color.parseColor("#E2FFE7"));
                ivClear.setBackgroundColor(Color.parseColor("#E2FFE7"));
                tvNumDot.setBackgroundColor(Color.parseColor("#E2FFE7"));
                llmic.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E2FFE7")));

                settingsTop.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                keyboardView.setBackground(ContextCompat.getDrawable(context, R.drawable.color_4));
                numericKeyboard.setBackground(ContextCompat.getDrawable(context, R.drawable.color_4));
                linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.color_4));
                numericKeyboard.setBackground(ContextCompat.getDrawable(context, R.drawable.color_4));
                llTop.setBackgroundColor(Color.parseColor("#C5ECCB"));
                llTopBangla.setBackgroundColor(Color.parseColor("#C5ECCB"));
//                recyclerView.setBackgroundColor(Color.parseColor("#C9A909"));
                ThemeAdapter.selectedPosition = 3;
                break;
            case "color5":
                text1.setBackgroundResource(R.drawable.btn_keyboard_pink);
                text2.setBackgroundResource(R.drawable.btn_keyboard_pink);
                text3.setBackgroundResource(R.drawable.btn_keyboard_pink);
                text4.setBackgroundResource(R.drawable.btn_keyboard_pink);
                text5.setBackgroundResource(R.drawable.btn_keyboard_pink);
                text6.setBackgroundResource(R.drawable.btn_keyboard_pink);
                text7.setBackgroundResource(R.drawable.btn_keyboard_pink);
                text8.setBackgroundResource(R.drawable.btn_keyboard_pink);
                text9.setBackgroundResource(R.drawable.btn_keyboard_pink);
                text10.setBackgroundResource(R.drawable.btn_keyboard_pink);
                llsign.setBackgroundColor(Color.parseColor("#FFE1E1"));
                tvAbc.setBackgroundColor(Color.parseColor("#FFE1E1"));
                tvPercent.setBackgroundColor(Color.parseColor("#FFE1E1"));
                ivSpace.setBackgroundColor(Color.parseColor("#FFE1E1"));
                ivClear.setBackgroundColor(Color.parseColor("#FFE1E1"));
                tvNumDot.setBackgroundColor(Color.parseColor("#FFE1E1"));
                llmic.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFE1E1")));

                settingsTop.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                keyboardView.setBackground(ContextCompat.getDrawable(context, R.drawable.color_5));
                numericKeyboard.setBackground(ContextCompat.getDrawable(context, R.drawable.color_5));
                linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.color_5));
                numericKeyboard.setBackground(ContextCompat.getDrawable(context, R.drawable.color_5));
                llTop.setBackgroundColor(Color.parseColor("#FBE7E7"));
                llTopBangla.setBackgroundColor(Color.parseColor("#FBE7E7"));
//                recyclerView.setBackgroundColor(Color.parseColor("#04AF3D"));
                ThemeAdapter.selectedPosition = 4;
                break;
            case "color6":
                text1.setBackgroundResource(R.drawable.btn_keyboard_yellow);
                text2.setBackgroundResource(R.drawable.btn_keyboard_yellow);
                text3.setBackgroundResource(R.drawable.btn_keyboard_yellow);
                text4.setBackgroundResource(R.drawable.btn_keyboard_yellow);
                text5.setBackgroundResource(R.drawable.btn_keyboard_yellow);
                text6.setBackgroundResource(R.drawable.btn_keyboard_yellow);
                text7.setBackgroundResource(R.drawable.btn_keyboard_yellow);
                text8.setBackgroundResource(R.drawable.btn_keyboard_yellow);
                text9.setBackgroundResource(R.drawable.btn_keyboard_yellow);
                text10.setBackgroundResource(R.drawable.btn_keyboard_yellow);
                llsign.setBackgroundColor(Color.parseColor("#FFE5B1"));
                tvAbc.setBackgroundColor(Color.parseColor("#FFE5B1"));
                tvPercent.setBackgroundColor(Color.parseColor("#FFE5B1"));
                ivSpace.setBackgroundColor(Color.parseColor("#FFE5B1"));
                ivClear.setBackgroundColor(Color.parseColor("#FFE5B1"));
                tvNumDot.setBackgroundColor(Color.parseColor("#FFE5B1"));
                llmic.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFE5B1")));

                settingsTop.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white)));
                keyboardView.setBackground(ContextCompat.getDrawable(context, R.drawable.color_6));
                linearLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.color_6));
                numericKeyboard.setBackground(ContextCompat.getDrawable(context, R.drawable.color_6));
                llTop.setBackgroundColor(Color.parseColor("#FFEEC8"));
                llTopBangla.setBackgroundColor(Color.parseColor("#FFEEC8"));
//                recyclerView.setBackgroundColor(Color.parseColor("#04AF3D"));
                ThemeAdapter.selectedPosition = 5;
                break;
////            case "color7":
//                keyboardView.setBackgroundColor(getResources().getColor(R.color.color7));
//                linearLayout.setBackgroundColor(getResources().getColor(R.color.color7));
//                recyclerView.setBackgroundColor(getResources().getColor(R.color.color7));
//                break;
//            case "color8":
//                keyboardView.setBackgroundColor(getResources().getColor(R.color.color8));
//                linearLayout.setBackgroundColor(getResources().getColor(R.color.color8));
//                recyclerView.setBackgroundColor(getResources().getColor(R.color.color8));
//                break;
//            case "color9":
//                keyboardView.setBackgroundColor(getResources().getColor(R.color.color9));
//                linearLayout.setBackgroundColor(getResources().getColor(R.color.color9));
//                recyclerView.setBackgroundColor(getResources().getColor(R.color.color9));
//                break;
//            case "color10":
//                keyboardView.setBackgroundColor(getResources().getColor(R.color.color10));
//                linearLayout.setBackgroundColor(getResources().getColor(R.color.color10));
//                recyclerView.setBackgroundColor(getResources().getColor(R.color.color10));
//                break;
        }
        language = findViewById(R.id.language);
        text = findViewById(R.id.text);
        listening = findViewById(R.id.listening);
        frameLayout = findViewById(R.id.frame);
        number = findViewById(R.id.number);
        settings = findViewById(R.id.settings);
        question = findViewById(R.id.question);
        comma = findViewById(R.id.comma);
        dot = findViewById(R.id.dot);
        space = findViewById(R.id.space);
        btn_hash = findViewById(R.id.btn_hash);
        btn_dollar = findViewById(R.id.btn_dollar);
        btn_and = findViewById(R.id.btn_and);
        cvEnglish = findViewById(R.id.cv_english);
        cvBengali = findViewById(R.id.cv_bengali);
        tvEnglish = findViewById(R.id.tv_english);
        tvBengali = findViewById(R.id.tv_bengali);
//        tvSpeakedText = findViewById(R.id.tv_speaked_text);
        ivMic = findViewById(R.id.iv_mic);
        final SharedPreferences preferences = context.getSharedPreferences("prefs", MODE_PRIVATE);

        if (preferences.getString("lang", "en").contains("en")) {
            tvBengali.setTextColor(ContextCompat.getColor(context, R.color.black));
            tvEnglish.setTextColor(ContextCompat.getColor(context, R.color.white));
            cvBengali.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));
            cvEnglish.setCardBackgroundColor(ContextCompat.getColor(context, R.color.themeColor));
            preferences.edit().putString("lang", "en-IN").commit();
            language.setText("en");
        } else {
            tvBengali.setTextColor(ContextCompat.getColor(context, R.color.white));
            tvEnglish.setTextColor(ContextCompat.getColor(context, R.color.black));
            cvBengali.setCardBackgroundColor(ContextCompat.getColor(context, R.color.themeColor));
            cvEnglish.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));
            preferences.edit().putString("lang", "bn").commit();
            language.setText("bn");
        }


        ivMic.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //mic
                //change keyboard
                llTopBangla.setVisibility(View.GONE);
                numericKeyboard.setVisibility(GONE);
                llTop.setVisibility(GONE);
                recyclerView.setVisibility(VISIBLE);
                isCaps = false;
                if (option == 0) {
                    context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 0).commit();
                    keyboard = new Keyboard(getContext(), R.xml.qwerty1);
                    keyboardView.setKeyboard(keyboard);
                    option = 1;
                } else if (option == 1) {
                    context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 1).commit();
                    keyboard = new Keyboard(getContext(), R.xml.qwerty);
                    keyboardView.setKeyboard(keyboard);
                    option = 0;
                } else if (option == 2) {
                    context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 2).commit();
                    keyboard = new Keyboard(getContext(), R.xml.qwerty4);
                    keyboardView.setKeyboard(keyboard);
                    option = 3;
                } else if (option == 3) {
                    context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 3).commit();
                    keyboard = new Keyboard(getContext(), R.xml.qwerty3);
                    keyboardView.setKeyboard(keyboard);
                    option = 2;
                }
                if (!haveNetworkConnection()) {
                    nointernet.setVisibility(VISIBLE);
                } else {
                    nointernet.setVisibility(GONE);
                }
                context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 2).commit();
                keyboardView.setVisibility(GONE);
                linearLayout.setVisibility(VISIBLE);
                setCityButtonClickListener((CityButtonClickListener) context);
                buttonClickListener.onSpeakButton(serviceRunning);
                serviceRunning = true;
                startGrow();
            }
        });
        cvBengali.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                stopp();
                tvBengali.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvEnglish.setTextColor(ContextCompat.getColor(context, R.color.black));
                cvBengali.setCardBackgroundColor(ContextCompat.getColor(context, R.color.themeColor));
                cvEnglish.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));
                preferences.edit().putString("lang", "bn").commit();
                listening.setText("কথা বলতে আলতো চাপুন");
                language.setText("bn");
            }
        });
        cvEnglish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                stopp();
                tvBengali.setTextColor(ContextCompat.getColor(context, R.color.black));
                tvEnglish.setTextColor(ContextCompat.getColor(context, R.color.white));
                cvBengali.setCardBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));
                cvEnglish.setCardBackgroundColor(ContextCompat.getColor(context, R.color.themeColor));
                final SharedPreferences preferences = context.getSharedPreferences("prefs", MODE_PRIVATE);
                preferences.edit().putString("lang", "en-IN").commit();
                listening.setText("Tap to speak");
                language.setText("en");
            }
        });
        if (!context.getSharedPreferences("inputType", MODE_PRIVATE).getBoolean("inputType", true)) {
            numericKeyboard.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            keyboardView.setVisibility(GONE);
            llTopBangla.setVisibility(GONE);
            cvLanguage.setVisibility(INVISIBLE);
            option = 1;
        } else {
            cvLanguage.setVisibility(VISIBLE);
            if (context.getSharedPreferences("options", MODE_PRIVATE).getInt("options", 1) == 0) {
                keyboard = new Keyboard(getContext(), R.xml.qwerty1);
                recyclerView.setVisibility(VISIBLE);
//                llTop.setVisibility(GONE);
                option = 1;
            } else if (context.getSharedPreferences("options", MODE_PRIVATE).getInt("options", 1) == 1) {
                recyclerView.setVisibility(GONE);
                keyboard = new Keyboard(getContext(), R.xml.qwerty);
                option = 0;
            } else if (context.getSharedPreferences("options", MODE_PRIVATE).getInt("options", 1) == 3) {
                recyclerView.setVisibility(GONE);
                keyboard = new Keyboard(getContext(), R.xml.qwerty3);
                option = 2;
            } else {
                keyboard = new Keyboard(getContext(), R.xml.qwerty);
                keyboardView.setVisibility(GONE);
                recyclerView.setVisibility(GONE);
                linearLayout.setVisibility(VISIBLE);
            }
        }
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        tvNum.setOnClickListener(view -> {
            llTopBangla.setVisibility(GONE);
            if (context.getSharedPreferences("options", MODE_PRIVATE).getInt("options", 1) == 3 || context.getSharedPreferences("options", MODE_PRIVATE).getInt("options", 1) == 2) {
                option = 2;
            } else {
                option = 0;
            }
            llTop.setVisibility(VISIBLE);
            linearLayout.setVisibility(GONE);
            numericKeyboard.setVisibility(GONE);
            numericKeyboard.setVisibility(GONE);

//                keyboard = new Keyboard(getContext(), R.xml.qwerty);
//                keyboardView.setKeyboard(keyboard);
            recyclerView.setVisibility(GONE);
            keyboardView.setVisibility(VISIBLE);
//                option = 0;
//                context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 1).commit();
            if (option == 0) {
                context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 0).commit();
                keyboard = new Keyboard(getContext(), R.xml.qwerty1);
                keyboardView.setKeyboard(keyboard);
                option = 1;
            } else if (option == 1) {
                context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 1).commit();
                keyboard = new Keyboard(getContext(), R.xml.qwerty);
                keyboardView.setKeyboard(keyboard);
                option = 0;
            } else if (option == 2) {
                context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 2).commit();
                keyboard = new Keyboard(getContext(), R.xml.qwerty4);
                keyboardView.setKeyboard(keyboard);
                option = 3;
            } else if (option == 3) {
                context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 3).commit();
                keyboard = new Keyboard(getContext(), R.xml.qwerty3);
                keyboardView.setKeyboard(keyboard);
                option = 2;
            }
            if (serviceRunning) {
                setCityButtonClickListener((CityButtonClickListener) context);
                buttonClickListener.onSpeakButton(serviceRunning);
                serviceRunning = false;
                stopp();
            }
        });
        text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                llTop.setVisibility(VISIBLE);
                cvLanguage.setVisibility(VISIBLE);
                linearLayout.setVisibility(GONE);
                numericKeyboard.setVisibility(GONE);

//                keyboard = new Keyboard(getContext(), R.xml.qwerty);
//                keyboardView.setKeyboard(keyboard);
                recyclerView.setVisibility(GONE);
                keyboardView.setVisibility(VISIBLE);
//                option = 0;
//                context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 1).commit();
                if (option == 1 || option == 0) {
                    llTopBangla.setVisibility(GONE);
                    context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 1).commit();
                    keyboard = new Keyboard(getContext(), R.xml.qwerty);
                    keyboardView.setKeyboard(keyboard);
                    option = 0;
                } else if (option == 3 || option == 2) {
                    llTopBangla.setVisibility(VISIBLE);
                    context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 3).commit();
                    keyboard = new Keyboard(getContext(), R.xml.qwerty3);
                    keyboardView.setKeyboard(keyboard);
                    option = 2;
                    if (keyboard != null) {
                        if (!context.getSharedPreferences("color", MODE_PRIVATE).getString("color", "color1").equalsIgnoreCase("color1")) {
                            Keyboard.Key key = findKey(keyboard, 654321);
                            Keyboard.Key key1 = findKey(keyboard, -5);
                            if (key != null)
                                key.icon = ContextCompat.getDrawable(context, R.drawable.group_arrow_white);
                            if (key1 != null)
                                key1.icon = ContextCompat.getDrawable(context, R.drawable.cross_white);

                        }
                    }
                }
                if (serviceRunning) {
                    setCityButtonClickListener((CityButtonClickListener) context);
                    buttonClickListener.onSpeakButton(serviceRunning);
                    serviceRunning = false;
                    stopp();
                }
            }
        });
        tvAbc.setOnClickListener(view -> {
            if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
                vibrationOnKeyPress();
            }
            llTop.setVisibility(VISIBLE);
            cvLanguage.setVisibility(VISIBLE);
            numericKeyboard.setVisibility(GONE);
            linearLayout.setVisibility(GONE);
//                keyboard = new Keyboard(getContext(), R.xml.qwerty);
//                keyboardView.setKeyboard(keyboard);
            recyclerView.setVisibility(GONE);
            keyboardView.setVisibility(VISIBLE);
//                option = 0;
//                context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 1).commit();
            if (option == 0) {
                context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 0).commit();
                keyboard = new Keyboard(getContext(), R.xml.qwerty1);
                keyboardView.setKeyboard(keyboard);
                option = 1;
            } else if (option == 1) {
                context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 1).commit();
                keyboard = new Keyboard(getContext(), R.xml.qwerty);
                keyboardView.setKeyboard(keyboard);
                option = 0;
            } else if (option == 2) {
                context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 2).commit();
                keyboard = new Keyboard(getContext(), R.xml.qwerty4);
                keyboardView.setKeyboard(keyboard);
                option = 3;
            } else if (option == 3) {
                llTopBangla.setVisibility(VISIBLE);
                context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 3).commit();
                keyboard = new Keyboard(getContext(), R.xml.qwerty3);
                keyboardView.setKeyboard(keyboard);
                option = 2;
                if (keyboard != null) {
                    if (!context.getSharedPreferences("color", MODE_PRIVATE).getString("color", "color1").equalsIgnoreCase("color1")) {
                        Keyboard.Key key = findKey(keyboard, 654321);
                        Keyboard.Key key1 = findKey(keyboard, -5);
                        if (key != null)
                            key.icon = ContextCompat.getDrawable(context, R.drawable.group_arrow_white);
                        if (key1 != null)
                            key1.icon = ContextCompat.getDrawable(context, R.drawable.cross_white);

                    }
                }
            }
            if (serviceRunning) {
                setCityButtonClickListener((CityButtonClickListener) context);
                buttonClickListener.onSpeakButton(serviceRunning);
                serviceRunning = false;
                stopp();
            }
        });
        cancel = findViewById(R.id.cancel);

///for performing the repeated action
        cancel.setOnTouchListener(new View.OnTouchListener() {

            private Handler mHandler;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 60);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            final Runnable mAction = new Runnable() {
                @Override
                public void run() {
                    setCityButtonClickListener((CityButtonClickListener) context);
                    buttonClickListener.onCityButtonClick(0);
                    if (TextUtils.isEmpty(inputConnection.getSelectedText(0)))
                        inputConnection.deleteSurroundingText(1, 0);
                    else inputConnection.commitText("", 1);
                    mHandler.postDelayed(this, 60);
                }
            };

        });

        ///for performing the repeated action

//        ivClear.setOnTouchListener(new View.OnTouchListener() {
//
//            private Handler mHandler;
//
//            @Override public boolean onTouch(View v, MotionEvent event) {
//                switch(event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        if (mHandler != null) return true;
//                        mHandler = new Handler();
//                        mHandler.postDelayed(mAction, 50);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        if (mHandler == null) return true;
//                        mHandler.removeCallbacks(mAction);
//                        mHandler = null;
//                        break;
//                }
//                return false;
//            }
//
//            final Runnable mAction = new Runnable() {
//                @Override public void run() {
//                    setCityButtonClickListener((CityButtonClickListener) context);
//                    buttonClickListener.onCityButtonClick(0);
//                    if (TextUtils.isEmpty(inputConnection.getSelectedText(0)))
//                        inputConnection.deleteSurroundingText(1, 0);
//                    else inputConnection.commitText("", 1);
//                    mHandler.postDelayed(this, 50);
//                }
//            };
//
//        });
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        number.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setText(64);
//                linearLayout.setVisibility(GONE);
//                keyboardView.setVisibility(VISIBLE);
//                recyclerView.setVisibility(VISIBLE);
//                recyclerView.setVisibility(VISIBLE);
//                keyboard = new Keyboard(getContext(), R.xml.qwerty1);
//                context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 0).commit();
//                keyboardView.setKeyboard(keyboard);
//                option = 1;
//                if (serviceRunning) {
//                    setCityButtonClickListener((CityButtonClickListener) context);
//                    buttonClickListener.onSpeakButton(serviceRunning);
//                    serviceRunning = false;
//                    stopp();
//                }
            }
        });

        settings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setText(33);
//                if (serviceRunning) {
//                    setCityButtonClickListener((CityButtonClickListener) context);
//                    buttonClickListener.onSpeakButton(serviceRunning);
//                    serviceRunning = false;
//                    stopp();
//                }
//                context.startActivity(new Intent(context, LanguageChange.class).addFlags(FLAG_ACTIVITY_NEW_TASK));
            }
        });
        question.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setText(63);
            }
        });
        comma.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setText(44);
            }
        });
        dot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setText(46);
            }
        });
        btn_hash.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setText(35);
            }
        });
        space.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setText(32);
            }
        });
        btn_and.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setText(38);
            }
        });
        btn_dollar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setText(36);
            }
        });
        done = findViewById(R.id.done);
        done.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                setCityButtonClickListener((CityButtonClickListener) context);
//                buttonClickListener.onCityButtonClick(0);
//                inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
//                inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                linearLayout.setVisibility(GONE);
                numericKeyboard.setVisibility(GONE);
                llTop.setVisibility(VISIBLE);
                cvLanguage.setVisibility(VISIBLE);

//                keyboard = new Keyboard(getContext(), R.xml.qwerty);
//                keyboardView.setKeyboard(keyboard);
                recyclerView.setVisibility(GONE);
                keyboardView.setVisibility(VISIBLE);
//                option = 0;
//                context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 1).commit();
                if (option == 0 || option == 1) {
                    llTopBangla.setVisibility(GONE);
                    context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 1).commit();
                    keyboard = new Keyboard(getContext(), R.xml.qwerty);
                    keyboardView.setKeyboard(keyboard);
                    option = 1;
                } else if (option == 2 || option == 3) {
                    llTopBangla.setVisibility(VISIBLE);
                    context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 3).commit();
                    keyboard = new Keyboard(getContext(), R.xml.qwerty3);
                    keyboardView.setKeyboard(keyboard);
                    option = 3;
                    if (keyboard != null) {
                        if (!context.getSharedPreferences("color", MODE_PRIVATE).getString("color", "color1").equalsIgnoreCase("color1")) {
                            Keyboard.Key key = findKey(keyboard, 654321);
                            Keyboard.Key key1 = findKey(keyboard, -5);
                            if (key != null)
                                key.icon = ContextCompat.getDrawable(context, R.drawable.group_arrow_white);
                            if (key1 != null)
                                key1.icon = ContextCompat.getDrawable(context, R.drawable.cross_white);

                        }
                    }
                }
                if (serviceRunning) {
                    setCityButtonClickListener((CityButtonClickListener) context);
                    buttonClickListener.onSpeakButton(serviceRunning);
                    serviceRunning = false;
                    stopp();
                }
            }
        });

//        final SharedPreferences preferences = context.getSharedPreferences("prefs",MODE_PRIVATE);
        if (preferences.getString("lang", "").equals("")) {
            current = "ENGLISH";
            language.setText(current);
        } else {
            current = preferences.getString("lang", "");
            language.setText(current);
        }
        nointernet = findViewById(R.id.nointernet);
        speak = findViewById(R.id.speak);
        speak.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!haveNetworkConnection()) {
                    nointernet.setVisibility(VISIBLE);
                } else {
                    nointernet.setVisibility(GONE);
                }
                if (serviceRunning) {
                    setCityButtonClickListener((CityButtonClickListener) context);
                    buttonClickListener.onSpeakButton(serviceRunning);
                    serviceRunning = false;
                    stopp();
                } else {
                    setCityButtonClickListener((CityButtonClickListener) context);
                    buttonClickListener.onSpeakButton(serviceRunning);
                    serviceRunning = true;
                    startGrow();
                }
            }
        });
        ivSpeak.setOnClickListener(view -> {
            if (!haveNetworkConnection()) {
                nointernet.setVisibility(VISIBLE);
            } else {
                nointernet.setVisibility(GONE);
            }
            if (serviceRunning) {
                setCityButtonClickListener((CityButtonClickListener) context);
                buttonClickListener.onSpeakButton(serviceRunning);
                serviceRunning = false;
                stopp();
            } else {
                setCityButtonClickListener((CityButtonClickListener) context);
                buttonClickListener.onSpeakButton(serviceRunning);
                serviceRunning = true;
                startGrow();
            }
        });

        if (!haveNetworkConnection()) {
            nointernet.setVisibility(VISIBLE);
        } else {
            nointernet.setVisibility(GONE);
        }

        if (color.equals("color1")) {
            listening.setTextColor(Color.parseColor("#80000000"));
            language.setTextColor(Color.parseColor("#80000000"));
            nointernet.setTextColor(Color.parseColor("#80000000"));
        } else {
            listening.setTextColor(Color.parseColor("#80ffffff"));
            language.setTextColor(Color.parseColor("#80ffffff"));
            nointernet.setTextColor(Color.parseColor("#80ffffff"));
        }

        if (keyboard != null) {
            if (!context.getSharedPreferences("color", MODE_PRIVATE).getString("color", "color1").equalsIgnoreCase("color1")) {
                Keyboard.Key key = findKey(keyboard, 654321);
                Keyboard.Key key1 = findKey(keyboard, -5);
                if (key != null)
                    key.icon = ContextCompat.getDrawable(context, R.drawable.group_arrow_white);
                if (key1 != null)
                    key1.icon = ContextCompat.getDrawable(context, R.drawable.cross_white);

            }
        }
    }


    public void setEnglishBackground() {
        llBengla.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_keyboard));
        viewBengali.setVisibility(GONE);
        viewEnglish.setVisibility(VISIBLE);
        llEnglish.setElevation(12f);
        llBengla.setElevation(0f);
        llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_keyboard_white));
        option = 0;
        context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 1).commit();
        keyboard = new Keyboard(getContext(), R.xml.qwerty);
        keyboardView.setKeyboard(keyboard);
    }

    public void setBanglaBackground() {
        if (numericKeyboard.getVisibility() != View.VISIBLE) {
            llTopBangla.setVisibility(View.VISIBLE);
        }
        llBengla.setElevation(12f);
        llEnglish.setElevation(0f);
        llBengla.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_keyboard_white));
        viewBengali.setVisibility(VISIBLE);
        viewEnglish.setVisibility(GONE);
        llEnglish.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_keyboard));
        option = 2;
        context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 3).commit();
        keyboard = new Keyboard(getContext(), R.xml.qwerty3);
        keyboardView.setKeyboard(keyboard);
        if (keyboard != null) {
            if (!context.getSharedPreferences("color", MODE_PRIVATE).getString("color", "color1").equalsIgnoreCase("color1")) {
                Keyboard.Key key = findKey(keyboard, 654321);
                Keyboard.Key key1 = findKey(keyboard, -5);
                if (key != null)
                    key.icon = ContextCompat.getDrawable(context, R.drawable.group_arrow_white);
                if (key1 != null)
                    key1.icon = ContextCompat.getDrawable(context, R.drawable.cross_white);

            }
        }
    }

    public void stopp() {
        if (language.getText().toString().equalsIgnoreCase("bn")) {
            listening.setText("কথা বলতে আলতো চাপুন");
        } else {
            listening.setText("Tap to Speak");
        }
        frameLayout.setAnimation(null);
        frameLayout.setVisibility(GONE);
        ivSpeak.setVisibility(VISIBLE);
    }

    public void startShrink() {
        final ScaleAnimation shrinkAnim = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrinkAnim.setDuration(2000);
        frameLayout.setAnimation(shrinkAnim);
        shrinkAnim.start();
        Log.e("sdfsdf", "STarting Shrink");
        shrinkAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startGrow();
            }
        });
    }

    public void startGrow() {
        frameLayout.setVisibility(VISIBLE);
        ivSpeak.setVisibility(GONE);
        if (!language.getText().toString().equalsIgnoreCase("bn")) {
            listening.setText("listening....");
        } else {
            listening.setText("এখন বলো...");
        }
        final ScaleAnimation growAnim = new ScaleAnimation(0.8f, 1.0f, 0.8f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        growAnim.setDuration(2000);
        Log.e("sdfsdf", "STarting Grow");
        frameLayout.setAnimation(growAnim);
        growAnim.start();
        growAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startShrink();
            }
        });
    }

    public void setText(int primaryCode) {
        setCityButtonClickListener((CityButtonClickListener) context);
        buttonClickListener.onCityButtonClick(primaryCode);
        char code = (char) primaryCode;
        if (Character.isLetter(code) && isCaps) {
            code = Character.toUpperCase(code);
        }
        inputConnection.commitText(String.valueOf(code), 1);
    }

    public void setInputConnection(InputConnection inputConnection) {
        this.inputConnection = inputConnection;
    }

    public void stop() {
        serviceRunning = false;
        stopp();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public void vibrationOnKeyPress() {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(20);
        }
//        keyboardView.performHapticFeedback(
//                HapticFeedbackConstants.VIRTUAL_KEY,
//                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING  // Ignore device's setting. Otherwise, you can use FLAG_IGNORE_VIEW_SETTING to ignore view's setting.
//        );
    }

    public interface CityButtonClickListener {
        void onCityButtonClick(int primaryCode);

        void onSpeakButton(Boolean serviceRunning);
    }

    private CityButtonClickListener buttonClickListener;

    public void setCityButtonClickListener(CityButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    @Override
    public void onPress(int i) {

        if (context.getSharedPreferences("options", MODE_PRIVATE).getBoolean("vibration_on", true)) {
            vibrationOnKeyPress();
        }
        if (i == -1 || i == -4 || i == -5 || i == 32) {
            keyboardView.setPreviewEnabled(false);
        } else {
            keyboardView.setPreviewEnabled(true);
        }
    }

    @Override
    public void onRelease(int i) {
        if (i != -1 && isCaps && i != -2 && i != -4 && needToSmall) {
            needToSmall = false;
            Keyboard.Key key = findKey(keyboard, -1);
            isCaps = !isCaps;
            if (isCaps) {
                key.icon = ContextCompat.getDrawable(context, R.drawable.keyboard_caps);
            } else {
                key.icon = ContextCompat.getDrawable(context, R.drawable.keyboard_not_caps);
            }
            keyboard.setShifted(isCaps);
            keyboardView.invalidateAllKeys();
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {


        playClick(primaryCode);
        setCityButtonClickListener((CityButtonClickListener) context);
        buttonClickListener.onCityButtonClick(primaryCode);

        switch (primaryCode) {

            case Keyboard.KEYCODE_DELETE:
                if (TextUtils.isEmpty(inputConnection.getSelectedText(0)))
                    inputConnection.deleteSurroundingText(1, 0);
                else inputConnection.commitText("", 1);
                break;

            case Keyboard.KEYCODE_SHIFT:
                Keyboard.Key key = findKey(keyboard, -1);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        // yourMethod();
                        twoSeconds = false;
                    }
                }, 400);
                if (twoSeconds) {
                    isCaps = true;
                    needToSmall = false;
                    if (isCaps) {
                        key.icon = ContextCompat.getDrawable(context, R.drawable.keyboard_caps);
                    }
                    keyboard.setShifted(isCaps);
                    keyboardView.invalidateAllKeys();
                } else {
                    twoSeconds = true;
                    isCaps = !isCaps;
                    if (isCaps) {
                        needToSmall = true;
                        key.icon = ContextCompat.getDrawable(context, R.drawable.keyboard_caps);
                    } else {
                        key.icon = ContextCompat.getDrawable(context, R.drawable.keyboard_not_caps);
                    }
                    keyboard.setShifted(isCaps);
                    keyboardView.invalidateAllKeys();
                }
                break;

            case Keyboard.KEYCODE_DONE:
                inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
                break;

            case -1005:
                //mic
                //change keyboard
                llTop.setVisibility(GONE);
                recyclerView.setVisibility(VISIBLE);
                isCaps = false;
                if (option == 0) {
                    context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 0).commit();
                    keyboard = new Keyboard(getContext(), R.xml.qwerty1);
                    recyclerView.setVisibility(VISIBLE);
                    keyboardView.setKeyboard(keyboard);
                    option = 1;
                } else if (option == 1) {
                    context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 1).commit();
                    keyboard = new Keyboard(getContext(), R.xml.qwerty);
                    recyclerView.setVisibility(GONE);
                    keyboardView.setKeyboard(keyboard);
                    option = 0;
                } else if (option == 2) {
                    context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 2).commit();
                    keyboard = new Keyboard(getContext(), R.xml.qwerty4);
                    keyboardView.setKeyboard(keyboard);
                    option = 3;
                } else if (option == 3) {
                    context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 3).commit();
                    keyboard = new Keyboard(getContext(), R.xml.qwerty3);
                    keyboardView.setKeyboard(keyboard);
                    option = 2;
                }
                if (!haveNetworkConnection()) {
                    nointernet.setVisibility(VISIBLE);
                } else {
                    nointernet.setVisibility(GONE);
                }
                context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 2).commit();
                keyboardView.setVisibility(GONE);
//                recyclerView.setVisibility(GONE);
                linearLayout.setVisibility(VISIBLE);
                setCityButtonClickListener((CityButtonClickListener) context);
                buttonClickListener.onSpeakButton(serviceRunning);
                serviceRunning = true;
                startGrow();
                break;

            case -2:
                //change keyboard
                numberKeyboard();
                break;
            case 4321:
                numericKeyboard.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                keyboardView.setVisibility(GONE);
                cvLanguage.setVisibility(INVISIBLE);
                break;

            case 654321:
                setTopRowBanglaText();
                break;
            case 2509:
                inputConnection.commitText("্য", 1);
                break;
            default:
                char code = (char) primaryCode;
                if (Character.isLetter(code) && isCaps) {
                    code = Character.toUpperCase(code);
                }
                inputConnection.commitText(String.valueOf(code), 1);
        }
        if (context.getSharedPreferences("options", MODE_PRIVATE).getInt("options", 1) == 3) {
            char code = (char) primaryCode;
            if (Character.isLetter(code) && isCaps) {
                code = Character.toUpperCase(code);
            }
            if (primaryCode != -2 && primaryCode != 63 && primaryCode != 34 && primaryCode != 44 && primaryCode != 46 && primaryCode != -4 && primaryCode != 2510 && primaryCode != 2509 && primaryCode != 2499 && primaryCode != 2434 && !String.valueOf(code).equals("।") && primaryCode != 654321) {
                if (primaryCode == -5 && inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text.length() >= 1) {
                    code = inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text.charAt(inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text.length() - 1);
                }
                if (inputConnection.getExtractedText(new ExtractedTextRequest(), 0).text.length() != 0) {
                    if (String.valueOf(code).equals("ক") || String.valueOf(code).equals("খ") || String.valueOf(code).equals("গ") || String.valueOf(code).equals("ঘ") || String.valueOf(code).equals("ঙ") || String.valueOf(code).equals("চ") || String.valueOf(code).equals("ছ") || String.valueOf(code).equals("জ") || String.valueOf(code).equals("ঝ") || String.valueOf(code).equals("ঞ") || String.valueOf(code).equals("ট") || String.valueOf(code).equals("ঠ") || String.valueOf(code).equals("ড") || String.valueOf(code).equals("ঢ") || String.valueOf(code).equals("ণ") || String.valueOf(code).equals("ত") || String.valueOf(code).equals("থ") || String.valueOf(code).equals("দ") || String.valueOf(code).equals("ধ") || String.valueOf(code).equals("ন") || String.valueOf(code).equals("প") || String.valueOf(code).equals("ফ") || String.valueOf(code).equals("ব") || String.valueOf(code).equals("ভ") || String.valueOf(code).equals("ম") || String.valueOf(code).equals("য") || String.valueOf(code).equals("র") || String.valueOf(code).equals("ল") || String.valueOf(code).equals("শ") || String.valueOf(code).equals("ষ") || String.valueOf(code).equals("স") || String.valueOf(code).equals("হ") || String.valueOf(code).equals("ড়") || String.valueOf(code).equals("ঢ়") || String.valueOf(code).equals("য়")) {
                        text1.setText(String.valueOf(code));
                        text2.setText(code + "া");
                        text3.setText(code + "ি");
                        text4.setText(code + "ী");
                        text5.setText(code + "ু");
                        text6.setText(code + "ূ");
                        text7.setText(code + "ে");
                        text8.setText(code + "ৈ");
                        text9.setText(code + "ে" + "া");
                        text10.setText(code + "ে" + "ৗ");
                    } else {
                        setTopRowBanglaText();
                    }
                } else {
                    setTopRowBanglaText();
                }
            }

        }
    }

    private void setTopRowBanglaText() {
        text1.setText("অ");
        text2.setText("আ");
        text3.setText("ই");
        text4.setText("ঈ");
        text5.setText("উ");
        text6.setText("ঊ");
        text7.setText("এ");
        text8.setText("ঐ");
        text9.setText("ও");
        text10.setText("ঔ");
    }

    private Keyboard.Key findKey(Keyboard keyboard, int primaryCode) {
        for (Keyboard.Key key : keyboard.getKeys()) {
            if (key.codes[0] == primaryCode) {
                return key;
            }
        }
        return null;
    }

    ///-2 code
    public void numberKeyboard() {
        llTopBangla.setVisibility(GONE);
        isCaps = false;
        if (option == 0) {
            context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 0).commit();
            keyboard = new Keyboard(getContext(), R.xml.qwerty1);
//                    recyclerView.setVisibility(VISIBLE);
            keyboardView.setKeyboard(keyboard);
            option = 1;
        } else if (option == 1) {
            context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 1).commit();
            keyboard = new Keyboard(getContext(), R.xml.qwerty);
//                    recyclerView.setVisibility(GONE);
            keyboardView.setKeyboard(keyboard);
            option = 0;
        } else if (option == 2) {
            context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 2).commit();
            keyboard = new Keyboard(getContext(), R.xml.qwerty4);
            keyboardView.setKeyboard(keyboard);
            option = 3;
        } else if (option == 3) {
            llTopBangla.setVisibility(VISIBLE);
            context.getSharedPreferences("options", MODE_PRIVATE).edit().putInt("options", 3).commit();
            keyboard = new Keyboard(getContext(), R.xml.qwerty3);
            keyboardView.setKeyboard(keyboard);
            option = 2;
            if (keyboard != null) {
                if (!context.getSharedPreferences("color", MODE_PRIVATE).getString("color", "color1").equalsIgnoreCase("color1")) {
                    Keyboard.Key key = findKey(keyboard, 654321);
                    Keyboard.Key key1 = findKey(keyboard, -5);
                    if (key != null)
                        key.icon = ContextCompat.getDrawable(context, R.drawable.group_arrow_white);
                    if (key1 != null)
                        key1.icon = ContextCompat.getDrawable(context, R.drawable.cross_white);

                }
            }
        }
    }

    private void playClick(int i) {

        AudioManager audioManager = (AudioManager) getContext().getSystemService(AUDIO_SERVICE);
        switch (i) {
            case 32:
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;

            case Keyboard.KEYCODE_DONE:
            case 10:
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;

            case Keyboard.KEYCODE_DELETE:
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;

            default:
                audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                // Let the underlying text editor always handle these.
                return false;

            default:
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    @Override
    public void onClick(View view) {

    }

    private void registerReceiverCustomKeyBoardView() {
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SENDMESSAGE");
        context.registerReceiver(myReceiver, intentFilter);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("message") != null) {
                Log.e("message", intent.getStringExtra("message"));
//                tvSpeakedText.setText(intent.getStringExtra("message"));
            } else {
//                tvSpeakedText.setText("");
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        List<Keyboard.Key> keys = keyboardView.getKeyboard().getKeys();
        for (Keyboard.Key key : keys) {
            if (key.codes[0] == 7) {
                Log.e("CHAGAN", "Drawing key with code " + key.codes[0]);
                Drawable dr = (Drawable) context.getResources().getDrawable(R.drawable.keyboard_tab_next);
                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                dr.draw(canvas);

            } else {
                Drawable dr = (Drawable) context.getResources().getDrawable(R.drawable.color_4);
                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                dr.draw(canvas);
            }
        }
    }
}


class AdapterListEmoji extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListEmoji(Context context, List<String> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public Button emoji;

        public OriginalViewHolder(View v) {
            super(v);
            emoji = (Button) v.findViewById(R.id.emoji);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.emoji_layout, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;
//            if(position>9){
//                view.emoji.setPadding(12,0,12,0);
//            }else{
//                view.emoji.setPadding(10,0,10,0);
//            }
            if (position < 10) {
                if (ctx.getSharedPreferences("color", MODE_PRIVATE).getString("color", "color1").equalsIgnoreCase("color1"))
                    view.emoji.setTextColor(ContextCompat.getColor(ctx, R.color.black));
                else
                    view.emoji.setTextColor(ContextCompat.getColor(ctx, R.color.white));
            }

            String p = items.get(position);
            // view.emoji.setText(p);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.emoji.setText(Html.fromHtml(p, Html.FROM_HTML_MODE_COMPACT));
            } else {
                view.emoji.setText(Html.fromHtml(p));
            }
            view.emoji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}