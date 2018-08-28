package com.example.android.moneyanalytics.utils;

import android.content.Context;

import com.example.android.moneyanalytics.R;

/**
 * This class is used to select the proper color for each category in the Pie Chart.
 */
public class ColorUtils {

    private Context context;

    public ColorUtils(Context context) {
        this.context = context;
    }

    public int getColor(String category) {
        if (category.equals(getStringFromResources(R.string.carString))) {
            return getColorInt(R.color.carColor);
        } else if (category.equals(getStringFromResources(R.string.clothesString))) {
            return getColorInt(R.color.clothesColor);
        } else if (category.equals(getStringFromResources(R.string.cosmeticsString))) {
            return getColorInt(R.color.cosmeticsColor);
        } else if (category.equals(getStringFromResources(R.string.foodString))) {
            return getColorInt(R.color.foodColor);
        } else if (category.equals(getStringFromResources(R.string.giftsString))) {
            return getColorInt(R.color.giftsColor);
        } else if (category.equals(getStringFromResources(R.string.healthString))) {
            return getColorInt(R.color.healthColor);
        } else if (category.equals(getStringFromResources(R.string.homeString))) {
            return getColorInt(R.color.homeColor);
        } else if (category.equals(getStringFromResources(R.string.petsString))) {
            return getColorInt(R.color.petsColor);
        } else if (category.equals(getStringFromResources(R.string.servicesString))) {
            return getColorInt(R.color.servicesColor);
        } else if (category.equals(getStringFromResources(R.string.sportsString))) {
            return getColorInt(R.color.sportsColor);
        } else if (category.equals(getStringFromResources(R.string.clothesString))) {
            return getColorInt(R.color.clothesColor);
        } else if (category.equals(getStringFromResources(R.string.salaryString))) {
            return getColorInt(R.color.salaryColor);
        } else if (category.equals(getStringFromResources(R.string.depositString))) {
            return getColorInt(R.color.depositColor);
        } else if (category.equals(getStringFromResources(R.string.royaltiesString))) {
            return getColorInt(R.color.royaltiesColor);
        } else if (category.equals(getStringFromResources(R.string.winningsString))) {
            return getColorInt(R.color.winningsColor);
        } else {
            return getColorInt(R.color.noColor);
        }
    }

    private String getStringFromResources(int id) {
        return context.getResources().getString(id);
    }

    private int getColorInt(int id) {
        return context.getResources().getColor(id);
    }
}
