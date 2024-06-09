package com.example.nidonnaedon;

public class ExpenseItem {
    private final String name;
    private final String date;

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
