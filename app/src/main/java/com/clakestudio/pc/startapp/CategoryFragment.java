package com.clakestudio.pc.startapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by pc on 2017-08-27.
 */

public class CategoryFragment extends Fragment {

    Context context;
    public CategoryFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_blank, container, false);


        context = container.getContext();



        final RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        ArrayList<categoryObject> categoryObjects = new ArrayList<>();


        categoryObjects.add(new categoryObject("Art & Design"));
        categoryObjects.add(new categoryObject("Auto & Vehicles"));
        categoryObjects.add(new categoryObject("Beauty"));
        categoryObjects.add(new categoryObject("Books & Reference"));
        categoryObjects.add(new categoryObject("Business"));
        categoryObjects.add(new categoryObject("Comics"));
        categoryObjects.add(new categoryObject("Communications"));
        categoryObjects.add(new categoryObject("Dating"));
        categoryObjects.add(new categoryObject("Education"));
        categoryObjects.add(new categoryObject("Entertainment"));
        categoryObjects.add(new categoryObject("Events"));
        categoryObjects.add(new categoryObject("Finance"));
        categoryObjects.add(new categoryObject("Food & Drink"));
        categoryObjects.add(new categoryObject("Health & Fitness"));
        categoryObjects.add(new categoryObject("Libraries & Demo"));
        categoryObjects.add(new categoryObject("Lifestyle"));
        categoryObjects.add(new categoryObject("Maps & Navigation"));
        categoryObjects.add(new categoryObject("Medical"));
        categoryObjects.add(new categoryObject("Music & Audio"));
        categoryObjects.add(new categoryObject("News & Magazines"));
        categoryObjects.add(new categoryObject("Parenting"));
        categoryObjects.add(new categoryObject("Personalization"));
        categoryObjects.add(new categoryObject("Photography"));
        categoryObjects.add(new categoryObject("Productivity"));
        categoryObjects.add(new categoryObject("Shopping"));
        categoryObjects.add(new categoryObject("Social"));
        categoryObjects.add(new categoryObject("Sports"));
        categoryObjects.add(new categoryObject("Tools"));
        categoryObjects.add(new categoryObject("Travel & Local"));
        categoryObjects.add(new categoryObject("Video Players & Editors"));
        categoryObjects.add(new categoryObject("Weather"));
        categoryObjects.add(new categoryObject("Games:Action"));
        categoryObjects.add(new categoryObject("Games:Adventure"));
        categoryObjects.add(new categoryObject("Games:Arcade"));
        categoryObjects.add(new categoryObject("Games:Board"));
        categoryObjects.add(new categoryObject("Games:Card"));
        categoryObjects.add(new categoryObject("Games:Casino"));
        categoryObjects.add(new categoryObject("Games:Casual"));
        categoryObjects.add(new categoryObject("Games:Educational"));
        categoryObjects.add(new categoryObject("Games:Music"));
        categoryObjects.add(new categoryObject("Games:Puzzle"));
        categoryObjects.add(new categoryObject("Games:Racing"));
        categoryObjects.add(new categoryObject("Games:Role Playing"));
        categoryObjects.add(new categoryObject("Games:Simulation"));
        categoryObjects.add(new categoryObject("Games:Sports"));
        categoryObjects.add(new categoryObject("Games:Strategy"));
        categoryObjects.add(new categoryObject("Games:Trivia"));
        categoryObjects.add(new categoryObject("Games:Word"));




        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(categoryObjects);
        recyclerView.setAdapter(categoriesAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        context.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putBoolean("fragment", true).apply();
    }
}
