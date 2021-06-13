package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.function.Consumer;

public class Response<T> extends JFrame {

    private final String question;
    private final Consumer<T> consumer;

    public Response(String string, Consumer<T> consumer) {

        super("Response");

        try {
            setIconImage(ImageIO.read(getClass().getClassLoader().getResource("Icon.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.question = string;
        this.consumer = consumer;

        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(2, 2);
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

        JTextField response = new JTextField(15);
        LoginUI.cleanTextField(response);

        panel.add(response);

        JButton cancel = new JButton("Cancel");

        cancel.addActionListener(e -> dispose());

        LoginUI.cleanButton(cancel, true);
        panel.add(cancel);

        JButton submit = new JButton("Proceed");

        submit.addActionListener(e -> {
            consumer.accept((T) response.getText());
            dispose();
        });

        LoginUI.cleanButton(submit, true);
        panel.add(submit);

    }


}