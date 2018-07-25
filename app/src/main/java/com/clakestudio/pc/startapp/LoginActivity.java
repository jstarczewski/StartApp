package com.clakestudio.pc.startapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.clakestudio.pc.startapp.MainActivity;
import com.clakestudio.pc.startapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by pc on 2017-08-19.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLoginEmail;
    private EditText editTextLoginPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextLoginEmail = (EditText) findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = (EditText) findViewById(R.id.editTextLoginPassword);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loggin in");

        Button buttonLogIn = (Button) findViewById(R.id.buttonLogIn);
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextLoginEmail.getText().toString();
                String password = editTextLoginPassword.getText().toString();

                if (checkEmailAndPasswordCorrectness(email, password)) {
                    logInWithEmailAndPassword(email, password);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid email/password lenght must be greater than 0", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void logInWithEmailAndPassword(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();
                }


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

}
