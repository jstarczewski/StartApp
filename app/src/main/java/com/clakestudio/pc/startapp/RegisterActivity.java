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
import android.util.Log;
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
    Button buttonCreateAccount;
    FirebaseAuth firebaseAuth;
    private Toolbar toolbarRegister;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbarRegister = (Toolbar)findViewById(R.id.registerToolbar);
        setSupportActionBar(toolbarRegister);
        toolbarRegister.setTitleTextColor(Color.parseColor("#FFFFFF"));

        firebaseAuth = FirebaseAuth.getInstance();
        Button privacyButton = (Button)findViewById(R.id.buttonPrivacy);

        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://privacypolicyclake.blogspot.com/2017/09/startapp-privacy-policy.html"));
                startActivity(browserIntent);

            }
        });

        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);

        progressDialog = new ProgressDialog(this);

        buttonCreateAccount = (Button)findViewById(R.id.buttonCreateAccount);

        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (!email.isEmpty() && email.contains("@") && !password.isEmpty() ) {
                    progressDialog.setTitle("Registering User");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    RegisterUser(email, password);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid email/password lenght must be greater than 0", Toast.LENGTH_LONG).show();
                }
            }


        });

    }

    private void RegisterUser(String email, String password) {



        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "User registered", Toast.LENGTH_LONG).show();
                    Intent MainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(MainActivity);
                    finish();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}