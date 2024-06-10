package com.example.nidonnaedon;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.nisonnaeson.R;

import java.io.IOException;
import java.util.Calendar;

public class InputViewActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_PHOTO = 1;

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

        // 권한 요청
        requestPermissions();

        // UI 요소 초기화
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
        backButton = findViewById(R.id.back_button);  // Back button 추가

        // 뒤로가기 버튼 클릭 이벤트 추가
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // 현재 액티비티를 종료하여 뒤로가기 동작을 수행합니다.
            }
        });

        // 통화 선택 스피너 설정
        ArrayAdapter<CharSequence> currencyAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.currency_array)) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setText(formatCurrencyTitle(textView.getText().toString()));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setText(formatCurrencyDescription(textView.getText().toString()));
                return view;
            }

            private String formatCurrencyTitle(String original) {
                if (original.contains("-")) {
                    String[] parts = original.split("-");
                    return parts[0].trim();
                }
                return original;
            }

            private String formatCurrencyDescription(String original) {
                return original; // 드롭다운에서는 전체 텍스트 표시
            }
        };

        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(currencyAdapter);

        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCurrency = parentView.getItemAtPosition(position).toString();
                // 선택된 통화에 따라 환율 적용 등의 로직을 추가합니다.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무것도 선택되지 않았을 때의 처리를 여기에 추가합니다.
            }
        });

        // 카테고리 스피너 설정
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        View.OnClickListener photoClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        };
        buttonAddPhoto.setOnClickListener(photoClickListener);
        photoContainer.setOnClickListener(photoClickListener);

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
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
                    String currency = spinnerCurrency.getSelectedItem().toString();

                    StringBuilder friends = new StringBuilder();
                    if (checkboxPayer.isChecked()) {
                        friends.append("신호연 ");
                    }
                    if (checkboxGwakJiwon.isChecked()) {
                        friends.append("곽지원 ");
                    }
                    if (checkboxYooJaewon.isChecked()) {
                        friends.append("유재원 ");
                    }

                    Intent intent = new Intent(InputViewActivity.this, AccountViewActivity.class);
                    intent.putExtra("amount", amount);
                    intent.putExtra("date", date);
                    intent.putExtra("usageDetails", usageDetails);
                    intent.putExtra("category", category);
                    intent.putExtra("currency", currency);
                    intent.putExtra("friends", friends.toString().trim());

                    startActivity(intent);
                }
            }
        });

        // Set the default date to today
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        editTextDate.setText(year + "-" + (month + 1) + "-" + day);
    }

    // 권한 요청 메서드
    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    100);
        }
    }

    // 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // 추가된 부분
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우
            } else {
                // 권한이 거부된 경우
                Toast.makeText(this, "권한이 거부되었습니다. 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 사진 선택
    private void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imageViewPhoto.setImageBitmap(bitmap);
                imageViewPhoto.setVisibility(View.VISIBLE);
                buttonAddPhoto.setVisibility(View.GONE); // Hide the camera icon when a photo is selected
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
