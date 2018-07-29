package com.clakestudio.pc.startapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference appReference = FirebaseDatabase.getInstance().getReference("Apps");
    private String passOld;
    private String passNew;
    private String email;
    private String oldMail;
    private String newMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.searchRecyclerView);
        final List<AppObject> appObjects = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        MyAdapter myAdapter = new MyAdapter(appObjects);
        recyclerView.setHasFixedSize(true);

        TextView textView = (TextView) findViewById(R.id.profileTextView);

        textView.setText("Hey " + firebaseUser.getEmail());
        recyclerView.setNestedScrollingEnabled(false);
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("profile", true).apply();


        appReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                appObjects.clear();
                for (DataSnapshot appSnapshot : dataSnapshot.getChildren()) {

                    AppObject appObject = appSnapshot.getValue(AppObject.class);

                    if (appObject.getUserId().equals(firebaseUser.getUid())) {
                        appObjects.add(new AppObject(appObject.getTitel(), appObject.getShortDesc(), appObject.getShortDesc(), appObject.getPackageName(), appObject.getUserId(), appObject.getAppRating(), appObject.getColor(), appObject.getCategory(), appObject.getAppTime(), appObject.getRootTitel(), appObject.getTextColor()));
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        TextView textViewApps = (TextView) findViewById(R.id.textViewYourApps);
        recyclerView.setAdapter(myAdapter);

        if (appObjects.size()<0) {
            textViewApps.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else {
            TextView textViewMyApps = (TextView)findViewById(R.id.myAppsTextView);
            textViewMyApps.setText("LongClick on avatar to delete it, tap to edit properties");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("profile", true).apply();
        Button buttonChangePassword = (Button)findViewById(R.id.buttonChangePassword);
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this).create();
                View viewAlertDialog = LayoutInflater.from(alertDialog.getContext()).inflate(R.layout.change_password_dialo, null);

                final EditText editTextOldPass = (EditText)viewAlertDialog.findViewById(R.id.editTextOldPassword);
                final EditText edtTextNewPass = (EditText)viewAlertDialog.findViewById(R.id.editTextNewPassword);
                Button button = (Button)viewAlertDialog.findViewById(R.id.dialogChangeEmail);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        passOld = editTextOldPass.getText().toString();
                        passNew = edtTextNewPass.getText().toString();
                        if (firebaseUser!= null) {
                            email = firebaseUser.getEmail();


                            AuthCredential authCredential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), passOld);
                            firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        firebaseUser.updatePassword(passNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Password changed", Toast.LENGTH_LONG).show();
                                                    alertDialog.dismiss();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Something went wrong with changing password", Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        });

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Unable to authenticate", Toast.LENGTH_LONG).show();
                                    }
                                }

                            });
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "You are not logged in", Toast.LENGTH_LONG).show();
                        }


                    }
                });
                alertDialog.setView(viewAlertDialog);
                alertDialog.show();



            }
        });

        Button logOut = (Button) findViewById(R.id.buttonLogOut);
        logOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        firebaseAuth.signOut();
                        Toast.makeText(getApplicationContext(),"User logged out", Toast.LENGTH_LONG).show();
                        Intent startIntent = new Intent(getApplicationContext(), StartActivity.class);
                        startActivity(startIntent);
                        finish();


                    }
                }
        );

        Button buttonEmail = (Button)findViewById(R.id.buttonChangeEmail);
        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final AlertDialog alertDialogEmail = new AlertDialog.Builder(ProfileActivity.this).create();
                View viewAlerDialogEmail = LayoutInflater.from(alertDialogEmail.getContext()).inflate(R.layout.change_email_dialog, null);


                final EditText editTextOldEmail = (EditText)viewAlerDialogEmail.findViewById(R.id.editTextOldEmail);
                final EditText edtTextNewEmail = (EditText)viewAlerDialogEmail.findViewById(R.id.editTextNewEmail);
                final EditText editTextCurrenPassword = (EditText)viewAlerDialogEmail.findViewById(R.id.editTextCurrenPassword);
                Button button = (Button)viewAlerDialogEmail.findViewById(R.id.dialogChangeEmail);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        oldMail =  editTextOldEmail.getText().toString();
                        newMail = edtTextNewEmail.getText().toString();

                        if (firebaseUser!=null && oldMail.equals(firebaseUser.getEmail())) {



                            AuthCredential authCredential = EmailAuthProvider.getCredential(oldMail, editTextCurrenPassword.getText().toString());
                            firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        firebaseUser.updateEmail(newMail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {

                                                    Toast.makeText(getApplicationContext(), "E-mail changed", Toast.LENGTH_LONG).show();
                                                    alertDialogEmail.dismiss();
                                                    onStart();


                                                }
                                                else {
                                                    Toast.makeText(getApplicationContext(), "Something went wrong with changing e-mail", Toast.LENGTH_LONG).show();
                                                }


                                            }
                                        });


                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "You are not logged in!", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                        }
                    }
                });

                alertDialogEmail.setView(viewAlerDialogEmail);
                alertDialogEmail.show();


            }
        });

    }
    @Override
    protected void onStop() {
        super.onStop();


       SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("profile", false).apply();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
