package com.example.nisonnaeson;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nisonnaeson.R;

import java.util.Calendar;

public class InputView extends AppCompatActivity {

    private EditText editTextAmount, editTextDate, editTextFriendName;
    private Spinner spinnerPayer;
    private CheckBox checkBoxForWhom;
    private ImageView imageViewReceipt;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputview);

        // Toolbar 설정
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDate = findViewById(R.id.editTextDate);
//        editTextFriendName = findViewById(R.id.editTextFriendName);
//        spinnerPayer = findViewById(R.id.spinnerPayer);
//        checkBoxForWhom = findViewById(R.id.checkBoxForWhom);
//        imageViewReceipt = findViewById(R.id.imageViewReceipt);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // 스피너 설정
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payer_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPayer.setAdapter(adapter);

        // 날짜 선택기 설정
        editTextDate.setOnClickListener(view -> showDatePickerDialog());

        // 버튼 클릭 리스너 설정
        buttonSubmit.setOnClickListener(view -> {
            // 입력 처리 로직
        });
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