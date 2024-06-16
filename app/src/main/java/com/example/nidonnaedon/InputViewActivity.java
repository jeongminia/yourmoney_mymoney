package com.example.nidonnaedon;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import com.example.nidonnaedon.ExpenditureDetailsDTO;
import com.example.nidonnaedon.NidonNaedonAPI;
import com.example.nidonnaedon.R;

import okhttp3.OkHttpClient;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class InputViewActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_PHOTO = 1;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 2;
    private static final String TAG = "InputViewActivity";

    private EditText editTextAmount, editTextDate, editTextUsageDetails;
    private Spinner spinnerCategory, spinnerCurrency;
    private ImageView buttonAddPhoto, imageViewPhoto, calendarIcon, backButton;
    private Button buttonSubmit;
    private CheckBox checkboxAddFriend, checkboxPayer, checkboxGwakJiwon, checkboxYooJaewon;
    private LinearLayout friendList, photoContainer;
    private String selectedImageUri;

    private NidonNaedonAPI nidonNaedonAPI;

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

        // 권한 요청
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_EXTERNAL_STORAGE);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        // Retrofit 클라이언트 설정
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .header("Authorization", Credentials.basic("user", "password")) // 여기에 사용자 이름과 비밀번호를 입력
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080") // baseUrl이 맞는지 확인하세요.
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        nidonNaedonAPI = retrofit.create(NidonNaedonAPI.class);

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

        View.OnClickListener photoClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        };
        buttonAddPhoto.setOnClickListener(photoClickListener);
        imageViewPhoto.setOnClickListener(photoClickListener);
        photoContainer.setOnClickListener(photoClickListener);

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

                    ExpenditureDetailsDTO expenditure = new ExpenditureDetailsDTO(
                            0, "", usageDetails, Double.parseDouble(amount), currency, 1.0,
                            new ArrayList<>(), date, selectedImageUri, "", category
                    );

                    Log.d(TAG, "createExpenditure 호출 전: " + expenditure.toString());
                    createExpenditure(expenditure);
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
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_PHOTO && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageViewPhoto.setImageURI(selectedImage);
            imageViewPhoto.setVisibility(View.VISIBLE);
            buttonAddPhoto.setVisibility(View.GONE);
            selectedImageUri = selectedImage.toString();
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
                        editTextDate.setTextColor(getResources().getColor(android.R.color.black));
                    }
                },
                year, month, day
        );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void createExpenditure(ExpenditureDetailsDTO expenditure) {
        Call<ExpenditureDetailsDTO> call = nidonNaedonAPI.createExpenditure(expenditure);
        call.enqueue(new Callback<ExpenditureDetailsDTO>() {
            @Override
            public void onResponse(Call<ExpenditureDetailsDTO> call, retrofit2.Response<ExpenditureDetailsDTO> response) {
                Log.d(TAG, "onResponse: 호출 성공");
                Log.d(TAG, "응답 코드: " + response.code());
                Log.d(TAG, "응답 메시지: " + response.message());
                if (response.isSuccessful() && response.body() != null) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("date", response.body().getExpenditureDate());
                    resultIntent.putExtra("price", String.valueOf(response.body().getExpenditureAmount()));
                    resultIntent.putExtra("usageDetails", response.body().getExpenditureName());
                    resultIntent.putExtra("category", response.body().getExpenditureCategory());
                    resultIntent.putExtra("currency", response.body().getExpenditureCurrency());
                    if (response.body().getExpenditurePhoto() != null) {
                        resultIntent.putExtra("imageUri", response.body().getExpenditurePhoto());
                    }
                    setResult(RESULT_OK, resultIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Log.d(TAG, "onResponse: 호출 실패");
                    Log.d(TAG, "응답 본문: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ExpenditureDetailsDTO> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.printStackTrace();
                Toast.makeText(InputViewActivity.this, "서버와의 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우 처리
                if (selectedImageUri != null) {
                    imageViewPhoto.setImageURI(Uri.parse(selectedImageUri));
                    imageViewPhoto.setVisibility(View.VISIBLE);
                    buttonAddPhoto.setVisibility(View.GONE);
                }
            } else {
                // 권한이 거부된 경우 처리
            }
        }
    }
}
