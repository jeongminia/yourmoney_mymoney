package com.example.nidonnaedon;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nisonnaeson.R;

import java.io.IOException;
import java.util.Calendar;

public class InputViewActivity extends AppCompatActivity {

    private EditText editTextAmount, editTextDate, editTextUsageDetails;
    private Spinner spinnerCategory, spinnerCurrency;
    private ImageView buttonAddPhoto, imageViewPhoto, calendarIcon, backButton;
    private Button buttonSubmit;
    private CheckBox checkboxAddFriend, checkboxPayer, checkboxGwakJiwon, checkboxYooJaewon;
    private LinearLayout friendList, photoContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputview);

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDate = findViewById(R.id.editTextDate);
        editTextUsageDetails = findViewById(R.id.editTextUsageDetails);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerCurrency = findViewById(R.id.spinnerCurrency);
        buttonAddPhoto = findViewById(R.id.buttonAddPhoto);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        photoContainer = findViewById(R.id.photoContainer);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        calendarIcon = findViewById(R.id.calendarIcon);
        checkboxAddFriend = findViewById(R.id.checkboxAddFriend);
        checkboxPayer = findViewById(R.id.checkboxPayer);
        checkboxGwakJiwon = findViewById(R.id.checkboxGwakJiwon);
        checkboxYooJaewon = findViewById(R.id.checkboxYooJaewon);
        friendList = findViewById(R.id.friendList);
        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // 받아온 데이터 설정
        Intent intent = getIntent();
        if (intent != null) {
            String date = intent.getStringExtra("date");
            String price = intent.getStringExtra("price");
            String usageDetails = intent.getStringExtra("usageDetails");
            String category = intent.getStringExtra("category");

            editTextDate.setText(date);
            editTextAmount.setText(price);
            editTextUsageDetails.setText(usageDetails);

            ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(categoryAdapter);
            if (category != null) {
                int spinnerPosition = categoryAdapter.getPosition(category);
                spinnerCategory.setSelection(spinnerPosition);
            }

            ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource(this, R.array.currency_array, android.R.layout.simple_spinner_item);
            currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCurrency.setAdapter(currencyAdapter);
        }

        buttonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });

        calendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        checkboxAddFriend.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                friendList.setVisibility(View.VISIBLE);
            } else {
                friendList.setVisibility(View.GONE);
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    String amount = editTextAmount.getText().toString();
                    String date = editTextDate.getText().toString();
                    String usageDetails = editTextUsageDetails.getText().toString();
                    String category = spinnerCategory.getSelectedItem().toString();
                    String currency = spinnerCurrency.getSelectedItem() != null ? spinnerCurrency.getSelectedItem().toString() : "";

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("date", date);
                    resultIntent.putExtra("price", amount);
                    resultIntent.putExtra("usageDetails", usageDetails);
                    resultIntent.putExtra("category", category);
                    resultIntent.putExtra("currency", currency);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
        });

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        editTextDate.setText(year + "-" + (month + 1) + "-" + day);
    }

    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageViewPhoto.setImageBitmap(bitmap);
                imageViewPhoto.setVisibility(View.VISIBLE);
                buttonAddPhoto.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateFields() {
        boolean isValid = true;

        if (TextUtils.isEmpty(editTextAmount.getText().toString())) {
            editTextAmount.setError("사용 금액을 입력하세요.");
            isValid = false;
        }

        if (TextUtils.isEmpty(editTextUsageDetails.getText().toString())) {
            editTextUsageDetails.setError("사용 내역을 입력하세요.");
            isValid = false;
        }

        if (TextUtils.isEmpty(editTextDate.getText().toString())) {
            editTextDate.setError("사용 날짜를 선택하세요.");
            isValid = false;
        }

        return isValid;
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        editTextDate.setText(selectedDate);
                    }
                },
                year, month, day
        );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
}
