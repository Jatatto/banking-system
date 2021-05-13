package com.jakehonea.ui;

import com.jakehonea.banking.CentralBank;
import com.jakehonea.banking.accounts.Account;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
            cleanTextField(username);

            usernameContainer.add(username);

            panel.add(usernameContainer);
        }

        Container passwordContainer = new Container();
        JPasswordField pin;
        {

            passwordContainer.setLayout(new GridLayout(1, 2));
            passwordContainer.add(new JLabel("PIN Number: "));

            pin = new JPasswordField();
            cleanTextField(pin);
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

                if (username.getText().length() < 2) {

                    JOptionPane.showMessageDialog(new JFrame(), "Username must be at least 2 characters long!", "Error:", JOptionPane.ERROR_MESSAGE);
                    return;

                }

                String password = String.valueOf(pin.getPassword());

                if (password.length() != 4) {

                    JOptionPane.showMessageDialog(new JFrame(), "PIN must be 4 digits long!", "Error:", JOptionPane.ERROR_MESSAGE);
                    return;

                }

                try {

                    Integer.parseInt(password);

                } catch (NumberFormatException error) {

                    JOptionPane.showMessageDialog(new JFrame(), "Your PIN may only include numbers!", "Error:", JOptionPane.ERROR_MESSAGE);
                    return;

                }

                Account account = bank.getAccountManager().registerAccount(username.getText(), password);

                this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                dispose();

                new AccountInformationUI(account);

            });

            panel.add(register);

        }

        for (Component comp : panel.getComponents())
            if (comp instanceof JButton)
                cleanButton((JButton) comp, true);

    }

    /**
     * @param button the button to apply the universal design to
     * @param border whether the button should have a border
     */
    public static void cleanButton(JButton button, boolean border) {

        if (border)
            button.setBorder(BorderFactory.createEtchedBorder());
        else button.setBorder(BorderFactory.createEmptyBorder());
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);

        button.getModel().addChangeListener(e -> {

            ButtonModel model = (ButtonModel) e.getSource();

            if (model.isPressed() || model.isSelected() || model.isRollover())
                button.setBackground(Color.GRAY);
            else
                button.setBackground(Color.WHITE);

        });

    }

    public static void cleanTextField(JTextField field) {

        field.setBackground(new Color(238, 238, 238));
        field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY));
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));
                field.setBackground(new Color(238, 238, 238).brighter());
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY));
                field.setBackground(new Color(238, 238, 238));
            }
        });

    }


}
