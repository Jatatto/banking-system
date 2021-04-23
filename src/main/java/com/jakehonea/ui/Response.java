package com.jakehonea.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.function.Consumer;

public class Response<T> extends JFrame {

    private final String question;
    private final Consumer<T> onResponse;

    public Response(String string, Consumer<T> onResponse) {

        super("Response");

        try {
            setIconImage(ImageIO.read(getClass().getClassLoader().getResource("Icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

                    onResponse.accept((T) response.getText());
                    dispose();

                }

            }

        });

        panel.add(response);

    }


}