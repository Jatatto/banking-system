package com.jakehonea.ui.components;

import com.jakehonea.banking.transactions.Transaction;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JTransactionList extends Component {

    private List<JTransaction> transactions;
    private int showing = 0;

    private final int perPage;

    public JTransactionList() {

        this.transactions = new ArrayList<>();

        this.perPage = 3;

        addMouseWheelListener(e -> {

            showing += e.getWheelRotation();
            showing = Math.max(Math.min(transactions.size() - perPage, showing), 0);

            repaint();

        });

    }

    @Override
    public void paint(Graphics g) {

        int y = 0;

        if (transactions.size() > 0) {

            List<JTransaction> list = transactions.subList(showing, Math.min(showing + perPage, transactions.size()));

            for (JTransaction transaction : list) {

                BufferedImage image = new BufferedImage(transaction.getBounds().width, transaction.getBounds().height, BufferedImage.TYPE_INT_ARGB);
                transaction.paint(image.getGraphics());
                g.drawImage(image, 0, y, null);

                y += transaction.getBounds().height;

            }

            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(getBounds().width - 8, 0, 8, getBounds().height);

            g.setColor(Color.BLACK);
            double bound = showing / (transactions.size() - 0.0);


            double length = getBounds().height * list.size() * (1 / (transactions.size() - 0.0));

            g.fillRect(getBounds().width - 6, (int) (getBounds().height * bound) + 2, 4, (int) length - 4);

        } else {

            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            g.drawString("No Transactions.", getBounds().width / 2 - g.getFontMetrics().stringWidth("No Transactions") / 2, getBounds().height / 2 - g.getFontMetrics().getHeight() );

        }

    }

    public List<JTransaction> getTransactions() {

        return transactions;

    }

    public void refreshTransactions(List<Transaction> transactions) {

        this.showing = 0;

        this.transactions = transactions.stream()
                .sorted((t1, t2) -> (int) (t2.getTimestamp() - t1.getTimestamp()))
                .map(transaction -> {

                    JTransaction t = new JTransaction(transaction);

                    t.setBounds(0, 0, getBounds().width - 8, getBounds().height / 3);

                    return t;

                })
                .collect(Collectors.toList());

        this.repaint();

    }

}
