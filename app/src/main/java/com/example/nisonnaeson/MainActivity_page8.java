package com.example.nisonnaeson;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity_page8 extends AppCompatActivity {

    private EditText accountName;
    private EditText date;
    private Spinner currencySpinner;
    private Button applyExchangeRateButton;
    private EditText participantName;
    private Button addParticipantButton;
    private Button createAccountButton;
    private LinearLayout participantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page8);

        accountName = findViewById(R.id.account_name);
        date = findViewById(R.id.date);
        currencySpinner = findViewById(R.id.currency_spinner);
        applyExchangeRateButton = findViewById(R.id.apply_exchange_rate_button);
        participantName = findViewById(R.id.participant_name);
        addParticipantButton = findViewById(R.id.add_participant_button);
        createAccountButton = findViewById(R.id.create_account_button);
        participantList = findViewById(R.id.participant_list);

        // 뒤로 가기 버튼 설정
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 이전 화면으로 돌아가기
            }
        });

        // 통화 선택 스피너 설정
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(adapter);

        // Spinner 항목 선택 리스너 설정
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCurrency = (String) parent.getItemAtPosition(position);
                if (selectedCurrency.startsWith("KRW")) {
                    applyExchangeRateButton.setVisibility(View.GONE);
                } else {
                    applyExchangeRateButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // 참가자 추가 버튼 클릭 리스너 설정
        addParticipantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String participant = participantName.getText().toString();
                if (!participant.isEmpty()) {
                    // 참가자 추가 로직
                    TextView textView = new TextView(MainActivity_page8.this);
                    textView.setText(participant);
                    textView.setTextSize(16);
                    participantList.addView(textView);
                    participantName.setText("");
                } else {
                    // 참가자 이름 입력 요청 로직
                }
            }
        });

        // 가계부 생성 버튼 클릭 리스너 설정
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newAccountName = accountName.getText().toString();
                String newAccountDate = date.getText().toString();

                Intent intent = new Intent(MainActivity_page8.this, AccountViewActivity.class);
                intent.putExtra("ACCOUNT_NAME", newAccountName);
                intent.putExtra("ACCOUNT_DATE", newAccountDate);
                startActivity(intent);
            }
        });
    }
}
