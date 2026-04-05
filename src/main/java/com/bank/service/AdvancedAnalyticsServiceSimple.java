package com.bank.service;

import com.bank.model.Transaction;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class AdvancedAnalyticsServiceSimple {

    // Simplified merchant categories
    private static final Map<String, String> MERCHANT_CATEGORIES = new HashMap<>();
    
    static {
        // Income categories
        MERCHANT_CATEGORIES.put("Salary Deposit", "INCOME");
        MERCHANT_CATEGORIES.put("Freelance Payment", "INCOME");
        MERCHANT_CATEGORIES.put("Bonus Payment", "INCOME");
        MERCHANT_CATEGORIES.put("Part-time Job", "INCOME");
        MERCHANT_CATEGORIES.put("Investment Return", "INCOME");
        MERCHANT_CATEGORIES.put("Dividend Payment", "INCOME");
        MERCHANT_CATEGORIES.put("Stock Dividend", "INCOME");
        MERCHANT_CATEGORIES.put("Business Income", "INCOME");
        MERCHANT_CATEGORIES.put("Project Payment", "INCOME");
        MERCHANT_CATEGORIES.put("Rental Income", "INCOME");
        MERCHANT_CATEGORIES.put("Interest Payment", "INCOME");
        MERCHANT_CATEGORIES.put("Cashback Reward", "INCOME");
        MERCHANT_CATEGORIES.put("Gift Payment", "INCOME");
        MERCHANT_CATEGORIES.put("Refund", "INCOME");
        MERCHANT_CATEGORIES.put("Online Sales", "INCOME");
        MERCHANT_CATEGORIES.put("Side Business", "INCOME");
        MERCHANT_CATEGORIES.put("Side Income", "INCOME");
        MERCHANT_CATEGORIES.put("Passive Income", "INCOME");
        MERCHANT_CATEGORIES.put("Royalties", "INCOME");
        MERCHANT_CATEGORIES.put("Licensing", "INCOME");
        MERCHANT_CATEGORIES.put("Affiliate", "INCOME");
        MERCHANT_CATEGORIES.put("Freelance Work", "INCOME");
        MERCHANT_CATEGORIES.put("Creative Work", "INCOME");
        MERCHANT_CATEGORIES.put("Photography Services", "INCOME");
        MERCHANT_CATEGORIES.put("Video Production", "INCOME");
        MERCHANT_CATEGORIES.put("Content Creation", "INCOME");
        MERCHANT_CATEGORIES.put("Graphic Design", "INCOME");
        MERCHANT_CATEGORIES.put("Consulting", "INCOME");
        MERCHANT_CATEGORIES.put("Consulting Fee", "INCOME");
        MERCHANT_CATEGORIES.put("E-commerce", "INCOME");
        MERCHANT_CATEGORIES.put("Digital Marketing", "INCOME");
        MERCHANT_CATEGORIES.put("Web Development", "INCOME");
        MERCHANT_CATEGORIES.put("Online Services", "INCOME");
        MERCHANT_CATEGORIES.put("Digital Products", "INCOME");
        MERCHANT_CATEGORIES.put("Web Hosting", "INCOME");
        MERCHANT_CATEGORIES.put("Domain Names", "INCOME");
        MERCHANT_CATEGORIES.put("Marketing Tools", "INCOME");
        MERCHANT_CATEGORIES.put("Courses", "INCOME");
        MERCHANT_CATEGORIES.put("Training", "INCOME");
        MERCHANT_CATEGORIES.put("Certification", "INCOME");
        MERCHANT_CATEGORIES.put("Photography Gear", "INCOME");
        MERCHANT_CATEGORIES.put("Camera Equipment", "INCOME");
        MERCHANT_CATEGORIES.put("Lighting", "INCOME");
        MERCHANT_CATEGORIES.put("Photo Editing Software", "INCOME");
        MERCHANT_CATEGORIES.put("Art Supplies", "INCOME");
        MERCHANT_CATEGORIES.put("Craft Materials", "INCOME");
        MERCHANT_CATEGORIES.put("Photography", "INCOME");
        MERCHANT_CATEGORIES.put("Workshop", "INCOME");
        MERCHANT_CATEGORIES.put("Art", "INCOME");
        MERCHANT_CATEGORIES.put("Design", "INCOME");
        MERCHANT_CATEGORIES.put("Hobbies", "INCOME");
        MERCHANT_CATEGORIES.put("Games", "INCOME");
        MERCHANT_CATEGORIES.put("Collectibles", "INCOME");
        MERCHANT_CATEGORIES.put("Toys", "INCOME");
        MERCHANT_CATEGORIES.put("Outdoor Gear", "INCOME");
        MERCHANT_CATEGORIES.put("Camping Equipment", "INCOME");
        MERCHANT_CATEGORIES.put("Hiking Gear", "INCOME");
        MERCHANT_CATEGORIES.put("Sports Equipment", "INCOME");
        MERCHANT_CATEGORIES.put("Smart Home", "INCOME");
        MERCHANT_CATEGORIES.put("Security System", "INCOME");
        MERCHANT_CATEGORIES.put("Smart Devices", "INCOME");
        MERCHANT_CATEGORIES.put("Home Automation", "INCOME");
        MERCHANT_CATEGORIES.put("IoT Devices", "INCOME");
        MERCHANT_CATEGORIES.put("Fitness Equipment", "INCOME");
        MERCHANT_CATEGORIES.put("Personal Training", "INCOME");
        MERCHANT_CATEGORIES.put("Gym Membership", "INCOME");
        MERCHANT_CATEGORIES.put("Yoga Classes", "INCOME");
        MERCHANT_CATEGORIES.put("Yoga Studio", "INCOME");
        MERCHANT_CATEGORIES.put("Software Tools", "INCOME");
        MERCHANT_CATEGORIES.put("Computer Upgrade", "INCOME");
        MERCHANT_CATEGORIES.put("Monitor", "INCOME");
        MERCHANT_CATEGORIES.put("Keyboard", "INCOME");
        MERCHANT_CATEGORIES.put("Real Estate", "INCOME");
        MERCHANT_CATEGORIES.put("Property Investment", "INCOME");
        MERCHANT_CATEGORIES.put("Legal Services", "INCOME");
        MERCHANT_CATEGORIES.put("Insurance Premium", "INCOME");
        MERCHANT_CATEGORIES.put("Real Estate Investment", "INCOME");
        MERCHANT_CATEGORIES.put("Property Development", "INCOME");
        MERCHANT_CATEGORIES.put("Land Purchase", "INCOME");
        MERCHANT_CATEGORIES.put("Legal Fees", "INCOME");
        MERCHANT_CATEGORIES.put("Portfolio Management", "INCOME");
        MERCHANT_CATEGORIES.put("Asset Management", "INCOME");
        MERCHANT_CATEGORIES.put("Wealth Management", "INCOME");
        MERCHANT_CATEGORIES.put("Tax Planning", "INCOME");
        MERCHANT_CATEGORIES.put("Rental Income", "INCOME");
        MERCHANT_CATEGORIES.put("Investment Returns", "INCOME");
        MERCHANT_CATEGORIES.put("Business Revenue", "INCOME");
        
        // Housing
        MERCHANT_CATEGORIES.put("Rent Payment", "HOUSING");
        MERCHANT_CATEGORIES.put("Mortgage Payment", "HOUSING");
        MERCHANT_CATEGORIES.put("Property Tax", "HOUSING");
        MERCHANT_CATEGORIES.put("Home Maintenance", "HOUSING");
        MERCHANT_CATEGORIES.put("Garden Supplies", "HOUSING");
        MERCHANT_CATEGORIES.put("Home Improvement", "HOUSING");
        MERCHANT_CATEGORIES.put("Furniture", "HOUSING");
        MERCHANT_CATEGORIES.put("Home Decor", "HOUSING");
        MERCHANT_CATEGORIES.put("Appliances", "HOUSING");
        MERCHANT_CATEGORIES.put("Tools", "HOUSING");
        MERCHANT_CATEGORIES.put("Landscaping", "HOUSING");
        MERCHANT_CATEGORIES.put("Patio", "HOUSING");
        MERCHANT_CATEGORIES.put("Outdoor Equipment", "HOUSING");
        MERCHANT_CATEGORIES.put("Home Renovation", "HOUSING");
        MERCHANT_CATEGORIES.put("Home Repair", "HOUSING");
        MERCHANT_CATEGORIES.put("Plumbing", "HOUSING");
        MERCHANT_CATEGORIES.put("Electrical", "HOUSING");
        MERCHANT_CATEGORIES.put("Painting", "HOUSING");
        MERCHANT_CATEGORIES.put("Renovation", "HOUSING");
        MERCHANT_CATEGORIES.put("Upgrades", "HOUSING");
        MERCHANT_CATEGORIES.put("Services", "HOUSING");
        MERCHANT_CATEGORIES.put("Home Security", "HOUSING");
        MERCHANT_CATEGORIES.put("Safety Equipment", "HOUSING");
        MERCHANT_CATEGORIES.put("Emergency Supplies", "HOUSING");
        MERCHANT_CATEGORIES.put("First Aid Kit", "HOUSING");
        MERCHANT_CATEGORIES.put("Smart Home Devices", "HOUSING");
        MERCHANT_CATEGORIES.put("Home Automation", "HOUSING");
        MERCHANT_CATEGORIES.put("IOT Devices", "HOUSING");
        
        // Transportation
        MERCHANT_CATEGORIES.put("Car Payment", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Car Insurance", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Auto Insurance", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Gas Station", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Gas", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Fuel", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Car Maintenance", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Car Repair", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Car Accessories", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Tires", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Auto Parts", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Maintenance", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Vehicle Upgrade", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Uber", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Transportation", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Public Transport", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Flight", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Flight Booking", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Hotel", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Hotel Booking", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Vacation Booking", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Vacation Package", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Tours", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Travel", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Taxi", "TRANSPORTATION");
        MERCHANT_CATEGORIES.put("Parking Garage", "TRANSPORTATION");
        
        // Food & Dining
        MERCHANT_CATEGORIES.put("Whole Foods Market", "FOOD_DINING");
        MERCHANT_CATEGORIES.put("Trader Joe's", "FOOD_DINING");
        MERCHANT_CATEGORIES.put("Costco", "FOOD_DINING");
        MERCHANT_CATEGORIES.put("Safeway", "FOOD_DINING");
        MERCHANT_CATEGORIES.put("Grocery Store", "FOOD_DINING");
        MERCHANT_CATEGORIES.put("Groceries", "FOOD_DINING");
        MERCHANT_CATEGORIES.put("Starbucks", "FOOD_DINING");
        MERCHANT_CATEGORIES.put("Coffee", "FOOD_DINING");
        MERCHANT_CATEGORIES.put("Coffee Shop", "FOOD_DINING");
        MERCHANT_CATEGORIES.put("Restaurant", "FOOD_DINING");
        MERCHANT_CATEGORIES.put("Dining Out", "FOOD_DINING");
        MERCHANT_CATEGORIES.put("Food Delivery", "FOOD_DINING");
        MERCHANT_CATEGORIES.put("Chipotle", "FOOD_DINING");
        MERCHANT_CATEGORIES.put("Subway", "FOOD_DINING");
        MERCHANT_CATEGORIES.put("Fast Food", "FOOD_DINING");
        MERCHANT_CATEGORIES.put("Ice Cream Shop", "FOOD_DINING");
        
        // Shopping
        MERCHANT_CATEGORIES.put("Walmart", "SHOPPING");
        MERCHANT_CATEGORIES.put("Target", "SHOPPING");
        MERCHANT_CATEGORIES.put("Amazon", "SHOPPING");
        MERCHANT_CATEGORIES.put("Online Shopping", "SHOPPING");
        MERCHANT_CATEGORIES.put("Shopping", "SHOPPING");
        MERCHANT_CATEGORIES.put("Retail Shopping", "SHOPPING");
        MERCHANT_CATEGORIES.put("Nordstrom", "SHOPPING");
        MERCHANT_CATEGORIES.put("Clothing", "SHOPPING");
        MERCHANT_CATEGORIES.put("Clothing Store", "SHOPPING");
        MERCHANT_CATEGORIES.put("Fashion", "SHOPPING");
        MERCHANT_CATEGORIES.put("Designer Clothes", "SHOPPING");
        MERCHANT_CATEGORIES.put("Shoes", "SHOPPING");
        MERCHANT_CATEGORIES.put("Jewelry", "SHOPPING");
        MERCHANT_CATEGORIES.put("Perfume", "SHOPPING");
        MERCHANT_CATEGORIES.put("Accessories", "SHOPPING");
        MERCHANT_CATEGORIES.put("Retail", "SHOPPING");
        MERCHANT_CATEGORIES.put("Online", "SHOPPING");
        MERCHANT_CATEGORIES.put("E-commerce", "SHOPPING");
        MERCHANT_CATEGORIES.put("Department Store", "SHOPPING");
        MERCHANT_CATEGORIES.put("Electronics", "SHOPPING");
        MERCHANT_CATEGORIES.put("Electronics Store", "SHOPPING");
        MERCHANT_CATEGORIES.put("Apple Store", "SHOPPING");
        MERCHANT_CATEGORIES.put("Gaming Console", "SHOPPING");
        MERCHANT_CATEGORIES.put("Video Games", "SHOPPING");
        MERCHANT_CATEGORIES.put("Gaming Accessories", "SHOPPING");
        MERCHANT_CATEGORIES.put("Luxury Items", "SHOPPING");
        MERCHANT_CATEGORIES.put("Beauty", "SHOPPING");
        MERCHANT_CATEGORIES.put("Beauty Products", "SHOPPING");
        MERCHANT_CATEGORIES.put("Cosmetics", "SHOPPING");
        MERCHANT_CATEGORIES.put("Personal Care", "SHOPPING");
        MERCHANT_CATEGORIES.put("Home Depot", "SHOPPING");
        MERCHANT_CATEGORIES.put("Hardware Store", "SHOPPING");
        MERCHANT_CATEGORIES.put("Pet Store", "SHOPPING");
        MERCHANT_CATEGORIES.put("Pet Supplies", "SHOPPING");
        
        // Entertainment
        MERCHANT_CATEGORIES.put("Netflix", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Spotify", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Hulu", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Amazon Prime", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Streaming Services", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Subscription Services", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Subscription", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Streaming", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Music Streaming", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Video Streaming", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Podcast Subscription", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Magazine Subscription", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Concert Tickets", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Concerts", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Events", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Tickets", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Entertainment", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Theater", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Movie Theater", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Books", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("E-books", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Audiobooks", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Gaming", "ENTERTAINMENT");
        MERCHANT_CATEGORIES.put("Games", "ENTERTAINMENT");
        
        // Health & Wellness
        MERCHANT_CATEGORIES.put("Pharmacy", "HEALTH_WELLNESS");
        MERCHANT_CATEGORIES.put("Healthcare", "HEALTH_WELLNESS");
        MERCHANT_CATEGORIES.put("Medical Expenses", "HEALTH_WELLNESS");
        MERCHANT_CATEGORIES.put("Health Insurance", "HEALTH_WELLNESS");
        MERCHANT_CATEGORIES.put("Insurance", "HEALTH_WELLNESS");
        MERCHANT_CATEGORIES.put("Insurance Payment", "HEALTH_WELLNESS");
        MERCHANT_CATEGORIES.put("Gym Membership", "HEALTH_WELLNESS");
        MERCHANT_CATEGORIES.put("Fitness", "HEALTH_WELLNESS");
        MERCHANT_CATEGORIES.put("Fitness Equipment", "HEALTH_WELLNESS");
        MERCHANT_CATEGORIES.put("Wellness", "HEALTH_WELLNESS");
        MERCHANT_CATEGORIES.put("Nutrition", "HEALTH_WELLNESS");
        MERCHANT_CATEGORIES.put("Personal Trainer", "HEALTH_WELLNESS");
        MERCHANT_CATEGORIES.put("Yoga Classes", "HEALTH_WELLNESS");
        MERCHANT_CATEGORIES.put("Yoga Studio", "HEALTH_WELLNESS");
        
        // Utilities
        MERCHANT_CATEGORIES.put("Utility Bill", "UTILITIES");
        MERCHANT_CATEGORIES.put("Utilities", "UTILITIES");
        MERCHANT_CATEGORIES.put("Internet Bill", "UTILITIES");
        MERCHANT_CATEGORIES.put("Internet", "UTILITIES");
        MERCHANT_CATEGORIES.put("Phone Bill", "UTILITIES");
        MERCHANT_CATEGORIES.put("Phone", "UTILITIES");
        MERCHANT_CATEGORIES.put("Cloud Storage", "UTILITIES");
        MERCHANT_CATEGORIES.put("Software", "UTILITIES");
        MERCHANT_CATEGORIES.put("Apps", "UTILITIES");
        MERCHANT_CATEGORIES.put("Music", "UTILITIES");
        
        // Financial Services
        MERCHANT_CATEGORIES.put("Banking Fees", "FINANCIAL");
        MERCHANT_CATEGORIES.put("Banking", "FINANCIAL");
        MERCHANT_CATEGORIES.put("Banking Services", "FINANCIAL");
        MERCHANT_CATEGORIES.put("Financial Advisor", "FINANCIAL");
        MERCHANT_CATEGORIES.put("Financial Planning", "FINANCIAL");
        MERCHANT_CATEGORIES.put("Finance", "FINANCIAL");
        MERCHANT_CATEGORIES.put("Investment", "FINANCIAL");
        MERCHANT_CATEGORIES.put("Investment Advisory", "FINANCIAL");
        MERCHANT_CATEGORIES.put("Trading", "FINANCIAL");
        MERCHANT_CATEGORIES.put("Stock Trading", "FINANCIAL");
        MERCHANT_CATEGORIES.put("Portfolio", "FINANCIAL");
        MERCHANT_CATEGORIES.put("Stocks", "FINANCIAL");
        MERCHANT_CATEGORIES.put("Bonds", "FINANCIAL");
        MERCHANT_CATEGORIES.put("Commodities", "FINANCIAL");
        MERCHANT_CATEGORIES.put("Accounting", "FINANCIAL");
        MERCHANT_CATEGORIES.put("Tax Payment", "FINANCIAL");
        MERCHANT_CATEGORIES.put("Legal Fees", "FINANCIAL");
        MERCHANT_CATEGORIES.put("Professional", "FINANCIAL");
        
        // Education
        MERCHANT_CATEGORIES.put("Online Course", "EDUCATION");
        MERCHANT_CATEGORIES.put("Education", "EDUCATION");
        MERCHANT_CATEGORIES.put("Courses", "EDUCATION");
        MERCHANT_CATEGORIES.put("Tutoring", "EDUCATION");
        MERCHANT_CATEGORIES.put("Professional Development", "EDUCATION");
        MERCHANT_CATEGORIES.put("Conference Fee", "EDUCATION");
        MERCHANT_CATEGORIES.put("Networking Event", "EDUCATION");
        
        // Business
        MERCHANT_CATEGORIES.put("Office Supplies", "BUSINESS");
        MERCHANT_CATEGORIES.put("Office Equipment", "BUSINESS");
        MERCHANT_CATEGORIES.put("Software License", "BUSINESS");
        MERCHANT_CATEGORIES.put("Hardware", "BUSINESS");
        MERCHANT_CATEGORIES.put("IT Support", "BUSINESS");
        MERCHANT_CATEGORIES.put("Property Management", "BUSINESS");
        MERCHANT_CATEGORIES.put("Maintenance", "BUSINESS");
        MERCHANT_CATEGORIES.put("Repairs", "BUSINESS");
        MERCHANT_CATEGORIES.put("Consulting", "BUSINESS");
        MERCHANT_CATEGORIES.put("Marketing", "BUSINESS");
        MERCHANT_CATEGORIES.put("Printing", "BUSINESS");
        MERCHANT_CATEGORIES.put("Shipping", "BUSINESS");
        MERCHANT_CATEGORIES.put("Postage", "BUSINESS");
        
        // Miscellaneous
        MERCHANT_CATEGORIES.put("Survey Reward", "MISCELLANEOUS");
        MERCHANT_CATEGORIES.put("Cashback Bonus", "MISCELLANEOUS");
        MERCHANT_CATEGORIES.put("Gift Card", "MISCELLANEOUS");
        MERCHANT_CATEGORIES.put("Recreation", "MISCELLANEOUS");
        MERCHANT_CATEGORIES.put("Services", "MISCELLANEOUS");
        MERCHANT_CATEGORIES.put("Other", "MISCELLANEOUS");
    }

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
