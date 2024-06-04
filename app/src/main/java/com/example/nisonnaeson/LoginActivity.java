package com.example.nisonnaeson;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexDirection;

public class LoginActivity extends AppCompatActivity {

    private FlexboxLayout flexboxLayout;
    private TextView textView;
    private Button kakaoLoginButton, kakaoSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flexboxLayout = new FlexboxLayout(this);
        flexboxLayout.setFlexDirection(FlexDirection.COLUMN);
        flexboxLayout.setAlignItems(AlignItems.CENTER);
        flexboxLayout.setBackgroundColor(Color.parseColor("#6db33f"));

        // Initialize the text view
        textView = new TextView(this);
        textView.setText("니돈내돈");
        textView.setTextSize(100);
        textView.setTextColor(Color.WHITE);
        textView.setTypeface(ResourcesCompat.getFont(this, R.font.nanumpenscript_regular)); // Set custom font

        FlexboxLayout.LayoutParams textLayoutParams = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        textLayoutParams.setMargins(convertToPx(76), convertToPx(237), convertToPx(75), convertToPx(80));
        textView.setLayoutParams(textLayoutParams);
        flexboxLayout.addView(textView);

        // Initialize the Kakao login button
        kakaoLoginButton = new Button(this);
        kakaoLoginButton.setText("카카오톡 로그인");
        kakaoLoginButton.setTextColor(Color.BLACK);
        kakaoLoginButton.setTypeface(null, Typeface.BOLD);
        // Set background with rounded corners and yellow color
        GradientDrawable loginButtonShape = new GradientDrawable();
        loginButtonShape.setShape(GradientDrawable.RECTANGLE);
        loginButtonShape.setColor(Color.parseColor("#f9e000"));
        loginButtonShape.setCornerRadius(convertToPx(5)); // 5px border radius
        kakaoLoginButton.setBackground(loginButtonShape);
        FlexboxLayout.LayoutParams loginButtonParams = new FlexboxLayout.LayoutParams(
                convertToPx(297), convertToPx(48)); // Set width and height
        loginButtonParams.setMargins(0, convertToPx(20), 0, 0); // top margin
        kakaoLoginButton.setLayoutParams(loginButtonParams);
        flexboxLayout.addView(kakaoLoginButton);

        // Set onClickListener for login button
        kakaoLoginButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainView.class);
            startActivity(intent);
        });

        // Initialize the Kakao signup button
        kakaoSignupButton = new Button(this);
        kakaoSignupButton.setText("카카오톡 회원가입");
        kakaoSignupButton.setTextColor(Color.BLACK);
        kakaoSignupButton.setTypeface(null, Typeface.BOLD);
        // Set background with rounded corners and yellow color
        GradientDrawable signupButtonShape = new GradientDrawable();
        signupButtonShape.setShape(GradientDrawable.RECTANGLE);
        signupButtonShape.setColor(Color.parseColor("#f9e000"));
        signupButtonShape.setCornerRadius(convertToPx(5)); // 5px border radius
        kakaoSignupButton.setBackground(signupButtonShape);
        FlexboxLayout.LayoutParams signupButtonParams = new FlexboxLayout.LayoutParams(
                convertToPx(297), convertToPx(48)); // Set width and height
        signupButtonParams.setMargins(0, convertToPx(20), 0, 0); // top margin
        kakaoSignupButton.setLayoutParams(signupButtonParams);
        flexboxLayout.addView(kakaoSignupButton);

        // Set onClickListener for signup button
        kakaoSignupButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainView.class);
            startActivity(intent);
        });

        setContentView(flexboxLayout);
    }

    private int convertToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
