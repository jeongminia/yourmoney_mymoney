package com.example.nidonnaedon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.nisonnaeson.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private HorizontalBarChart barChart;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Parent RelativeLayout
        RelativeLayout parentLayout = new RelativeLayout(this);
        parentLayout.setBackgroundColor(Color.WHITE); // 배경 색 흰색으로 설정
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

        // Bar chart for user balances
        barChart = new HorizontalBarChart(this);
        barChart.setId(View.generateViewId());
        RelativeLayout.LayoutParams barChartParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, convertToPx(200)); // Use int for conversion
        barChartParams.addRule(RelativeLayout.BELOW, toolbarLayout.getId());
        barChartParams.setMargins(0, 0, 0, convertToPx(20)); // Use int for conversion
        barChart.setLayoutParams(barChartParams);
        parentLayout.addView(barChart);

        // Pie chart
        pieChart = new PieChart(this);
        pieChart.setId(View.generateViewId());
        RelativeLayout.LayoutParams pieChartParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, convertToPx(300)); // Use int for conversion
        pieChartParams.addRule(RelativeLayout.BELOW, barChart.getId());
        pieChartParams.setMargins(0, convertToPx(20), 0, convertToPx(20)); // Use int for conversion
        pieChart.setLayoutParams(pieChartParams);
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

        // Initialize charts with dummy data
        updateBarChart();
        updatePieChart();
    }

    private void updateBarChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 5900f));
        entries.add(new BarEntry(1f, -8500f));
        entries.add(new BarEntry(2f, 1800f));

        BarDataSet dataSet = new BarDataSet(entries, "User Balances");
        dataSet.setColors(Color.parseColor("#bcdaa8"), Color.parseColor("#eebebe"), Color.parseColor("#bcdaa8"));
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);

        barChart.setData(barData);
        barChart.invalidate(); // refresh

        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        XAxis xAxis = barChart.getXAxis();

        leftAxis.setDrawLabels(false); // Remove left Y-axis labels
        rightAxis.setDrawLabels(false); // Remove right Y-axis labels
        xAxis.setDrawLabels(false); // Remove X-axis labels

        leftAxis.setDrawGridLines(false); // Remove grid lines
        rightAxis.setDrawGridLines(false); // Remove grid lines
        xAxis.setDrawGridLines(false); // Remove grid lines

        leftAxis.setDrawAxisLine(false); // Remove axis line
        rightAxis.setDrawAxisLine(false); // Remove axis line

        barChart.getDescription().setEnabled(false); // Remove description label
        barChart.getLegend().setEnabled(false); // Remove legend

        barChart.setDrawBorders(false); // Remove border lines
        barChart.setDrawGridBackground(false); // Remove grid background

        barChart.getAxisRight().setEnabled(false); // Remove right axis completely

        // Custom renderer to draw values inside bars and names next to bars
        barChart.setRenderer(new CustomBarChartRenderer(barChart, barChart.getAnimator(), barChart.getViewPortHandler(), this));
    }

    private void updatePieChart() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(40f, "식비"));
        entries.add(new PieEntry(30f, "교통"));
        entries.add(new PieEntry(20f, "숙박"));
        entries.add(new PieEntry(10f, "기타"));

        PieDataSet dataSet = new PieDataSet(entries, "Expense Categories");
        dataSet.setColors(Color.parseColor("#e6c0ff"),
                Color.parseColor("#b8ffcc"),
                Color.parseColor("#ffdca7"),
                Color.parseColor("#ff8e8f")); // 예시 색상
        dataSet.setValueTextColor(Color.BLACK); // 글씨 색을 검정색으로 설정
        dataSet.setValueTextSize(14f); // 숫자 폰트 사이즈 키우기

        dataSet.setSliceSpace(1f); // 조각 간의 간격 설정
        dataSet.setValueLineColor(Color.parseColor("#262625")); // 경계선 색상 검은색으로 설정

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new CustomValueFormatter()); // 커스텀 ValueFormatter 설정

        pieChart.setData(pieData);
        pieChart.setDrawHoleEnabled(false); // 중심의 구멍 제거
        pieChart.getDescription().setEnabled(false); // 설명 라벨 제거
        pieChart.getLegend().setEnabled(false); // 범례 제거
        pieChart.invalidate(); // 새로 고침

        pieChart.setEntryLabelColor(Color.TRANSPARENT); // 항목 이름 비활성화
    }

    private int convertToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

}
