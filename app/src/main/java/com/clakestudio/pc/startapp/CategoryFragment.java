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

    public CategoryFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_blank, container, false);


        context = container.getContext();


        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        ArrayList<categoryObject> categoryObjects = new ArrayList<>();


        categoryObjects.add(new categoryObject(getString(R.string.art_and_design)));
        categoryObjects.add(new categoryObject(getString(R.string.auto_and_vehicles)));
        categoryObjects.add(new categoryObject(getString(R.string.beauty)));
        categoryObjects.add(new categoryObject(getString(R.string.books_and_reference)));
        categoryObjects.add(new categoryObject(getString(R.string.buisness)));
        categoryObjects.add(new categoryObject(getString(R.string.comics)));
        categoryObjects.add(new categoryObject(getString(R.string.communications)));
        categoryObjects.add(new categoryObject(getString(R.string.dating)));
        categoryObjects.add(new categoryObject(getString(R.string.education)));
        categoryObjects.add(new categoryObject(getString(R.string.entertainment)));
        categoryObjects.add(new categoryObject(getString(R.string.events)));
        categoryObjects.add(new categoryObject(getString(R.string.finance)));
        categoryObjects.add(new categoryObject(getString(R.string.food_and_dring)));
        categoryObjects.add(new categoryObject(getString(R.string.health_and_fitness)));
        categoryObjects.add(new categoryObject(getString(R.string.libraries_and_demo)));
        categoryObjects.add(new categoryObject(getString(R.string.lifestyle)));
        categoryObjects.add(new categoryObject(getString(R.string.maps_and_navigation)));
        categoryObjects.add(new categoryObject(getString(R.string.medical)));
        categoryObjects.add(new categoryObject(getString(R.string.music_and_audio)));
        categoryObjects.add(new categoryObject(getString(R.string.news_and_magazines)));
        categoryObjects.add(new categoryObject(getString(R.string.parenting)));
        categoryObjects.add(new categoryObject(getString(R.string.personalization)));
        categoryObjects.add(new categoryObject(getString(R.string.photography)));
        categoryObjects.add(new categoryObject(getString(R.string.productivity)));
        categoryObjects.add(new categoryObject(getString(R.string.shopping)));
        categoryObjects.add(new categoryObject(getString(R.string.social)));
        categoryObjects.add(new categoryObject(getString(R.string.sports)));
        categoryObjects.add(new categoryObject(getString(R.string.tools)));
        categoryObjects.add(new categoryObject(getString(R.string.travel_and_local)));
        categoryObjects.add(new categoryObject(getString(R.string.vide_players_and_editors)));
        categoryObjects.add(new categoryObject(getString(R.string.weather)));
        categoryObjects.add(new categoryObject(getString(R.string.game_action)));
        categoryObjects.add(new categoryObject(getString(R.string.game_adventure)));
        categoryObjects.add(new categoryObject(getString(R.string.game_arcade)));
        categoryObjects.add(new categoryObject(getString(R.string.game_board)));
        categoryObjects.add(new categoryObject(getString(R.string.game_card)));
        categoryObjects.add(new categoryObject(getString(R.string.game_casino)));
        categoryObjects.add(new categoryObject(getString(R.string.game_casual)));
        categoryObjects.add(new categoryObject(getString(R.string.game_educational)));
        categoryObjects.add(new categoryObject(getString(R.string.game_music)));
        categoryObjects.add(new categoryObject(getString(R.string.game_puzzle)));
        categoryObjects.add(new categoryObject(getString(R.string.game_racing)));
        categoryObjects.add(new categoryObject(getString(R.string.game_role_playing)));
        categoryObjects.add(new categoryObject(getString(R.string.game_simulation)));
        categoryObjects.add(new categoryObject(getString(R.string.game_sports)));
        categoryObjects.add(new categoryObject(getString(R.string.game_strategy)));
        categoryObjects.add(new categoryObject(getString(R.string.game_trivia)));
        categoryObjects.add(new categoryObject(getString(R.string.game_word)));

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
