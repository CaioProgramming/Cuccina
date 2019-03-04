package com.inlustris.cuccina;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.Random;

public class Splash extends AppCompatActivity {

    private android.widget.TextView appname;
    private android.widget.LinearLayout background;
    private Activity activity = this;
    boolean animating = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        this.background = findViewById(R.id.background);
        this.appname = findViewById(R.id.appname);
        Typeface font = Typeface.createFromAsset(this.getAssets(),"fonts/GrandHotel-Regular.ttf");
        appname.setTypeface(font);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.pop_in);
        appname.startAnimation(animation);
        background();

    }








    private void background() {
        final int colors[] ={
                this.getResources().getColor(R.color.md_blue_400),
                this.getResources().getColor(R.color.md_yellow_400),
                this.getResources().getColor(R.color.md_blue_900),
                this.getResources().getColor(R.color.md_red_400),
                this.getResources().getColor(R.color.md_purple_400),
                this.getResources().getColor(R.color.md_green_400),
                this.getResources().getColor(R.color.md_red_A200),
                this.getResources().getColor(R.color.md_light_green_500),
                this.getResources().getColor(R.color.md_yellow_200),
                this.getResources().getColor(R.color.md_red_A400)
        };
        final int positions[] = {
                background.getTop(),
                background.getBottom()

        };

        CountDownTimer timer = new CountDownTimer(15000, 600) {
            @Override
            public void onTick(long l) {
                CountDownTimer timer1 = new CountDownTimer(4000,200) {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        if (animating) {
                            Rect bounds = new Rect();
                            background.getDrawingRect(bounds);

                            Random random = new Random();
                            int color = random.nextInt(colors.length);
                            background.setBackgroundColor(colors[color]);
                            int cx = bounds.centerX();
                            int cy = bounds.centerY();
                            int radius = Math.max(background.getWidth(), background.getHeight());
                            Animator anim = ViewAnimationUtils.createCircularReveal(background, cx, cy,
                                    100, radius);
                            anim.setDuration(1000);
                            background.setVisibility(View.VISIBLE);
                            anim.start();
                            System.out.println(color);
                        }
                    }
                }.start();

            }

            @Override
            public void onFinish() {
                animating = false;
                Intent i = new Intent(activity,MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left);
                activity.finish();
            }
        }.start();



    }
}
