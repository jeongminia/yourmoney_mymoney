package com.example.nidonnaedon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountViewActivity extends AppCompatActivity {

    private ArrayList<ExpenditureDetailsDTO> expenditureList;
    private AccountAdapter adapter;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "account_data";
    private static final String KEY_ACCOUNTS = "accounts";
    private Retrofit retrofit;
    private NidonNaedonAPI nidonNaedonAPI;

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

        ImageButton buttonOptions = findViewById(R.id.buttonOptions);
        buttonOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOptionsMenu(v);
            }
        });

        Intent intent = getIntent();
        String itemName = intent.getStringExtra("itemName");
        if (itemName != null) {
            toolbarTitle.setText(itemName);
        }

        expenditureList = new ArrayList<>();
        ListView listViewAccounts = findViewById(R.id.listViewAccounts);
        adapter = new AccountAdapter(this, expenditureList);
        listViewAccounts.setAdapter(adapter);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        nidonNaedonAPI = retrofit.create(NidonNaedonAPI.class);

        loadExpenditures();

        listViewAccounts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mainPageIntent = new Intent(AccountViewActivity.this, MainActivity_page7.class);
                mainPageIntent.putExtra("itemName", expenditureList.get(position).getExpenditureName());
                mainPageIntent.putExtra("date", expenditureList.get(position).getExpenditureDate());
                mainPageIntent.putExtra("price", expenditureList.get(position).getExpenditureAmount() + " " + expenditureList.get(position).getExpenditureCurrency());
                mainPageIntent.putExtra("category", expenditureList.get(position).getExpenditureCategory());
                mainPageIntent.putExtra("imageUri", expenditureList.get(position).getExpenditurePhoto());
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
                ExpenditureDetailsDTO expenditure = new ExpenditureDetailsDTO(0, "", usageDetails, Double.parseDouble(amount),
                        currency, 1.0, new ArrayList<>(), formatDate(date), imageUri, "", category);
                createExpenditure(expenditure);
            }
        }
    }

    private void showOptionsMenu(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.menu_accountview, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return onOptionsItemSelected(item);
            }
        });
        popup.show();
    }

    private void loadExpenditures() {
        Call<List<ExpenditureDetailsDTO>> call = nidonNaedonAPI.getAllExpenditureDetailsByAccountId("your_account_id");
        call.enqueue(new Callback<List<ExpenditureDetailsDTO>>() {
            @Override
            public void onResponse(Call<List<ExpenditureDetailsDTO>> call, Response<List<ExpenditureDetailsDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    expenditureList.clear();
                    expenditureList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ExpenditureDetailsDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void createExpenditure(ExpenditureDetailsDTO expenditure) {
        Call<ExpenditureDetailsDTO> call = nidonNaedonAPI.createExpenditure(expenditure);
        call.enqueue(new Callback<ExpenditureDetailsDTO>() {
            @Override
            public void onResponse(Call<ExpenditureDetailsDTO> call, Response<ExpenditureDetailsDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    expenditureList.add(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ExpenditureDetailsDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });
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
