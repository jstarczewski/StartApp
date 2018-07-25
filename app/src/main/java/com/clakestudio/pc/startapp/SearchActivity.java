package com.clakestudio.pc.startapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



import java.util.ArrayList;

import java.util.List;

/**
 * Created by pc on 2017-08-27.
 */

public class SearchActivity extends AppCompatActivity {

    DatabaseReference appReference = FirebaseDatabase.getInstance().getReference("Apps");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.searchRecyclerView);
        final List<AppObject>appObjects = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        MyAdapter myAdapter = new MyAdapter(appObjects);
        recyclerView.setHasFixedSize(true);

        TextView textView = (TextView)findViewById(R.id.textViewHastag);

        final String hashTag = getIntent().getStringExtra("hashTag");

        if (hashTag.contains("#")) {
            textView.setText(hashTag);
        }
        else {
        textView.setText("#"+hashTag);
        }
        appReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                appObjects.clear();
                for (DataSnapshot appSnapshot : dataSnapshot.getChildren()) {

                    AppObject appObject = appSnapshot.getValue(AppObject.class);

                    if (appObject.getShortDesc().contains(hashTag)) {
                        appObjects.add(new AppObject(appObject.getTitel(), appObject.getShortDesc(), appObject.getLongDesc(), appObject.getPackageName(), appObject.getUserId(), appObject.getAppRating(), appObject.getColor(), appObject.getCategory(), appObject.getAppTime(), appObject.getRootTitel(), appObject.getTextColor()));
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        appReference.onDisconnect();
        finish();

    }
}
