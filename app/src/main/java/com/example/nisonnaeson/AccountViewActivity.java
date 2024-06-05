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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AccountViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountview);

        TextView accountName = findViewById(R.id.account_name);
        TextView accountDate = findViewById(R.id.account_date);

        Intent intent = getIntent();
        String name = intent.getStringExtra("ACCOUNT_NAME");
        String date = intent.getStringExtra("ACCOUNT_DATE");

        accountName.setText(name);
        accountDate.setText(date);
    }
}
