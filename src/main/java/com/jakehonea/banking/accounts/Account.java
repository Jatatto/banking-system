package com.jakehonea.banking.accounts;

import com.jakehonea.banking.CentralBank;
import com.jakehonea.banking.transactions.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Account {

    private final String id;
    private final CentralBank bank;
    private double balance;

    public Account(CentralBank bank, String id, double currentBalance) {

        this.bank    = bank;
        this.id      = id;
        this.balance = currentBalance;

    }

    public boolean processTransaction(Transaction transaction) {

        switch (transaction.getType()) {

            case DEPOSIT:
                balance += transaction.getAmount();
                return true;
            case WITHDRAW:
                if (balance >= transaction.getAmount()) {
                    balance -= transaction.getAmount();
                    return true;
                }
                return false;

        }

        return false;

    }

    public void save() {

        try (Connection connection = bank.getDatabase().openConnection()) {

            PreparedStatement update = connection.prepareStatement("UPDATE `accounts` SET balance=? WHERE id=?");

            update.setDouble(1, balance);
            update.setString(2, id);

            update.executeUpdate();
            update.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public double getBalance() {
        return balance;
    }

    public String getId() {
        return id;
    }
}
