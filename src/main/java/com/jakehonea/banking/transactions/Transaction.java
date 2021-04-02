package com.jakehonea.banking.transactions;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Transaction {

    private final String id;
    private final double amount;
    private final TransactionType type;
    private long timestamp;

    public Transaction(String id, double amount, TransactionType type) {

        this.id        = id;
        this.amount    = amount;
        this.type      = type;
        this.timestamp = System.currentTimeMillis();

    }

    public Transaction(ResultSet set)
            throws SQLException {

        this(set.getString("id"), set.getDouble("amount"), TransactionType.from(set.getString("transaction-type")));

        this.timestamp = set.getLong("timestamp");

    }

    public String getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
