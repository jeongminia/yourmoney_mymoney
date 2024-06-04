package com.example.nisonnaeson;

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
        valuePaint.setTextSize(convertToPx(12));
        valuePaint.setTextAlign(Paint.Align.CENTER);
        // valuePaint.setTypeface(ResourcesCompat.getFont(activity, R.font.nanumpenscript_regular)); // Remove custom font for bar values

        // Draw value inside bar, adjusting x position based on value direction
        float adjustedX = x > 0 ? x - convertToPx(10) : x + convertToPx(10);
        c.drawText(valueText, adjustedX, y, valuePaint);
    }

    @Override
    public void drawExtras(Canvas c) {
        super.drawExtras(c);
        // Draw custom labels next to the bars
        Paint textPaint = new Paint();
        textPaint.setTextSize(convertToPx(12));
        textPaint.setColor(Color.BLACK);
        textPaint.setTypeface(ResourcesCompat.getFont(activity, R.font.nanumpenscript_regular)); // Set custom font for labels

        // Labels for bars
        String[] labels = new String[]{"신호연", "곽지원", "유재원"};
        for (int i = 0; i < mChart.getBarData().getDataSetCount(); i++) {
            BarDataSet dataSet = (BarDataSet) mChart.getBarData().getDataSetByIndex(i);
            for (int j = 0; j < dataSet.getEntryCount(); j++) {
                BarEntry entry = dataSet.getEntryForIndex(j);
                float[] positions = new float[2];
                positions[0] = entry.getX();
                positions[1] = entry.getY();
                mChart.getTransformer(dataSet.getAxisDependency()).pointValuesToPixel(positions);
                float x = positions[0];
                float y = positions[1];
                if (entry.getY() > 0) {
                    c.drawText(labels[j], x + convertToPx(8), y, textPaint);
                } else {
                    c.drawText(labels[j], x - textPaint.measureText(labels[j]) - convertToPx(8), y, textPaint);
                }
            }
        }
    }

    private float convertToPx(int dp) {
        return dp * chart.getResources().getDisplayMetrics().density;
    }
}
