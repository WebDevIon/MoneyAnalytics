package com.example.android.moneyanalytics.model;

import android.arch.persistence.room.ColumnInfo;

public class EntryByCategory {

    @ColumnInfo(name = "amount")
    private Double amount;
    @ColumnInfo(name = "category")
    private String category;
    @ColumnInfo(name = "type")
    private String type;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
