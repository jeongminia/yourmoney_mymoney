package com.example.nidonnaedon;

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

import com.example.nisonnaeson.R;

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

        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCurrency = parentView.getItemAtPosition(position).toString();
                // 선택된 통화에 따라 환율 적용 등의 로직을 추가합니다.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무것도 선택되지 않았을 때의 처리를 여기에 추가합니다.
            }
        });

        // 참여자 추가 버튼 클릭 시 처리
        addParticipantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String participant = participantName.getText().toString();
                if (!participant.isEmpty()) {
                    TextView textView = new TextView(MainActivity_page8.this);
                    textView.setText(participant);
                    participantList.addView(textView);
                    participantName.setText(""); // 입력 필드를 초기화합니다.
                }
            }
        });

        // 계좌 생성 버튼 클릭 시 처리
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newAccountName = accountName.getText().toString();
                String newAccountDate = date.getText().toString();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("ACCOUNT_NAME", newAccountName);
                resultIntent.putExtra("ACCOUNT_DATE", newAccountDate);
                setResult(RESULT_OK, resultIntent);
                finish(); // 이전 화면으로 돌아가기
            }
        });
    }
}
