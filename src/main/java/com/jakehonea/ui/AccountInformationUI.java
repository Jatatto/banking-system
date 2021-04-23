package com.jakehonea.ui;

import com.jakehonea.banking.accounts.Account;
import com.jakehonea.banking.transactions.Transaction;
import com.jakehonea.banking.transactions.TransactionType;
import com.jakehonea.ui.components.JTransactionList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class AccountInformationUI extends JFrame {

    private final Account account;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

    public AccountInformationUI(Account account) {

        super("Account Information");
        this.account = account;

        try {
            setIconImage(ImageIO.read(getClass().getClassLoader().getResource("Icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setContentPane(generatePanel());
        setSize(500, 500);

        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public JPanel generatePanel() {

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JPanel userInfo = new JPanel();
        {
            GridLayout layout = new GridLayout(2, 1);

            userInfo.setLayout(layout);

            userInfo.add(new JLabel("ID: " + account.getId()));
            userInfo.add(new JLabel("Balance: $" + decimalFormat.format(account.getBalance())) {
                @Override
                public void paint(Graphics graphics) {

                    setText("Balance: $" + decimalFormat.format(account.getBalance()));
                    super.paint(graphics);

                }
            });

            userInfo.setSize(120, 50);
            userInfo.setLocation(30, 30);
        }

        JTransactionList pastTransactions = new JTransactionList();
        {

            pastTransactions.setSize(500 - (30 * 2) - 8, 300);
            pastTransactions.setLocation(30, 90);

            pastTransactions.refreshTransactions(account.getBank().getTransactionManager().fetchTransactions(account));

        }
        panel.add(pastTransactions);

        Container buttons = new Container();
        {

            buttons.setLayout(new GridLayout(1, 4));

            JButton deposit = new JButton("Deposit");
            deposit.addActionListener(event -> new Response<String>("Amount to deposit:", input -> {

                try {

                    double amount = Double.parseDouble(input);

                    new Response<String>("Comment:", comment -> {

                        if (account.getBank().getTransactionManager().processTransaction(new Transaction(account.getId(), amount, comment, TransactionType.DEPOSIT))) {

                            pastTransactions.refreshTransactions(account.getBank().getTransactionManager().fetchTransactions(account));
                            AccountInformationUI.this.repaint();

                        } else
                            JOptionPane.showMessageDialog(new JFrame(), "Error happened while processing payment!", "Error.", JOptionPane.ERROR_MESSAGE);

                    });

                } catch (NumberFormatException e) {

                    JOptionPane.showMessageDialog(new JFrame(), "Invalid number entered.", "Error!", JOptionPane.ERROR_MESSAGE);

                }

            }));

            buttons.add(deposit);

            JButton withdraw = new JButton("Withdraw");
            withdraw.addActionListener(event -> new Response<String>("Amount to withdraw:", input -> {

                try {

                    double amount = Double.parseDouble(input);

                    new Response<String>("Comment:", comment -> {

                        if (account.getBank().getTransactionManager().processTransaction(new Transaction(account.getId(), amount, comment, TransactionType.WITHDRAW))) {

                            pastTransactions.refreshTransactions(account.getBank().getTransactionManager().fetchTransactions(account));
                            AccountInformationUI.this.repaint();

                        } else
                            JOptionPane.showMessageDialog(new JFrame(), "Insufficient funds in account to fulfill transaction.", "Error!", JOptionPane.ERROR_MESSAGE);

                    });

                } catch (NumberFormatException e) {

                    JOptionPane.showMessageDialog(new JFrame(), "Invalid number entered.", "Error!", JOptionPane.ERROR_MESSAGE);

                }

            }));

            buttons.add(withdraw);

            JButton logout = new JButton("Logout");
            logout.addActionListener(e -> {

                setDefaultCloseOperation(HIDE_ON_CLOSE);
                dispose();

                new LoginUI(account.getBank());

            });

            buttons.add(logout);

            JButton deleteAccount = new JButton("Close Account");
            deleteAccount.addActionListener(e -> new Response<String>("Type CONFIRM to close your account: ", input -> {

                if (input.equalsIgnoreCase("confirm")) {

                    setDefaultCloseOperation(HIDE_ON_CLOSE);
                    dispose();

                    account.getBank().getAccountManager().unregisterAccount(account.getId());

                    new LoginUI(account.getBank());

                }

            }));

            buttons.add(deleteAccount);

            for (Component comp : buttons.getComponents())
                if (comp instanceof JButton)
                    LoginUI.cleanButton((JButton) comp, false);

            buttons.setSize(500 - 16, 50);
            buttons.setLocation(0, 500 - 31 - 55);

        }

        panel.add(userInfo);
        panel.add(buttons);
        panel.setBorder(new EmptyBorder(10, 30, 10, 30));

        return panel;

    }

}
