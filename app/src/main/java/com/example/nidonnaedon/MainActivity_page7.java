package com.example.nidonnaedon;

import com.bumptech.glide.Glide;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity_page7 extends AppCompatActivity {

    private static final int REQUEST_CODE_EDIT = 1;

    private TextView itemDate, itemPrice, itemUsageDetails, itemCategory;
    private ImageView itemImageView;
    private Uri itemImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page7);
        itemDate = findViewById(R.id.item_date);
        itemPrice = findViewById(R.id.item_price);
        itemUsageDetails = findViewById(R.id.item_usage_details);
        itemCategory = findViewById(R.id.item_category);
        itemImageView = findViewById(R.id.item_image);

        // Intent에서 데이터 가져오기
        Intent intent = getIntent();
        if (intent != null) {
            String date = intent.getStringExtra("date");
            String price = intent.getStringExtra("price");
            String usageDetails = intent.getStringExtra("itemName");
            String category = intent.getStringExtra("category");
            String imageUri = intent.getStringExtra("imageUri");

            itemDate.setText(date);
            itemPrice.setText(price);
            itemUsageDetails.setText(usageDetails);
            itemCategory.setText(category);

            Log.d("MainActivity_page7", "Received image URI: " + imageUri);

            if (imageUri != null) {
                itemImageUri = Uri.parse(imageUri);
                // Glide를 사용하여 이미지 로드
                Glide.with(this)
                        .load(itemImageUri)
                        .placeholder(R.drawable.ic_menu_gallery) // 이미지가 비어있을 때 기본 이미지 로드
                        .into(itemImageView);
            } else {
                // 이미지 URI가 비어있을 때 기본 이미지 로드
                Glide.with(this)
                        .load(R.drawable.ic_menu_gallery)
                        .into(itemImageView);
            }
        }

        // 뒤로 가기 버튼 기능
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        // 수정하기 버튼 기능
        Button modifyButton = findViewById(R.id.modify_button);
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 수정하기 버튼 클릭 시 처리
                Intent intent = new Intent(MainActivity_page7.this, InputViewActivity.class);
                intent.putExtra("date", itemDate.getText().toString());
                intent.putExtra("price", itemPrice.getText().toString());
                intent.putExtra("usageDetails", itemUsageDetails.getText().toString());
                intent.putExtra("category", itemCategory.getText().toString());
                if (itemImageUri != null) {
                    intent.putExtra("imageUri", itemImageUri.toString());
                }
                startActivityForResult(intent, REQUEST_CODE_EDIT);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK && data != null) {
            String date = data.getStringExtra("date");
            String price = data.getStringExtra("price");
            String usageDetails = data.getStringExtra("usageDetails");
            String category = data.getStringExtra("category");
            String imageUri = data.getStringExtra("imageUri");
            String currency = data.getStringExtra("currency");

            itemDate.setText(date);
            itemPrice.setText(price + " " + currency);
            itemUsageDetails.setText(usageDetails);
            itemCategory.setText(category);

            Log.d("MainActivity_page7", "Updated image URI: " + imageUri);

            if (imageUri != null) {
                itemImageUri = Uri.parse(imageUri);
                // Glide를 사용하여 수정된 이미지 로드
                Glide.with(this)
                        .load(itemImageUri)
                        .placeholder(R.drawable.ic_menu_gallery) // 이미지가 비어있을 때 기본 이미지 로드
                        .into(itemImageView);
            } else {
                // 이미지 URI가 비어있을 때 기본 이미지 로드
                Glide.with(this)
                        .load(R.drawable.ic_menu_gallery)
                        .into(itemImageView);
            }
        }
    }
}
