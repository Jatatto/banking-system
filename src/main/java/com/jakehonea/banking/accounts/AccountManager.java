package com.jakehonea.banking.accounts;

import com.jakehonea.banking.CentralBank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountManager {

    private final CentralBank bank;

    public AccountManager(CentralBank bank) {

        this.bank = bank;

        try (Connection connection = bank.getDatabase().openConnection()) {

            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS `accounts` (" +
                    "id VARCHAR(16)," +
                    "pin VARCHAR(4)," +
                    "balance DOUBLE" +
                    ")");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public Account getAccount(String id, String pin) {

        try (Connection connection = bank.getDatabase().openConnection()) {

            PreparedStatement getAccount = connection.prepareStatement("SELECT * FROM `accounts` where id=?,pin=?");

            getAccount.setString(1, id);
            getAccount.setString(2, pin);

            ResultSet set = getAccount.executeQuery();

            getAccount.close();

            if (!set.next())
                return null;

            return new Account(bank, id, set.getDouble("balance"));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public Account registerAccount(String id, String pin) {

        Account account = new Account(bank, id, 0);

        try (Connection connection = bank.getDatabase().openConnection()) {

            PreparedStatement createAccount = connection.prepareStatement("INSERT INTO `accounts` VALUES(?, ?, ?)");

            createAccount.setString(1, id);
            createAccount.setString(2, pin);
            createAccount.setDouble(3, 0);

            createAccount.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return account;

    }

    public void unregisterAccount(String id) {

        try (Connection connection = bank.getDatabase().openConnection()) {

            PreparedStatement deleteAccount = connection.prepareStatement("DELETE FROM `accounts` WHERE id=?");

            deleteAccount.setString(1, id);

            deleteAccount.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public boolean isRegistered(String id) {

        try (Connection connection = bank.getDatabase().openConnection()) {

            PreparedStatement checkRegistry = connection.prepareStatement("SELECT * FROM `accounts` WHERE id=?");

            checkRegistry.setString(1, id);

            boolean isRegistered = checkRegistry.executeQuery().next();

            checkRegistry.close();

            return isRegistered;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;

    }

}
