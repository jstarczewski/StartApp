package com.clakestudio.pc.startapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2017-08-27.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {


    Context context;

    List<categoryObject> categoryObjects = new ArrayList<>();
    CategoriesAdapter(List<categoryObject> categoryObjects) {
        this.categoryObjects = categoryObjects;
    }


    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView textView;




        public CategoriesViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView)itemView.findViewById(R.id.card_view_category);
            textView = (TextView)itemView.findViewById(R.id.text_view_category);

        }
    }


    @Override
    public CategoriesAdapter.CategoriesViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {



        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        final CategoriesViewHolder categoriesViewHolder = new CategoriesViewHolder(view);

        context = parent.getContext();



        return categoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(final CategoriesAdapter.CategoriesViewHolder holder, int position) {
        holder.textView.setText(categoryObjects.get(position).getCategory());



            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (context.getSharedPreferences("prefs", Context.MODE_PRIVATE).getBoolean("fragment", false)) {

                        Intent intent = new Intent(context, CategoryAppView.class);
                        intent.putExtra("category", holder.textView.getText().toString());
                        context.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putBoolean("fragment", false).apply();
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    }
                    else {

                        if (context.getSharedPreferences("prefs", Context.MODE_PRIVATE).getBoolean("edit", true)) {
                            Intent intent = new Intent(context, ActivityEditApp.class);
                            context.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putString("editCategory", holder.textView.getText().toString()).apply();
                            context.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putBoolean("edit", false).apply();
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        }
                        else {
                            Intent intent = new Intent(context, AddAppActivity.class);
                            intent.putExtra("category", holder.textView.getText().toString());
                            context.getSharedPreferences("prefs", Context.MODE_PRIVATE).edit().putString("category", holder.textView.getText().toString()).apply();
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        }


                    }
                }
            });


/*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
*
* **/



    }

    @Override
    public int getItemCount() {
        return categoryObjects.size();
    }

}
