package com.example.nidonnaedon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        nidonNaedonAPI = retrofit.create(NidonNaedonAPI.class);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountName = accountNameEditText.getText().toString();
                createAccount(accountName);
            }
        });
    }

    private void createAccount(String accountName) {
        AccountDTO accountDTO = new AccountDTO(null, accountName, null, "KRW", 1.0, new ArrayList<>());
        Call<AccountDTO> call = nidonNaedonAPI.createAccount(accountDTO);
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
