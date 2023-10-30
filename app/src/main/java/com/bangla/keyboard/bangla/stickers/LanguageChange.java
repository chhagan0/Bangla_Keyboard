package com.bangla.keyboard.bangla.stickers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class LanguageChange extends AppCompatActivity {
    ImageButton back;
    RecyclerView recyclerView;
    HashMap<String,String> hashMap;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = activity.getWindow();
//            Drawable background = activity.getResources().getDrawable(R.drawable.gradient_theme);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
//            window.setNavigationBarColor(activity.getResources().getColor(android.R.color.transparent));
//            window.setBackgroundDrawable(background);
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(LanguageChange.this);
        setContentView(R.layout.activity_language_change);
//        back = findViewById(R.id.back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LanguageChange.this,MainActivity.class));
//            }
//        });
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setHasFixedSize(true);
//        hashMap = new LinkedHashMap<>();
//        putMap();
//        final ArrayList<String> country = new ArrayList<>();
//        for(Map.Entry<String,String> map:hashMap.entrySet()){
//            country.add(map.getKey());
//        }
//        AdapterListBasic mAdapter = new AdapterListBasic(this, country);
//        recyclerView.setAdapter(mAdapter);
//        mAdapter.setOnItemClickListener(new AdapterListBasic.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, String obj, int position) {
//                final SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);
//                preferences.edit().putString("lang", obj).commit();
//                startActivity(new Intent(LanguageChange.this,MainActivity.class));;
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(LanguageChange.this,MainActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    void putMap(){
        hashMap.put("ENGLISH","en-IN");
        hashMap.put("AFRIKAANS","af");
        hashMap.put("ARABIC","ar");
        hashMap.put("ARMENIAN","hy");
        hashMap.put("AZERBAIJANI","az-AZ");
        hashMap.put("BASQUE","eu");
        hashMap.put("BENGALI","bn");
        hashMap.put("BULGARIAN","bg");
        hashMap.put("CATALAN","ca");
        hashMap.put("CHINESE","zh-CN");
        hashMap.put("CROATIAN","hr");
        hashMap.put("CZECH","cs");
        hashMap.put("DANISH","da");
        hashMap.put("DUTCH","nl");
        hashMap.put("ESTONIAN","et");
        hashMap.put("FILIPINO","tl");
        hashMap.put("FINNISH","fi");
        hashMap.put("FRENCH","fr");
        hashMap.put("GALICIAN","gl");
        hashMap.put("GEORGIAN","ka-GE");
        hashMap.put("GERMAN","de");
        hashMap.put("GREEK","el");
        hashMap.put("GUJARATI","gu-IN");
        hashMap.put("HEBREW","iw");
        hashMap.put("HINDI","hi");
        hashMap.put("HUNGARIAN","hu");
        hashMap.put("ICELANDIC","is");
        hashMap.put("INDONESIAN","id");
        hashMap.put("ITALIAN","it");
        hashMap.put("JAPANESE","ja");
        hashMap.put("KANNADA","kn-IN");
        hashMap.put("KOREAN","ko");
        hashMap.put("LATVIAN","lv");
        hashMap.put("LITHUANIAN","lt");
        hashMap.put("MALAY","ms");
        hashMap.put("MALAYALAM","ml-IN");
        hashMap.put("NORWEGIAN","no");
        hashMap.put("PERSIAN","fa");
        hashMap.put("POLISH","pl");
        hashMap.put("PORTUGUESE","pt");
        hashMap.put("ROMANIAN","ro");
        hashMap.put("RUSSIAN","ru");
        hashMap.put("SERBIAN","sr");
        hashMap.put("SLOVAK","sk");
        hashMap.put("SLOVENIAN","sl");
        hashMap.put("SPANISH","es");
        hashMap.put("SWAHILI","sw");
        hashMap.put("SWEDISH","sv");
        hashMap.put("TAMIL","ta");
        hashMap.put("TELUGU","te-IN");
        hashMap.put("THAI","th");
        hashMap.put("TURKISH","tr");
        hashMap.put("UKRAINIAN","uk");
        hashMap.put("URDU","ur-PK");
        hashMap.put("VIETNAMESE","vi");
    }
}

class AdapterListBasic extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListBasic(Context context, ArrayList<String> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        CheckBox checkBox;
        LinearLayout linearLayout;

        public OriginalViewHolder(View v) {
            super(v);
//            name = (TextView) v.findViewById(R.id.name);
//            checkBox = v.findViewById(R.id.checkbox);
//            linearLayout = v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            view.name.setText(items.get(position));

            view.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.checkBox.setChecked(true);
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, items.get(position), position);
                    }
                }
            });
            view.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(buttonView, items.get(position), position);
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
