package com.alexnegara.androidproject.unwiredvault;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // use timer to showcase splash screen and to schedule startActivity for splashscreen
        // timer can be removed to use cold start inherited by android
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                overridePendingTransition(R.anim.splash_slide_in, R.anim.splash_slide_out);
            }
        }, 800);
    }

    // do nothing
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
