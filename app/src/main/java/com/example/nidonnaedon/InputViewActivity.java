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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.nisonnaeson.R;

import java.io.IOException;
import java.util.Calendar;

public class InputViewActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_PHOTO = 1;
    private static final int REQUEST_CODE_PERMISSIONS = 2;

    private EditText editTextAmount, editTextDate, editTextUsageDetails;
    private Spinner spinnerCategory, spinnerCurrency;
    private ImageView buttonAddPhoto, imageViewPhoto, calendarIcon, backButton;
    private Button buttonSubmit;
    private CheckBox checkboxAddFriend, checkboxPayer, checkboxGwakJiwon, checkboxYooJaewon;
    private LinearLayout friendList, photoContainer;
    private String selectedImageUri; // 선택된 이미지 URI를 저장할 변수

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
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        // 받아온 데이터 설정
        Intent intent = getIntent();
        if (intent != null) {
            String date = intent.getStringExtra("date");
            String price = intent.getStringExtra("price");
            String usageDetails = intent.getStringExtra("usageDetails");
            String category = intent.getStringExtra("category");
            String imageUri = intent.getStringExtra("imageUri");

            editTextDate.setText(date);
            editTextAmount.setText(price != null ? price.split(" ")[0] : "");
            editTextUsageDetails.setText(usageDetails);
            if (imageUri != null) {
                Uri uri = Uri.parse(imageUri);
                imageViewPhoto.setImageURI(uri);
                imageViewPhoto.setVisibility(View.VISIBLE);
                buttonAddPhoto.setVisibility(View.GONE);
                selectedImageUri = imageUri; // 이미지 URI 설정
            } else {
                imageViewPhoto.setVisibility(View.GONE);
                buttonAddPhoto.setVisibility(View.VISIBLE);
            }
        }

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> currencyAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.currency_array)) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    TextView textView = (TextView) view;
                    String currentText = textView.getText().toString();
                    if (currentText.contains("-")) {
                        currentText = currentText.substring(0, currentText.indexOf("-")).trim();
                    }
                    textView.setText(currentText);
                }
                return view;
            }
        };
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(currencyAdapter);

        spinnerCurrency.setSelection(currencyAdapter.getPosition("KRW"));

        // 버튼과 사진 이미지뷰에 클릭 리스너를 추가합니다.
        View.OnClickListener photoClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsAndSelectPhoto();
            }
        };
        buttonAddPhoto.setOnClickListener(photoClickListener);
        imageViewPhoto.setOnClickListener(photoClickListener);
        photoContainer.setOnClickListener(photoClickListener); // photoContainer 클릭 리스너 추가

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
                    String category = spinnerCategory.getSelectedItem() != null ? spinnerCategory.getSelectedItem().toString() : "기타";
                    String currency = spinnerCurrency.getSelectedItem() != null ? spinnerCurrency.getSelectedItem().toString() : "";

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("date", date);
                    resultIntent.putExtra("price", amount);
                    resultIntent.putExtra("usageDetails", usageDetails);
                    resultIntent.putExtra("category", category);
                    resultIntent.putExtra("currency", currency);

                    // selectedImageUri가 null인지 확인하고, null이 아닌 경우에만 추가합니다.
                    if (selectedImageUri != null) {
                        resultIntent.putExtra("imageUri", selectedImageUri);
                    }

                    setResult(RESULT_OK, resultIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        editTextDate.setText(year + "-" + (month + 1) + "-" + day);
    }

    private void checkPermissionsAndSelectPhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);
        } else {
            selectPhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                selectPhoto();
            } else {
                Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

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
                buttonAddPhoto.setVisibility(View.GONE);

                // 이미지 URI를 저장합니다.
                selectedImageUri = selectedImage.toString();
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
