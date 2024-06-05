package com.example.nisonnaeson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AccountViewActivity extends AppCompatActivity {

    private ArrayList<String> accountList;
    private AccountAdapter adapter;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "account_data";
    private static final String KEY_ACCOUNTS = "accounts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        accountList = new ArrayList<>();
        RecyclerView recyclerViewAccounts = findViewById(R.id.recyclerViewAccounts);
        recyclerViewAccounts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AccountAdapter(accountList);
        recyclerViewAccounts.setAdapter(adapter);

        // Divider 추가
        recyclerViewAccounts.addItemDecoration(new DividerItemDecoration(this));

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        loadAccounts();

        // 데이터 전달받기
        Intent intent = getIntent();
        if (intent != null) {
            String amount = intent.getStringExtra("amount");
            String date = intent.getStringExtra("date");
            String usageDetails = intent.getStringExtra("usageDetails");
            String category = intent.getStringExtra("category");
            String currency = intent.getStringExtra("currency");

            if (amount != null && date != null && usageDetails != null && category != null && currency != null) {
                String displayText = usageDetails + " " + category + " " + date + " " + amount + " " + currency;
                accountList.add(displayText);
                adapter.notifyDataSetChanged();
                saveAccounts();
            }
        }

        ImageButton buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(AccountViewActivity.this, InputViewActivity.class);
                startActivity(addIntent);
            }
        });

        ImageButton buttonSearch = findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 검색 기능 구현 예정
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_accountview, menu);
        return true;
    }


    private void loadAccounts() {
        Set<String> accountSet = sharedPreferences.getStringSet(KEY_ACCOUNTS, new HashSet<>());
        accountList.clear();
        accountList.addAll(accountSet);
        adapter.notifyDataSetChanged();
    }

    private void saveAccounts() {
        Set<String> accountSet = new HashSet<>(accountList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_ACCOUNTS, accountSet);
        editor.apply();
    }
}