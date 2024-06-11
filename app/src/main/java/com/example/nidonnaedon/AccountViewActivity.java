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
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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

        // Clear all accounts and add initial data
        clearAccounts();
        addInitialData();

        if (intent != null) {
            String amount = intent.getStringExtra("amount");
            String date = intent.getStringExtra("date");
            String usageDetails = intent.getStringExtra("usageDetails");
            String category = intent.getStringExtra("category");
            String currency = intent.getStringExtra("currency");
            String imageUri = intent.getStringExtra("imageUri");

            if (amount != null && date != null && usageDetails != null && category != null && currency != null) {
                accountList.add(new Account(usageDetails, category, formatDate(date), amount + " " + currency, imageUri));
                adapter.notifyDataSetChanged();
                saveAccounts();
            }
        }

        listViewAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mainPageIntent = new Intent(AccountViewActivity.this, MainActivity_page7.class);
                mainPageIntent.putExtra("itemName", accountList.get(position).getUsageDetails());
                mainPageIntent.putExtra("date", accountList.get(position).getDate());
                mainPageIntent.putExtra("price", accountList.get(position).getAmount());
                mainPageIntent.putExtra("category", accountList.get(position).getCategory());
                mainPageIntent.putExtra("imageUri", accountList.get(position).getImageUri());
                startActivity(mainPageIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        FloatingActionButton buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(AccountViewActivity.this, InputViewActivity.class);
                startActivityForResult(addIntent, 1);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        FloatingActionButton buttonSearch = findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(AccountViewActivity.this, ReportActivity.class);
                searchIntent.putExtra("title", toolbarTitle.getText().toString());
                startActivity(searchIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String amount = data.getStringExtra("price");
            String date = data.getStringExtra("date");
            String usageDetails = data.getStringExtra("usageDetails");
            String category = data.getStringExtra("category");
            String currency = data.getStringExtra("currency");
            String imageUri = data.getStringExtra("imageUri");

            if (amount != null && date != null && usageDetails != null && category != null && currency != null) {
                accountList.add(new Account(usageDetails, category, formatDate(date), amount + " " + currency, imageUri));
                adapter.notifyDataSetChanged();
                saveAccounts();
            }
        }
    }

    private void loadAccounts() {
        Set<String> accountSet = sharedPreferences.getStringSet(KEY_ACCOUNTS, new HashSet<>());
        accountList.clear();
        for (String accountString : accountSet) {
            String[] parts = accountString.split(" ");
            if (parts.length >= 6) {
                String usageDetails = parts[0];
                String category = parts[1];
                String date = formatDate(parts[2]);
                String amount = parts[3];
                StringBuilder imageUri = new StringBuilder();
                for (int i = 4; i < parts.length; i++) {
                    if (i > 4) {
                        imageUri.append(" ");
                    }
                    imageUri.append(parts[i]);
                }
                accountList.add(new Account(usageDetails, category, date, amount, imageUri.toString()));
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void saveAccounts() {
        Set<String> accountSet = new HashSet<>();
        for (Account account : accountList) {
            accountSet.add(account.getUsageDetails() + " " + account.getCategory() + " " + account.getDate() + " " + account.getAmount() + " " + account.getImageUri());
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
        accountList.add(new Account("휴지", "기타", formatDate("2024-6-5"), "3000 KRW", "android.resource://com.example.nisonnaeson/drawable/ic_menu_gallery"));
        accountList.add(new Account("도넛", "식비", formatDate("2024-5-31"), "2500 KRW", "android.resource://com.example.nisonnaeson/drawable/ic_menu_gallery"));
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
            return date;
        }
    }
}
