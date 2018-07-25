package com.clakestudio.pc.startapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * Created by pc on 2017-08-19.
 */

public class StartActivity extends AppCompatActivity {

    Button registerButton;
    Button logInButton;
    FirebaseAuth firebaseAuth;
    Button buttonBrowse;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        firebaseAuth = FirebaseAuth.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser!=null || sharedPreferences.getBoolean("anonymous", false)){
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
        else {
        registerButton = (Button)findViewById(R.id.buttonRegister);
        logInButton = (Button)findViewById(R.id.buttonLogIn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
        buttonBrowse = (Button)findViewById(R.id.buttonBrowse);
        buttonBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent anonymousIntent = new Intent(getApplicationContext(), AnonymusUserActivity.class);
                startActivity(anonymousIntent);



            }
        }); }
    }

}
