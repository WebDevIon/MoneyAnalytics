package com.example.android.moneyanalytics.chart;

import android.support.annotation.Nullable;

import com.razerdp.widget.animatedpieview.data.IPieInfo;
import com.razerdp.widget.animatedpieview.data.PieOption;

public class PieChartData implements IPieInfo {

    private double mValue;
    private int mColor;
    private String mDescription;

    public PieChartData(double value, int color, String description) {
        mValue = value;
        mColor = color;
        mDescription = description;
    }

    @Override
    public double getValue() {
        return mValue;
    }

    @Override
    public int getColor() {
        return mColor;
    }

    @Override
    public String getDesc() {
        return mDescription;
    }

    @Nullable
    @Override
    public PieOption getPieOpeion() {
        return null;
    }
}
