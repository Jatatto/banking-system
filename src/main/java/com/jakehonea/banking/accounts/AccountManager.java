package com.jakehonea.banking.accounts;

import com.jakehonea.banking.CentralBank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AccountManager {

    private final Map<String, Account> cachedAccounts;
    private final CentralBank bank;

    public AccountManager(CentralBank bank) {

        this.bank           = bank;
        this.cachedAccounts = new HashMap<>();

        try (Connection connection = bank.getDatabase().getConnection()) {

            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS `accounts` (" +
                    "id VARCHAR(16)," +
                    "pin VARCHAR(4)," +
                    "balance DOUBLE" +
                    ")");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public Account getAccount(String id) {

        if (cachedAccounts.containsKey(id))
            return cachedAccounts.get(id);

        try (Connection connection = bank.getDatabase().getConnection()) {

            PreparedStatement getAccount = connection.prepareStatement("SELECT * FROM `accounts` where id=?");

            getAccount.setString(1, id);

            ResultSet set = getAccount.executeQuery();

            if (!set.next())
                return null;

            Account account = new Account(bank, id, set.getDouble("balance"));

            cachedAccounts.put(id, account);

            return account;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }


    public Account getAccount(String id, String pin) {

        if (cachedAccounts.containsKey(id))
            return cachedAccounts.get(id);

        try (Connection connection = bank.getDatabase().getConnection()) {

            PreparedStatement getAccount = connection.prepareStatement("SELECT * FROM `accounts` WHERE id=? AND pin=?");

            getAccount.setString(1, id);
            getAccount.setString(2, pin);

            ResultSet set = getAccount.executeQuery();

            if (!set.next())
                return null;

            Account account = new Account(bank, id, set.getDouble("balance"));

            cachedAccounts.put(id, account);

            return account;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public Account registerAccount(String id, String pin) {

        Account account = new Account(bank, id, 0);

        cachedAccounts.put(id, account);

        try (Connection connection = bank.getDatabase().getConnection()) {

            PreparedStatement createAccount = connection.prepareStatement("INSERT INTO `accounts` VALUES(?, ?, ?, ?)");

            createAccount.setString(1, id);
            createAccount.setString(2, pin);
            createAccount.setDouble(3, 0);
            createAccount.setBinaryStream(4, null);

            createAccount.execute();
            createAccount.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return account;

    }

    public void unregisterAccount(String id) {

        try (Connection connection = bank.getDatabase().getConnection()) {

            for (String table : new String[]{"accounts", "transactions"}) {

                PreparedStatement statement = connection.prepareStatement("DELETE FROM `" + table + "` WHERE id=?");

                statement.setString(1, id);

                statement.executeUpdate();

            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public boolean isRegistered(String id) {

        try (Connection connection = bank.getDatabase().getConnection()) {

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
