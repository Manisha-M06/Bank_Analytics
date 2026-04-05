# Banking Transaction Analytics with Visualization

A comprehensive JavaFX application for analyzing banking transaction data with advanced visualizations.

## Features

### 📊 Analytics Dashboard
- **Summary Statistics**: Total amount, average amount, transaction counts by type
- **Success/Failure Rates**: Real-time tracking of transaction status
- **Transaction Type Distribution**: Visual breakdown of DEBIT vs CREDIT transactions

### 📈 Visualizations
- **Pie Chart**: Transaction types distribution (DEBIT/CREDIT)
- **Bar Chart**: Daily transaction amounts over time
- **Line Chart**: Transaction trends and patterns
- **Merchant Analysis**: Top 10 merchants by transaction amount

### 📋 Data Management
- **CSV Data Import**: Automatic loading from `transactions.csv`
- **Tabbed Interface**: Separate views for data table and visualizations
- **Sortable Table**: Complete transaction details with all fields

## Project Structure

```
Banking_analytics/
├── pom.xml                          # Maven configuration with JavaFX dependencies
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── bank/
│       │           ├── app/
│       │           │   └── MainApp.java          # Main application with tabbed interface
│       │           ├── model/
│       │           │   └── Transaction.java       # Transaction data model
│       │           ├── service/
│       │           │   ├── AnalyticsService.java  # Business logic for calculations
│       │           │   └── ChartService.java      # Data visualization service
│       │           └── util/
│       │               └── CsvReader.java         # CSV parsing utility
│       └── resources/
│           └── transactions.csv                    # Sample transaction data
└── README.md
```

## Technology Stack

- **Java 17**: Modern Java with latest features
- **JavaFX 21**: Rich client application framework
- **Maven**: Dependency management and build tool
- **Charts**: JavaFX built-in charting library

## Data Model

The `Transaction` class includes:
- `transactionId`: Unique transaction identifier
- `accountId`: Source account ID
- `customerId`: Customer identifier
- `transactionType`: DEBIT or CREDIT
- `amount`: Transaction amount
- `transactionDate`: Date of transaction
- `merchant`: Merchant name
- `location`: Transaction location
- `status`: SUCCESS or FAILED

## CSV Format

The application expects transactions in this format:

```csv
transactionId,accountId,customerId,transactionType,amount,transactionDate,merchant,location,status
TXN001,ACC001,CUST001,CREDIT,1500.00,2024-01-15,Salary Deposit,New York,SUCCESS
TXN002,ACC001,CUST001,DEBIT,250.50,2024-01-16,Walmart,Los Angeles,SUCCESS
```

## How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- JavaFX runtime (included with Maven dependencies)

### Steps

1. **Navigate to project directory:**
   ```bash
   cd c:\Users\LOQ\Banking_analytics
   ```

2. **Compile the project:**
   ```bash
   mvn clean compile
   ```

3. **Run the application:**
   ```bash
   mvn clean javafx:run
   ```

### Alternative: Using IDE
1. Open the project in VS Code or IntelliJ IDEA
2. Ensure Java 17 is configured
3. Run the `MainApp` class directly

## Application Interface

### Main Dashboard
- **Top Panel**: Analytics summary with key metrics
- **Tabbed Interface**: 
  - **Data View**: Complete transaction table
  - **Visualizations**: Charts and graphs

### Visualizations Tab
1. **Transaction Types Pie Chart**: Shows DEBIT vs CREDIT distribution
2. **Daily Amounts Bar Chart**: Transaction amounts by date
3. **Trend Line Chart**: Transaction count over time
4. **Top Merchants Bar Chart**: Highest spending merchants

## Customization

### Adding New Charts
1. Add chart creation method in `ChartService.java`
2. Update `createChartsDashboard()` method
3. Rebuild and run the application

### Modifying Data Source
1. Update `CsvReader.java` for different file formats
2. Modify `Transaction.java` for new fields
3. Update table columns in `MainApp.java`

## Troubleshooting

### Common Issues

1. **JavaFX Runtime Issues**: Ensure JavaFX dependencies are properly configured
2. **CSV Not Found**: Verify `transactions.csv` is in `src/main/resources/`
3. **Compilation Errors**: Check Java version is 17+

### Performance Tips
- For large datasets (>10,000 transactions), consider pagination
- Use background threads for data loading
- Implement data caching for repeated calculations

## Future Enhancements

- [ ] Real-time data updates
- [ ] Export functionality (PDF, Excel)
- [ ] Advanced filtering and search
- [ ] Custom date range selection
- [ ] Multi-currency support
- [ ] Database integration

## License

This project is for educational purposes. Feel free to modify and enhance for your specific banking analytics needs.
