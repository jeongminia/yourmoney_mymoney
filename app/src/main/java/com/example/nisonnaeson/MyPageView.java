package com.example.nisonnaeson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nisonnaeson.R;

public class MyPageView extends AppCompatActivity {

    private TextView nameTextView;
    private TextView nicknameTextView;
    private Button saveButton;
    private Button logoutButton;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nameTextView = findViewById(R.id.name_text_view);
        nicknameTextView = findViewById(R.id.nickname_text_view);
        saveButton = findViewById(R.id.save_button);
        logoutButton = findViewById(R.id.logout_button);
        exitButton = findViewById(R.id.exit_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 저장하기 로직 구현
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그아웃 로직 구현
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 탈퇴하기 로직 구현
            }
        });
    }
}
