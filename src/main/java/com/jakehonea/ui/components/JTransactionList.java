package com.jakehonea.ui.components;

import com.jakehonea.banking.transactions.Transaction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JTransactionList extends Component {

    private List<JTransaction> transactions;
    private double scrollBarTicks = 0;

    private final int perPage;

    public JTransactionList() {

        this.transactions = new ArrayList<>();

        this.perPage = 3;

        double ticksPerTransaction = 12.0;

        addMouseWheelListener(e -> {

            scrollBarTicks += e.getWheelRotation() * (getBounds().height / (perPage + 0.0) / ticksPerTransaction / 100.0);
            scrollBarTicks = Math.max(Math.min(transactions.size() - perPage, scrollBarTicks), 0);

            repaint();

        });

    }

    @Override
    public void paint(Graphics g) {

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (transactions.size() > 0) {

            List<JTransaction> list = transactions.subList((int) scrollBarTicks, Math.min((int) Math.round(scrollBarTicks + 0.49) + perPage, transactions.size()));

            int y = list.size() == 4 ? (int) -(list.get(0).getBounds().height * ((scrollBarTicks - (int) scrollBarTicks))) : 0;

            for (JTransaction transaction : list) {

                g.drawImage(transaction.getImage(), 0, y, null);

                y += transaction.getBounds().height;

            }

            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(getBounds().width - 8, 0, 8, getBounds().height);

            g.setColor(Color.GRAY);

            g.fillRect(
                    getBounds().width - 6,
                    (int) (getBounds().height * scrollBarTicks / transactions.size() + 2),
                    4,
                    getBounds().height * (perPage) / transactions.size() - 4
            );

        } else {

            g.setFont(new Font("Times New Roman", Font.PLAIN, 20));
            g.drawString("No Transactions.", getBounds().width / 2 - g.getFontMetrics().stringWidth("No Transactions") / 2, getBounds().height / 2 - g.getFontMetrics().getHeight());

        }

    }

    /**
     * Updates the transactions page
     *
     * @param transactions the transaction to display
     */
    public void refreshTransactions(List<Transaction> transactions) {

        // reset the scroll bar
        this.scrollBarTicks = 0;

        this.transactions = transactions.stream()
                // sort the array from newest transactions to oldest
                .sorted((t1, t2) -> (int) (t2.getTimestamp() - t1.getTimestamp()))
                .map(t -> {

                    JTransaction transaction = new JTransaction(t);

                    transaction.setBounds(0, 0, getBounds().width - 8, getBounds().height / perPage);
                    // pre-render the image before ever using, helps with performance
                    transaction.renderImage();

                    return transaction;

                })
                .collect(Collectors.toList());

        this.repaint();

    }

}
