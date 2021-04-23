package com.jakehonea.ui.components;

import com.jakehonea.banking.transactions.Transaction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JTransactionList extends Component {

    private List<JTransaction> transactions;
    private double showing = 0;

    private final int perPage;

    public JTransactionList() {

        this.transactions = new ArrayList<>();

        this.perPage = 3;

        addMouseWheelListener(e -> {

            showing += e.getWheelRotation() * (getBounds().height / 3.0 / 12.0 / 100.0);
            showing = Math.max(Math.min(transactions.size() - perPage, showing), 0);

            repaint();

        });

    }

    @Override
    public void paint(Graphics g) {

        if (transactions.size() > 0) {

            List<JTransaction> list = transactions.subList((int) showing, Math.min((int) Math.round(showing + 0.49) + perPage, transactions.size())); // GOOD

            int y = 0;

            if (list.size() == 4)
                y = (int) -(list.get(0).getBounds().height * ((showing - (int) showing)));

            for (JTransaction transaction : list) {

                g.drawImage(transaction.getImage(), 0, y, null);

                y += transaction.getBounds().height;

            }

            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(getBounds().width - 8, 0, 8, getBounds().height);

            g.setColor(Color.GRAY);

            g.fillRect(
                    getBounds().width - 6,
                    (int) (getBounds().height * showing / transactions.size() + 2),
                    4,
                    getBounds().height * 3 / transactions.size() - 4
            );

        } else {

            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            g.drawString("No Transactions.", getBounds().width / 2 - g.getFontMetrics().stringWidth("No Transactions") / 2, getBounds().height / 2 - g.getFontMetrics().getHeight());

        }

    }

    public List<JTransaction> getTransactions() {

        return transactions;

    }

    public void refreshTransactions(List<Transaction> transactions) {

        this.showing = 0;

        this.transactions = transactions.stream()
                .sorted((t1, t2) -> (int) (t2.getTimestamp() - t1.getTimestamp()))
                .map(t -> {

                    JTransaction transaction = new JTransaction(t);

                    transaction.setBounds(0, 0, getBounds().width - 8, getBounds().height / 3);
                    transaction.renderImage();

                    return transaction;

                })
                .collect(Collectors.toList());

        this.repaint();

    }

}
