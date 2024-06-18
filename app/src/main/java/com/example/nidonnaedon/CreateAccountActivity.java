package com.example.nidonnaedon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateAccountActivity extends AppCompatActivity {

    private NidonNaedonAPI nidonNaedonAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        EditText accountNameEditText = findViewById(R.id.account_name);
        Button saveButton = findViewById(R.id.save_button);

        // HTTP 로깅 인터셉터 추가
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", Credentials.basic("user", "password")); // Replace with your actual credentials
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080") // Use 10.0.2.2 for Android emulator to access localhost
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        nidonNaedonAPI = retrofit.create(NidonNaedonAPI.class);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountName = accountNameEditText.getText().toString();
                createAccount(accountName, "test_user_id"); // Replace "test_user_id" with the actual user ID
            }
        });
    }

    private void createAccount(String accountName, String userId) {
        AccountDTO accountDTO = new AccountDTO(null, accountName, null, "KRW", 1.0, new ArrayList<>());
        Call<AccountDTO> call = nidonNaedonAPI.createAccount(accountDTO, userId);
        call.enqueue(new Callback<AccountDTO>() {
            @Override
            public void onResponse(Call<AccountDTO> call, Response<AccountDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("ACCOUNT_NAME", response.body().getAccountName());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<AccountDTO> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
