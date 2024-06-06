package com.example.nisonnaeson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainView extends AppCompatActivity {

    private static final int REQUEST_CODE_CREATE_ACCOUNT = 1;
    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;
    private List<String> myExpenseList;
    private List<String> sharedExpenseList;
    private boolean isMyExpense = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainview);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myExpenseList = new ArrayList<>();
        sharedExpenseList = new ArrayList<>();

        // 예제 데이터를 추가합니다.
        myExpenseList.add("독일 여행 2024.3.31~2024.4.3");
        myExpenseList.add("데이트 통장 2024.3.31~2024.4.3");

        sharedExpenseList.add("지원이 서프라이즈 2024.3.31~2024.4.3");
        sharedExpenseList.add("저녁 산책 모임 2024.3.31~2024.4.3");
        sharedExpenseList.add("어버이날 선물 2024.3.31~2024.4.3");

        expenseAdapter = new ExpenseAdapter(myExpenseList, new ExpenseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainView.this, AccountViewActivity.class);
                startActivity(intent);
            }
        }, this);

        recyclerView.setAdapter(expenseAdapter);

        ImageView profileIcon = findViewById(R.id.profile_image);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainView.this, MyPageView.class);
                startActivity(intent);
            }
        });

        Button myExpenseButton = findViewById(R.id.my_expense_button);
        myExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMyExpense = true;
                updateExpenseList();
            }
        });

        Button sharedExpenseButton = findViewById(R.id.shared_expense_button);
        sharedExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMyExpense = false;
                updateExpenseList();
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainView.this, MainActivity_page8.class);
                startActivityForResult(intent, REQUEST_CODE_CREATE_ACCOUNT);
            }
        });
    }
    private void updateExpenseList() {
        if (isMyExpense) {
            expenseAdapter = new ExpenseAdapter(myExpenseList, new ExpenseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(MainView.this, AccountViewActivity.class);
                    startActivity(intent);
                }
            }, this);
        } else {
            expenseAdapter = new ExpenseAdapter(sharedExpenseList, new ExpenseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(MainView.this, AccountViewActivity.class);
                    startActivity(intent);
                }
            }, this);
        }

        recyclerView.setAdapter(expenseAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CREATE_ACCOUNT && resultCode == RESULT_OK && data != null) {
            String accountName = data.getStringExtra("ACCOUNT_NAME");
            String accountDate = data.getStringExtra("ACCOUNT_DATE");
            if (accountName != null && accountDate != null) {
                String newAccount = accountName + " " + accountDate;
                addAccountToList(newAccount, isMyExpense);
            }
        }
    }

    private void addAccountToList(String accountName, boolean isMyExpense) {
        if (isMyExpense) {
            myExpenseList.add(accountName);
        } else {
            sharedExpenseList.add(accountName);
        }
        updateExpenseList();
    }
}
