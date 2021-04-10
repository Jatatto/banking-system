package com.jakehonea.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public class Response extends JFrame {

    private final String question;
    private final Consumer<String> onResponse;

    public Response(String string, Consumer<String> onResponse) {

        super("Response");

        this.question   = string;
        this.onResponse = onResponse;

        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(1, 2);
        layout.setHgap(10);
        layout.setVgap(10);
        panel.setLayout(layout);

        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        loadComponents(panel);
        setContentPane(panel);
        pack();

        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

    }

    public void loadComponents(JPanel panel) {

        panel.add(new JLabel(question));

        JTextField response = new JTextField();
        response.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

                if (e.getKeyChar() == '\n') {

                    onResponse.accept(response.getText());
                    dispose();

                }

            }

        });

        panel.add(response);

    }


}