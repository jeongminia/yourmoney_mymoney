package com.example.nisonnaeson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AccountViewActivity extends AppCompatActivity {

    private ArrayList<String> accountList;
    private ArrayAdapter<String> adapter;
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
        ListView listViewAccounts = findViewById(R.id.listViewAccounts);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, accountList);
        listViewAccounts.setAdapter(adapter);

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
                Intent intent = new Intent(AccountViewActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });

        // ListView item click listener
        listViewAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Only proceed if the first item is clicked
                if (position == 0) {
                    Intent mainPageIntent = new Intent(AccountViewActivity.this, MainActivity_page7.class);
                    startActivity(mainPageIntent);
                }
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