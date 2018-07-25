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

    EditText editTextLoginEmail;
    EditText editTextLoginPassword;
    Button buttonLogIn;
    FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextLoginEmail = (EditText)findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = (EditText)findViewById(R.id.editTextLoginPassword);

        progressDialog = new ProgressDialog(this);

        buttonLogIn = (Button)findViewById(R.id.buttonLogIn);
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextLoginEmail.getText().toString();
                String password = editTextLoginPassword.getText().toString();

                if (email.length()>4 && email.contains("@") && password.length()>0) {
                    FirebaseLogInWithEmailAndPassword(email, password);
                    progressDialog.setMessage("Loggin in");
                    progressDialog.show();}
                else {
                    Toast.makeText(getApplicationContext(), "Invalid email/password lenght must be greater than 0", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void FirebaseLogInWithEmailAndPassword(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Logged in", Toast.LENGTH_LONG).show();
                    Intent MainActivity = new Intent(getApplicationContext(), com.clakestudio.pc.startapp.MainActivity.class);
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
