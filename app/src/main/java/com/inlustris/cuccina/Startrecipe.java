package com.inlustris.cuccina;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import javax.microedition.khronos.opengles.GL;

import static java.security.AccessController.getContext;

public class Startrecipe extends AppCompatActivity {
    TabLayout tabs;
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

        AppBarLayout appbarlayout = findViewById(R.id.appbar);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ViewPager items = findViewById(R.id.items);
        tabs = findViewById(R.id.tabs);
        TextView calories = findViewById(R.id.calories);
        TextView time = findViewById(R.id.time);
        TextView prato = findViewById(R.id.prato);
        ImageView pick = findViewById(R.id.foodpick);
        Glide.with(this).load(activity.getIntent().getExtras().getString("picurl")).into(pick);
        StartRecipeAdapter adapter = new StartRecipeAdapter(activity);
        items.setAdapter(adapter);
        tabs.setupWithViewPager(items);
        tabs.getTabAt(0).setText("Ingredientes");
        tabs.getTabAt(1).setText("Passo a passo");
        prato.setText(activity.getIntent().getExtras().getString("prato"));
        calories.setText(activity.getIntent().getExtras().getString("calorias") + "kcal por 100/g");
        time.setText(activity.getIntent().getExtras().getString("tempo"));



        /*appbarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    tabs.setVisibility(View.VISIBLE);
                    isShow = true;
                } else if (isShow) {
                    tabs.setVisibility(View.GONE);//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });*/
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
