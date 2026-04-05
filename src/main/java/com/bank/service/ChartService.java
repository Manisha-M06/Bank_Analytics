package com.bank.service;

import com.bank.model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ChartService {

    public PieChart createTransactionTypePieChart(List<Transaction> transactions) {
        Map<Transaction.TransactionType, Long> typeCounts = transactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
                .collect(Collectors.groupingBy(Transaction::getTransactionType, Collectors.counting()));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        typeCounts.entrySet().forEach(entry -> {
            String typeName = entry.getKey().toString();
            Long count = entry.getValue();
            pieChartData.add(new PieChart.Data(typeName + " (" + count + ")", count));
        });

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Transaction Types Distribution");
        pieChart.setLegendVisible(true);
        pieChart.setPrefSize(400, 300);

        return pieChart;
    }

    public BarChart<String, Number> createDailyAmountBarChart(List<Transaction> transactions) {
        Map<LocalDate, Double> dailyAmounts = transactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
                .collect(Collectors.groupingBy(
                        Transaction::getTransactionDate,
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        List<LocalDate> sortedDates = dailyAmounts.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Total Amount ($)");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Daily Transaction Amounts");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        for (LocalDate date : sortedDates) {
            series.getData().add(new XYChart.Data<>(
                    date.format(formatter), 
                    dailyAmounts.get(date)
            ));
        }

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Daily Transaction Amounts");
        barChart.getData().add(series);
        barChart.setPrefSize(600, 400);

        return barChart;
    }

    public LineChart<String, Number> createTransactionTrendLineChart(List<Transaction> transactions) {
        Map<LocalDate, Long> dailyCounts = transactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
                .collect(Collectors.groupingBy(
                        Transaction::getTransactionDate,
                        Collectors.counting()
                ));

        List<LocalDate> sortedDates = dailyCounts.keySet().stream()
                .sorted()
                .collect(Collectors.toList());

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Transactions");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Transaction Count Trend");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        for (LocalDate date : sortedDates) {
            series.getData().add(new XYChart.Data<>(
                    date.format(formatter), 
                    dailyCounts.get(date)
            ));
        }

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Transaction Trends Over Time");
        lineChart.getData().add(series);
        lineChart.setPrefSize(600, 400);

        return lineChart;
    }

    public BarChart<String, Number> createMerchantAnalysisChart(List<Transaction> transactions) {
        Map<String, Double> merchantAmounts = transactions.stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
                .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
                .collect(Collectors.groupingBy(
                        Transaction::getMerchant,
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        List<Map.Entry<String, Double>> sortedMerchants = merchantAmounts.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toList());

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Merchant");
        
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Total Amount ($)");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Top Merchants by Amount");

        for (Map.Entry<String, Double> entry : sortedMerchants) {
            series.getData().add(new XYChart.Data<>(
                    entry.getKey(), 
                    entry.getValue()
            ));
        }

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Top 10 Merchants by Transaction Amount");
        barChart.getData().add(series);
        barChart.setPrefSize(700, 400);

        return barChart;
    }

    public VBox createChartsDashboard(List<Transaction> transactions) {
        VBox dashboard = new VBox(20);
        dashboard.setPadding(new javafx.geometry.Insets(20));

        PieChart pieChart = createTransactionTypePieChart(transactions);
        BarChart<String, Number> dailyBarChart = createDailyAmountBarChart(transactions);
        LineChart<String, Number> trendLineChart = createTransactionTrendLineChart(transactions);
        BarChart<String, Number> merchantChart = createMerchantAnalysisChart(transactions);

        dashboard.getChildren().addAll(pieChart, dailyBarChart, trendLineChart, merchantChart);
        
        return dashboard;
    }
}
