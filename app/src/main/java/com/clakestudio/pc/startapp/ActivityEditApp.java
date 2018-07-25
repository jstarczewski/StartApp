package com.clakestudio.pc.startapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
 * Created by pc on 2017-09-13.
 */

public class ActivityEditApp extends AppCompatActivity {


    DatabaseReference AppReference = FirebaseDatabase.getInstance().getReference("Apps");
    DatabaseReference RatingReference = FirebaseDatabase.getInstance().getReference("AppRatings");
    private final int GALLERY_ACTIVITY_CODE = 200;
    StorageReference appAvatar = FirebaseStorage.getInstance().getReference("Avatars");


    String picturePath;
    EditText etTitel;
    EditText etSdesc;
    EditText etLdesc;
    EditText etPackage;
    TextView editAppTextView;
    int Color;
    int textColor;
    TextView textViewCategory;
 private   ProgressDialog progressDialog;
    String titel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_app_activity);

        final Button submitApp = (Button)findViewById(R.id.butt);
        final Button editColor = (Button)findViewById(R.id.buttColor);
        editColor.setText("Edit color");
        submitApp.setText("Submit edit");
        TextView textViewEditInfo = (TextView) findViewById(R.id.editInfo);
        textViewEditInfo.setVisibility(View.VISIBLE);
        final StorageReference appAvatar = FirebaseStorage.getInstance().getReference("Avatars");
        if (getIntent().getExtras()!=null){
        titel  = getIntent().getExtras().getString("titel", "no");
        }
        else {
            titel = getSharedPreferences("prefs", Context.MODE_PRIVATE).getString("editRoot", "no");
        }

        picturePath = "noUrl";

        etTitel = (EditText) findViewById(R.id.et_titel);
        etSdesc = (EditText) findViewById(R.id.et_sdesc);
        etLdesc = (EditText) findViewById(R.id.et_ldesc);
        etPackage = (EditText) findViewById(R.id.et_package);

        progressDialog = new ProgressDialog(this);

        textViewCategory = (TextView)findViewById(R.id.categoryTextView);
        editAppTextView = (TextView)findViewById(R.id.appEditAdd);
        editAppTextView.setText("Edit app's properties");
        final Button editTextColor = (Button)findViewById(R.id.buttTextColor);
        editTextColor.setText("Edit text color");
        AppReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot AppSnapshot: dataSnapshot.getChildren()) {
                        AppObject appObject = AppSnapshot.getValue(AppObject.class);
                        if (appObject.getRootTitel().equals(titel)) {

                            etTitel.setText(appObject.getTitel());
                            etSdesc.setText(appObject.getShortDesc());
                            etLdesc.setText(appObject.getLongDesc());
                            etPackage.setText(appObject.getPackageName());
                            Color = appObject.getColor();
                            textViewCategory.setText(appObject.getCategory());
                            editColor.setTextColor(Color);
                            editTextColor.setTextColor(appObject.getTextColor());
                            textViewCategory.setText(getSharedPreferences("prefs", Context.MODE_PRIVATE).getString("editCategory", appObject.getCategory()));
                        }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        submitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                progressDialog.setMessage("Updating data");

                if (picturePath.equals("noUrl")) {
                    submitApp.setVisibility(View.INVISIBLE);
                    AppReference.child(titel).child("titel").setValue(etTitel.getText().toString());
                    AppReference.child(titel).child("shortDesc").setValue(etSdesc.getText().toString());
                    AppReference.child(titel).child("longDesc").setValue(etLdesc.getText().toString());
                    AppReference.child(titel).child("packageName").setValue(etPackage.getText().toString());
                    AppReference.child(titel).child("color").setValue(Color);
                    AppReference.child(titel).child("textColor").setValue(textColor)
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    submitApp.setVisibility(View.VISIBLE);
                                }
                            })


                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                submitApp.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                                getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putBoolean("edit", false).apply();
                                getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().remove("editRoot").apply();
                                getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().remove("editCategory").apply();
                                Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intentMain);

                            }
                        }
                    });

                }
                else {
                    submitApp.setVisibility(View.INVISIBLE);
                    appAvatar.child(titel).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            File f = new File(picturePath);
                            Uri contentUri = Uri.fromFile(f);
                            appAvatar.child(etTitel.getText().toString()).putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressDialog.dismiss();
                                    getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putBoolean("edit", false).apply();
                                    getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().remove("editRoot").apply();
                                    getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().remove("editCategory").apply();
                                    AppReference.child(titel).child("titel").setValue(etTitel.getText().toString());
                                    AppReference.child(titel).child("shortDesc").setValue(etSdesc.getText().toString());
                                    AppReference.child(titel).child("longDesc").setValue(etLdesc.getText().toString());
                                    AppReference.child(titel).child("packageName").setValue(etPackage.getText().toString());
                                    AppReference.child(titel).child("color").setValue(Color);
                                    Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intentMain);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    submitApp.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


                }





            }
        });
        textViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putBoolean("edit", true).apply();
                getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putString("editRoot", titel).apply();
                Intent categoryIntent = new Intent(getApplicationContext(), CategorySelectActivity.class);
                startActivity(categoryIntent);


            }
        });

        editColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ColorChooserDialog dialog = new ColorChooserDialog(ActivityEditApp.this);
                dialog.setTitle("Choose color");
                dialog.setColorListener(new ColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {
                        editColor.setVisibility(View.VISIBLE);
                        Color = color;
                    }
                });
                dialog.show();


            }
        });

        editTextColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                ColorChooserDialog dialog = new ColorChooserDialog(ActivityEditApp.this);
                dialog.setTitle("Choose color");
                dialog.setColorListener(new ColorListener() {
                    @Override
                    public void OnColorClick(View v, int color) {
                        editColor.setVisibility(View.VISIBLE);
                        textColor = color;
                    }
                });
                dialog.show();


            }
        });

      ImageView imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etTitel.getText().toString().length()==0) {
                    Toast.makeText(getApplicationContext(),"Declare title of your app first!",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent gallery_Intent = new Intent(getApplicationContext(), GalleryUtil.class);
                    gallery_Intent.putExtra("titel", titel);
                    startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {


                picturePath = data.getStringExtra("picturePath");

                Toast.makeText(getApplicationContext(), "Avatar added, remember to tap submit after filling other properties", Toast.LENGTH_LONG).show();

            }

        }


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {




        getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().remove("editRoot").apply();
        getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().remove("editCategory").apply();

        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().remove("editCategory").apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
