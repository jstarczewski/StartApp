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

    private Intent nextActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);

        Button registerButton = (Button) findViewById(R.id.buttonRegister);
        Button logInButton = (Button) findViewById(R.id.buttonLogIn);
        Button buttonBrowse = (Button) findViewById(R.id.buttonBrowse);

        if (firebaseUser != null || sharedPreferences.getBoolean("anonymous", false)) {
            nextActivity = new Intent(this, MainActivity.class);
        } else {
            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nextActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                    startNextActivity(nextActivity);
                }
            });
            logInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nextActivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startNextActivity(nextActivity);
                }
            });
            buttonBrowse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nextActivity = new Intent(getApplicationContext(), AnonymusUserActivity.class);
                    startNextActivity(nextActivity);
                }
            });

        }
    }

    private void startNextActivity(Intent intent) {
        startActivity(nextActivity);
        finish();

    }

}
