package com.inlustris.cuccina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.inlustris.cuccina.Adapters.RecycleIngredientsAdapter;
import com.inlustris.cuccina.Adapters.RecyclerStepsAdapter;
import com.inlustris.cuccina.Adapters.ViewPagerAdapter;
import com.inlustris.cuccina.Beans.Ingredient;
import com.inlustris.cuccina.Beans.Step;

import java.util.ArrayList;

public class Startrecipe extends AppCompatActivity {

    Activity activity = this;
    ViewPagerAdapter adapter;
    private android.support.v7.widget.RecyclerView items;
    private ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
    ArrayList<Step> stepArrayList = new ArrayList<>();
    private String id;
    private android.support.design.widget.BottomNavigationView navigation;
    private android.support.v7.widget.Toolbar toolbar;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startrecipe);
        this.toolbar = findViewById(R.id.toolbar);
        this.navigation = findViewById(R.id.navigation);
        this.items = findViewById(R.id.items);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(activity.getIntent().getStringExtra("prato"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        id = this.getIntent().getStringExtra("id");

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ingredients:
                        recycleringredientes(ingredientArrayList);
                        return true;

                    case R.id.step:
                        recyclerPassos(stepArrayList);
                        return true;

                    default:
                        return false;
                }
            }
        });
        CarregarIngredientes();
        MobileAds.initialize(this, getString(R.string.addmob_id));

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            System.out.println("Ad didnt loaded");
        }

        //recycleringredientes(ingredientArrayList);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void CarregarIngredientes() {
        if (ingredientArrayList == null) {
            ingredientArrayList = new ArrayList<>();
        }
        ingredientArrayList.clear();
        DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
        firebase.child("recipes").child(id).child("ingredientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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

                               } else {
                                   ingredient.setIngrediente(i.getIngrediente() + "s");

                               }
                           }
                        }
                        if (ingredient.getMedidas().equals("Unidade")) {
                            ingredient.setMedidas("");
                        }
                        ingredientArrayList.add(ingredient);

                        System.out.println(" ingredientes " + ingredientArrayList.size());
                        recycleringredientes(ingredientArrayList);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void recycleringredientes(ArrayList<Ingredient> ingredientes) {
        items.removeAllViews();
        // Collections.reverse(quotes);
        if (ingredientes == null || ingredientes.size() == 0) {
            CarregarIngredientes();
        }
        GridLayoutManager llm = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        items.setHasFixedSize(true);
        RecycleIngredientsAdapter myadapter = new RecycleIngredientsAdapter(this, ingredientes, this, items);
        items.setAdapter(myadapter);
        items.setLayoutManager(llm);
    }

    private void recyclerPassos(ArrayList<Step> passos) {
        items.removeAllViews();
        // Collections.reverse(quotes);
        if (passos == null || passos.size() == 0) {
            CarregarPassos();
        } else {
            GridLayoutManager llm = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
            items.setHasFixedSize(true);
            RecyclerStepsAdapter myadapter = new RecyclerStepsAdapter(this, stepArrayList, this, items);
            items.setAdapter(myadapter);
            items.setLayoutManager(llm);
        }
    }

    private void CarregarPassos() {

        DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
        firebase.child("recipes").child(id).child("passos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (stepArrayList == null) {
                    stepArrayList = new ArrayList<>();
                }
                stepArrayList.clear();
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Step step = new Step();
                    Step s = d.getValue(Step.class);
                    if (s != null) {
                        step.setCount(d.getKey());
                        step.setPasso(s.getPasso());
                    }
                    stepArrayList.add(step);

                }
                recyclerPassos(stepArrayList);

                // getSupportActionBar().setTitle(title);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}
