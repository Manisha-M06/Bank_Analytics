package com.bank.app;

import com.bank.model.Transaction;
import com.bank.util.CsvReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TextVisualizationApp {

    public static void main(String[] args) {
        CsvReader csvReader = new CsvReader();
        List<Transaction> transactions = csvReader.readTransactionsFromCsv();
        
        System.out.println("=".repeat(80));
        System.out.println("💳 CREDIT vs DEBIT & FRAUD ANALYSIS");
        System.out.println("=".repeat(80));
        
        // Credit vs Debit Analysis
        double totalCredit = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.CREDIT)
            .mapToDouble(Transaction::getAmount)
            .sum();

        double totalDebit = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .mapToDouble(Transaction::getAmount)
            .sum();

        long creditCount = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.CREDIT)
            .count();

        long debitCount = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .count();

        System.out.println("\n📊 CREDIT/DEBIT SUMMARY:");
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│  CREDIT (Money IN)                                          │");
        System.out.println("│  Amount: $" + String.format("%,12.2f", totalCredit) + "                    │");
        System.out.println("│  Count:  " + String.format("%,12d", creditCount) + " transactions           │");
        System.out.println("├─────────────────────────────────────────────────────────────┤");
        System.out.println("│  DEBIT (Money OUT)                                          │");
        System.out.println("│  Amount: $" + String.format("%,12.2f", totalDebit) + "                    │");
        System.out.println("│  Count:  " + String.format("%,12d", debitCount) + " transactions           │");
        System.out.println("├─────────────────────────────────────────────────────────────┤");
        System.out.println("│  BALANCE                                                    │");
        System.out.println("│  Amount: $" + String.format("%,12.2f", totalCredit - totalDebit) + "                    │");
        System.out.println("│  Status: " + ((totalCredit - totalDebit) >= 0 ? "POSITIVE ✅" : "NEGATIVE ⚠️") + "                    │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");

        // Fraud Detection
        List<Double> amounts = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .map(Transaction::getAmount)
            .collect(Collectors.toList());

        double average = amounts.isEmpty() ? 0 : amounts.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double max = amounts.isEmpty() ? 0 : amounts.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        double fraudThreshold = average + (max - average) * 0.6;

        System.out.println("\n🚨 FRAUD DETECTION:");
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│  Average Transaction: $" + String.format("%,10.2f", average) + "                           │");
        System.out.println("│  Fraud Alert Threshold: $" + String.format("%,10.2f", fraudThreshold) + "                    │");
        
        List<Transaction> suspiciousTransactions = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .filter(t -> t.getAmount() > fraudThreshold)
            .sorted((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()))
            .collect(Collectors.toList());

        System.out.println("│  Suspicious Transactions: " + String.format("%,3d", suspiciousTransactions.size()) + "                           │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");

        if (!suspiciousTransactions.isEmpty()) {
            System.out.println("\n⚠️  SUSPICIOUS TRANSACTIONS (Top 10):");
            System.out.println("┌──────────────┬─────────────────────────┬──────────────┬─────────────────┐");
            System.out.println("│     Amount   │        Merchant         │    Date      │     Location     │");
            System.out.println("├──────────────┼─────────────────────────┼──────────────┼─────────────────┤");
            
            for (int i = 0; i < Math.min(10, suspiciousTransactions.size()); i++) {
                Transaction txn = suspiciousTransactions.get(i);
                System.out.printf("│ $%11.2f │ %-23s │ %-12s │ %-15s │%n", 
                    txn.getAmount(), 
                    txn.getMerchant().length() > 23 ? txn.getMerchant().substring(0, 20) + "..." : txn.getMerchant(),
                    txn.getTransactionDate().toString(),
                    txn.getLocation().length() > 15 ? txn.getLocation().substring(0, 12) + "..." : txn.getLocation());
            }
            
            System.out.println("└──────────────┴─────────────────────────┴──────────────┴─────────────────┘");
            
            if (suspiciousTransactions.size() > 10) {
                System.out.println("... and " + (suspiciousTransactions.size() - 10) + " more suspicious transactions");
            }
        } else {
            System.out.println("\n✅ No suspicious transactions detected! All transactions appear normal.");
        }

        // Transaction Timeline
        Map<String, Long> dailyTransactions = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .collect(Collectors.groupingBy(
                t -> t.getTransactionDate().toString(),
                Collectors.counting()
            ));

        System.out.println("\n📅 TRANSACTION TIMELINE (Last 10 days):");
        System.out.println("┌──────────────┬─────────────────────────┐");
        System.out.println("│     Date      │ Number of Transactions  │");
        System.out.println("├──────────────┼─────────────────────────┤");
        
        dailyTransactions.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .skip(Math.max(0, dailyTransactions.size() - 10))
            .forEach(entry -> {
                System.out.printf("│ %-12s │ %,21d │%n", entry.getKey(), entry.getValue());
            });
        
        System.out.println("└──────────────┴─────────────────────────┘");

        // Top Spending Categories
        Map<String, Double> categorySpending = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .collect(Collectors.groupingBy(
                t -> getCategorySimple(t.getMerchant()),
                Collectors.summingDouble(Transaction::getAmount)
            ));

        System.out.println("\n🛍️  TOP SPENDING CATEGORIES:");
        System.out.println("┌──────────────────┬─────────────────────────┐");
        System.out.println("│     Category     │       Total Spent       │");
        System.out.println("├──────────────────┼─────────────────────────┤");
        
        categorySpending.entrySet().stream()
            .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
            .limit(5)
            .forEach(entry -> {
                System.out.printf("│ %-16s │ $%,21.2f │%n", entry.getKey(), entry.getValue());
            });
        
        System.out.println("└──────────────────┴─────────────────────────┘");

        System.out.println("\n" + "=".repeat(80));
        System.out.println("📊 SUMMARY: Credit vs Debit Analysis Complete!");
        System.out.println("=".repeat(80));
    }

    private static String getCategorySimple(String merchant) {
        if (merchant.toLowerCase().contains("salary") || merchant.toLowerCase().contains("payment") || 
            merchant.toLowerCase().contains("income") || merchant.toLowerCase().contains("freelance")) {
            return "Income";
        } else if (merchant.toLowerCase().contains("rent") || merchant.toLowerCase().contains("mortgage")) {
            return "Housing";
        } else if (merchant.toLowerCase().contains("food") || merchant.toLowerCase().contains("grocery") || 
                   merchant.toLowerCase().contains("restaurant") || merchant.toLowerCase().contains("coffee")) {
            return "Food";
        } else if (merchant.toLowerCase().contains("gas") || merchant.toLowerCase().contains("car") || 
                   merchant.toLowerCase().contains("uber") || merchant.toLowerCase().contains("transport")) {
            return "Transport";
        } else if (merchant.toLowerCase().contains("amazon") || merchant.toLowerCase().contains("walmart") || 
                   merchant.toLowerCase().contains("target") || merchant.toLowerCase().contains("shopping")) {
            return "Shopping";
        } else if (merchant.toLowerCase().contains("netflix") || merchant.toLowerCase().contains("spotify") || 
                   merchant.toLowerCase().contains("entertainment")) {
            return "Entertainment";
        } else {
            return "Other";
        }
    }
}
