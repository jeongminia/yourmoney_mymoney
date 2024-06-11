package com.example.nidonnaedon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.nidonnaedon.R;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class MyPageView extends AppCompatActivity {

    private EditText nameEditText;
    private EditText nicknameEditText;
    private Button saveButton;
    private Button logoutButton;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        // UI 요소 초기화
        nameEditText = findViewById(R.id.name_edit_text);
        nicknameEditText = findViewById(R.id.nickname_edit_text);
        saveButton = findViewById(R.id.save_button);
        logoutButton = findViewById(R.id.logout_button);
        exitButton = findViewById(R.id.exit_button);

        // 이전에 저장된 데이터 로드
        loadUserData();

        // 뒤로가기 버튼 클릭 시 액티비티 종료
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 저장하기 버튼 클릭 이벤트
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });

        // 로그아웃 버튼 클릭 이벤트
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // 탈퇴하기 버튼 클릭 이벤트
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAccount();
            }
        });
    }

    // 사용자 데이터 로드
    private void loadUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "김블랑");
        String nickname = sharedPreferences.getString("nickname", "동에번쩍");

        nameEditText.setText(name);
        nicknameEditText.setText(nickname);
    }

    // 사용자 데이터 저장
    private void saveUserData() {
        String name = nameEditText.getText().toString();
        String nickname = nicknameEditText.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("name", name);
        editor.putString("nickname", nickname);
        editor.apply();

        // 키보드 숨기기
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show();
    }

    // 로그아웃
    private void logout() {
        // 로그인 액티비티로 이동
        Intent intent = new Intent(MyPageView.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    // 탈퇴하기
    private void exitAccount() {
        // 사용자 데이터 삭제 (옵션)
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // 로그인 액티비티로 이동
        Intent intent = new Intent(MyPageView.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
