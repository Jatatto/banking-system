package com.jakehonea.ui.account;

import com.jakehonea.banking.accounts.Account;
import com.jakehonea.banking.transactions.Transaction;
import com.jakehonea.banking.transactions.TransactionType;
import com.jakehonea.ui.Response;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class AccountInformationUI extends JFrame {

    private final Account account;

    public AccountInformationUI(Account account) {

        super("Account Information");

        try {
            setIconImage(ImageIO.read(getClass().getClassLoader().getResource("Icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.account = account;

        setContentPane(generatePanel());
        pack();

        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public JPanel generatePanel() {

        JPanel panel = new JPanel();

        Container userInfo = new Container();
        {
            GridLayout layout = new GridLayout(5, 1);

            layout.setHgap(3);
            layout.setVgap(15);

            userInfo.setLayout(layout);

            userInfo.add(new JLabel("ID: " + account.getId()));
            userInfo.add(new JLabel("Balance: " + account.getBalance()) {
                @Override
                public void paint(Graphics g) {

                    setText("Balance: " + account.getBalance());
                    super.paint(g);

                }
            });

            JButton deposit = new JButton("Deposit");
            deposit.addActionListener(event -> new Response("Amount to deposit:", input -> {

                try {

                    double amount = Double.parseDouble(input);

                    new Response("Comments:", comment ->
                            account.getBank().getTransactionManager().processTransaction(new Transaction(account.getId(), amount, comment, TransactionType.DEPOSIT)));


                } catch (NumberFormatException e) {

                    JOptionPane.showMessageDialog(new JFrame(), "Invalid number entered!", "Error!", JOptionPane.ERROR_MESSAGE);

                }

            }));

            userInfo.add(deposit);

            JButton withdraw = new JButton("Withdraw");
            withdraw.addActionListener(event -> new Response("Amount to withdraw:", input -> {

                try {

                    double amount = Double.parseDouble(input);

                    new Response("Comments:", comment ->
                            account.getBank().getTransactionManager().processTransaction(new Transaction(account.getId(), amount, comment, TransactionType.WITHDRAW)));

                } catch (NumberFormatException e) {

                    JOptionPane.showMessageDialog(new JFrame(), "Invalid number entered!", "Error!", JOptionPane.ERROR_MESSAGE);

                }

            }));

            userInfo.add(withdraw);

            JButton viewTransactions = new JButton("View Transactions");
            viewTransactions.addActionListener(e -> {

                setDefaultCloseOperation(HIDE_ON_CLOSE);
                dispose();

                new TransactionsUI(account);

            });

            userInfo.add(viewTransactions);


        }

        panel.add(userInfo);

        panel.setLayout(new GridLayout(1, 1));
        panel.setBorder(new EmptyBorder(10, 30, 30, 30));

        return panel;

    }

}
