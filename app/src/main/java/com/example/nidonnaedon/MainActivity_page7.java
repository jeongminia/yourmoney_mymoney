package com.example.nidonnaedon;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nisonnaeson.R;

public class MainActivity_page7 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page7);

        // 뒤로 가기 버튼 기능
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 뒤로 가기 버튼 클릭 시 처리
                finish(); // 액티비티 종료
            }
        });

        // 수정하기 버튼 기능
        Button modifyButton = findViewById(R.id.modify_button);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 수정하기 버튼 클릭 시 처리
                Toast.makeText(MainActivity_page7.this, "수정하기 버튼 클릭됨", Toast.LENGTH_SHORT).show();
                // 수정 로직 추가
            }
        });
    }
}
