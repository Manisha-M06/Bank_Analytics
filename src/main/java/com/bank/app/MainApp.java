package com.bank.app;

import com.bank.model.Transaction;
import com.bank.service.AnalyticsService;
import com.bank.service.AdvancedAnalyticsServiceSimple;
import com.bank.service.AdvancedChartService;
import com.bank.service.ChartService;
import com.bank.service.SimpleChartService;
import com.bank.service.CreditDebitFraudService;
import com.bank.util.CsvReader;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class MainApp extends Application {
    
    private TableView<Transaction> transactionTable;
    private ObservableList<Transaction> transactionData;
    private AnalyticsService analyticsService;
    private AdvancedAnalyticsServiceSimple advancedAnalyticsService;
    private ChartService chartService;
    private AdvancedChartService advancedChartService;
    private SimpleChartService simpleChartService;
    private CreditDebitFraudService creditDebitFraudService;
    private CsvReader csvReader;
    private List<Transaction> allTransactions;

    private Label totalAmountLabel;
    private Label averageAmountLabel;
    private Label debitCountLabel;
    private Label creditCountLabel;
    private Label successCountLabel;
    private Label failedCountLabel;

    @Override
    public void start(Stage primaryStage) {
        analyticsService = new AnalyticsService();
        advancedAnalyticsService = new AdvancedAnalyticsServiceSimple();
        chartService = new ChartService();
        advancedChartService = new AdvancedChartService();
        simpleChartService = new SimpleChartService();
        creditDebitFraudService = new CreditDebitFraudService();
        csvReader = new CsvReader();

        primaryStage.setTitle("Banking Transaction Analytics");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        VBox analyticsBox = createAnalyticsSummary();
        root.setTop(analyticsBox);

        TabPane tabPane = new TabPane();
        
        Tab dataTab = new Tab("Data View");
        dataTab.setClosable(false);
        VBox dataContent = createDataView();
        dataTab.setContent(dataContent);
        
        Tab chartsTab = new Tab("Basic Visualizations");
        chartsTab.setClosable(false);
        ScrollPane chartsContent = new ScrollPane();
        chartsContent.setFitToWidth(true);
        chartsContent.setFitToHeight(true);
        chartsTab.setContent(chartsContent);
        
        Tab simpleTab = new Tab("Simple Dashboard");
        simpleTab.setClosable(false);
        ScrollPane simpleContent = new ScrollPane();
        simpleContent.setFitToWidth(true);
        simpleContent.setFitToHeight(true);
        simpleTab.setContent(simpleContent);
        
        Tab creditDebitFraudTab = new Tab("Credit/Debit/Fraud");
        creditDebitFraudTab.setClosable(false);
        ScrollPane creditDebitFraudContent = new ScrollPane();
        creditDebitFraudContent.setFitToWidth(true);
        creditDebitFraudContent.setFitToHeight(true);
        creditDebitFraudTab.setContent(creditDebitFraudContent);
        
        Tab advancedTab = new Tab("Advanced Analytics");
        advancedTab.setClosable(false);
        ScrollPane advancedContent = new ScrollPane();
        advancedContent.setFitToWidth(true);
        advancedContent.setFitToHeight(true);
        advancedTab.setContent(advancedContent);
        
        tabPane.getTabs().addAll(dataTab, chartsTab, simpleTab, creditDebitFraudTab, advancedTab);
        root.setCenter(tabPane);

        loadTransactionData(chartsContent, advancedContent, simpleContent, creditDebitFraudContent);

        Scene scene = new Scene(root, 1600, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createAnalyticsSummary() {
        VBox analyticsBox = new VBox(15);
        analyticsBox.setPadding(new Insets(15));
        analyticsBox.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-radius: 5;");

        Label title = new Label("Transaction Analytics Summary");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setAlignment(Pos.CENTER);

        HBox firstRow = new HBox(20);
        firstRow.setAlignment(Pos.CENTER);
        
        totalAmountLabel = new Label("Total Amount: $0.00");
        totalAmountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        averageAmountLabel = new Label("Average Amount: $0.00");
        averageAmountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        HBox secondRow = new HBox(20);
        secondRow.setAlignment(Pos.CENTER);
        
        debitCountLabel = new Label("Debit Transactions: 0");
        debitCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        creditCountLabel = new Label("Credit Transactions: 0");
        creditCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        HBox thirdRow = new HBox(20);
        thirdRow.setAlignment(Pos.CENTER);
        
        successCountLabel = new Label("Successful: 0");
        successCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        
        failedCountLabel = new Label("Failed: 0");
        failedCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        firstRow.getChildren().addAll(totalAmountLabel, averageAmountLabel);
        secondRow.getChildren().addAll(debitCountLabel, creditCountLabel);
        thirdRow.getChildren().addAll(successCountLabel, failedCountLabel);

        analyticsBox.getChildren().addAll(title, firstRow, secondRow, thirdRow);
        return analyticsBox;
    }

    private void setupTableColumns() {
        TableColumn<Transaction, String> transactionIdCol = new TableColumn<>("Transaction ID");
        transactionIdCol.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        transactionIdCol.setPrefWidth(150);

        TableColumn<Transaction, String> accountIdCol = new TableColumn<>("Account ID");
        accountIdCol.setCellValueFactory(new PropertyValueFactory<>("accountId"));
        accountIdCol.setPrefWidth(120);

        TableColumn<Transaction, String> customerIdCol = new TableColumn<>("Customer ID");
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerIdCol.setPrefWidth(120);

        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
        typeCol.setPrefWidth(80);

        TableColumn<Transaction, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setPrefWidth(100);

        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
        dateCol.setPrefWidth(120);

        TableColumn<Transaction, String> merchantCol = new TableColumn<>("Merchant");
        merchantCol.setCellValueFactory(new PropertyValueFactory<>("merchant"));
        merchantCol.setPrefWidth(150);

        TableColumn<Transaction, String> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationCol.setPrefWidth(120);

        TableColumn<Transaction, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(80);

        transactionTable.getColumns().addAll(
                transactionIdCol, accountIdCol, customerIdCol, typeCol, 
                amountCol, dateCol, merchantCol, locationCol, statusCol
        );
    }

    private VBox createDataView() {
        transactionTable = new TableView<>();
        setupTableColumns();
        
        VBox tableBox = new VBox(10);
        tableBox.setPadding(new Insets(10));
        Label tableTitle = new Label("Transaction Details");
        tableTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        tableBox.getChildren().addAll(tableTitle, transactionTable);
        return tableBox;
    }

    private void loadTransactionData(ScrollPane chartsContent, ScrollPane advancedContent, ScrollPane simpleContent, ScrollPane creditDebitFraudContent) {
        allTransactions = csvReader.readTransactionsFromCsv();
        transactionData = FXCollections.observableArrayList(allTransactions);
        transactionTable.setItems(transactionData);

        updateAnalyticsDisplay(allTransactions);
        
        VBox chartsDashboard = chartService.createChartsDashboard(allTransactions);
        chartsContent.setContent(chartsDashboard);
        
        VBox advancedDashboard = advancedChartService.createAdvancedAnalyticsDashboard(allTransactions);
        advancedContent.setContent(advancedDashboard);
        
        // Use the new simple dashboard
        VBox simpleDashboard = simpleChartService.createSimpleDashboard(allTransactions);
        simpleContent.setContent(simpleDashboard);
        
        // Use the new credit/debit/fraud dashboard
        VBox creditDebitFraudDashboard = creditDebitFraudService.createCreditDebitFraudDashboard(allTransactions);
        creditDebitFraudContent.setContent(creditDebitFraudDashboard);
    }

    private void updateAnalyticsDisplay(List<Transaction> transactions) {
        double totalAmount = analyticsService.calculateTotalAmount(transactions);
        double averageAmount = analyticsService.calculateAverageAmount(transactions);
        Map<Transaction.TransactionType, Long> transactionsByType = analyticsService.countTransactionsByType(transactions);
        long successfulCount = analyticsService.countSuccessfulTransactions(transactions);
        long failedCount = analyticsService.countFailedTransactions(transactions);

        totalAmountLabel.setText(String.format("Total Amount: $%.2f", totalAmount));
        averageAmountLabel.setText(String.format("Average Amount: $%.2f", averageAmount));
        
        long debitCount = transactionsByType.getOrDefault(Transaction.TransactionType.DEBIT, 0L);
        long creditCount = transactionsByType.getOrDefault(Transaction.TransactionType.CREDIT, 0L);
        
        debitCountLabel.setText("Debit Transactions: " + debitCount);
        creditCountLabel.setText("Credit Transactions: " + creditCount);
        successCountLabel.setText("Successful: " + successfulCount);
        failedCountLabel.setText("Failed: " + failedCount);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
