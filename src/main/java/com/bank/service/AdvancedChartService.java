package com.bank.service;

import com.bank.model.Transaction;
import com.bank.service.AdvancedAnalyticsServiceSimple;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.Map;

public class AdvancedChartService {

    private AdvancedAnalyticsServiceSimple advancedAnalytics;

    public AdvancedChartService() {
        this.advancedAnalytics = new AdvancedAnalyticsServiceSimple();
    }

    // Monthly Spending Patterns Bar Chart
    public BarChart<String, Number> createMonthlySpendingChart(List<Transaction> transactions) {
        Map<String, Double> monthlySpending = advancedAnalytics.analyzeMonthlySpendingPatterns(transactions);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Total Spending ($)");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Spending");

        monthlySpending.forEach((month, amount) -> {
            series.getData().add(new XYChart.Data<>(month, amount));
        });

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Monthly Spending Patterns");
        barChart.getData().add(series);
        barChart.setPrefSize(800, 400);

        return barChart;
    }

    // Category-wise Analysis Pie Chart
    public PieChart createCategorySpendingPieChart(List<Transaction> transactions) {
        Map<String, AdvancedAnalyticsServiceSimple.CategoryAnalysis> categoryAnalysis = 
            advancedAnalytics.analyzeByCategory(transactions);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        categoryAnalysis.entrySet().stream()
            .filter(entry -> entry.getValue().getDebitAmount() > 0) // Only show spending categories
            .sorted((e1, e2) -> Double.compare(e2.getValue().getDebitAmount(), e1.getValue().getDebitAmount()))
            .forEach(entry -> {
                String category = entry.getKey();
                double amount = entry.getValue().getDebitAmount();
                String label = category.replace("_", " ") + " ($" + String.format("%.0f", amount) + ")";
                pieChartData.add(new PieChart.Data(label, amount));
            });

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Spending by Category");
        pieChart.setLegendVisible(true);
        pieChart.setPrefSize(600, 400);

        return pieChart;
    }

    // Suspicious Transactions Alert
    public VBox createSuspiciousTransactionsAlert(List<Transaction> transactions) {
        List<Transaction> suspiciousTransactions = advancedAnalytics.detectSuspiciousTransactions(transactions);

        VBox alertBox = new VBox(10);
        alertBox.setPadding(new javafx.geometry.Insets(15));
        
        Label title = new Label("⚠️ Fraud Detection Alert");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        if (suspiciousTransactions.isEmpty()) {
            Label noAlert = new Label("No suspicious transactions detected. All transactions appear normal.");
            noAlert.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            alertBox.getChildren().addAll(title, noAlert);
        } else {
            Label subtitle = new Label("Large transactions requiring attention:");
            subtitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            
            VBox transactionsList = new VBox(5);
            suspiciousTransactions.stream().limit(10).forEach(txn -> {
                String alertText = String.format("• $%.2f - %s (%s) on %s at %s", 
                    txn.getAmount(), 
                    txn.getTransactionType(),
                    txn.getMerchant(),
                    txn.getTransactionDate(),
                    txn.getLocation());
                Label alertLabel = new Label(alertText);
                alertLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                transactionsList.getChildren().add(alertLabel);
            });
            
            if (suspiciousTransactions.size() > 10) {
                Label moreLabel = new Label("... and " + (suspiciousTransactions.size() - 10) + " more");
                moreLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                transactionsList.getChildren().add(moreLabel);
            }
            
            alertBox.getChildren().addAll(title, subtitle, transactionsList);
        }
        
        alertBox.setStyle("-fx-background-color: #fff3cd; -fx-border-color: #ffeaa7; -fx-border-radius: 5; -fx-padding: 15;");
        
        return alertBox;
    }

    // Customer Behavior Analysis
    public VBox createCustomerBehaviorAnalysis(List<Transaction> allTransactions) {
        Map<String, AdvancedAnalyticsServiceSimple.CustomerBehavior> customerBehaviors = 
            advancedAnalytics.analyzeCustomerBehavior(allTransactions);

        VBox analysisBox = new VBox(15);
        analysisBox.setPadding(new javafx.geometry.Insets(15));
        
        Label title = new Label("👥 Customer Behavior Analysis");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        VBox customersList = new VBox(10);
        
        customerBehaviors.entrySet().stream()
            .sorted((e1, e2) -> Double.compare(e2.getValue().getTotalEarned(), e1.getValue().getTotalEarned()))
            .forEach(entry -> {
                AdvancedAnalyticsServiceSimple.CustomerBehavior behavior = entry.getValue();
                
                VBox customerBox = new VBox(5);
                customerBox.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 5; -fx-padding: 10;");
                
                Label customerId = new Label("Customer: " + behavior.getCustomerId());
                customerId.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                
                Label earned = new Label("💰 Total Earned: $" + String.format("%.2f", behavior.getTotalEarned()));
                Label spent = new Label("💳 Total Spent: $" + String.format("%.2f", behavior.getTotalSpent()));
                Label netBalance = new Label("📊 Net Balance: $" + String.format("%.2f", behavior.getNetBalance()));
                Label transactions = new Label("📈 Transactions: " + behavior.getTransactionCount());
                Label favoriteCategory = new Label("🏪 Favorite Category: " + behavior.getMostFrequentCategory().replace("_", " "));
                
                // Color code net balance
                if (behavior.getNetBalance() >= 0) {
                    netBalance.setStyle("-fx-text-fill: green;");
                } else {
                    netBalance.setStyle("-fx-text-fill: red;");
                }
                
                customerBox.getChildren().addAll(customerId, earned, spent, netBalance, transactions, favoriteCategory);
                customersList.getChildren().add(customerBox);
            });
        
        analysisBox.getChildren().addAll(title, customersList);
        return analysisBox;
    }

    // Category Comparison Bar Chart
    public BarChart<String, Number> createCategoryComparisonChart(List<Transaction> transactions) {
        Map<String, AdvancedAnalyticsServiceSimple.CategoryAnalysis> categoryAnalysis = 
            advancedAnalytics.analyzeByCategory(transactions);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Category");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount ($)");

        XYChart.Series<String, Number> creditSeries = new XYChart.Series<>();
        creditSeries.setName("Income");
        
        XYChart.Series<String, Number> debitSeries = new XYChart.Series<>();
        debitSeries.setName("Spending");

        categoryAnalysis.entrySet().stream()
            .sorted((e1, e2) -> Double.compare(e2.getValue().getTotalAmount(), e1.getValue().getTotalAmount()))
            .limit(10) // Top 10 categories
            .forEach(entry -> {
                String category = entry.getKey().replace("_", " ");
                AdvancedAnalyticsServiceSimple.CategoryAnalysis analysis = entry.getValue();
                
                creditSeries.getData().add(new XYChart.Data<>(category, analysis.getCreditAmount()));
                debitSeries.getData().add(new XYChart.Data<>(category, analysis.getDebitAmount()));
            });

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Category-wise Income vs Spending Comparison");
        barChart.getData().addAll(creditSeries, debitSeries);
        barChart.setPrefSize(900, 500);

        return barChart;
    }

    // Complete Advanced Analytics Dashboard
    public VBox createAdvancedAnalyticsDashboard(List<Transaction> transactions) {
        VBox dashboard = new VBox(20);
        dashboard.setPadding(new javafx.geometry.Insets(20));

        // Suspicious Transactions Alert
        VBox suspiciousAlert = createSuspiciousTransactionsAlert(transactions);
        
        // Monthly Spending Chart
        BarChart<String, Number> monthlyChart = createMonthlySpendingChart(transactions);
        
        // Category Spending Pie Chart
        PieChart categoryPieChart = createCategorySpendingPieChart(transactions);
        
        // Category Comparison Chart
        BarChart<String, Number> categoryComparisonChart = createCategoryComparisonChart(transactions);
        
        // Customer Behavior Analysis
        VBox customerBehavior = createCustomerBehaviorAnalysis(transactions);

        dashboard.getChildren().addAll(
            suspiciousAlert,
            monthlyChart,
            categoryPieChart,
            categoryComparisonChart,
            customerBehavior
        );
        
        return dashboard;
    }
}
