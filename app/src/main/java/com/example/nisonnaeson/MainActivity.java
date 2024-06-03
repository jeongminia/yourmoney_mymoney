package com.example.nisonnaeson;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText editTextAmount, editTextDate, editTextFriendName;
    private Spinner spinnerPayer;
    private CheckBox checkBoxForWhom;
    private ImageView imageViewReceipt;
    private Button buttonSubmit, reportButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 초기화
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDate = findViewById(R.id.editTextDate);
        editTextFriendName = findViewById(R.id.editTextFriendName);
        spinnerPayer = findViewById(R.id.spinnerPayer);
        checkBoxForWhom = findViewById(R.id.checkBoxForWhom);
        imageViewReceipt = findViewById(R.id.imageViewReceipt);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        reportButton = findViewById(R.id.reportButton);

        // InputViewActivity로 이동하는 버튼 설정
        Button buttonOpenInputView = findViewById(R.id.buttonOpenInputView);
        buttonOpenInputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InputViewActivity.class);
                startActivity(intent);
            }
        });

        // ReportActivity로 이동하는 버튼 설정
        reportButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            startActivity(intent);
        });

        // DatePickerDialog를 여는 메서드
        editTextDate.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                    editTextDate.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // 현재 액티비티를 종료하여 뒤로가기 효과를 줍니다.
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
