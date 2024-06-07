package com.example.nidonnaedon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nisonnaeson.R;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);

        EditText accountNameEditText = findViewById(R.id.account_name);
        Button saveButton = findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountName = accountNameEditText.getText().toString();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ACCOUNT_NAME", accountName);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
