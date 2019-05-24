package com.example.heroapi;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import heroesapi.HeroesAPI;
import model.Heroes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import url.Url;

public class RecyclerHeroes extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Heroes> heroesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_heroes);
        recyclerView=findViewById(R.id.recyclerView);
//        heroesList= new ArrayList<>();
        HeroesAPI heroesAPI = Url.getInstance().create(HeroesAPI.class);
        Call<List<Heroes>> call = heroesAPI.getAllEmployee(Url.Cookie);
        call.enqueue(new Callback<List<Heroes>>() {
            @Override
            public void onResponse(Call<List<Heroes>> call, Response<List<Heroes>> response) {

                heroesList = response.body();
                HeroesAdapter heroesAdapter = new HeroesAdapter(heroesList, RecyclerHeroes.this);
                recyclerView.setAdapter(heroesAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(RecyclerHeroes.this));
            }

            @Override
            public void onFailure(Call<List<Heroes>> call, Throwable t) {

            }
        });






    }
}
