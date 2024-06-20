package com.example.nidonnaedon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Locale;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;

public class AccountViewActivity extends AppCompatActivity {

    private ArrayList<ExpenditureDetailsDTO> expenditureList;
    private AccountAdapter adapter;
    private Retrofit retrofit;
    private NidonNaedonAPI nidonNaedonAPI;
    private static final String TAG = "AccountViewActivity";
    private String accountId;  // 추가된 변수

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
        String itemName = intent.getStringExtra("accountName");
        accountId = intent.getStringExtra("accountId");  // 추가된 부분
        if (itemName != null) {
            toolbarTitle.setText(itemName);
        }

        expenditureList = new ArrayList<>();
        ListView listViewAccounts = findViewById(R.id.listViewAccounts);
        adapter = new AccountAdapter(this, expenditureList);
        listViewAccounts.setAdapter(adapter);

        // Retrofit 클라이언트 설정
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .header("Authorization", Credentials.basic("user", "password")) // 사용자 이름과 비밀번호 입력
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080") // baseUrl이 맞는지 확인하세요.
                .client(client)
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
                if (item.getItemId() == R.id.action_delete_account) {
                    deleteAccount();
                    return true;
                }
                return false;
            }
        });
        popup.show();
    }

    private void loadExpenditures() {
        Call<List<ExpenditureDetailsDTO>> call = nidonNaedonAPI.getAllExpenditureDetailsByAccountId(accountId);  // 수정된 부분
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
                } else {
                    Log.d(TAG, "createExpenditure onResponse: 서버 응답 실패");
                    Log.d(TAG, "응답 코드: " + response.code());
                    Log.d(TAG, "응답 메시지: " + response.message());
                    try {
                        Log.d(TAG, "응답 본문: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ExpenditureDetailsDTO> call, Throwable t) {
                Log.d(TAG, "createExpenditure onFailure: 서버 통신 실패");
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

    private void deleteAccount() {
        Call<Void> call = nidonNaedonAPI.deleteAccount(accountId);  // 수정된 부분
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "deleteAccount onResponse: 계정 삭제 성공");
                    finish();  // 현재 액티비티 종료
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Log.d(TAG, "deleteAccount onResponse: 서버 응답 실패");
                    Log.d(TAG, "응답 코드: " + response.code());
                    Log.d(TAG, "응답 메시지: " + response.message());
                    try {
                        Log.d(TAG, "응답 본문: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, "deleteAccount onFailure: 서버 통신 실패");
                t.printStackTrace();
            }
        });
    }
}
