package com.example.nidonnaedon;

public class ExpenseItem {
    private String name;
    private String date;

    public ExpenseItem(String name, String date) {
        this.name = name;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
}
