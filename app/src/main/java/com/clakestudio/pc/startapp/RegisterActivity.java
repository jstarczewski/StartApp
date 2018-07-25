package com.clakestudio.pc.startapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by pc on 2017-08-19.
 */

public class RegisterActivity extends AppCompatActivity {


    private EditText editTextEmail;
    private EditText editTextPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbarRegister = (Toolbar) findViewById(R.id.registerToolbar);
        setSupportActionBar(toolbarRegister);
        toolbarRegister.setTitleTextColor(Color.parseColor("#FFFFFF"));

        firebaseAuth = FirebaseAuth.getInstance();
        final Button privacyButton = (Button) findViewById(R.id.buttonPrivacy);

        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://privacypolicyclake.blogspot.com/2017/09/startapp-privacy-policy.html"));
                startActivity(browserIntent);
            }
        });

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        progressDialog = new ProgressDialog(this);

        Button buttonCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                if (checkEmailAndPasswordCorrectness(email, password))
                    registerUser(email, password);

            }


        });

    }

    private boolean checkEmailAndPasswordCorrectness(String email, String password) {
        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Puste pole e-mail", Toast.LENGTH_LONG).show();
            return false;
        } else if (!email.contains("@")) {
            Toast.makeText(getApplicationContext(), "Pole e-mail nie zawiera znaku @", Toast.LENGTH_LONG).show();
            return false;
        } else if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Puste pole hasła", Toast.LENGTH_LONG).show();
            return false;
        } else if (password.length() >= 8) {
            Toast.makeText(getApplicationContext(), "Hasło musi być dłuższe niż 8 znaków", Toast.LENGTH_LONG).show();
            return false;
        } else {
            progressDialog.show();
            return true;
        }
    }

    private void registerUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "User registered", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}