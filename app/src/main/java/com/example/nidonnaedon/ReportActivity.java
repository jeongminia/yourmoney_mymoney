package com.example.nidonnaedon;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReportActivity extends AppCompatActivity {

    private PieChart pieChart;
    private HorizontalBarChart barChart;
    private NidonNaedonAPI nidonNaedonAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrofit 초기화
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        nidonNaedonAPI = retrofit.create(NidonNaedonAPI.class);

        // Parent RelativeLayout
        RelativeLayout parentLayout = new RelativeLayout(this);
        parentLayout.setBackgroundColor(Color.parseColor("#F5F5F5")); // 배경 색 흰색으로 설정
        parentLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        // Toolbar
        RelativeLayout toolbarLayout = new RelativeLayout(this);
        toolbarLayout.setId(View.generateViewId());
        RelativeLayout.LayoutParams toolbarParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, convertToPx(58)); // Use int for conversion
        toolbarLayout.setLayoutParams(toolbarParams);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        ImageView backButton = new ImageView(this);
        backButton.setImageResource(R.drawable.ic_back); // replace with your back icon
        RelativeLayout.LayoutParams backButtonParams = new RelativeLayout.LayoutParams(
                convertToPx(30), convertToPx(30)); // Use int for conversion
        backButtonParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        backButtonParams.setMargins(convertToPx(14), convertToPx(14), convertToPx(14), convertToPx(14)); // Use int for conversion
        backButton.setLayoutParams(backButtonParams);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes the current activity and returns to the previous one
            }
        });
        toolbarLayout.addView(backButton);

        TextView titleView = new TextView(this);
        titleView.setId(View.generateViewId());
        Intent intent = getIntent();
        String title = intent.getStringExtra("title"); // AccountViewActivity에서 전달된 타이틀
        if (title != null) {
            titleView.setText(title);
        }
        titleView.setTextSize(40); // Use float for text size
        titleView.setTextColor(Color.WHITE);
        titleView.setTypeface(ResourcesCompat.getFont(this, R.font.nanumpenscript_regular)); // Set custom font for title
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        titleView.setLayoutParams(titleParams);
        toolbarLayout.addView(titleView);

        parentLayout.addView(toolbarLayout);

        // Horizontal bar chart
        barChart = new HorizontalBarChart(this);
        barChart.setId(View.generateViewId());
        RelativeLayout.LayoutParams barChartParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, convertToPx(200)); // Use int for conversion
        barChartParams.addRule(RelativeLayout.BELOW, toolbarLayout.getId());
        barChartParams.setMargins(0, convertToPx(20), 0, 0); // Use int for conversion
        barChart.setLayoutParams(barChartParams);
        barChart.setTouchEnabled(false); // 터치 이벤트 비활성화
        parentLayout.addView(barChart);

        // Pie chart
        pieChart = new PieChart(this);
        pieChart.setId(View.generateViewId());
        RelativeLayout.LayoutParams pieChartParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, convertToPx(300)); // Use int for conversion
        pieChartParams.addRule(RelativeLayout.BELOW, barChart.getId());
        pieChartParams.setMargins(0, convertToPx(20), 0, convertToPx(20)); // Use int for conversion
        pieChart.setLayoutParams(pieChartParams);
        pieChart.setTouchEnabled(false); // 터치 이벤트 비활성화
        pieChart.setRotationEnabled(false); // 회전 비활성화
        parentLayout.addView(pieChart);

        // 버튼 생성
        Button button = new Button(this);
        button.setText("나의 가게부에 담기");

        // 버튼 스타일 설정
        button.setBackgroundColor(Color.parseColor("#DCE7D5"));
        button.setTextColor(Color.parseColor("#000000"));
        button.setElevation(convertToPx(2));

        GradientDrawable buttonBackground = new GradientDrawable();
        buttonBackground.setColor(Color.parseColor("#DCE7D5"));
        buttonBackground.setCornerRadius(convertToPx(5));
        buttonBackground.setStroke(convertToPx(1), Color.parseColor("#818181"));
        button.setBackground(buttonBackground);

        RelativeLayout.LayoutParams buttonLayoutParams = new RelativeLayout.LayoutParams(
                convertToPx(330), convertToPx(56)); // 버튼 크기를 명시적으로 설정
        buttonLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        buttonLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        buttonLayoutParams.setMargins(convertToPx(16), convertToPx(16), convertToPx(16), convertToPx(16));
        button.setLayoutParams(buttonLayoutParams);

        parentLayout.addView(button);

        setContentView(parentLayout);

        // Initialize charts with data
        loadBarChartData();
        loadPieChartData();
    }

    private void loadBarChartData() {
        Call<List<BarEntry>> call = nidonNaedonAPI.getBarChartData();
        call.enqueue(new Callback<List<BarEntry>>() {
            @Override
            public void onResponse(Call<List<BarEntry>> call, Response<List<BarEntry>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateBarChart(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<BarEntry>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateBarChart(List<BarEntry> entries) {
        BarDataSet dataSet = new BarDataSet(entries, "Values");
        dataSet.setColors(Color.parseColor("#BCDAA9"), Color.parseColor("#BCDAA9"), Color.parseColor("#EFBEBE"));
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);
        barData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                return String.format("%+d원", (int) barEntry.getY());
            }
        });

        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        barChart.getXAxis().setDrawGridLines(false); // Hide grid lines
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getXAxis().setDrawAxisLine(false); // Hide x-axis line

        // Add names next to the bars
        barChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return ""; // Return empty to avoid labels on the x-axis
            }
        });

        barChart.getAxisLeft().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0) return "신호연";
                if (value == 1) return "곽지원";
                if (value == 2) return "유재원";
                return "";
            }
        });

        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisRight().setDrawAxisLine(false);

        barChart.setFitBars(true);
        barChart.invalidate();
    }

    private void loadPieChartData() {
        Call<List<PieEntry>> call = nidonNaedonAPI.getPieChartData();
        call.enqueue(new Callback<List<PieEntry>>() {
            @Override
            public void onResponse(Call<List<PieEntry>> call, Response<List<PieEntry>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updatePieChart(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<PieEntry>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updatePieChart(List<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "Expense Categories");
        dataSet.setColors(Color.parseColor("#e6c0ff"),
                Color.parseColor("#b8ffcc"),
                Color.parseColor("#ffdca7"),
                Color.parseColor("#ff8e8f"));
        dataSet.setSliceSpace(1f);
        dataSet.setValueLineColor(Color.BLACK);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.BLACK);

        // Add the following line to set the offset percentage
        dataSet.setValueLinePart1OffsetPercentage(80.f);

        // Apply the custom value formatter
        CustomValueFormatter customFormatter = new CustomValueFormatter();
        dataSet.setValueFormatter(customFormatter);

        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.setDrawHoleEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);

        // Disable default entry labels to avoid duplication
        pieChart.setDrawEntryLabels(false);

        // Refresh the chart
        pieChart.notifyDataSetChanged(); // Notify that the data has changed
        pieChart.invalidate(); // Refresh the chart
    }

    private int convertToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
