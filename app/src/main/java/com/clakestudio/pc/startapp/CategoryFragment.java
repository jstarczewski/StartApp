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
import java.util.List;

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
        ArrayList<CategoryObjcet> categoryObjcets = new ArrayList<>();


        categoryObjcets.add(new CategoryObjcet("Art & Design"));
        categoryObjcets.add(new CategoryObjcet("Auto & Vehicles"));
        categoryObjcets.add(new CategoryObjcet("Beauty"));
        categoryObjcets.add(new CategoryObjcet("Books & Reference"));
        categoryObjcets.add(new CategoryObjcet("Business"));
        categoryObjcets.add(new CategoryObjcet("Comics"));
        categoryObjcets.add(new CategoryObjcet("Communications"));
        categoryObjcets.add(new CategoryObjcet("Dating"));
        categoryObjcets.add(new CategoryObjcet("Education"));
        categoryObjcets.add(new CategoryObjcet("Entertainment"));
        categoryObjcets.add(new CategoryObjcet("Events"));
        categoryObjcets.add(new CategoryObjcet("Finance"));
        categoryObjcets.add(new CategoryObjcet("Food & Drink"));
        categoryObjcets.add(new CategoryObjcet("Health & Fitness"));
        categoryObjcets.add(new CategoryObjcet("Libraries & Demo"));
        categoryObjcets.add(new CategoryObjcet("Lifestyle"));
        categoryObjcets.add(new CategoryObjcet("Maps & Navigation"));
        categoryObjcets.add(new CategoryObjcet("Medical"));
        categoryObjcets.add(new CategoryObjcet("Music & Audio"));
        categoryObjcets.add(new CategoryObjcet("News & Magazines"));
        categoryObjcets.add(new CategoryObjcet("Parenting"));
        categoryObjcets.add(new CategoryObjcet("Personalization"));
        categoryObjcets.add(new CategoryObjcet("Photography"));
        categoryObjcets.add(new CategoryObjcet("Productivity"));
        categoryObjcets.add(new CategoryObjcet("Shopping"));
        categoryObjcets.add(new CategoryObjcet("Social"));
        categoryObjcets.add(new CategoryObjcet("Sports"));
        categoryObjcets.add(new CategoryObjcet("Tools"));
        categoryObjcets.add(new CategoryObjcet("Travel & Local"));
        categoryObjcets.add(new CategoryObjcet("Video Players & Editors"));
        categoryObjcets.add(new CategoryObjcet("Weather"));
        categoryObjcets.add(new CategoryObjcet("Games:Action"));
        categoryObjcets.add(new CategoryObjcet("Games:Adventure"));
        categoryObjcets.add(new CategoryObjcet("Games:Arcade"));
        categoryObjcets.add(new CategoryObjcet("Games:Board"));
        categoryObjcets.add(new CategoryObjcet("Games:Card"));
        categoryObjcets.add(new CategoryObjcet("Games:Casino"));
        categoryObjcets.add(new CategoryObjcet("Games:Casual"));
        categoryObjcets.add(new CategoryObjcet("Games:Educational"));
        categoryObjcets.add(new CategoryObjcet("Games:Music"));
        categoryObjcets.add(new CategoryObjcet("Games:Puzzle"));
        categoryObjcets.add(new CategoryObjcet("Games:Racing"));
        categoryObjcets.add(new CategoryObjcet("Games:Role Playing"));
        categoryObjcets.add(new CategoryObjcet("Games:Simulation"));
        categoryObjcets.add(new CategoryObjcet("Games:Sports"));
        categoryObjcets.add(new CategoryObjcet("Games:Strategy"));
        categoryObjcets.add(new CategoryObjcet("Games:Trivia"));
        categoryObjcets.add(new CategoryObjcet("Games:Word"));




        CategoriesAdapter categoriesAdapter = new CategoriesAdapter(categoryObjcets);
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
