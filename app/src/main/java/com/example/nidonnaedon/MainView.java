package com.example.nidonnaedon;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainView extends AppCompatActivity {

    private static final int REQUEST_CODE_CREATE_ACCOUNT = 1;
    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;
    private List<ExpenditureDetailsDTO> myExpenseList;
    private NidonNaedonAPI nidonNaedonAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainview);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myExpenseList = new ArrayList<>();

        // Retrofit 초기화
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        nidonNaedonAPI = retrofit.create(NidonNaedonAPI.class);

        loadMyExpenses();

        expenseAdapter = new ExpenseAdapter(myExpenseList, new ExpenseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainView.this, AccountViewActivity.class);
                intent.putExtra("itemName", myExpenseList.get(position).getExpenditureName());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        }, this);

        recyclerView.setAdapter(expenseAdapter);

        ImageView profileIcon = findViewById(R.id.profile_image);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainView.this, MyPageView.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainView.this, MainActivity_page8.class);
                startActivityForResult(intent, REQUEST_CODE_CREATE_ACCOUNT);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CREATE_ACCOUNT && resultCode == RESULT_OK && data != null) {
            String accountName = data.getStringExtra("ACCOUNT_NAME");
            String accountDate = data.getStringExtra("ACCOUNT_DATE");
            if (accountName != null && accountDate != null) {
                ExpenditureDetailsDTO newAccount = new ExpenditureDetailsDTO(0, "", accountName, 0, "KRW", 1.0, new ArrayList<>(), accountDate, null, "", "기타");
                addAccountToList(newAccount);
            }
        }
    }

    private void addAccountToList(ExpenditureDetailsDTO account) {
        myExpenseList.add(account);
        expenseAdapter.notifyDataSetChanged();
    }

    private void loadMyExpenses() {
        // 내 지출 내역 API 호출 로직 구현
        Call<List<ExpenditureDetailsDTO>> call = nidonNaedonAPI.getAllExpenditureDetailsByAccountId("my_account_id");
        call.enqueue(new Callback<List<ExpenditureDetailsDTO>>() {
            @Override
            public void onResponse(Call<List<ExpenditureDetailsDTO>> call, Response<List<ExpenditureDetailsDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    myExpenseList.clear();
                    myExpenseList.addAll(response.body());
                    expenseAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ExpenditureDetailsDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
