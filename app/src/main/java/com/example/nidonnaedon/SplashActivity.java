package com.example.nidonnaedon;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nisonnaeson.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 3초 후 LoginActivity로 이동
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

                // Shared Element Transition 설정
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                        SplashActivity.this,
                        findViewById(R.id.title),
                        "title_transition"
                );

                startActivity(intent, options.toBundle());
                finish(); // 현재 액티비티 종료
            }
        }, 3000); // 3000 milliseconds = 3 seconds
    }
}