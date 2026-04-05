package com.bank.service;

import com.bank.model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CreditDebitFraudService {

    // Credit vs Debit Overview
    public VBox createCreditDebitView(List<Transaction> transactions) {
        VBox container = new VBox(20);
        container.setPadding(new javafx.geometry.Insets(20));
        container.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-padding: 20;");

        Label title = new Label("💳 Credit vs Debit Transactions");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.DARKBLUE);

        // Calculate totals
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

        // Create summary boxes
        HBox summaryRow = new HBox(15);
        
        VBox creditBox = createTransactionBox("CREDIT", totalCredit, creditCount, "#4CAF50"); // Green
        VBox debitBox = createTransactionBox("DEBIT", totalDebit, debitCount, "#F44336"); // Red
        VBox balanceBox = createBalanceBox(totalCredit - totalDebit);
        
        summaryRow.getChildren().addAll(creditBox, debitBox, balanceBox);

        // Pie chart
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
            new PieChart.Data("Credit: $" + String.format("%,.0f", totalCredit), totalCredit),
            new PieChart.Data("Debit: $" + String.format("%,.0f", totalDebit), totalDebit)
        );

        PieChart pieChart = new PieChart(pieData);
        pieChart.setTitle("Credit vs Debit Balance");
        pieChart.setPrefSize(400, 300);
        pieChart.setLegendVisible(true);

        // Set colors
        pieChart.getData().get(0).getNode().setStyle("-fx-pie-color: #4CAF50;");
        pieChart.getData().get(1).getNode().setStyle("-fx-pie-color: #F44336;");

        container.getChildren().addAll(title, summaryRow, pieChart);
        return container;
    }

    // Fraud Detection View
    public VBox createFraudDetectionView(List<Transaction> transactions) {
        VBox container = new VBox(20);
        container.setPadding(new javafx.geometry.Insets(20));
        container.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-padding: 20;");

        Label title = new Label("🚨 Fraud Detection");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.DARKBLUE);

        // Calculate average transaction amount
        List<Double> amounts = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .map(Transaction::getAmount)
            .collect(Collectors.toList());

        double average = amounts.isEmpty() ? 0 : amounts.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double max = amounts.isEmpty() ? 0 : amounts.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        double fraudThreshold = average + (max - average) * 0.6; // 60% above average

        // Find suspicious transactions
        List<Transaction> suspiciousTransactions = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .filter(t -> t.getAmount() > fraudThreshold)
            .sorted((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()))
            .collect(Collectors.toList());

        // Summary box
        VBox summaryBox = new VBox(10);
        summaryBox.setStyle("-fx-background-color: #fff3cd; -fx-padding: 15; -fx-border-radius: 8; -fx-border-color: #ffeaa7;");
        
        Label avgLabel = new Label("Average Transaction: $" + String.format("%.2f", average));
        avgLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Label thresholdLabel = new Label("Fraud Alert Threshold: $" + String.format("%.2f", fraudThreshold));
        thresholdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        thresholdLabel.setTextFill(Color.ORANGE);
        
        Label detectedLabel = new Label("Suspicious Transactions Found: " + suspiciousTransactions.size());
        detectedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        detectedLabel.setTextFill(suspiciousTransactions.isEmpty() ? Color.GREEN : Color.RED);
        
        summaryBox.getChildren().addAll(avgLabel, thresholdLabel, detectedLabel);

        // List suspicious transactions
        if (!suspiciousTransactions.isEmpty()) {
            Label alertTitle = new Label("⚠️ SUSPICIOUS TRANSACTIONS:");
            alertTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            alertTitle.setTextFill(Color.RED);
            
            VBox suspiciousList = new VBox(8);
            
            for (Transaction txn : suspiciousTransactions.stream().limit(10).collect(Collectors.toList())) {
                HBox txnRow = new HBox(10);
                txnRow.setStyle("-fx-background-color: #ffebee; -fx-padding: 10; -fx-border-radius: 5; -fx-border-color: #ffcdd2;");
                
                Label amount = new Label("$" + String.format("%,.2f", txn.getAmount()));
                amount.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                amount.setTextFill(Color.RED);
                amount.setPrefWidth(120);
                
                Label merchant = new Label(txn.getMerchant());
                merchant.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                merchant.setPrefWidth(150);
                
                Label date = new Label(txn.getTransactionDate().toString());
                date.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
                date.setTextFill(Color.GRAY);
                
                Label location = new Label(txn.getLocation());
                location.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
                location.setTextFill(Color.GRAY);
                
                txnRow.getChildren().addAll(amount, merchant, date, location);
                suspiciousList.getChildren().add(txnRow);
            }
            
            if (suspiciousTransactions.size() > 10) {
                Label moreLabel = new Label("... and " + (suspiciousTransactions.size() - 10) + " more suspicious transactions");
                moreLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                moreLabel.setTextFill(Color.ORANGE);
                suspiciousList.getChildren().add(moreLabel);
            }
            
            container.getChildren().addAll(title, summaryBox, alertTitle, suspiciousList);
        } else {
            Label noFraudLabel = new Label("✅ No suspicious transactions detected!");
            noFraudLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            noFraudLabel.setTextFill(Color.GREEN);
            
            container.getChildren().addAll(title, summaryBox, noFraudLabel);
        }

        return container;
    }

    // Transaction Timeline
    public VBox createTransactionTimeline(List<Transaction> transactions) {
        VBox container = new VBox(20);
        container.setPadding(new javafx.geometry.Insets(20));
        container.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-padding: 20;");

        Label title = new Label("📅 Transaction Timeline");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.DARKBLUE);

        // Group by date
        Map<String, Long> dailyTransactions = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .collect(Collectors.groupingBy(
                t -> t.getTransactionDate().toString(),
                Collectors.counting()
            ));

        // Create line chart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Transactions");

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Daily Transaction Volume");
        lineChart.setPrefSize(700, 400);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Transactions per Day");

        dailyTransactions.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .limit(30) // Show last 30 days
            .forEach(entry -> {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            });

        lineChart.getData().add(series);
        
        // Style the line
        series.getNode().setStyle("-fx-stroke: #2196F3; -fx-stroke-width: 2px;");

        container.getChildren().addAll(title, lineChart);
        return container;
    }

    // Helper methods
    private VBox createTransactionBox(String type, double amount, long count, String color) {
        VBox box = new VBox(10);
        box.setStyle("-fx-background-color: " + color + "; -fx-padding: 20; -fx-border-radius: 10;");
        box.setPrefWidth(180);
        
        Label typeLabel = new Label(type);
        typeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        typeLabel.setTextFill(Color.WHITE);
        
        Label amountLabel = new Label("$" + String.format("%,.2f", amount));
        amountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        amountLabel.setTextFill(Color.WHITE);
        
        Label countLabel = new Label(count + " transactions");
        countLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        countLabel.setTextFill(Color.WHITE);
        
        box.getChildren().addAll(typeLabel, amountLabel, countLabel);
        return box;
    }

    private VBox createBalanceBox(double balance) {
        VBox box = new VBox(10);
        String color = balance >= 0 ? "#4CAF50" : "#F44336";
        box.setStyle("-fx-background-color: " + color + "; -fx-padding: 20; -fx-border-radius: 10;");
        box.setPrefWidth(180);
        
        Label typeLabel = new Label("BALANCE");
        typeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        typeLabel.setTextFill(Color.WHITE);
        
        Label amountLabel = new Label("$" + String.format("%,.2f", Math.abs(balance)));
        amountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        amountLabel.setTextFill(Color.WHITE);
        
        Label statusLabel = new Label(balance >= 0 ? "Positive" : "Negative");
        statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        statusLabel.setTextFill(Color.WHITE);
        
        box.getChildren().addAll(typeLabel, amountLabel, statusLabel);
        return box;
    }

    // Main Dashboard
    public VBox createCreditDebitFraudDashboard(List<Transaction> transactions) {
        VBox dashboard = new VBox(20);
        dashboard.setPadding(new javafx.geometry.Insets(20));
        dashboard.setStyle("-fx-background-color: #f0f0f0;");

        // Main title
        Label mainTitle = new Label("💳 Credit, Debit & Fraud Dashboard");
        mainTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        mainTitle.setTextFill(Color.DARKBLUE);
        mainTitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Create views
        VBox creditDebitView = createCreditDebitView(transactions);
        VBox fraudView = createFraudDetectionView(transactions);
        VBox timelineView = createTransactionTimeline(transactions);

        dashboard.getChildren().addAll(mainTitle, creditDebitView, fraudView, timelineView);
        
        return dashboard;
    }
}
