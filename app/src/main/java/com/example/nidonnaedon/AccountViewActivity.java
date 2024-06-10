package com.example.nidonnaedon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.nisonnaeson.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.Date;

public class AccountViewActivity extends AppCompatActivity {

    private ArrayList<Account> accountList;
    private AccountAdapter adapter;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "account_data";
    private static final String KEY_ACCOUNTS = "accounts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String itemName = intent.getStringExtra("itemName");
        if (itemName != null) {
            toolbarTitle.setText(itemName);
        }

        accountList = new ArrayList<>();
        ListView listViewAccounts = findViewById(R.id.listViewAccounts);
        adapter = new AccountAdapter(this, accountList);
        listViewAccounts.setAdapter(adapter);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // 기존 데이터 초기화 및 초기 데이터 추가
        clearAccounts();
        addInitialData();

        // 데이터 전달받기
        if (intent != null) {
            String amount = intent.getStringExtra("amount");
            String date = intent.getStringExtra("date");
            String usageDetails = intent.getStringExtra("usageDetails");
            String category = intent.getStringExtra("category");
            String currency = intent.getStringExtra("currency");

            if (amount != null && date != null && usageDetails != null && category != null && currency != null) {
                accountList.add(new Account(usageDetails, category, formatDate(date), amount + " " + currency));
                adapter.notifyDataSetChanged();
                saveAccounts();
            }
        }

        listViewAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mainPageIntent = new Intent(AccountViewActivity.this, MainActivity_page7.class);
                mainPageIntent.putExtra("itemName", accountList.get(position).getUsageDetails());
                startActivity(mainPageIntent);
            }
        });

        FloatingActionButton buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(AccountViewActivity.this, InputViewActivity.class);
                startActivity(addIntent);
            }
        });

        FloatingActionButton buttonSearch = findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(AccountViewActivity.this, ReportActivity.class);
                searchIntent.putExtra("title", toolbarTitle.getText().toString()); // 타이틀 전달
                startActivity(searchIntent);
            }
        });
    }

    private void loadAccounts() {
        Set<String> accountSet = sharedPreferences.getStringSet(KEY_ACCOUNTS, new HashSet<>());
        accountList.clear();
        for (String accountString : accountSet) {
            String[] parts = accountString.split(" ");
            if (parts.length >= 5) { // Check if the length is at least 5 to prevent errors
                String usageDetails = parts[0];
                String category = parts[1];
                String date = formatDate(parts[2]); // 날짜 포맷 변경
                String amount = parts[3];
                for (int i = 4; i < parts.length; i++) {
                    amount += " " + parts[i];
                }
                accountList.add(new Account(usageDetails, category, date, amount));
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void saveAccounts() {
        Set<String> accountSet = new HashSet<>();
        for (Account account : accountList) {
            accountSet.add(account.getUsageDetails() + " " + account.getCategory() + " " + account.getDate() + " " + account.getAmount());
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(KEY_ACCOUNTS, accountSet);
        editor.apply();
    }

    private void clearAccounts() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        accountList.clear();
        adapter.notifyDataSetChanged();
    }

    private void addInitialData() {
        accountList.add(new Account("휴지", "기타", formatDate("2024-6-5"), "3000 KRW"));
        accountList.add(new Account("도넛", "식비", formatDate("2024-5-31"), "2500 KRW"));
        saveAccounts();
        adapter.notifyDataSetChanged();
    }

    private String formatDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-M-d", Locale.KOREA);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
        try {
            Date parsedDate = inputFormat.parse(date);
            return outputFormat.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return date; // 원본 형식 반환
        }
    }
}
