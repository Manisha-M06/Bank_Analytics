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

public class SimpleChartService {

    // Very Simple Money Overview
    public VBox createSimpleMoneyView(List<Transaction> transactions) {
        VBox container = new VBox(20);
        container.setPadding(new javafx.geometry.Insets(20));
        container.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-padding: 20;");

        // Title
        Label title = new Label("Your Money Summary");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.DARKBLUE);

        // Calculate simple totals
        double moneyIn = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.CREDIT)
            .mapToDouble(Transaction::getAmount)
            .sum();

        double moneyOut = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .mapToDouble(Transaction::getAmount)
            .sum();

        double remaining = moneyIn - moneyOut;

        // Create simple boxes for each amount
        HBox moneyBoxes = new HBox(15);
        
        VBox inBox = createMoneyBox("Money IN", moneyIn, "#4CAF50"); // Green
        VBox outBox = createMoneyBox("Money OUT", moneyOut, "#F44336"); // Red
        VBox leftBox = createMoneyBox("Money LEFT", remaining, "#2196F3"); // Blue
        
        moneyBoxes.getChildren().addAll(inBox, outBox, leftBox);

        // Simple pie chart
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
            new PieChart.Data("Money In: $" + String.format("%,.0f", moneyIn), moneyIn),
            new PieChart.Data("Money Out: $" + String.format("%,.0f", moneyOut), moneyOut)
        );

        PieChart pieChart = new PieChart(pieData);
        pieChart.setTitle("Where Your Money Goes");
        pieChart.setPrefSize(400, 300);
        pieChart.setLegendVisible(true);

        // Set colors
        pieChart.getData().get(0).getNode().setStyle("-fx-pie-color: #4CAF50;");
        pieChart.getData().get(1).getNode().setStyle("-fx-pie-color: #F44336;");

        container.getChildren().addAll(title, moneyBoxes, pieChart);
        return container;
    }

    // Simple Monthly Spending
    public VBox createSimpleMonthlyView(List<Transaction> transactions) {
        VBox container = new VBox(20);
        container.setPadding(new javafx.geometry.Insets(20));
        container.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-padding: 20;");

        Label title = new Label("Monthly Spending");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.DARKBLUE);

        // Group by month
        Map<String, Double> monthlySpending = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .collect(Collectors.groupingBy(
                t -> t.getTransactionDate().getMonth() + " " + t.getTransactionDate().getYear(),
                Collectors.summingDouble(Transaction::getAmount)
            ));

        // Create bar chart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount ($)");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("How Much You Spend Each Month");
        barChart.setPrefSize(600, 400);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Spending");

        monthlySpending.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            });

        barChart.getData().add(series);
        
        // Make bars blue
        series.getNode().setStyle("-fx-bar-fill: #2196F3;");

        container.getChildren().addAll(title, barChart);
        return container;
    }

    // Simple Top Spending Categories
    public VBox createSimpleCategoriesView(List<Transaction> transactions) {
        VBox container = new VBox(20);
        container.setPadding(new javafx.geometry.Insets(20));
        container.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-padding: 20;");

        Label title = new Label("Where You Spend Most");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.DARKBLUE);

        // Simple category mapping
        Map<String, Double> categorySpending = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .collect(Collectors.groupingBy(
                t -> getCategorySimple(t.getMerchant()),
                Collectors.summingDouble(Transaction::getAmount)
            ));

        // Create simple list instead of complex chart
        VBox categoriesList = new VBox(10);
        
        categorySpending.entrySet().stream()
            .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
            .limit(5)
            .forEach(entry -> {
                HBox categoryRow = new HBox(10);
                categoryRow.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 10; -fx-border-radius: 5;");
                
                Label categoryName = new Label(entry.getKey());
                categoryName.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                categoryName.setPrefWidth(150);
                
                Label amountLabel = new Label("$" + String.format("%,.2f", entry.getValue()));
                amountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                amountLabel.setTextFill(Color.DARKRED);
                
                categoryRow.getChildren().addAll(categoryName, amountLabel);
                categoriesList.getChildren().add(categoryRow);
            });

        container.getChildren().addAll(title, categoriesList);
        return container;
    }

    // Simple Large Transactions Alert
    public VBox createSimpleAlertView(List<Transaction> transactions) {
        VBox container = new VBox(20);
        container.setPadding(new javafx.geometry.Insets(20));
        container.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 10; -fx-padding: 20;");

        Label title = new Label("Big Transactions Alert");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.DARKBLUE);

        // Find transactions larger than $1000
        List<Transaction> bigTransactions = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .filter(t -> t.getAmount() > 1000)
            .sorted((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()))
            .limit(5)
            .collect(Collectors.toList());

        if (bigTransactions.isEmpty()) {
            Label noBig = new Label("✅ No big transactions found! All good!");
            noBig.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            noBig.setTextFill(Color.GREEN);
            container.getChildren().addAll(title, noBig);
        } else {
            Label subtitle = new Label("⚠️ Found " + bigTransactions.size() + " big transactions:");
            subtitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            subtitle.setTextFill(Color.ORANGE);
            
            VBox transactionsList = new VBox(8);
            
            for (Transaction txn : bigTransactions) {
                HBox txnRow = new HBox(10);
                txnRow.setStyle("-fx-background-color: #fff3cd; -fx-padding: 10; -fx-border-radius: 5;");
                
                Label amount = new Label("$" + String.format("%,.2f", txn.getAmount()));
                amount.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                amount.setTextFill(Color.RED);
                amount.setPrefWidth(100);
                
                Label merchant = new Label(txn.getMerchant());
                merchant.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
                merchant.setPrefWidth(150);
                
                Label date = new Label(txn.getTransactionDate().toString());
                date.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                date.setTextFill(Color.GRAY);
                
                txnRow.getChildren().addAll(amount, merchant, date);
                transactionsList.getChildren().add(txnRow);
            }
            
            container.getChildren().addAll(title, subtitle, transactionsList);
        }

        return container;
    }

    // Helper method to create money boxes
    private VBox createMoneyBox(String label, double amount, String color) {
        VBox box = new VBox(10);
        box.setStyle("-fx-background-color: " + color + "; -fx-padding: 20; -fx-border-radius: 10;");
        box.setPrefWidth(150);
        
        Label titleLabel = new Label(label);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.WHITE);
        
        Label amountLabel = new Label("$" + String.format("%,.2f", amount));
        amountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        amountLabel.setTextFill(Color.WHITE);
        
        box.getChildren().addAll(titleLabel, amountLabel);
        return box;
    }

    // Simple category mapping
    private String getCategorySimple(String merchant) {
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

    // Main Simple Dashboard
    public VBox createSimpleDashboard(List<Transaction> transactions) {
        VBox dashboard = new VBox(20);
        dashboard.setPadding(new javafx.geometry.Insets(20));
        dashboard.setStyle("-fx-background-color: #f0f0f0;");

        // Main title
        Label mainTitle = new Label("📊 Your Simple Banking Dashboard");
        mainTitle.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        mainTitle.setTextFill(Color.DARKBLUE);
        mainTitle.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Create all simple views
        VBox moneyView = createSimpleMoneyView(transactions);
        VBox monthlyView = createSimpleMonthlyView(transactions);
        VBox categoriesView = createSimpleCategoriesView(transactions);
        VBox alertView = createSimpleAlertView(transactions);

        dashboard.getChildren().addAll(mainTitle, moneyView, monthlyView, categoriesView, alertView);
        
        return dashboard;
    }
}
