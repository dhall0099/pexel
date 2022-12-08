package com.example.pexels;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,RecyclerViewClickInterface {

    RecyclerView recyclerView;
    private String API_KEY="563492ad6f91700001000001408647ec35334e55b9aae2cbadca91fd";
    List<Pexel> dataList;
    boolean isScrolling=false;
    int totalItem, visibleItem, scrolledOutItem;
    SearchView searchView;
    CardView nature,wildlife,technology,education,love, popular;
    String searchText;
    RecyclerViewClickInterface recyclerViewClickInterface;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        searchView=findViewById(R.id.search);
        progressBar=findViewById(R.id.progressBar);
        searchText=searchView.getQuery().toString().toLowerCase();

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                getData();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                getSearchedData(query);
                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {


                if(newText.isEmpty()){
                    getData();
                }
                else {
                    getSearchedData(newText);
                }


                return true;
            }
        });




        recyclerView=findViewById(R.id.recycler);
        nature=findViewById(R.id.nature);
        wildlife=findViewById(R.id.wildlife);
        education=findViewById(R.id.education);
        technology=findViewById(R.id.technologies);
        love=findViewById(R.id.love);
        popular=findViewById(R.id.popular);

        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(gridLayoutManager);


        popular.setOnClickListener(this);
        nature.setOnClickListener(this);
        wildlife.setOnClickListener(this);
        education.setOnClickListener(this);
        love.setOnClickListener(this);
        technology.setOnClickListener(this);
        recyclerViewClickInterface=(RecyclerViewClickInterface)MainActivity.this;
        getData();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.popular:
                getData();
                break;
            case R.id.nature:
                getSearchedData("nature");
                break;
            case R.id.wildlife:
                getSearchedData("wildlife");
                break;
            case R.id.love:
                getSearchedData("love");
                break;
            case R.id.technologies:
                getSearchedData("technologies");
                break;
            case R.id.education:
                getSearchedData("education");
                break;

        }

    }

    private void getSearchedData(String query) {


        Call<PexelResponse> wallpaperResponseCall=RetrofitClient
                .getInstance()
                .getApi()
                .getSearch(API_KEY,query);

        wallpaperResponseCall.enqueue(new Callback<PexelResponse>() {
            @Override
            public void onResponse(Call<PexelResponse> call, Response<PexelResponse> response) {



                if(response.isSuccessful()){

                    dataList=response.body().getPhotosList();

                    PexelAdapter pexelAdapter=new PexelAdapter(getApplicationContext(),dataList,recyclerViewClickInterface);
                    recyclerView.setAdapter(pexelAdapter);
                    pexelAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<PexelResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getData() {

        Call<PexelResponse> wallpaperResponseCall=RetrofitClient
                .getInstance()
                .getApi()
                .getWallpaper(API_KEY);

        wallpaperResponseCall.enqueue(new Callback<PexelResponse>() {
            @Override
            public void onResponse(Call<PexelResponse> call, Response<PexelResponse> response) {

                PexelResponse pexelResponse=response.body();

                if(response.isSuccessful()){

                    dataList=response.body().getPhotosList();

                    PexelAdapter pAdapter=new PexelAdapter(getApplication(),dataList,recyclerViewClickInterface);
                    recyclerView.setAdapter(pAdapter);
                    pAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(MainActivity.this,"Error", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<PexelResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onItemClick(int position) {

    Intent intent = new Intent(getBaseContext(), ImageDetailActivity.class);
        intent.putExtra("imageUrl", dataList.get(position).getSrc().getLarge());
        startActivity(intent);

    }
}