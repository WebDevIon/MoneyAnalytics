package com.example.android.moneyanalytics.model;

import java.util.Date;

public class Income {

    private String name;
    private Date date;
    private Double amount;

    public Income(){}

    public Income(String name, Date date, Double amount) {
        this.name = name;
        this.date = date;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
