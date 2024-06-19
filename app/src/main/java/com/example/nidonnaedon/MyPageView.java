package com.example.nidonnaedon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyPageView extends AppCompatActivity {

    private EditText nameEditText;
    private EditText nicknameEditText;
    private Button saveButton;
    private Button logoutButton;
    private Button exitButton;
    private NidonNaedonAPI nidonNaedonAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        // HTTP 로깅 인터셉터 추가
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 기본 인증 추가
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", Credentials.basic("user", "password"));  // Postman에서 사용한 인증 정보로 대체
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                })
                .build();

        // Retrofit 초기화
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .client(client)  // 클라이언트 추가
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        nidonNaedonAPI = retrofit.create(NidonNaedonAPI.class);

        // UI 요소 초기화
        nameEditText = findViewById(R.id.name_edit_text);
        nicknameEditText = findViewById(R.id.nickname_edit_text);
        saveButton = findViewById(R.id.save_button);
        logoutButton = findViewById(R.id.logout_button);
        exitButton = findViewById(R.id.exit_button);

        // 이전에 저장된 데이터 로드
        loadUserData();

        // 뒤로가기 버튼 클릭 시 액티비티 종료
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 저장하기 버튼 클릭 이벤트
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });

        // 로그아웃 버튼 클릭 이벤트
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // 탈퇴하기 버튼 클릭 이벤트
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitAccount();
            }
        });
    }

    // 사용자 데이터 로드
    private void loadUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String kakaoId = sharedPreferences.getString("kakao_id", null);

        if (kakaoId != null) {
            Call<UserDTO> call = nidonNaedonAPI.getUserNickname(kakaoId);
            call.enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserDTO userDTO = response.body();
                        nameEditText.setText(userDTO.getName()); // DB의 name을 nameEditText에 설정
                        nicknameEditText.setText(userDTO.getNickname());
                    } else {
                        Toast.makeText(MyPageView.this, "사용자 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    Toast.makeText(MyPageView.this, "사용자 정보를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "로그인 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            // 로그인 액티비티로 이동
            Intent intent = new Intent(MyPageView.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // 사용자 데이터 저장
    private void saveUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String kakaoId = sharedPreferences.getString("kakao_id", null);

        if (kakaoId == null) {
            Toast.makeText(this, "로그인 정보가 없습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MyPageView.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        String name = nameEditText.getText().toString();
        String nickname = nicknameEditText.getText().toString();

        // 사용자 데이터 업데이트 API 호출
        Call<Void> call = nidonNaedonAPI.updateUserData(kakaoId, name, nickname);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 키보드 숨기기
                    View view = MyPageView.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    Toast.makeText(MyPageView.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyPageView.this, "저장에 실패했습니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyPageView.this, "저장에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 로그아웃
    private void logout() {
        // 로그아웃 API 호출 (옵션)
        Call<Void> call = nidonNaedonAPI.logout();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 로그인 액티비티로 이동
                    Intent intent = new Intent(MyPageView.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyPageView.this, "로그아웃에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 탈퇴하기
    private void exitAccount() {
        // 계정 탈퇴 API 호출
        Call<Void> call = nidonNaedonAPI.exitAccount();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 사용자 데이터 삭제 (옵션)
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    // 로그인 액티비티로 이동
                    Intent intent = new Intent(MyPageView.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MyPageView.this, "탈퇴에 실패했습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
