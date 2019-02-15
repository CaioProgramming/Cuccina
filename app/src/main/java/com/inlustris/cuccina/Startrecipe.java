package com.inlustris.cuccina;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.inlustris.cuccina.Adapters.ViewPagerAdapter;

import java.util.Objects;

public class Startrecipe extends AppCompatActivity {

    private android.support.v4.view.ViewPager slides;
    private android.widget.Button goback;
    private android.support.design.widget.TabLayout tabs;
    private android.widget.Button begin;
    Activity activity = this;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startrecipe);
        this.begin = findViewById(R.id.begin);
        this.tabs = findViewById(R.id.tabs);
        this.goback = findViewById(R.id.goback);
        this.slides = findViewById(R.id.slides);
        tabs.setupWithViewPager(slides,true);
        adapter = new ViewPagerAdapter(this);
        slides.setAdapter(adapter);
        setupTabIcons();
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
            }
        });
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity,Steps.class);
                i.putExtra("id",activity.getIntent().getStringExtra("id"));
                i.putExtra("prato",activity.getIntent().getStringExtra("prato"));
                startActivity(i);
                activity.finish();
            }
        });
        getSupportActionBar().hide();







    }

    private void setupTabIcons() {
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimaryLight));

        for (int i = 0; i  < 3; i++){
            Objects.requireNonNull(tabs.getTabAt(i)).setIcon(R.drawable.dot);
        }

    }
}
