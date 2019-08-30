package com.inlustris.cuccina;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

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
import com.inlustris.cuccina.Adapters.StartRecipeAdapter;
import com.inlustris.cuccina.Adapters.ViewPagerAdapter;
import com.inlustris.cuccina.Beans.Ingredient;
import com.inlustris.cuccina.Beans.Step;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class Startrecipe extends AppCompatActivity {

    Activity activity = this;
    ViewPagerAdapter adapter;
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


        toolbar = findViewById(R.id.toolbar);
        ViewPager items = findViewById(R.id.items);
        TabLayout tabs = findViewById(R.id.tabs);


        StartRecipeAdapter adapter = new StartRecipeAdapter(activity);
        items.setAdapter(adapter);
        tabs.setupWithViewPager(items);
        tabs.getTabAt(0).setText("Ingredientes");
        tabs.getTabAt(1).setText("Passo a passo");
       toolbar.setTitle(activity.getIntent().getExtras().getString("prato"));

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// app icon in action bar clicked; go home
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
