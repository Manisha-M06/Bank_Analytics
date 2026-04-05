package com.bank.service;

import com.bank.model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.List;
import java.util.Map;

public class UserFriendlyChartService {

    private AdvancedAnalyticsServiceSimple advancedAnalytics;

    public UserFriendlyChartService() {
        this.advancedAnalytics = new AdvancedAnalyticsServiceSimple();
    }

    // Simple Money Overview - Total Income vs Expenses
    public VBox createSimpleMoneyOverview(List<Transaction> transactions) {
        VBox container = new VBox(15);
        container.setPadding(new javafx.geometry.Insets(20));
        container.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 10; -fx-padding: 20;");

        Label title = new Label("💰 Money Overview");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#2c3e50"));

        // Calculate totals
        double totalIncome = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.CREDIT)
            .mapToDouble(Transaction::getAmount)
            .sum();

        double totalExpenses = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .mapToDouble(Transaction::getAmount)
            .sum();

        double balance = totalIncome - totalExpenses;

        // Create summary cards
        HBox cardsRow = new HBox(15);
        
        VBox incomeCard = createSummaryCard("💵 Total Income", String.format("$%,.2f", totalIncome), "#27ae60");
        VBox expenseCard = createSummaryCard("💸 Total Expenses", String.format("$%,.2f", totalExpenses), "#e74c3c");
        VBox balanceCard = createSummaryCard(balance >= 0 ? "✅ Balance" : "⚠️ Balance", 
                                           String.format("$%,.2f", Math.abs(balance)), 
                                           balance >= 0 ? "#3498db" : "#f39c12");

        cardsRow.getChildren().addAll(incomeCard, expenseCard, balanceCard);

        // Simple Pie Chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Income $" + String.format("%,.0f", totalIncome), totalIncome),
            new PieChart.Data("Expenses $" + String.format("%,.0f", totalExpenses), totalExpenses)
        );

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Income vs Expenses");
        pieChart.setLegendVisible(true);
        pieChart.setPrefSize(400, 300);

        // Customize colors
        pieChart.getData().get(0).getNode().setStyle("-fx-pie-color: #27ae60;");
        pieChart.getData().get(1).getNode().setStyle("-fx-pie-color: #e74c3c;");

        container.getChildren().addAll(title, cardsRow, pieChart);
        return container;
    }

    // Monthly Spending Trend - Easy to understand line chart
    public VBox createMonthlySpendingTrend(List<Transaction> transactions) {
        VBox container = new VBox(15);
        container.setPadding(new javafx.geometry.Insets(20));
        container.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 10; -fx-padding: 20;");

        Label title = new Label("📈 Monthly Spending Trend");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#2c3e50"));

        Label subtitle = new Label("How much you spent each month");
        subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        subtitle.setTextFill(Color.web("#7f8c8d"));

        Map<String, Double> monthlySpending = advancedAnalytics.analyzeMonthlySpendingPatterns(transactions);

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");
        xAxis.setTickLabelFont(Font.font("Arial", 12));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount ($)");
        yAxis.setTickLabelFont(Font.font("Arial", 12));

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Your Monthly Spending Pattern");
        lineChart.setPrefSize(700, 400);
        lineChart.setCreateSymbols(true);
        lineChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Spending");

        monthlySpending.forEach((month, amount) -> {
            XYChart.Data<String, Number> data = new XYChart.Data<>(month, amount);
            series.getData().add(data);
            
            // Add tooltip
            Tooltip tooltip = new Tooltip(month + ": $" + String.format("%,.2f", amount));
            Tooltip.install(data.getNode(), tooltip);
        });

        lineChart.getData().add(series);

        // Style the line
        series.getNode().setStyle("-fx-stroke: #3498db; -fx-stroke-width: 3px;");

        container.getChildren().addAll(title, subtitle, lineChart);
        return container;
    }

    // Top Spending Categories - Clear and simple
    public VBox createTopSpendingCategories(List<Transaction> transactions) {
        VBox container = new VBox(15);
        container.setPadding(new javafx.geometry.Insets(20));
        container.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 10; -fx-padding: 20;");

        Label title = new Label("🛍️ Where You Spend Most");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#2c3e50"));

        Label subtitle = new Label("Your top spending categories");
        subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        subtitle.setTextFill(Color.web("#7f8c8d"));

        Map<String, AdvancedAnalyticsServiceSimple.CategoryAnalysis> categoryAnalysis = 
            advancedAnalytics.analyzeByCategory(transactions);

        // Create horizontal bar chart for better readability
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Category");
        xAxis.setTickLabelFont(Font.font("Arial", 11));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount ($)");
        yAxis.setTickLabelFont(Font.font("Arial", 11));

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Top Spending Categories");
        barChart.setPrefSize(700, 400);
        barChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Spending");

        // Get top 5 spending categories
        categoryAnalysis.entrySet().stream()
            .filter(entry -> entry.getValue().getDebitAmount() > 0)
            .sorted((e1, e2) -> Double.compare(e2.getValue().getDebitAmount(), e1.getValue().getDebitAmount()))
            .limit(5)
            .forEach(entry -> {
                String category = entry.getKey().replace("_", " ");
                double amount = entry.getValue().getDebitAmount();
                XYChart.Data<String, Number> data = new XYChart.Data<>(category, amount);
                series.getData().add(data);
                
                // Add tooltip
                Tooltip tooltip = new Tooltip(category + ": $" + String.format("%,.2f", amount));
                Tooltip.install(data.getNode(), tooltip);
            });

        barChart.getData().add(series);

        // Style bars
        series.getNode().setStyle("-fx-bar-fill: #9b59b6;");

        container.getChildren().addAll(title, subtitle, barChart);
        return container;
    }

    // Suspicious Transactions - Simple alert system
    public VBox createFraudAlert(List<Transaction> transactions) {
        VBox container = new VBox(15);
        container.setPadding(new javafx.geometry.Insets(20));

        List<Transaction> suspiciousTransactions = advancedAnalytics.detectSuspiciousTransactions(transactions);

        Label title = new Label("🔍 Large Transaction Alert");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#2c3e50"));

        if (suspiciousTransactions.isEmpty()) {
            VBox noAlertBox = new VBox(10);
            noAlertBox.setStyle("-fx-background-color: #d4edda; -fx-border-color: #c3e6cb; -fx-border-radius: 8; -fx-padding: 15;");
            
            Label goodNews = new Label("✅ All transactions look normal!");
            goodNews.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            goodNews.setTextFill(Color.web("#155724"));
            
            Label explanation = new Label("No unusually large transactions were detected.");
            explanation.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            explanation.setTextFill(Color.web("#155724"));
            
            noAlertBox.getChildren().addAll(goodNews, explanation);
            container.getChildren().addAll(title, noAlertBox);
        } else {
            VBox alertBox = new VBox(10);
            alertBox.setStyle("-fx-background-color: #fff3cd; -fx-border-color: #ffeaa7; -fx-border-radius: 8; -fx-padding: 15;");
            
            Label alertTitle = new Label("⚠️ Found " + suspiciousTransactions.size() + " large transactions");
            alertTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            alertTitle.setTextFill(Color.web("#856404"));
            
            Label explanation = new Label("These transactions are larger than your normal spending:");
            explanation.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
            explanation.setTextFill(Color.web("#856404"));
            
            VBox transactionsList = new VBox(5);
            suspiciousTransactions.stream().limit(5).forEach(txn -> {
                String alertText = String.format("💰 $%,.2f - %s on %s", 
                    txn.getAmount(), 
                    txn.getMerchant(),
                    txn.getTransactionDate());
                Label alertLabel = new Label(alertText);
                alertLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                alertLabel.setTextFill(Color.web("#856404"));
                transactionsList.getChildren().add(alertLabel);
            });
            
            if (suspiciousTransactions.size() > 5) {
                Label moreLabel = new Label("... and " + (suspiciousTransactions.size() - 5) + " more");
                moreLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                moreLabel.setTextFill(Color.web("#856404"));
                transactionsList.getChildren().add(moreLabel);
            }
            
            alertBox.getChildren().addAll(alertTitle, explanation, transactionsList);
            container.getChildren().addAll(title, alertBox);
        }
        
        return container;
    }

    // Customer Summary - Simple customer profiles
    public VBox createCustomerSummary(List<Transaction> transactions) {
        VBox container = new VBox(15);
        container.setPadding(new javafx.geometry.Insets(20));
        container.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-radius: 10; -fx-padding: 20;");

        Label title = new Label("👥 Customer Summary");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#2c3e50"));

        Label subtitle = new Label("How each customer is doing");
        subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        subtitle.setTextFill(Color.web("#7f8c8d"));

        Map<String, AdvancedAnalyticsServiceSimple.CustomerBehavior> customerBehaviors = 
            advancedAnalytics.analyzeCustomerBehavior(transactions);

        VBox customersList = new VBox(10);
        
        customerBehaviors.entrySet().stream()
            .sorted((e1, e2) -> Double.compare(e2.getValue().getTotalEarned(), e1.getValue().getTotalEarned()))
            .forEach(entry -> {
                AdvancedAnalyticsServiceSimple.CustomerBehavior behavior = entry.getValue();
                
                VBox customerCard = new VBox(8);
                customerCard.setStyle("-fx-background-color: white; -fx-border-color: #dee2e6; -fx-border-radius: 8; -fx-padding: 15;");
                
                Label customerName = new Label("Customer " + behavior.getCustomerId());
                customerName.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                customerName.setTextFill(Color.web("#2c3e50"));
                
                HBox statsRow = new HBox(20);
                
                VBox earnedBox = createMiniStat("💰 Earned", String.format("$%,.0f", behavior.getTotalEarned()), "#27ae60");
                VBox spentBox = createMiniStat("💳 Spent", String.format("$%,.0f", behavior.getTotalSpent()), "#e74c3c");
                VBox balanceBox = createMiniStat("📊 Balance", String.format("$%,.0f", behavior.getNetBalance()), 
                                               behavior.getNetBalance() >= 0 ? "#3498db" : "#f39c12");
                
                statsRow.getChildren().addAll(earnedBox, spentBox, balanceBox);
                
                Label favoriteCategory = new Label("🏪 Favorite: " + behavior.getMostFrequentCategory().replace("_", " "));
                favoriteCategory.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
                favoriteCategory.setTextFill(Color.web("#7f8c8d"));
                
                customerCard.getChildren().addAll(customerName, statsRow, favoriteCategory);
                customersList.getChildren().add(customerCard);
            });
        
        container.getChildren().addAll(title, subtitle, customersList);
        return container;
    }

    // Helper methods
    private VBox createSummaryCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-border-color: " + color + "; -fx-border-radius: 8; -fx-padding: 15; -fx-border-width: 2;");
        card.setPrefWidth(200);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        titleLabel.setTextFill(Color.web("#7f8c8d"));
        titleLabel.setTextAlignment(TextAlignment.CENTER);
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        valueLabel.setTextFill(Color.web(color));
        valueLabel.setTextAlignment(TextAlignment.CENTER);
        
        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private VBox createMiniStat(String title, String value, String color) {
        VBox stat = new VBox(5);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
        titleLabel.setTextFill(Color.web("#7f8c8d"));
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        valueLabel.setTextFill(Color.web(color));
        
        stat.getChildren().addAll(titleLabel, valueLabel);
        return stat;
    }

    // Main Dashboard - All charts in one place
    public VBox createUserFriendlyDashboard(List<Transaction> transactions) {
        VBox dashboard = new VBox(20);
        dashboard.setPadding(new javafx.geometry.Insets(20));
        dashboard.setStyle("-fx-background-color: #ecf0f1;");

        // Header
        Label mainTitle = new Label("📊 Your Banking Dashboard");
        mainTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        mainTitle.setTextFill(Color.web("#2c3e50"));
        mainTitle.setTextAlignment(TextAlignment.CENTER);

        // Main overview
        VBox moneyOverview = createSimpleMoneyOverview(transactions);
        
        // Charts row
        HBox chartsRow = new HBox(20);
        chartsRow.getChildren().addAll(
            createMonthlySpendingTrend(transactions),
            createTopSpendingCategories(transactions)
        );
        
        // Bottom row
        HBox bottomRow = new HBox(20);
        bottomRow.getChildren().addAll(
            createFraudAlert(transactions),
            createCustomerSummary(transactions)
        );

        dashboard.getChildren().addAll(mainTitle, moneyOverview, chartsRow, bottomRow);
        
        return dashboard;
    }
}
