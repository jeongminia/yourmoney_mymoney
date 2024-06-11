package com.example.nidonnaedon;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.renderer.HorizontalBarChartRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

class CustomBarChartRenderer extends HorizontalBarChartRenderer {

    private final HorizontalBarChart chart;
    private final AppCompatActivity activity;

    public CustomBarChartRenderer(HorizontalBarChart chart, com.github.mikephil.charting.animation.ChartAnimator animator,
                                  ViewPortHandler viewPortHandler, AppCompatActivity activity) {
        super(chart, animator, viewPortHandler);
        this.chart = chart;
        this.activity = activity;
    }

    @Override
    public void drawValue(Canvas c, String valueText, float x, float y, int color) {
        Paint valuePaint = new Paint();
        valuePaint.setColor(color);
        valuePaint.setTextSize(convertToPx(12)); // Use float for text size
        valuePaint.setTextAlign(Paint.Align.CENTER);

        float adjustedX = x > 0 ? x - convertToPx(10) : x + convertToPx(10); // Use float for adjustment
        String formattedValue = valueText + "원"; // 값 뒤에 "원" 추가
        c.drawText(formattedValue, adjustedX, y, valuePaint);
    }

    @Override
    public void drawExtras(Canvas c) {
        super.drawExtras(c);

        Paint textPaint = new Paint();
        textPaint.setTextSize(convertToPx(12)); // Use float for text size
        textPaint.setColor(Color.BLACK);
        textPaint.setTypeface(ResourcesCompat.getFont(activity, com.example.nidonnaedon.R.font.nanumpenscript_regular)); // Correct R import

        String[] labels = new String[]{"유재원", "곽지원", "신호연"};
        for (int i = 0; i < labels.length; i++) {
            BarEntry entry = chart.getBarData().getDataSetByIndex(0).getEntryForIndex(i);
            float[] positions = new float[2];
            positions[0] = entry.getX();
            positions[1] = entry.getY();
            chart.getTransformer(chart.getData().getDataSetByIndex(0).getAxisDependency()).pointValuesToPixel(positions);
            float x = positions[0];
            float y = positions[1];

            c.drawText(labels[i], chart.getViewPortHandler().contentLeft() - textPaint.measureText(labels[i]) - convertToPx(8), y + (textPaint.getTextSize() / 2), textPaint);
        }
    }

    private float convertToPx(int dp) {
        return dp * chart.getResources().getDisplayMetrics().density;
    }
}