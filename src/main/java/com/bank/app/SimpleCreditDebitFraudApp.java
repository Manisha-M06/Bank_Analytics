package com.bank.app;

import com.bank.model.Transaction;
import com.bank.service.CreditDebitFraudService;
import com.bank.util.CsvReader;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class SimpleCreditDebitFraudApp extends Application {

    private CreditDebitFraudService creditDebitFraudService;
    private CsvReader csvReader;

    @Override
    public void start(Stage primaryStage) {
        creditDebitFraudService = new CreditDebitFraudService();
        csvReader = new CsvReader();

        primaryStage.setTitle("Credit, Debit & Fraud Analysis");

        // Load transaction data
        List<Transaction> transactions = csvReader.readTransactionsFromCsv();
        
        // Create the dashboard
        VBox dashboard = creditDebitFraudService.createCreditDebitFraudDashboard(transactions);
        
        // Put in scroll pane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(dashboard);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        Scene scene = new Scene(scrollPane, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
