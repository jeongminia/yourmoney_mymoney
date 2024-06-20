package com.example.nidonnaedon;

import android.content.Intent;
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

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainView extends AppCompatActivity {

    private static final int REQUEST_CODE_CREATE_ACCOUNT = 1;
    private RecyclerView recyclerView;
    private ExpenseAdapter expenseAdapter;
    private List<AccountDTO> myAccountList;
    private NidonNaedonAPI nidonNaedonAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainview);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myAccountList = new ArrayList<>();

        // Retrofit 초기화
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", Credentials.basic("user", "password")); // 설정된 사용자 이름과 비밀번호 사용
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080") // localhost 대신 10.0.2.2 사용
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        nidonNaedonAPI = retrofit.create(NidonNaedonAPI.class);

        loadMyAccounts();

        expenseAdapter = new ExpenseAdapter(myAccountList, new ExpenseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainView.this, AccountViewActivity.class);
                intent.putExtra("accountName", myAccountList.get(position).getAccountName());
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
                AccountDTO newAccount = new AccountDTO(null, accountName, accountDate, "KRW", 1.0, new ArrayList<>());
                addAccountToList(newAccount);
            }
        }
    }

    private void addAccountToList(AccountDTO account) {
        myAccountList.add(account);
        expenseAdapter.notifyDataSetChanged();
    }

    private void loadMyAccounts() {
        Call<List<AccountDTO>> call = nidonNaedonAPI.getAllAccounts();
        call.enqueue(new Callback<List<AccountDTO>>() {
            @Override
            public void onResponse(Call<List<AccountDTO>> call, Response<List<AccountDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    myAccountList.clear();
                    myAccountList.addAll(response.body());
                    expenseAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<AccountDTO>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
