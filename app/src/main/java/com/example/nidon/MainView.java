package com.example.nidon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainView extends AppCompatActivity {

    private static final int REQUEST_CODE_CREATE_ACCOUNT = 1;
    private LinearLayout myExpenseLayout;
    private LinearLayout sharedExpenseLayout;
    private boolean isMyExpense = true;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainview);

        myExpenseLayout = findViewById(R.id.my_expense_layout);
        sharedExpenseLayout = findViewById(R.id.shared_expense_layout);

        // 상단 좌측 사람 아이콘
        ImageView profileIcon = findViewById(R.id.profile_image);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainView.this, MyPageView.class);
                startActivity(intent);
            }
        });

        // "나의 가계부" 버튼
        Button myExpenseButton = findViewById(R.id.my_expense_button);
        myExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMyExpense = true;
                myExpenseLayout.setVisibility(View.VISIBLE);
                sharedExpenseLayout.setVisibility(View.GONE);
            }
        });

        // "공유 가계부" 버튼
        Button sharedExpenseButton = findViewById(R.id.shared_expense_button);
        sharedExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMyExpense = false;
                myExpenseLayout.setVisibility(View.GONE);
                sharedExpenseLayout.setVisibility(View.VISIBLE);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainView.this, InputView.class);
                startActivityForResult(intent, REQUEST_CODE_CREATE_ACCOUNT);
            }
        });

        loadFragment(new MyExpenseFragment());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CREATE_ACCOUNT && resultCode == RESULT_OK && data != null) {
            String accountName = data.getStringExtra("ACCOUNT_NAME");
            if (accountName != null && !accountName.isEmpty()) {
                addAccountToList(accountName, isMyExpense);
            }
        }
    }

    private void addAccountToList(String accountName, boolean isMyExpense) {
        TextView newAccountTextView = new TextView(this);
        newAccountTextView.setText(accountName);
        newAccountTextView.setTextSize(18);
        newAccountTextView.setPadding(8, 8, 8, 8);
        newAccountTextView.setBackgroundResource(android.R.drawable.list_selector_background);

        if (isMyExpense) {
            myExpenseLayout.addView(newAccountTextView);
        } else {
            sharedExpenseLayout.addView(newAccountTextView);
        }
    }

    public void onMyExpenseClick(View view) {
        loadFragment(new MyExpenseFragment());
    }

    public void onSharedExpenseClick(View view) {
        loadFragment(new SharedExpenseFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
