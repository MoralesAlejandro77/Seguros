package com.seguros.presupuestos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.seguros.MainActivity;

public class SplashActivity1 extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH =5500;
    ImageView imag_logo1, imag_logo2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash1);
        imag_logo1 = (ImageView) findViewById(R.id.imag_logo1);
        imag_logo2 = (ImageView) findViewById(R.id.imag_logo2);
        this.setTitle("");
        imag_logo2.setVisibility(View.INVISIBLE);
        imag_logo1.setVisibility(View.INVISIBLE);


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                imag_logo1.setVisibility(View.VISIBLE);
                Animation an1= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
                imag_logo1.startAnimation(an1);
            }
        }, 1000);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                imag_logo2.setVisibility(View.VISIBLE);
                Animation an2= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
                imag_logo2.startAnimation(an2);
            }
        }, 2500);



        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity1.this,MainActivity.class);
                SplashActivity1.this.startActivity(mainIntent);
                SplashActivity1.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);


    }
}
