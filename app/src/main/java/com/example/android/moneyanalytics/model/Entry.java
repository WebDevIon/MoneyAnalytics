package com.example.android.moneyanalytics.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "entries")
public class Entry {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Double amount;
    private String name;
    private String category;
    private boolean recurring;
    private Long date;
    private String type;

    @Ignore
    public Entry(Double amount, String name, String category,
                 boolean recurring, Long date, String type){
        this.amount = amount;
        this.name = name;
        this.category = category;
        this.recurring = recurring;
        this.date = date;
        this.type = type;
    }

    public Entry(int id, Double amount, String name, String category,
                 boolean recurring, Long date, String type){
        this.id = id;
        this.amount = amount;
        this.name = name;
        this.category = category;
        this.recurring = recurring;
        this.date = date;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
