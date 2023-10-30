package com.bangla.keyboard.bangla.stickers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.bangla.keyboard.bangla.stickers.datamodel.ThemeData;

import java.util.ArrayList;

public class ThemeActivity extends AppCompatActivity {

    RecyclerView themeRV;
    ImageView backBtn;

//    ArrayList<Integer> themeImageArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        backBtn = findViewById(R.id.iv_back);
        themeRV = findViewById(R.id.themeRV);

        backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

//        themeImageArray = new ArrayList<>();
//        themeImageArray.add(R.drawable.theme_1 );
//        themeImageArray.add(R.drawable.theme_2);
//        themeImageArray.add(R.drawable.theme_6);
//        themeImageArray.add(R.drawable.theme_5);
//        themeImageArray.add(R.drawable.theme_4);
//        themeImageArray.add(R.drawable.theme_3);
        ArrayList<ThemeData> themeImageArray = new ArrayList<>();

        themeImageArray.add(new ThemeData(R.drawable.theme_1, "Light"));
        themeImageArray.add(new ThemeData(R.drawable.theme_2, "Dark"));
        themeImageArray.add(new ThemeData(R.drawable.theme_6, "Blue"));
        themeImageArray.add(new ThemeData(R.drawable.theme_5, "Green"));
        themeImageArray.add(new ThemeData(R.drawable.theme_4, "Red"));
        themeImageArray.add(new ThemeData(R.drawable.theme_3, "Yellowish Orange"));


        themeRV.setLayoutManager(new LinearLayoutManager(this));
        themeRV.setAdapter(new ThemeAdapter(themeImageArray, this));
    }
}