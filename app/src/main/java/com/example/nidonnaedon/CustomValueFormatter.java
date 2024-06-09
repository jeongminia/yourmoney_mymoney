package com.example.nidonnaedon;

import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.data.PieEntry;

public class CustomValueFormatter extends ValueFormatter {
    @Override
    public String getPieLabel(float value, PieEntry pieEntry) {
        return pieEntry.getLabel() + "\n" + value;
    }
}
