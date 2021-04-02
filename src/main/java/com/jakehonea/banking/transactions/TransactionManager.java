package com.jakehonea.banking.transactions;

import com.jakehonea.banking.CentralBank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {

    private final CentralBank bank;

    public TransactionManager(CentralBank bank) {

        this.bank = bank;
        try (Connection connection = bank.getDatabase().openConnection()) {

            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS `transactions` (" +
                    "id VARCHAR(16)," +
                    "amount DOUBLE," +
                    "transaction-type VARCHAR(12)" +
                    "timestamp BIGINT" +
                    ")");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void storeTransaction(Transaction transaction) {

        try (Connection connection = bank.getDatabase().openConnection()) {

            PreparedStatement insert = connection.prepareStatement("INSERT INTO `transactions` values(?,?,?,?)");

            insert.setString(1, transaction.getId());
            insert.setDouble(2, transaction.getAmount());
            insert.setString(3, transaction.getType().name().toLowerCase());
            insert.setLong(4, transaction.getTimestamp());

            insert.execute();
            insert.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public List<Transaction> fetchTransactions(String id) {

        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = bank.getDatabase().openConnection()) {

            PreparedStatement fetchTransactions = connection.prepareStatement("SELECT * FROM `transactions` WHERE id=?");

            fetchTransactions.setString(1, id);

            ResultSet set = fetchTransactions.executeQuery();

            while (set.next())
                transactions.add(new Transaction(set));

            set.close();
            fetchTransactions.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return transactions;

    }

}
