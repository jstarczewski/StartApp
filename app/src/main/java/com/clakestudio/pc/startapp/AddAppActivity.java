package com.clakestudio.pc.startapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;

import java.io.File;


/**
 * Created by pc on 2017-08-26.
 */

public class AddAppActivity extends AppCompatActivity {

    private EditText etTitel;
    private EditText etSdesc;
    private EditText etLdesc;
    private EditText etPackage;
    private boolean addApp = false;
    public static final int READ_STORAGE_PERMISSION = 100;
    private final int GALLERY_ACTIVITY_CODE = 200;
    private ImageView imageView;
    private String picturePath;
    private int Color = 0;
    private int textColor = 0;
    private int time;

    private DatabaseReference appReference = FirebaseDatabase.getInstance().getReference("Apps");
    private StorageReference appAvatar = FirebaseStorage.getInstance().getReference("Avatars");

    private DatabaseReference timeReference = FirebaseDatabase.getInstance().getReference("AppsNumber");
    private DatabaseReference appRating = FirebaseDatabase.getInstance().getReference("AppRatings");
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_app_activity);

        etTitel = (EditText) findViewById(R.id.et_titel);
        etSdesc = (EditText) findViewById(R.id.et_sdesc);
        etLdesc = (EditText) findViewById(R.id.et_ldesc);
        etPackage = (EditText) findViewById(R.id.et_package);
        TextView textView = (TextView) findViewById(R.id.categoryTextView);


        final Button button = (Button) findViewById(R.id.butt);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION);
        }

        timeReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot timeSnapshot : dataSnapshot.getChildren()) {
                    TimeObject timeObject = timeSnapshot.getValue(TimeObject.class);
                    time = timeObject.getTime();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        String category = getSharedPreferences("prefs", Context.MODE_PRIVATE).getString("category", null);
        textView.setText(category);
        getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().remove("category").apply();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (firebaseUser != null) {

                    if (etTitel.getText().toString().isEmpty() || etSdesc.getText().toString().isEmpty() || etLdesc.getText().toString().isEmpty() || etPackage.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "All propreties must be filled before submitting", Toast.LENGTH_LONG).show();
                    } else {

                        if (Color == 0 || picturePath == null || textColor == 0) {
                            Toast.makeText(getApplicationContext(), "Declare the color of mini-bar before submiting the app", Toast.LENGTH_LONG).show();
                        } else {


                            appReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(etTitel.getText().toString())) {
                                        Toast.makeText(getApplicationContext(), "This Application Already exists :(", Toast.LENGTH_LONG).show();

                                    } else {
                                        button.setVisibility(View.INVISIBLE);
                                        final String titel = etTitel.getText().toString();
                                        final String sdesc = etSdesc.getText().toString();
                                        final String ldesc = etLdesc.getText().toString();
                                        final String epack = etPackage.getText().toString();
                                        final String category = getIntent().getStringExtra("category");
                                        time--;


                                        File f = new File(picturePath);

                                        Uri contentUri = Uri.fromFile(f);
                                        imageView.setImageURI(contentUri);
                                        appAvatar.child(etTitel.getText().toString()).putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                                timeReference.child("time").child("time").setValue(time);
                                                RatingObject ratingObject = new RatingObject("id", "99", titel);
                                                appRating.child(titel).setValue(ratingObject);
                                                AppObject appObject = new AppObject(titel, sdesc, ldesc, epack, firebaseUser.getUid(), 99, Color, category, time, titel, textColor);
                                                appReference.child(titel).setValue(appObject);

                                                Toast.makeText(getApplicationContext(), "App added", Toast.LENGTH_LONG).show();
                                                Intent inten = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(inten);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                button.setVisibility(View.VISIBLE);

                                            }
                                        });

                                    }


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }

                    }


                }

            }
        });
        imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etTitel.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.declare_title_of_your_arr_first, Toast.LENGTH_LONG).show();
                } else {
                    Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
                    gallery_Intent.putExtra("titel", etTitel.getText().toString());
                    startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
                }
            }
        });
        final Button buttonColor = (Button) findViewById(R.id.buttColor);
        buttonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ColorChooserDialog dialog = new ColorChooserDialog(AddAppActivity.this);
                dialog.setTitle(R.string.choose_color);
                dialog.setColorListener(new ColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {
                        button.setVisibility(View.VISIBLE);
                        Color = color;
                    }
                });
                //customize the dialog however you want
                dialog.show();

            }
        });
        Button titelColor = (Button) findViewById(R.id.buttTextColor);
        titelColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ColorChooserDialog dialog = new ColorChooserDialog(AddAppActivity.this);
                dialog.setTitle(R.string.choose_color);
                dialog.setColorListener(new ColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {
                        buttonColor.setVisibility(View.VISIBLE);
                        textColor = color;
                    }
                });
                //customize the dialog however you want
                dialog.show();

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                picturePath = data.getStringExtra("picturePath");

                Toast.makeText(getApplicationContext(), R.string.avatar_added_remember_to_tap_submit_after, Toast.LENGTH_LONG).show();

            }

        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.permission_granted, Toast.LENGTH_SHORT).show();
                    onStart();
                } else {
                    Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_SHORT).show();

                }
                return;
        }

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();


    }
}
