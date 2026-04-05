package com.bank.service;

import com.bank.model.Transaction;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalyticsService {

    public double calculateTotalAmount(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double calculateAverageAmount(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
                .mapToDouble(Transaction::getAmount)
                .average()
                .orElse(0.0);
    }

    public Map<Transaction.TransactionType, Long> countTransactionsByType(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
                .collect(Collectors.groupingBy(
                        Transaction::getTransactionType,
                        Collectors.counting()
                ));
    }

    public long countSuccessfulTransactions(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
                .count();
    }

    public long countFailedTransactions(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.FAILED)
                .count();
    }

    public double getTotalDebitAmount(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
                .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalCreditAmount(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
                .filter(t -> t.getTransactionType() == Transaction.TransactionType.CREDIT)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
}
