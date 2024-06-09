package com.example.nidonnaedon;

public class Account {
    private String usageDetails;
    private String category;
    private String date;
    private String amount;

    public Account(String usageDetails, String category, String date, String amount) {
        this.usageDetails = usageDetails;
        this.category = category;
        this.date = date;
        this.amount = amount;
    }

    public String getUsageDetails() {
        return usageDetails;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }
}
