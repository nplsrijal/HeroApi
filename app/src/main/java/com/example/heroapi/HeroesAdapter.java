package com.example.heroapi;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import model.Heroes;
import url.Url;

class HeroesAdapter extends RecyclerView.Adapter<HeroesAdapter.ViewHolder> {

    private List<Heroes> heroesList;
    private Context context;

    public HeroesAdapter(List<Heroes> heroesList, Context context){
        this.heroesList=heroesList;
        this.context=context;
    }

    @NonNull
    @Override
    public HeroesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.heroes,viewGroup,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HeroesAdapter.ViewHolder viewHolder, int i) {
        Heroes heroes = heroesList.get(i);
        String imgPath = Url.BASE_URL+"uploads/"+heroes.getImage();
        StrictMode();

        try{
            URL url = new URL(imgPath);
            viewHolder.imgPhoto.setImageBitmap(BitmapFactory.decodeStream((InputStream) url.getContent()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewHolder.tvName.setText(heroes.getName());
        viewHolder.tvDesc.setText(heroes.getDesc());
    }

    @Override
    public int getItemCount() {
        return heroesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName, tvDesc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto=itemView.findViewById(R.id.imgPhoto);
            tvName=itemView.findViewById(R.id.tvName);
            tvDesc=itemView.findViewById(R.id.tvDesc);
        }
    }

    private void StrictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
