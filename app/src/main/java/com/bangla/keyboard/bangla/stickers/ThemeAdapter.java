package com.bangla.keyboard.bangla.stickers;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bangla.keyboard.bangla.stickers.datamodel.ThemeData;

import java.util.ArrayList;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeAdapter.ViewHolder> {
static int selectedPosition=0;
    ArrayList<ThemeData> imageList;
    Context mContext;

    public ThemeAdapter(ArrayList<ThemeData> imageList, Context mContext) {
        this.imageList = imageList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        ThemeAdapter.ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThemeData themeData = imageList.get(position);

        holder.themeImage.setImageResource(imageList.get(position).getResourceId());
        holder.themename.setText(themeData.getText());
        if (selectedPosition==position){
            holder.tickImage.setVisibility(View.VISIBLE);
            holder.blackiv.setVisibility(View.VISIBLE);
        }else{
            holder.tickImage.setVisibility(View.GONE);
            holder.blackiv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView themeImage,tickImage,blackiv;
        TextView themename;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            themeImage = itemView.findViewById(R.id.themeImage);
            tickImage = itemView.findViewById(R.id.iv_tick);
            blackiv = itemView.findViewById(R.id.iv_black);
            themename = itemView.findViewById(R.id.theme_name);
            themeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedPosition=getAdapterPosition();
                    if(mContext.getSharedPreferences("color", MODE_PRIVATE).getBoolean("dark", false))
                    mContext.getSharedPreferences("color", MODE_PRIVATE).edit().putBoolean("change",true).apply();
                    else
                        mContext.getSharedPreferences("color", MODE_PRIVATE).edit().putBoolean("light",true).apply();
                    mContext.getSharedPreferences("color", MODE_PRIVATE).edit().putString("color","color"+(getAdapterPosition()+1)).apply();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
