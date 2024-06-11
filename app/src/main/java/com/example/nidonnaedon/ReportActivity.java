package com.example.nidonnaedon;

import android.content.Context;
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

import com.example.nisonnaeson.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

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

        // Pie chart
        pieChart = new PieChart(this);
        pieChart.setId(View.generateViewId());
        RelativeLayout.LayoutParams pieChartParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, convertToPx(300)); // Use int for conversion
        pieChartParams.addRule(RelativeLayout.BELOW, toolbarLayout.getId());
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

        // Initialize charts with dummy data
        updatePieChart();
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
                Color.parseColor("#ff8e8f"));
        dataSet.setSliceSpace(1f);
        dataSet.setValueLineColor(Color.BLACK);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setValueFormatter(new PercentFormatter(pieChart));

        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.setDrawHoleEnabled(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);

        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(14f);
        pieChart.setUsePercentValues(true);

        pieChart.invalidate();
    }

    private int convertToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
