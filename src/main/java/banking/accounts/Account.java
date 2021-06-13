package banking.accounts;

import banking.CentralBank;
import banking.transactions.Transaction;

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

    /**
     *
     * @param transaction the transaction to process
     * @return whether the transaction was successfully processed or not
     */
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

    /**
     * Will return the profile picture that the user has uploaded,
     * or return the default profile picture as an {@link ImageIcon}.
     *
     * @return the profile picture as a {@link ImageIcon}
     */
    public ImageIcon getProfilePicture() {

        try (Connection connection = bank.getDatabase().getConnection()) {

            PreparedStatement getPicture = connection.prepareStatement("SELECT * FROM `accounts` WHERE id=?");

            getPicture.setString(1, id);

            ResultSet set = getPicture.executeQuery();

            if (set.next()) {

                InputStream stream = set.getBinaryStream("pfp");

                if (stream != null)
                    return new ImageIcon(ImageIO.read(stream));

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

    /**
     * Change's the {@link Account}'s profile picture to the given file.
     * The file is also resized to 80x80 before being uploaded to {@link banking.Database}
     *
     * @param file the file to set as the profile picture
     */
    public void setProfilePicture(File file) {

        try (Connection connection = bank.getDatabase().getConnection()) {

            PreparedStatement setPicture = connection.prepareStatement("UPDATE `accounts` SET pfp=? WHERE id=?");

            try {
                Image image = ImageIO.read(file).getScaledInstance(80, 80, Image.SCALE_SMOOTH);

                ByteArrayOutputStream output = new ByteArrayOutputStream();

                BufferedImage reformatted = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
                reformatted.getGraphics().drawImage(image, 0, 0, null);

                ImageIO.write(reformatted, "png", output);
                InputStream stream = new ByteArrayInputStream(output.toByteArray());

                setPicture.setBinaryStream(1, stream, output.size());
                setPicture.setString(2, id);

                setPicture.executeUpdate();

                setPicture.close();


            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * @return the balance of the {@link Account}
     */
    public double getBalance() {

        return balance;

    }

    /**
     * Updates the balance of the account in the {@link banking.Database}
     */
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

    /**
     *
     * @return the id of the {@link Account}
     */
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
