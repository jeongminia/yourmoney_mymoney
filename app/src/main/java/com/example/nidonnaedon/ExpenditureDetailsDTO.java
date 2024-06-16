package com.example.nidonnaedon;

import java.util.List;

public class ExpenditureDetailsDTO {
    private int id;
    private String expenditureId;
    private String expenditureName;
    private double expenditureAmount;
    private String expenditureCurrency;
    private double expenditureExchangeRate;
    private List<String> expenditureParticipant;
    private String expenditureDate;
    private String expenditurePhoto;
    private String accountId;
    private String expenditureCategory;

    public ExpenditureDetailsDTO(int id, String expenditureId, String expenditureName, double expenditureAmount, String expenditureCurrency,
                                 double expenditureExchangeRate, List<String> expenditureParticipant, String expenditureDate,
                                 String expenditurePhoto, String accountId, String expenditureCategory) {
        this.id = id;
        this.expenditureId = expenditureId;
        this.expenditureName = expenditureName;
        this.expenditureAmount = expenditureAmount;
        this.expenditureCurrency = expenditureCurrency;
        this.expenditureExchangeRate = expenditureExchangeRate;
        this.expenditureParticipant = expenditureParticipant;
        this.expenditureDate = expenditureDate;
        this.expenditurePhoto = expenditurePhoto;
        this.accountId = accountId;
        this.expenditureCategory = expenditureCategory;
    }

    // Getter 메서드 추가
    public int getId() {
        return id;
    }

    public String getExpenditureId() {
        return expenditureId;
    }

    public String getExpenditureName() {
        return expenditureName;
    }

    public double getExpenditureAmount() {
        return expenditureAmount;
    }

    public String getExpenditureCurrency() {
        return expenditureCurrency;
    }

    public double getExpenditureExchangeRate() {
        return expenditureExchangeRate;
    }

    public List<String> getExpenditureParticipant() {
        return expenditureParticipant;
    }

    public String getExpenditureDate() {
        return expenditureDate;
    }

    public String getExpenditurePhoto() {
        return expenditurePhoto;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getExpenditureCategory() {
        return expenditureCategory;
    }
}
