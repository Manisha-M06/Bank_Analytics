package com.bank.model;

import java.time.LocalDate;

public class Transaction {
    private String transactionId;
    private String accountId;
    private String customerId;
    private TransactionType transactionType;
    private double amount;
    private LocalDate transactionDate;
    private String merchant;
    private String location;
    private TransactionStatus status;

    public enum TransactionType {
        DEBIT, CREDIT
    }

    public enum TransactionStatus {
        SUCCESS, FAILED
    }

    public Transaction() {
    }

    public Transaction(String transactionId, String accountId, String customerId, 
                      TransactionType transactionType, double amount, 
                      LocalDate transactionDate, String merchant, 
                      String location, TransactionStatus status) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.customerId = customerId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.merchant = merchant;
        this.location = location;
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", accountId='" + accountId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", transactionType=" + transactionType +
                ", amount=" + amount +
                ", transactionDate=" + transactionDate +
                ", merchant='" + merchant + '\'' +
                ", location='" + location + '\'' +
                ", status=" + status +
                '}';
    }
}
