package com.clakestudio.pc.startapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pc on 2017-08-26.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private String[] mDataset;
    StorageReference appAvatar = FirebaseStorage.getInstance().getReference("Avatars");
    DatabaseReference appReference = FirebaseDatabase.getInstance().getReference("Apps");
    DatabaseReference appRating = FirebaseDatabase.getInstance().getReference("AppRatings");
    Context context;
    String packageName;
    private HashTagHelper mTextHashTagHelper;
    ProgressDialog progressDialog;
    int i = 0;
    String rootTitel;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    List<AppObject> appObjects = new ArrayList<>();

    MyAdapter(List<AppObject> appObjects) {
        this.appObjects = appObjects;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textView;
        TextView textViewTitel;
        ImageView imageView;
        ImageButton buttonInstall;
        TextView textViewContainer;
        Toolbar toolbarBottom;
        ImageButton buttonViewDesc;
        TextView textViewLongDesc;
        ImageButton buttonRating;
        LinearLayout linearSpace;
        TextView rootTitel;
        TextView categoryContainer;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            textView = (TextView) itemView.findViewById(R.id.textViewTitel);
            textViewTitel = (TextView) itemView.findViewById(R.id.toolbar_titel_text);
            imageView = (ImageView) itemView.findViewById(R.id.imageAvatar);
            buttonInstall = (ImageButton) itemView.findViewById(R.id.buttonInstall);
            textViewContainer = (TextView) itemView.findViewById(R.id.packageContainerTextView);
            toolbarBottom = (Toolbar) itemView.findViewById(R.id.toolbar_bottom);
            buttonViewDesc = (ImageButton) itemView.findViewById(R.id.buttonViewDesc);
            textViewLongDesc = (TextView) itemView.findViewById(R.id.tvldesc);
            buttonRating = (ImageButton) itemView.findViewById(R.id.buttonRating);
            linearSpace = (LinearLayout) itemView.findViewById(R.id.linearSpace);
            rootTitel = (TextView) itemView.findViewById(R.id.rootTitelContainer);

        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);


        context = parent.getContext();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (context.getSharedPreferences("prefs", Context.MODE_PRIVATE).getBoolean("profile", false)) {
                    Intent editAppIntent = new Intent(context, ActivityEditApp.class);
                    editAppIntent.putExtra("titel", myViewHolder.rootTitel.getText().toString());
                    context.startActivity(editAppIntent);
                }
            }
        });


        appReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot appSnapshot : dataSnapshot.getChildren()) {

                    AppObject appObject = appSnapshot.getValue(AppObject.class);

                    if (appObject.getTitel().equals(myViewHolder.textViewTitel.getText().toString())) {
                        packageName = appObject.getPackageName();
                        myViewHolder.toolbarBottom.setBackgroundColor(appObject.getColor());
                        myViewHolder.buttonInstall.setBackgroundColor(appObject.getColor());
                        myViewHolder.buttonRating.setBackgroundColor(appObject.getColor());
                        myViewHolder.buttonViewDesc.setBackgroundColor(appObject.getColor());
                        myViewHolder.linearSpace.setBackgroundColor(appObject.getColor());
                        myViewHolder.textViewTitel.setTextColor(appObject.getTextColor());
                        myViewHolder.textViewContainer.setText(appObject.getPackageName());

                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myViewHolder.buttonInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + myViewHolder.textViewContainer.getText().toString()));
                context.startActivity(intent);

            }
        });

        mTextHashTagHelper = HashTagHelper.Creator.create(Color.parseColor("#DD2C00"), new HashTagHelper.OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {
                Intent intent = new Intent(context, SearchActivity.class);
                intent.putExtra("hashTag", hashTag);
                context.startActivity(intent);
            }
        });
        mTextHashTagHelper.handle(myViewHolder.textView);

        myViewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                if (context.getSharedPreferences("prefs", Context.MODE_PRIVATE).getBoolean("profile", false)) {


                    final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    View viewAlertDialog = LayoutInflater.from(context).inflate(R.layout.dialog_delete, null);

                    Button buttonDelete = (Button) viewAlertDialog.findViewById(R.id.buttonDelete);
                    buttonDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            alertDialog.dismiss();


                            if (firebaseUser != null) {


                                appAvatar.child(myViewHolder.rootTitel.getText().toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "App removed", Toast.LENGTH_LONG).show();
                                        appReference.child(myViewHolder.rootTitel.getText().toString()).removeValue();
                                        appObjects.remove(myViewHolder.getAdapterPosition());
                                        appRating.child(myViewHolder.rootTitel.getText().toString()).removeValue();
                                        notifyItemRemoved(myViewHolder.getAdapterPosition());


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else {
                                Toast.makeText(context, "You are not logged in", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                    alertDialog.setView(viewAlertDialog);
                    alertDialog.show();


                }


                return true;
            }
        });

        myViewHolder.buttonRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String titel = myViewHolder.rootTitel.getText().toString();


                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                appRating.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot ratingSnapshot : dataSnapshot.getChildren()) {
                            RatingObject ratingObject = ratingSnapshot.getValue(RatingObject.class);
                            if (ratingObject.getTitel().equals(titel)) {
                                if (!ratingObject.getId().contains(firebaseUser.getUid())) {

                                    appRating.child(titel).child("id").setValue(ratingObject.getId() + " " + firebaseUser.getUid());
                                    appReference.child(titel).child("appRating").setValue((Integer.parseInt(ratingObject.getRating()) - 1));
                                    appRating.child(titel).child("rating").setValue(Integer.toString((Integer.parseInt(ratingObject.getRating()) - 1)));


                                } else {
                                    String idnew = ratingObject.getId().replace(firebaseUser.getUid(), "");
                                    appRating.child(titel).child("id").setValue(idnew);
                                    appReference.child(titel).child("appRating").setValue((Integer.parseInt(ratingObject.getRating()) + 1));
                                    appRating.child(titel).child("rating").setValue(Integer.toString((Integer.parseInt(ratingObject.getRating()) + 1)));

                                }

                            }


                        }


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        myViewHolder.textViewLongDesc.setVisibility(View.GONE);
        myViewHolder.linearSpace.setVisibility(View.GONE);

        if (context.getSharedPreferences("prefs", Context.MODE_PRIVATE).getBoolean("anonymous", false)) {
            myViewHolder.buttonRating.setVisibility(View.GONE);
        } else {

            appRating.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ratingSnapshot : dataSnapshot.getChildren()) {

                        RatingObject ratingObject = ratingSnapshot.getValue(RatingObject.class);
                        if (ratingObject.getTitel().equals(myViewHolder.rootTitel.getText().toString())) {
                            if (ratingObject.getId().contains(firebaseUser.getUid())) {
                                myViewHolder.buttonRating.setImageResource(R.drawable.ic_thumb_down_white_24dp);
                            } else {
                                myViewHolder.buttonRating.setImageResource(R.drawable.ic_thumb_up_white_24dp);

                            }
                        }

                    }


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        myViewHolder.buttonViewDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation fade = AnimationUtils.loadAnimation(context, R.anim.fade_out_long);
                Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in_long);

                if (myViewHolder.textViewLongDesc.getVisibility() == View.GONE) {
                    myViewHolder.linearSpace.startAnimation(fadeIn);
                    myViewHolder.linearSpace.setVisibility(View.VISIBLE);
                    myViewHolder.textViewLongDesc.startAnimation(fadeIn);
                    myViewHolder.textViewLongDesc.setVisibility(View.VISIBLE);
                } else {
                    myViewHolder.linearSpace.startAnimation(fade);
                    myViewHolder.linearSpace.setVisibility(View.GONE);
                    myViewHolder.textViewLongDesc.startAnimation(fade);
                    myViewHolder.textViewLongDesc.setVisibility(View.GONE);
                }


            }
        });


        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        holder.textViewTitel.setText(appObjects.get(position).getTitel());
        holder.textView.setText(appObjects.get(position).getShortDesc());
        holder.textViewContainer.setText(appObjects.get(position).getPackageName());
        holder.textViewLongDesc.setText(appObjects.get(position).getLongDesc());
        holder.rootTitel.setText(appObjects.get(position).getRootTitel());


        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(appAvatar.child(holder.rootTitel.getText().toString()))
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return appObjects.size();
    }

}
