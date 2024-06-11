package com.example.nidonnaedon;

public class Account {
    private String usageDetails;
    private String category;
    private String date;
    private String amount;
    private String imageUri; // 이미지 URI 추가

    public Account(String usageDetails, String category, String date, String amount, String imageUri) {
        this.usageDetails = usageDetails;
        this.category = category;
        this.date = date;
        this.amount = amount;
        this.imageUri = imageUri;
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

    public String getImageUri() {
        return imageUri;
    }
}
