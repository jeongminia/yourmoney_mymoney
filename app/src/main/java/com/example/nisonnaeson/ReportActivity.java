package com.example.nisonnaeson;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    private HorizontalBarChart barChart;
    private PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Parent LinearLayout
        LinearLayout parentLayout = new LinearLayout(this);
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        parentLayout.setBackgroundColor(Color.parseColor("#F0F8FF")); // light cyan background
        parentLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        // Toolbar
        LinearLayout toolbarLayout = new LinearLayout(this);
        toolbarLayout.setOrientation(LinearLayout.HORIZONTAL);
        toolbarLayout.setBackgroundColor(Color.parseColor("#82A33E"));
        toolbarLayout.setPadding(20, 20, 20, 20);
        toolbarLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        ImageView backButton = new ImageView(this);
        backButton.setImageResource(android.R.drawable.ic_media_previous); // replace with your back icon
        backButton.setColorFilter(Color.WHITE); // Set icon color to white
        toolbarLayout.addView(backButton);

        TextView titleView = new TextView(this);
        titleView.setText("독일 여행");
        titleView.setTextSize(24);
        titleView.setTextColor(Color.WHITE);
        titleView.setPadding(20, 0, 0, 0);
        toolbarLayout.addView(titleView);

        parentLayout.addView(toolbarLayout);

        // FlexboxLayout for user balances
        FlexboxLayout flexboxLayout = new FlexboxLayout(this);
        flexboxLayout.setFlexDirection(FlexDirection.COLUMN);
        flexboxLayout.setJustifyContent(JustifyContent.CENTER);
        flexboxLayout.setAlignItems(AlignItems.CENTER);
        flexboxLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));

        parentLayout.addView(flexboxLayout);

        // Bar chart for user balances
        barChart = new HorizontalBarChart(this);
        LinearLayout.LayoutParams barChartParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, convertToPx(200));
        barChartParams.setMargins(0, 20, 0, 20);
        barChart.setLayoutParams(barChartParams);
        parentLayout.addView(barChart);

        // Set bar chart background to transparent
        barChart.setDrawGridBackground(false);
        barChart.setDrawBorders(false);
        barChart.setDrawBarShadow(false);

        // Pie chart
        pieChart = new PieChart(this);
        LinearLayout.LayoutParams pieChartParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, convertToPx(300)); // Set your desired width and height
        pieChartParams.setMargins(0, 20, 0, 20);
        pieChart.setLayoutParams(pieChartParams);
        parentLayout.addView(pieChart);

        // Save button
        Button saveButton = new Button(this);
        saveButton.setText("나의 가계부에 담기");
        saveButton.setBackgroundColor(Color.parseColor("#D9EAD3")); // light green background
        saveButton.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(0, 20, 0, 20);
        saveButton.setLayoutParams(buttonParams);

        LinearLayout saveButtonContainer = new LinearLayout(this);
        saveButtonContainer.setOrientation(LinearLayout.HORIZONTAL);
        saveButtonContainer.setGravity(android.view.Gravity.CENTER);
        saveButtonContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        saveButtonContainer.addView(saveButton);
        parentLayout.addView(saveButtonContainer);

        setContentView(parentLayout);

        // Initialize charts with dummy data
        updateBarChart();
        updatePieChart();
    }

    private void updateBarChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, 5900));
        entries.add(new BarEntry(1, -8500));
        entries.add(new BarEntry(2, 1800));

        BarDataSet dataSet = new BarDataSet(entries, "User Balances");
        dataSet.setColors(new int[]{Color.GREEN, Color.RED, Color.GREEN});
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);
        barData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                return String.format("%,.0f원", barEntry.getY());
            }
        });

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

        barChart.getDescription().setEnabled(false); // Remove description label
        barChart.getLegend().setEnabled(false); // Remove legend
    }

    private void updatePieChart() {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(40, "식비"));
        entries.add(new PieEntry(30, "교통"));
        entries.add(new PieEntry(20, "숙박"));
        entries.add(new PieEntry(10, "기타"));

        PieDataSet dataSet = new PieDataSet(entries, "Expense Categories");
        dataSet.setColors(new int[]{Color.MAGENTA, Color.CYAN, Color.YELLOW, Color.RED});
        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate(); // refresh
    }

    private int convertToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }
}
