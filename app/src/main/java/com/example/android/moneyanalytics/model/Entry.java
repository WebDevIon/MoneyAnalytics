package com.example.android.moneyanalytics.model;

import java.util.Date;

public class Entry {

    private Double amount;
    private String name;
    private String category;
    private boolean recurring;
    private Date date;
    private String type;

    public Entry(){}

    // TODO Remove constructor it is used for testing purposes only.
    public Entry(String name, Date date, Double amount) {
        this.name = name;
        this.date = date;
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
