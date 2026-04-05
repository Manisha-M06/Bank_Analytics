package com.bank.util;

import com.bank.model.Transaction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<Transaction> readTransactionsFromCsv() {
        List<Transaction> transactions = new ArrayList<>();
        
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("transactions.csv");
            if (inputStream == null) {
                System.err.println("transactions.csv file not found in resources");
                return transactions;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 9) {
                    try {
                        Transaction transaction = new Transaction();
                        transaction.setTransactionId(values[0].trim());
                        transaction.setAccountId(values[1].trim());
                        transaction.setCustomerId(values[2].trim());
                        transaction.setTransactionType(Transaction.TransactionType.valueOf(values[3].trim().toUpperCase()));
                        transaction.setAmount(Double.parseDouble(values[4].trim()));
                        transaction.setTransactionDate(LocalDate.parse(values[5].trim(), DATE_FORMATTER));
                        transaction.setMerchant(values[6].trim());
                        transaction.setLocation(values[7].trim());
                        transaction.setStatus(Transaction.TransactionStatus.valueOf(values[8].trim().toUpperCase()));
                        
                        transactions.add(transaction);
                    } catch (Exception e) {
                        System.err.println("Error parsing line: " + line);
                        System.err.println("Error: " + e.getMessage());
                    }
                }
            }
            
            reader.close();
        } catch (Exception e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
        
        return transactions;
    }
}
