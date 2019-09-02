package com.inlustris.cuccina.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inlustris.cuccina.Beans.Ingredient;
import com.inlustris.cuccina.Beans.Step;
import com.inlustris.cuccina.R;

import java.util.ArrayList;

public class StartRecipeAdapter extends PagerAdapter {
    private Activity activity;
    public static String titleingredients = "Title";
    private String id;
    public StartRecipeAdapter(Activity activity) {
        this.activity = activity;
        id = activity.getIntent().getStringExtra("id");

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.startpagerlayout, container, false);

        LinearLayout card;
        RecyclerView recycler;

        card = view.findViewById(R.id.card);
        recycler = view.findViewById(R.id.recycler);
        if (position == 0){
            System.out.println("Ingredientes");
            CarregarIngredientes(recycler);
        }else{
            System.out.println("Passos");
            CarregarPassos(recycler);
        }
        Animation animation = AnimationUtils.loadAnimation(activity,R.anim.slide_in_bottom);
        card.startAnimation(animation);
        card.setVisibility(View.VISIBLE);


        container.addView(view);
        return view;
    }


    private void CarregarIngredientes(final RecyclerView items) {
        final ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();

        ingredientArrayList.clear();
        DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
        firebase.child("recipes").child(id).child("ingredientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Ingredient i1 = new Ingredient();
                i1.setIngrediente(titleingredients);
                ingredientArrayList.add(i1);

                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Ingredient ingredient = new Ingredient();
                    Ingredient i = d.getValue(Ingredient.class);
                    if (i != null) {
                        System.out.println(i.getIngrediente());
                        System.out.println(i.getQuantidade());
                        ingredient.setCount(d.getKey());
                        ingredient.setIngrediente(i.getIngrediente());
                        ingredient.setQuantidade(i.getQuantidade());
                        ingredient.setMedidas(i.getMedidas());
                        if (ingredient.getQuantidade().equals("0.5")) {
                            ingredient.setQuantidade("1/2");
                        }
                        if (!ingredient.getQuantidade().equals("1/2")) {

                            int q = Integer.parseInt(ingredient.getQuantidade());
                            if (q >= 2) {
                                if (i.getMedidas().equals("Unidade")) {
                                    ingredient.setMedidas("");

                                }
                            }
                        }
                        if (ingredient.getMedidas().equals("Unidade")) {
                            ingredient.setMedidas("");
                        }
                        ingredientArrayList.add(ingredient);

                        System.out.println(" ingredientes " + ingredientArrayList.size());
                        GridLayoutManager llm = new GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false);
                        items.setHasFixedSize(true);
                        RecycleIngredientsAdapter myadapter = new RecycleIngredientsAdapter(activity, ingredientArrayList, activity, items);
                        items.setAdapter(myadapter);
                        items.setLayoutManager(llm);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void CarregarPassos(final RecyclerView items) {
        final ArrayList<Step> steps = new ArrayList<>();

       steps.clear();
        DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
        firebase.child("recipes").child(id).child("passos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Step step;
                    Step s = d.getValue(Step.class);
                    if (s!= null) {
                        s.setCount(d.getKey());
                        step = s;
                        steps.add(step);
                        System.out.println(" passos " + steps.size());

                    }
                }
                GridLayoutManager llm = new GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false);
                items.setHasFixedSize(true);
                RecyclerStepsAdapter myadapter = new RecyclerStepsAdapter(activity, steps, activity, items);
                items.setAdapter(myadapter);
                items.setLayoutManager(llm);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }







}
