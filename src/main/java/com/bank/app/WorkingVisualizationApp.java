package com.bank.app;

import com.bank.model.Transaction;
import com.bank.util.CsvReader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class WorkingVisualizationApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Credit, Debit & Fraud Analysis");

        // Load transaction data
        CsvReader csvReader = new CsvReader();
        List<Transaction> transactions = csvReader.readTransactionsFromCsv();
        
        // Calculate statistics
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

        // Create main container
        VBox mainContainer = new VBox(20);
        mainContainer.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20;");

        // Title
        Label title = new Label("💳 Credit, Debit & Fraud Analysis");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setTextFill(Color.DARKBLUE);

        // Credit vs Debit Summary
        VBox summaryBox = new VBox(15);
        summaryBox.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-padding: 20;");

        Label summaryTitle = new Label("📊 Credit vs Debit Summary");
        summaryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        summaryTitle.setTextFill(Color.DARKBLUE);

        HBox creditDebitRow = new HBox(20);
        
        VBox creditBox = createInfoBox("CREDIT", totalCredit, creditCount, "#4CAF50");
        VBox debitBox = createInfoBox("DEBIT", totalDebit, debitCount, "#F44336");
        VBox balanceBox = createBalanceBox(totalCredit - totalDebit);
        
        creditDebitRow.getChildren().addAll(creditBox, debitBox, balanceBox);

        // Fraud Detection
        VBox fraudBox = createFraudDetectionBox(transactions);

        // Transaction Details
        VBox detailsBox = createTransactionDetailsBox(transactions);

        summaryBox.getChildren().addAll(summaryTitle, creditDebitRow, fraudBox, detailsBox);
        mainContainer.getChildren().addAll(title, summaryBox);

        Scene scene = new Scene(mainContainer, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createInfoBox(String type, double amount, long count, String color) {
        VBox box = new VBox(10);
        box.setStyle("-fx-background-color: " + color + "; -fx-padding: 20; -fx-border-radius: 10;");
        box.setPrefWidth(200);
        
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
        box.setPrefWidth(200);
        
        Label typeLabel = new Label("BALANCE");
        typeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        typeLabel.setTextFill(Color.WHITE);
        
        Label amountLabel = new Label("$" + String.format("%,.2f", Math.abs(balance)));
        amountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        amountLabel.setTextFill(Color.WHITE);
        
        Label statusLabel = new Label(balance >= 0 ? "POSITIVE ✅" : "NEGATIVE ⚠️");
        statusLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        statusLabel.setTextFill(Color.WHITE);
        
        box.getChildren().addAll(typeLabel, amountLabel, statusLabel);
        return box;
    }

    private VBox createFraudDetectionBox(List<Transaction> transactions) {
        VBox box = new VBox(15);
        box.setStyle("-fx-background-color: #fff3cd; -fx-border-color: #ffeaa7; -fx-border-radius: 10; -fx-padding: 20;");

        Label fraudTitle = new Label("🚨 Fraud Detection");
        fraudTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        fraudTitle.setTextFill(Color.DARKBLUE);

        // Calculate average and threshold
        double average = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .mapToDouble(Transaction::getAmount)
            .average().orElse(0);

        double max = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .mapToDouble(Transaction::getAmount)
            .max().orElse(0);

        double fraudThreshold = average + (max - average) * 0.6;

        // Find suspicious transactions
        List<Transaction> suspiciousTransactions = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .filter(t -> t.getAmount() > fraudThreshold)
            .sorted((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()))
            .limit(5)
            .collect(java.util.stream.Collectors.toList());

        Label avgLabel = new Label("Average Transaction: $" + String.format("%.2f", average));
        avgLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        Label thresholdLabel = new Label("Fraud Alert Threshold: $" + String.format("%.2f", fraudThreshold));
        thresholdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        thresholdLabel.setTextFill(Color.ORANGE);
        
        Label detectedLabel = new Label("Suspicious Transactions: " + suspiciousTransactions.size());
        detectedLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        detectedLabel.setTextFill(suspiciousTransactions.isEmpty() ? Color.GREEN : Color.RED);

        box.getChildren().addAll(fraudTitle, avgLabel, thresholdLabel, detectedLabel);

        // Add suspicious transactions list
        if (!suspiciousTransactions.isEmpty()) {
            Label alertTitle = new Label("⚠️ Top Suspicious Transactions:");
            alertTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            alertTitle.setTextFill(Color.RED);
            
            VBox suspiciousList = new VBox(5);
            
            for (Transaction txn : suspiciousTransactions) {
                HBox txnRow = new HBox(10);
                txnRow.setStyle("-fx-background-color: #ffebee; -fx-padding: 8; -fx-border-radius: 5;");
                
                Label amount = new Label("$" + String.format("%,.2f", txn.getAmount()));
                amount.setFont(Font.font("Arial", FontWeight.BOLD, 12));
                amount.setTextFill(Color.RED);
                
                Label merchant = new Label(txn.getMerchant());
                merchant.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                
                Label date = new Label(txn.getTransactionDate().toString());
                date.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
                date.setTextFill(Color.GRAY);
                
                txnRow.getChildren().addAll(amount, merchant, date);
                suspiciousList.getChildren().add(txnRow);
            }
            
            box.getChildren().addAll(alertTitle, suspiciousList);
        }

        return box;
    }

    private VBox createTransactionDetailsBox(List<Transaction> transactions) {
        VBox box = new VBox(15);
        box.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-padding: 20;");

        Label detailsTitle = new Label("📋 Transaction Summary");
        detailsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        detailsTitle.setTextFill(Color.DARKBLUE);

        // Success vs Failed
        long successCount = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .count();

        long failedCount = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.FAILED)
            .count();

        HBox statusRow = new HBox(20);
        
        VBox successBox = createMiniBox("✅ Successful", successCount, "#4CAF50");
        VBox failedBox = createMiniBox("❌ Failed", failedCount, "#F44336");
        
        statusRow.getChildren().addAll(successBox, failedBox);

        box.getChildren().addAll(detailsTitle, statusRow);
        return box;
    }

    private VBox createMiniBox(String label, long count, String color) {
        VBox box = new VBox(5);
        box.setStyle("-fx-background-color: " + color + "; -fx-padding: 15; -fx-border-radius: 8;");
        box.setPrefWidth(150);
        
        Label countLabel = new Label(String.valueOf(count));
        countLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        countLabel.setTextFill(Color.WHITE);
        
        Label textLabel = new Label(label);
        textLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        textLabel.setTextFill(Color.WHITE);
        
        box.getChildren().addAll(countLabel, textLabel);
        return box;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
