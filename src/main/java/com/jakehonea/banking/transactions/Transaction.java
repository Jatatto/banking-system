package com.jakehonea.banking.transactions;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Transaction {

    private final String id;
    private final double amount;
    private final TransactionType type;
    private final String comment;
    private long timestamp;

    public Transaction(String id, double amount, String comment, TransactionType type) {

        this.id        = id;
        this.amount    = amount;
        this.type      = type;
        this.timestamp = System.currentTimeMillis();
        this.comment   = comment;

    }

    public Transaction(ResultSet set)
            throws SQLException {

        this(set.getString("id"), set.getDouble("amount"), set.getString("comment"), TransactionType.from(set.getString("type")));

        this.timestamp = set.getLong("time");

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

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", type=" + type +
                ", comment='" + comment + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
