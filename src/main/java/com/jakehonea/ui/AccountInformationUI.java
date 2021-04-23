package com.jakehonea.ui;

import com.jakehonea.banking.accounts.Account;
import com.jakehonea.banking.transactions.Transaction;
import com.jakehonea.banking.transactions.TransactionType;
import com.jakehonea.ui.components.JTransactionList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FileChooserUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

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

        setResizable(false);
        setContentPane(generatePanel());
        setSize(500, 500);

        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    public JPanel generatePanel() {

        JPanel panel = new JPanel();
        panel.setLayout(null);

        Component balance = new Component() {
            @Override
            public void paint(Graphics g) {

                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


                g.setColor(Color.GRAY);
                g.setFont(new Font("Times New Roman", Font.BOLD, 26));
                String balanceText = "$" + decimalFormat.format(account.getBalance());
                g.drawString(balanceText, getWidth() - g.getFontMetrics().stringWidth(balanceText), getHeight() - 5);


            }
        };
        {
            balance.setSize(200, 50);
            balance.setLocation(500 - balance.getWidth() - 30 - 8, 30);
        }

        panel.add(balance);

        JLabel profilePicture = new JLabel(account.getProfilePicture());
        {

            profilePicture.setSize(80, 80);
            profilePicture.setLocation(30, 15);

            profilePicture.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    JFileChooser fileChooser = new JFileChooser();

                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "JPG & PNG Images", "jpg", "png");
                    fileChooser.setFileFilter(filter);
                    int returnVal = fileChooser.showOpenDialog(new JFrame());

                    if (returnVal == JFileChooser.APPROVE_OPTION) {

                        File selected = fileChooser.getSelectedFile();

                        account.setProfilePicture(selected);

                        try {
                            profilePicture.setIcon(new ImageIcon(ImageIO.read(selected).getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }

                    }

                }

            });

            profilePicture.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        }
        panel.add(profilePicture);


        JTransactionList pastTransactions = new JTransactionList();
        {

            pastTransactions.setSize(500 - (30 * 2) - 8, 300);
            pastTransactions.setLocation(30, 100);

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
        panel.add(buttons);

        panel.setBorder(new EmptyBorder(10, 30, 10, 30));

        return panel;

    }

}
