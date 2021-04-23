package com.jakehonea.banking.accounts;

import com.jakehonea.banking.CentralBank;
import com.jakehonea.banking.transactions.Transaction;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                save();
                return true;
            case WITHDRAW:
                if (balance >= transaction.getAmount()) {
                    balance -= transaction.getAmount();
                    save();
                    return true;
                }
                return false;

        }

        return false;

    }

    public ImageIcon getProfilePicture() {

        try (Connection connection = bank.getDatabase().getConnection()) {

            PreparedStatement getPicture = connection.prepareStatement("SELECT * FROM `accounts` WHERE id=?");

            getPicture.setString(1, id);

            ResultSet set = getPicture.executeQuery();

            if (set.next()) {

                InputStream stream = set.getBinaryStream("pfp");

                if (stream != null) {

                    BufferedImage image = ImageIO.read(stream);

                    return new ImageIcon(image.getScaledInstance(80, 80, Image.SCALE_SMOOTH));

                }

            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        try {
            return new ImageIcon(ImageIO.read(getClass().getClassLoader().getResource("default-pfp.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public void setProfilePicture(File file) {

        try (Connection connection = bank.getDatabase().getConnection()) {

            PreparedStatement setPicture = connection.prepareStatement("UPDATE `accounts` SET pfp=? WHERE id=?");

            setPicture.setBinaryStream(1, new FileInputStream(file), file.length());
            setPicture.setString(2, id);

            setPicture.executeUpdate();

            setPicture.close();

        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public double getBalance() {

        return balance;

    }

    public void save() {

        try (Connection connection = bank.getDatabase().getConnection()) {

            PreparedStatement update = connection.prepareStatement("UPDATE `accounts` SET balance=? WHERE id=?");

            update.setDouble(1, balance);
            update.setString(2, id);

            update.executeUpdate();
            update.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public CentralBank getBank() {
        return bank;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {

        return "Account{" +
                "id='" + id + '\'' +
                ", balance=" + balance +
                '}';

    }

}
