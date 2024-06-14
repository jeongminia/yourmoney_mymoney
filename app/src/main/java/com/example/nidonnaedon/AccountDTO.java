package com.example.nidonnaedon;

import java.util.List;

public class AccountDTO {
    private String accountId;
    private String accountName;
    private String accountSchedule;
    private String accountCurrency;
    private double accountExchangeRate;
    private List<String> accountParticipantList;

    public AccountDTO(String accountId, String accountName, String accountSchedule, String accountCurrency,
                      double accountExchangeRate, List<String> accountParticipantList) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountSchedule = accountSchedule;
        this.accountCurrency = accountCurrency;
        this.accountExchangeRate = accountExchangeRate;
        this.accountParticipantList = accountParticipantList;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountSchedule() {
        return accountSchedule;
    }

    public String getAccountCurrency() {
        return accountCurrency;
    }

    public double getAccountExchangeRate() {
        return accountExchangeRate;
    }

    public List<String> getAccountParticipantList() {
        return accountParticipantList;
    }
}
