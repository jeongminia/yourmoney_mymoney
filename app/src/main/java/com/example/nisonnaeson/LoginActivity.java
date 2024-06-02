package com.example.nisonnaeson;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
        flexboxLayout.setBackgroundColor(Color.parseColor("#82A33E"));

        // Initialize the text view
        textView = new TextView(this);
        textView.setText("니돈내돈");
        textView.setTextSize(24);
        textView.setTextColor(Color.WHITE);

        FlexboxLayout.LayoutParams textLayoutParams = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        textLayoutParams.setMargins(convertToPx(76), convertToPx(237), convertToPx(75), convertToPx(80));
        textView.setLayoutParams(textLayoutParams);
        flexboxLayout.addView(textView);

        // Initialize the Kakao login button
        kakaoLoginButton = new Button(this);
        kakaoLoginButton.setText("카카오톡 로그인");
        kakaoLoginButton.setBackgroundColor(Color.YELLOW);
        kakaoLoginButton.setTextColor(Color.BLACK);
        FlexboxLayout.LayoutParams loginButtonParams = new FlexboxLayout.LayoutParams(
                convertToPx(300), convertToPx(100)); // Set width and height
        loginButtonParams.setMargins(0, convertToPx(20), 0, 0); // top margin
        kakaoLoginButton.setLayoutParams(loginButtonParams);
        flexboxLayout.addView(kakaoLoginButton);

        // Initialize the Kakao signup button
        kakaoSignupButton = new Button(this);
        kakaoSignupButton.setText("카카오톡 회원가입");
        kakaoSignupButton.setBackgroundColor(Color.YELLOW);
        kakaoSignupButton.setTextColor(Color.BLACK);
        FlexboxLayout.LayoutParams signupButtonParams = new FlexboxLayout.LayoutParams(
                convertToPx(300), convertToPx(100)); // Set width and height
        signupButtonParams.setMargins(0, convertToPx(20), 0, 0); // top margin
        kakaoSignupButton.setLayoutParams(signupButtonParams);
        flexboxLayout.addView(kakaoSignupButton);

        setContentView(flexboxLayout);
    }

    private int convertToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
