package com.example.nisonnaeson;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity_page8 extends AppCompatActivity {

    private EditText accountName;
    private EditText date;
    private Spinner currencySpinner;
    private Button applyExchangeRateButton;
    private EditText participantName;
    private Button addParticipantButton;
    private Button createAccountButton;

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
                    Toast.makeText(MainActivity_page8.this, participant + " 추가됨", Toast.LENGTH_SHORT).show();
                    participantName.setText("");
                } else {
                    Toast.makeText(MainActivity_page8.this, "참가자 이름을 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 가계부 생성 버튼 클릭 리스너 설정
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountName.getText().toString();
                String selectedDate = date.getText().toString();
                String currency = currencySpinner.getSelectedItem().toString();

                if (!account.isEmpty() && !selectedDate.isEmpty() && !currency.isEmpty()) {
                    Toast.makeText(MainActivity_page8.this, "가계부 생성됨: " + account, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity_page8.this, "모든 필드를 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
