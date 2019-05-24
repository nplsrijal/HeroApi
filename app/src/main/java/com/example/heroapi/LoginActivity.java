package com.example.heroapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import heroesapi.HeroesAPI;
import model.LoginSignupResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import url.Url;

public class LoginActivity extends AppCompatActivity {
    private EditText txtusername,txtpassword;
    private Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtusername=findViewById(R.id.txtusername);
        txtpassword=findViewById(R.id.txtpassword);
        btnlogin=findViewById(R.id.btnlogin);

         btnlogin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 checkUser();
             }
         });

    }

    private void checkUser() {
        HeroesAPI heroesAPI = Url.getInstance().create(HeroesAPI.class);

        String username= txtusername.getText().toString().trim();
        String password= txtpassword.getText().toString().trim();

       Call<LoginSignupResponse> usersCall = heroesAPI.checkUser(username,password);
        usersCall.enqueue(new Callback<LoginSignupResponse>() {
            @Override
            public void onResponse(Call<LoginSignupResponse> call, Response<LoginSignupResponse> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Either username or password incorrect",Toast.LENGTH_SHORT).show();
                    return;

                }else{
                    if(response.body().getSuccess()){
                      Url.Cookie=response.headers().get("Set-Cookie");
                        Intent intent = new Intent(LoginActivity.this,firstActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginSignupResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Error "+t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
}
