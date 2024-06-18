package com.example.nidonnaedon;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NidonNaedonAPI {

    @GET("/expenditures/account/{accountId}")
    Call<List<ExpenditureDetailsDTO>> getAllExpenditureDetailsByAccountId(@Path("accountId") String accountId);

    @POST("/expenditures")
    Call<ExpenditureDetailsDTO> createExpenditure(@Body ExpenditureDetailsDTO expenditure);

    @POST("/accounts")
    Call<AccountDTO> createAccount(@Body AccountDTO account);

    @POST("/auth/login")
    Call<String> login(@Query("username") String username, @Query("password") String password);

    @POST("/auth/signup")
    Call<String> signUp(@Query("username") String username, @Query("password") String password);

    @PUT("/user")
    Call<Void> updateUserData(@Query("name") String name, @Query("nickname") String nickname);

    @POST("/auth/logout")
    Call<Void> logout();

    @DELETE("/account")
    Call<Void> exitAccount();

    @GET("/barChartData")
    Call<List<BarEntry>> getBarChartData();

    @GET("/pieChartData")
    Call<List<PieEntry>> getPieChartData();

    @GET("/endpoint")
    Call<ResponseBody> getMessage();


    @GET("/validateUser")
    Call<Boolean> validateUser(@Query("kakaoId") String kakaoId);
}
