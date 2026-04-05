package com.bank.service;

import com.bank.model.Transaction;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class AdvancedAnalyticsService {

    // Transaction categories based on merchant names
    private static final Map<String, String> MERCHANT_CATEGORIES = Map.ofEntries(
        Map.entry("Salary Deposit", "INCOME"),
        Map.entry("Freelance Payment", "INCOME"),
        Map.entry("Bonus Payment", "INCOME"),
        Map.entry("Part-time Job", "INCOME"),
        Map.entry("Investment Return", "INCOME"),
        Map.entry("Dividend Payment", "INCOME"),
        Map.entry("Stock Dividend", "INCOME"),
        Map.entry("Business Income", "INCOME"),
        Map.entry("Project Payment", "INCOME"),
        Map.entry("Rental Income", "INCOME"),
        Map.entry("Interest Payment", "INCOME"),
        Map.entry("Cashback Reward", "INCOME"),
        Map.entry("Gift Payment", "INCOME"),
        Map.entry("Refund", "INCOME"),
        Map.entry("Online Sales", "INCOME"),
        Map.entry("Side Business", "INCOME"),
        Map.entry("Side Income", "INCOME"),
        Map.entry("Passive Income", "INCOME"),
        Map.entry("Royalties", "INCOME"),
        Map.entry("Licensing", "INCOME"),
        Map.entry("Affiliate", "INCOME"),
        Map.entry("Freelance Work", "INCOME"),
        Map.entry("Creative Work", "INCOME"),
        Map.entry("Photography Services", "INCOME"),
        Map.entry("Video Production", "INCOME"),
        Map.entry("Content Creation", "INCOME"),
        Map.entry("Graphic Design", "INCOME"),
        Map.entry("Consulting", "INCOME"),
        Map.entry("Consulting Fee", "INCOME"),
        Map.entry("E-commerce", "INCOME"),
        Map.entry("Digital Marketing", "INCOME"),
        Map.entry("Web Development", "INCOME"),
        Map.entry("Online Services", "INCOME"),
        Map.entry("Digital Products", "INCOME"),
        Map.entry("Web Hosting", "INCOME"),
        Map.entry("Domain Names", "INCOME"),
        Map.entry("Marketing Tools", "INCOME"),
        Map.entry("Courses", "INCOME"),
        Map.entry("Training", "INCOME"),
        Map.entry("Certification", "INCOME"),
        Map.entry("Photography Gear", "INCOME"),
        Map.entry("Camera Equipment", "INCOME"),
        Map.entry("Lighting", "INCOME"),
        Map.entry("Photo Editing Software", "INCOME"),
        Map.entry("Art Supplies", "INCOME"),
        Map.entry("Craft Materials", "INCOME"),
        Map.entry("Photography", "INCOME"),
        Map.entry("Workshop", "INCOME"),
        Map.entry("Art", "INCOME"),
        Map.entry("Design", "INCOME"),
        Map.entry("Hobbies", "INCOME"),
        Map.entry("Games", "INCOME"),
        Map.entry("Collectibles", "INCOME"),
        Map.entry("Toys", "INCOME"),
        Map.entry("Outdoor Gear", "INCOME"),
        Map.entry("Camping Equipment", "INCOME"),
        Map.entry("Hiking Gear", "INCOME"),
        Map.entry("Sports Equipment", "INCOME"),
        Map.entry("Smart Home", "INCOME"),
        Map.entry("Security System", "INCOME"),
        Map.entry("Smart Devices", "INCOME"),
        Map.entry("Home Automation", "INCOME"),
        Map.entry("IoT Devices", "INCOME"),
        Map.entry("Fitness Equipment", "INCOME"),
        Map.entry("Personal Training", "INCOME"),
        Map.entry("Gym Membership", "INCOME"),
        Map.entry("Yoga Classes", "INCOME"),
        Map.entry("Yoga Studio", "INCOME"),
        Map.entry("Software Tools", "INCOME"),
        Map.entry("Computer Upgrade", "INCOME"),
        Map.entry("Monitor", "INCOME"),
        Map.entry("Keyboard", "INCOME"),
        Map.entry("Real Estate", "INCOME"),
        Map.entry("Property Investment", "INCOME"),
        Map.entry("Legal Services", "INCOME"),
        Map.entry("Insurance Premium", "INCOME"),
        Map.entry("Real Estate Investment", "INCOME"),
        Map.entry("Property Development", "INCOME"),
        Map.entry("Land Purchase", "INCOME"),
        Map.entry("Legal Fees", "INCOME"),
        Map.entry("Portfolio Management", "INCOME"),
        Map.entry("Asset Management", "INCOME"),
        Map.entry("Wealth Management", "INCOME"),
        Map.entry("Tax Planning", "INCOME"),
        Map.entry("Rental Income", "INCOME"),
        Map.entry("Investment Returns", "INCOME"),
        Map.entry("Business Revenue", "INCOME"),
        
        // Housing
        Map.entry("Rent Payment", "HOUSING"),
        Map.entry("Mortgage Payment", "HOUSING"),
        Map.entry("Property Tax", "HOUSING"),
        Map.entry("Home Maintenance", "HOUSING"),
        Map.entry("Garden Supplies", "HOUSING"),
        Map.entry("Home Improvement", "HOUSING"),
        Map.entry("Furniture", "HOUSING"),
        Map.entry("Home Decor", "HOUSING"),
        Map.entry("Appliances", "HOUSING"),
        Map.entry("Tools", "HOUSING"),
        Map.entry("Landscaping", "HOUSING"),
        Map.entry("Patio", "HOUSING"),
        Map.entry("Outdoor Equipment", "HOUSING"),
        Map.entry("Home Renovation", "HOUSING"),
        Map.entry("Home Repair", "HOUSING"),
        Map.entry("Plumbing", "HOUSING"),
        Map.entry("Electrical", "HOUSING"),
        Map.entry("Painting", "HOUSING"),
        Map.entry("Renovation", "HOUSING"),
        Map.entry("Upgrades", "HOUSING"),
        Map.entry("Services", "HOUSING"),
        Map.entry("Home Security", "HOUSING"),
        Map.entry("Safety Equipment", "HOUSING"),
        Map.entry("Emergency Supplies", "HOUSING"),
        Map.entry("First Aid Kit", "HOUSING"),
        Map.entry("Smart Home Devices", "HOUSING"),
        Map.entry("Security System", "HOUSING"),
        Map.entry("Home Automation", "HOUSING"),
        Map.entry("IOT Devices", "HOUSING"),
        
        // Transportation
        Map.entry("Car Payment", "TRANSPORTATION"),
        Map.entry("Car Insurance", "TRANSPORTATION"),
        Map.entry("Auto Insurance", "TRANSPORTATION"),
        Map.entry("Gas Station", "TRANSPORTATION"),
        Map.entry("Gas", "TRANSPORTATION"),
        Map.entry("Fuel", "TRANSPORTATION"),
        Map.entry("Car Maintenance", "TRANSPORTATION"),
        Map.entry("Car Repair", "TRANSPORTATION"),
        Map.entry("Car Accessories", "TRANSPORTATION"),
        Map.entry("Tires", "TRANSPORTATION"),
        Map.entry("Auto Parts", "TRANSPORTATION"),
        Map.entry("Maintenance", "TRANSPORTATION"),
        Map.entry("Vehicle Upgrade", "TRANSPORTATION"),
        Map.entry("Uber", "TRANSPORTATION"),
        Map.entry("Transportation", "TRANSPORTATION"),
        Map.entry("Public Transport", "TRANSPORTATION"),
        Map.entry("Flight", "TRANSPORTATION"),
        Map.entry("Flight Booking", "TRANSPORTATION"),
        Map.entry("Hotel", "TRANSPORTATION"),
        Map.entry("Hotel Booking", "TRANSPORTATION"),
        Map.entry("Vacation Booking", "TRANSPORTATION"),
        Map.entry("Vacation Package", "TRANSPORTATION"),
        Map.entry("Tours", "TRANSPORTATION"),
        Map.entry("Travel", "TRANSPORTATION"),
        Map.entry("Taxi", "TRANSPORTATION"),
        Map.entry("Parking Garage", "TRANSPORTATION"),
        
        // Food & Dining
        Map.entry("Whole Foods Market", "FOOD_DINING"),
        Map.entry("Trader Joe's", "FOOD_DINING"),
        Map.entry("Costco", "FOOD_DINING"),
        Map.entry("Safeway", "FOOD_DINING"),
        Map.entry("Grocery Store", "FOOD_DINING"),
        Map.entry("Groceries", "FOOD_DINING"),
        Map.entry("Starbucks", "FOOD_DINING"),
        Map.entry("Coffee", "FOOD_DINING"),
        Map.entry("Coffee Shop", "FOOD_DINING"),
        Map.entry("Restaurant", "FOOD_DINING"),
        Map.entry("Dining Out", "FOOD_DINING"),
        Map.entry("Food Delivery", "FOOD_DINING"),
        Map.entry("Chipotle", "FOOD_DINING"),
        Map.entry("Subway", "FOOD_DINING"),
        Map.entry("Fast Food", "FOOD_DINING"),
        Map.entry("Ice Cream Shop", "FOOD_DINING"),
        
        // Shopping
        Map.entry("Walmart", "SHOPPING"),
        Map.entry("Target", "SHOPPING"),
        Map.entry("Amazon", "SHOPPING"),
        Map.entry("Amazon Prime", "SHOPPING"),
        Map.entry("Online Shopping", "SHOPPING"),
        Map.entry("Shopping", "SHOPPING"),
        Map.entry("Retail Shopping", "SHOPPING"),
        Map.entry("Nordstrom", "SHOPPING"),
        Map.entry("Clothing", "SHOPPING"),
        Map.entry("Clothing Store", "SHOPPING"),
        Map.entry("Fashion", "SHOPPING"),
        Map.entry("Designer Clothes", "SHOPPING"),
        Map.entry("Shoes", "SHOPPING"),
        Map.entry("Jewelry", "SHOPPING"),
        Map.entry("Perfume", "SHOPPING"),
        Map.entry("Accessories", "SHOPPING"),
        Map.entry("Retail", "SHOPPING"),
        Map.entry("Online", "SHOPPING"),
        Map.entry("E-commerce", "SHOPPING"),
        Map.entry("Department Store", "SHOPPING"),
        Map.entry("Electronics", "SHOPPING"),
        Map.entry("Electronics Store", "SHOPPING"),
        Map.entry("Apple Store", "SHOPPING"),
        Map.entry("Gaming Console", "SHOPPING"),
        Map.entry("Video Games", "SHOPPING"),
        Map.entry("Gaming Accessories", "SHOPPING"),
        Map.entry("Luxury Items", "SHOPPING"),
        Map.entry("Beauty", "SHOPPING"),
        Map.entry("Beauty Products", "SHOPPING"),
        Map.entry("Cosmetics", "SHOPPING"),
        Map.entry("Personal Care", "SHOPPING"),
        Map.entry("Home Depot", "SHOPPING"),
        Map.entry("Hardware Store", "SHOPPING"),
        Map.entry("Pet Store", "SHOPPING"),
        Map.entry("Pet Supplies", "SHOPPING"),
        
        // Entertainment
        Map.entry("Netflix", "ENTERTAINMENT"),
        Map.entry("Spotify", "ENTERTAINMENT"),
        Map.entry("Hulu", "ENTERTAINMENT"),
        Map.entry("Amazon Prime", "ENTERTAINMENT"),
        Map.entry("Streaming Services", "ENTERTAINMENT"),
        Map.entry("Subscription Services", "ENTERTAINMENT"),
        Map.entry("Subscription", "ENTERTAINMENT"),
        Map.entry("Streaming", "ENTERTAINMENT"),
        Map.entry("Music Streaming", "ENTERTAINMENT"),
        Map.entry("Video Streaming", "ENTENTAINMENT"),
        Map.entry("Podcast Subscription", "ENTERTAINMENT"),
        Map.entry("Magazine Subscription", "ENTERTAINMENT"),
        Map.entry("Concert Tickets", "ENTERTAINMENT"),
        Map.entry("Concerts", "ENTERTAINMENT"),
        Map.entry("Events", "ENTERTAINMENT"),
        Map.entry("Tickets", "ENTERTAINMENT"),
        Map.entry("Entertainment", "ENTERTAINMENT"),
        Map.entry("Theater", "ENTERTAINMENT"),
        Map.entry("Movie Theater", "ENTERTAINMENT"),
        Map.entry("Books", "ENTERTAINMENT"),
        Map.entry("E-books", "ENTERTAINMENT"),
        Map.entry("Audiobooks", "ENTERTAINMENT"),
        Map.entry("Magazine Subscription", "ENTERTAINMENT"),
        Map.entry("Gaming", "ENTERTAINMENT"),
        Map.entry("Games", "ENTERTAINMENT"),
        
        // Health & Wellness
        Map.entry("Pharmacy", "HEALTH_WELLNESS"),
        Map.entry("Healthcare", "HEALTH_WELLNESS"),
        Map.entry("Medical Expenses", "HEALTH_WELLNESS"),
        Map.entry("Health Insurance", "HEALTH_WELLNESS"),
        Map.entry("Insurance", "HEALTH_WELLNESS"),
        Map.entry("Insurance Payment", "HEALTH_WELLNESS"),
        Map.entry("Gym Membership", "HEALTH_WELLNESS"),
        Map.entry("Fitness", "HEALTH_WELLNESS"),
        Map.entry("Fitness Equipment", "HEALTH_WELLNESS"),
        Map.entry("Wellness", "HEALTH_WELLNESS"),
        Map.entry("Nutrition", "HEALTH_WELLNESS"),
        Map.entry("Personal Trainer", "HEALTH_WELLNESS"),
        Map.entry("Yoga Classes", "HEALTH_WELLNESS"),
        Map.entry("Yoga Studio", "HEALTH_WELLNESS"),
        
        // Utilities
        Map.entry("Utility Bill", "UTILITIES"),
        Map.entry("Utilities", "UTILITIES"),
        Map.entry("Internet Bill", "UTILITIES"),
        Map.entry("Internet", "UTILITIES"),
        Map.entry("Phone Bill", "UTILITIES"),
        Map.entry("Phone", "UTILITIES"),
        Map.entry("Cloud Storage", "UTILITIES"),
        Map.entry("Software", "UTILITIES"),
        Map.entry("Apps", "UTILITIES"),
        Map.entry("Music", "UTILITIES"),
        
        // Financial Services
        Map.entry("Banking Fees", "FINANCIAL"),
        Map.entry("Banking", "FINANCIAL"),
        Map.entry("Banking Services", "FINANCIAL"),
        Map.entry("Financial Advisor", "FINANCIAL"),
        Map.entry("Financial Planning", "FINANCIAL"),
        Map.entry("Finance", "FINANCIAL"),
        Map.entry("Investment", "FINANCIAL"),
        Map.entry("Investment Advisory", "FINANCIAL"),
        Map.entry("Trading", "FINANCIAL"),
        Map.entry("Stock Trading", "FINANCIAL"),
        Map.entry("Portfolio", "FINANCIAL"),
        Map.entry("Stocks", "FINANCIAL"),
        Map.entry("Bonds", "FINANCIAL"),
        Map.entry("Commodities", "FINANCIAL"),
        Map.entry("Accounting", "FINANCIAL"),
        Map.entry("Tax Payment", "FINANCIAL"),
        Map.entry("Legal Fees", "FINANCIAL"),
        Map.entry("Professional", "FINANCIAL"),
        
        // Education
        Map.entry("Online Course", "EDUCATION"),
        Map.entry("Education", "EDUCATION"),
        Map.entry("Courses", "EDUCATION"),
        Map.entry("Tutoring", "EDUCATION"),
        Map.entry("Books", "EDUCATION"),
        Map.entry("Professional Development", "EDUCATION"),
        Map.entry("Conference Fee", "EDUCATION"),
        Map.entry("Networking Event", "EDUCATION"),
        
        // Business
        Map.entry("Office Supplies", "BUSINESS"),
        Map.entry("Office Equipment", "BUSINESS"),
        Map.entry("Software License", "BUSINESS"),
        Map.entry("Hardware", "BUSINESS"),
        Map.entry("IT Support", "BUSINESS"),
        Map.entry("Property Management", "BUSINESS"),
        Map.entry("Maintenance", "BUSINESS"),
        Map.entry("Repairs", "BUSINESS"),
        Map.entry("Consulting", "BUSINESS"),
        Map.entry("Marketing", "BUSINESS"),
        Map.entry("Printing", "BUSINESS"),
        Map.entry("Shipping", "BUSINESS"),
        Map.entry("Postage", "BUSINESS"),
        
        // Miscellaneous
        Map.entry("Survey Reward", "MISCELLANEOUS"),
        Map.entry("Cashback Bonus", "MISCELLANEOUS"),
        Map.entry("Gift Card", "MISCELLANEOUS"),
        Map.entry("Recreation", "MISCELLANEOUS"),
        Map.entry("Services", "MISCELLANEOUS"),
        Map.entry("Other", "MISCELLANEOUS")
    );

    // Monthly Spending Patterns Analysis
    public Map<String, Double> analyzeMonthlySpendingPatterns(List<Transaction> transactions) {
        Map<String, Double> monthlySpending = new LinkedHashMap<>();
        
        // Group by month and calculate total spending
        Map<YearMonth, Double> monthlyTotals = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .collect(Collectors.groupingBy(
                t -> YearMonth.from(t.getTransactionDate()),
                Collectors.summingDouble(Transaction::getAmount)
            ));
        
        // Sort by month and format
        monthlyTotals.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                String monthKey = entry.getKey().format(DateTimeFormatter.ofPattern("MMM yyyy"));
                monthlySpending.put(monthKey, entry.getValue());
            });
        
        return monthlySpending;
    }

    // Fraud Detection - Identify suspicious large transactions
    public List<Transaction> detectSuspiciousTransactions(List<Transaction> transactions) {
        // Calculate average and standard deviation for transaction amounts
        List<Double> amounts = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .map(Transaction::getAmount)
            .collect(Collectors.toList());
        
        if (amounts.isEmpty()) {
            return new ArrayList<>();
        }
        
        double average = amounts.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double max = amounts.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        double threshold = average + (max - average) * 0.7; // 70% of the way to max
        
        // Find transactions exceeding threshold
        return transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
            .filter(t -> t.getAmount() > threshold)
            .sorted((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount())) // Sort by amount descending
            .collect(Collectors.toList());
    }

    // Category-wise Analysis
    public Map<String, CategoryAnalysis> analyzeByCategory(List<Transaction> transactions) {
        Map<String, List<Transaction>> categorizedTransactions = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .collect(Collectors.groupingBy(t -> getCategory(t.getMerchant())));
        
        Map<String, CategoryAnalysis> analysis = new LinkedHashMap<>();
        
        categorizedTransactions.forEach((category, categoryTransactions) -> {
            double totalAmount = categoryTransactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
            
            double creditAmount = categoryTransactions.stream()
                .filter(t -> t.getTransactionType() == Transaction.TransactionType.CREDIT)
                .mapToDouble(Transaction::getAmount)
                .sum();
            
            double debitAmount = categoryTransactions.stream()
                .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
                .mapToDouble(Transaction::getAmount)
                .sum();
            
            long transactionCount = categoryTransactions.size();
            double averageAmount = transactionCount > 0 ? totalAmount / transactionCount : 0;
            
            analysis.put(category, new CategoryAnalysis(
                category, totalAmount, creditAmount, debitAmount, 
                transactionCount, averageAmount
            ));
        });
        
        return analysis;
    }

    // Get category for merchant
    private String getCategory(String merchant) {
        return MERCHANT_CATEGORIES.getOrDefault(merchant, "MISCELLANEOUS");
    }

    // Customer spending behavior analysis
    public Map<String, CustomerBehavior> analyzeCustomerBehavior(List<Transaction> transactions) {
        Map<String, List<Transaction>> customerTransactions = transactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
            .collect(Collectors.groupingBy(Transaction::getCustomerId));
        
        Map<String, CustomerBehavior> behaviors = new LinkedHashMap<>();
        
        customerTransactions.forEach((customerId, customerTxns) -> {
            double totalSpent = customerTxns.stream()
                .filter(t -> t.getTransactionType() == Transaction.TransactionType.DEBIT)
                .mapToDouble(Transaction::getAmount)
                .sum();
            
            double totalEarned = customerTxns.stream()
                .filter(t -> t.getTransactionType() == Transaction.TransactionType.CREDIT)
                .mapToDouble(Transaction::getAmount)
                .sum();
            
            long transactionCount = customerTxns.size();
            String mostFrequentCategory = getMostFrequentCategory(customerTxns);
            
            behaviors.put(customerId, new CustomerBehavior(
                customerId, totalSpent, totalEarned, transactionCount, mostFrequentCategory
            ));
        });
        
        return behaviors;
    }

    private String getMostFrequentCategory(List<Transaction> transactions) {
        Map<String, Long> categoryCounts = transactions.stream()
            .collect(Collectors.groupingBy(t -> getCategory(t.getMerchant()), Collectors.counting()));
        
        return categoryCounts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("MISCELLANEOUS");
    }

    // Data classes for analysis results
    public static class CategoryAnalysis {
        private final String category;
        private final double totalAmount;
        private final double creditAmount;
        private final double debitAmount;
        private final long transactionCount;
        private final double averageAmount;

        public CategoryAnalysis(String category, double totalAmount, double creditAmount, 
                              double debitAmount, long transactionCount, double averageAmount) {
            this.category = category;
            this.totalAmount = totalAmount;
            this.creditAmount = creditAmount;
            this.debitAmount = debitAmount;
            this.transactionCount = transactionCount;
            this.averageAmount = averageAmount;
        }

        // Getters
        public String getCategory() { return category; }
        public double getTotalAmount() { return totalAmount; }
        public double getCreditAmount() { return creditAmount; }
        public double getDebitAmount() { return debitAmount; }
        public long getTransactionCount() { return transactionCount; }
        public double getAverageAmount() { return averageAmount; }
    }

    public static class CustomerBehavior {
        private final String customerId;
        private final double totalSpent;
        private final double totalEarned;
        private final long transactionCount;
        private final String mostFrequentCategory;

        public CustomerBehavior(String customerId, double totalSpent, double totalEarned, 
                              long transactionCount, String mostFrequentCategory) {
            this.customerId = customerId;
            this.totalSpent = totalSpent;
            this.totalEarned = totalEarned;
            this.transactionCount = transactionCount;
            this.mostFrequentCategory = mostFrequentCategory;
        }

        // Getters
        public String getCustomerId() { return customerId; }
        public double getTotalSpent() { return totalSpent; }
        public double getTotalEarned() { return totalEarned; }
        public long getTransactionCount() { return transactionCount; }
        public String getMostFrequentCategory() { return mostFrequentCategory; }
        
        public double getNetBalance() { return totalEarned - totalSpent; }
    }
}
