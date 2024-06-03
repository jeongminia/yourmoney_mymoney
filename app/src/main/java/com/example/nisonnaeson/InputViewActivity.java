package com.example.nisonnaeson;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.Calendar;

public class InputViewActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SELECT_PHOTO = 1;

    private EditText editTextAmount, editTextDate, editTextPayer, editTextUsageDetails;
    private Spinner spinnerCategory, spinnerCurrency;
    private ImageButton buttonAddPhoto, buttonAddFriend;
    private ImageView imageViewPhoto;
    private Button buttonSubmit;
    private LinearLayout linearLayoutFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDate = findViewById(R.id.editTextDate);
        editTextPayer = findViewById(R.id.editTextPayer);
        editTextUsageDetails = findViewById(R.id.editTextUsageDetails);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerCurrency = findViewById(R.id.spinnerCurrency);
        buttonAddPhoto = findViewById(R.id.buttonAddPhoto);
        imageViewPhoto = findViewById(R.id.imageViewPhoto);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonAddFriend = findViewById(R.id.buttonAddFriend);
        linearLayoutFriends = findViewById(R.id.linearLayoutFriends);

        buttonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_SELECT_PHOTO);
            }
        });

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        buttonAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriendEditText();
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = editTextAmount.getText().toString();
                String date = editTextDate.getText().toString();
                String payer = editTextPayer.getText().toString();
                String usageDetails = editTextUsageDetails.getText().toString();
                String category = spinnerCategory.getSelectedItem().toString();
                String currency = spinnerCurrency.getSelectedItem().toString();

                Intent intent = new Intent(InputViewActivity.this, AccountViewActivity.class);
                intent.putExtra("amount", amount);
                intent.putExtra("date", date);
                intent.putExtra("payer", payer);
                intent.putExtra("usageDetails", usageDetails);
                intent.putExtra("category", category);
                intent.putExtra("currency", currency);

                startActivity(intent);
            }
        });
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
        datePickerDialog.show();
    }

    private void addFriendEditText() {
        EditText editTextFriend = new EditText(this);
        editTextFriend.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        editTextFriend.setHint("친구 이름");
        linearLayoutFriends.addView(editTextFriend);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
