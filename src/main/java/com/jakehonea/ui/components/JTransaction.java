package com.jakehonea.ui.components;

import com.jakehonea.banking.transactions.Transaction;
import com.jakehonea.banking.transactions.TransactionType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JTransaction extends Component {

    private Transaction transaction;
    private DecimalFormat numberFormat;
    private DateFormat dateFormat;

    private BufferedImage image;

    public JTransaction(Transaction transaction) {

        this.transaction = transaction;

        this.numberFormat = new DecimalFormat("#,###.##");
        this.dateFormat   = new SimpleDateFormat("MM/dd/yy hh:mm a");

    }

    public void renderImage() {

        if (this.image == null)
            this.image = new BufferedImage(getBounds().width, getBounds().height, BufferedImage.TYPE_INT_ARGB);

        Graphics g = image.getGraphics();
        // Turning on antialiasing, makes the graphics smoother
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Background
        g.setColor(new Color(238, 238, 238));
        g.fillRect(0, 0, getBounds().width, getBounds().height);

        // Comment
        g.setFont(new Font("Times New Roman", Font.BOLD, 20));
        g.setColor(Color.DARK_GRAY);
        g.drawString(transaction.getComment(), 0, g.getFontMetrics().getHeight() * 2);

        // Balance
        g.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        g.setColor(transaction.getType() == TransactionType.DEPOSIT ? Color.GREEN.darker() : Color.DARK_GRAY);

        String balanceText = (transaction.getType() == TransactionType.DEPOSIT ? "+ " : "") + "$" + numberFormat.format(transaction.getAmount());
        g.drawString(balanceText, getBounds().width - g.getFontMetrics().stringWidth(balanceText), getBounds().height - 5);

        // Date
        g.setColor(Color.DARK_GRAY);
        g.setFont(new Font("Times New Roman", Font.PLAIN, 12));

        Date date = new Date(transaction.getTimestamp());
        String dateText = dateFormat.format(date);
        g.drawString(dateText, 0, getBounds().height - 5);

        // Lower bound border
        g.setColor(Color.GRAY);
        g.fillRect(0, getBounds().height - 2, getBounds().width, 2);

    }

    public BufferedImage getImage() {

        return image;

    }

}
