package com.jakehonea.ui;

import com.jakehonea.banking.CentralBank;
import com.jakehonea.banking.accounts.Account;
import com.jakehonea.ui.account.AccountInformationUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class LoginUI extends JFrame {

    private CentralBank bank;

    public LoginUI(CentralBank bank) {

        super("Login");

        this.bank = bank;

        try {
            setIconImage(ImageIO.read(getClass().getClassLoader().getResource("Icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel();

        GridLayout layout = new GridLayout(4, 1);
        layout.setHgap(3);
        layout.setVgap(15);

        panel.setLayout(layout);
        panel.setBorder(new EmptyBorder(10, 30, 30, 30));

        loadComponents(panel);
        setContentPane(panel);

        pack();

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLocationRelativeTo(null);


    }

    public void loadComponents(JPanel panel) {

        Container usernameContainer = new Container();
        JTextField username;
        {
            usernameContainer.setLayout(new GridLayout(1, 2));
            usernameContainer.add(new JLabel("Account ID: "));

            username = new JTextField();

            usernameContainer.add(username);

            panel.add(usernameContainer);
        }

        Container passwordContainer = new Container();
        JPasswordField pin;
        {

            passwordContainer.setLayout(new GridLayout(1, 2));
            passwordContainer.add(new JLabel("PIN Number: "));

            pin = new JPasswordField();

            passwordContainer.add(pin);

            panel.add(passwordContainer);
        }

        JButton login = new JButton("Login");
        {
            login.addActionListener(e -> {

                char[] password = pin.getPassword();

                Account account = bank.getAccountManager().getAccount(username.getText(), String.valueOf(password));
                Arrays.fill(password, (char) 0);

                if (account != null) {

                    this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    dispose();

                    new AccountInformationUI(account);

                } else

                    JOptionPane.showMessageDialog(new JFrame(), "Could not locate account!", "Error:", JOptionPane.ERROR_MESSAGE);

            });

            panel.add(login);
        }

        JButton register = new JButton("Register Account");
        {
            register.addActionListener(e -> {

                if (bank.getAccountManager().isRegistered(username.getText())) {

                    JOptionPane.showMessageDialog(new JFrame(), "Account ID already registered!", "Error:", JOptionPane.ERROR_MESSAGE);
                    return;

                }
                char[] password = pin.getPassword();

                Account account = bank.getAccountManager().registerAccount(username.getText(), String.valueOf(password));

                Arrays.fill(password, (char) 0);

                this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                dispose();

                new AccountInformationUI(account);

            });

            panel.add(register);

        }

    }

    public static void main(String[] args) {

        try {
            new LoginUI(new CentralBank());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
