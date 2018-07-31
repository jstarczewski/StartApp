package com.clakestudio.pc.startapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * Created by pc on 2017-08-26.
 */

public class BlankFragment extends Fragment {

    private DatabaseReference appReference = FirebaseDatabase.getInstance().getReference("Apps");
    private ArrayList<AppObject> appObjects;
    private ArrayList<AppObject> appObjectsSort;


    public BlankFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragemnt_blank, container, false);
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        appObjects = new ArrayList<>();

        appObjectsSort = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);


        appReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                appObjects.clear();
                int a = 0;
                for (int i = 0; i < 100; i++) {
                    for (DataSnapshot appSnapshot : dataSnapshot.getChildren()) {
                        final AppObject appObject = appSnapshot.getValue(AppObject.class);
                        if (appObject.getAppRating() == i) {
                            appObjects.add(a, new AppObject(appObject.getTitel(), appObject.getShortDesc(), appObject.getLongDesc(), appObject.getUserId(), appObject.getPackageName(), appObject.getAppRating(), appObject.getColor(), appObject.getCategory(), appObject.getAppTime(), appObject.getRootTitel(), appObject.getTextColor()));
                            a++;

                        }

                    }


                }
                final MyAdapter myAdapter = new MyAdapter(appObjects);
                recyclerView.setAdapter(myAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


        return rootView;
    }

}
