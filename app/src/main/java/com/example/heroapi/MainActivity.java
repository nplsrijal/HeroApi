package com.example.heroapi;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import heroesapi.HeroesAPI;
import model.Heroes;
import model.ImageResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import url.Url;

public class MainActivity extends AppCompatActivity {
    private EditText etname, etdesc;
    private Button btnsave;
    private ImageView imgProfile;
    String imagePath;
    String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etname = findViewById(R.id.etname);
        etdesc = findViewById(R.id.etdesc);
        btnsave = findViewById(R.id.btnsave);
        imgProfile = findViewById(R.id.imgPhoto);

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save();
                Intent intent = new Intent(MainActivity.this,firstActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseImage();
            }
        });
    }

    private void BrowseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            if (data==null){
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        }
        Uri uri=data.getData();
        imagePath=getRealPathFromUri(uri);
        previewImage(imagePath);
    }


    private String getRealPathFromUri(Uri uri) {
     String[] projection ={MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection,
                null, null, null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }
    private void previewImage(String imagePath) {
        File imgFile = new File(imagePath);
        if (imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgProfile.setImageBitmap(myBitmap);
        }
    }
    private void StrictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void SaveImageOnly(){


        File file = new File(imagePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile",file.getName(),requestBody);
        HeroesAPI heroesAPI = Url.getInstance().create(HeroesAPI.class);
        Call<ImageResponse> responseBodyCall = heroesAPI.uploadImage(body);

        StrictMode();
        try{
            Response<ImageResponse> imageResponseResponse = responseBodyCall.execute();
            imageName = imageResponseResponse.body().getFilename();
        } catch (IOException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void Save() {
        SaveImageOnly();
        String name = etname.getText().toString();
        String desc = etdesc.getText().toString();

        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("desc", desc);
        map.put("image", imageName);

        HeroesAPI heroesAPI = Url.getInstance().create(HeroesAPI.class);
        Call<Void> heroesCall = heroesAPI.addHero(Url.Cookie,map);
        heroesCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "code " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent(MainActivity.this,RecyclerHeroes.class);
        startActivity(intent);
        finish();
    }
}

