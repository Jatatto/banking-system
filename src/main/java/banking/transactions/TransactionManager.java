package banking.transactions;

import banking.CentralBank;
import banking.accounts.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {

    private final CentralBank bank;

    public TransactionManager(CentralBank bank) {

        this.bank = bank;

        try (Connection connection = bank.getDatabase().getConnection()) {

            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS `transactions` (" +
                    "id VARCHAR(16)," +
                    "amount DOUBLE," +
                    "type VARCHAR(8)," +
                    "time BIGINT," +
                    "comment TEXT" +
                    ")");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     *
     * @param transaction the transaction to process
     * @return whether the transaction was successfully processed and stored
     */
    public boolean processTransaction(Transaction transaction) {

        Account account = bank.getAccountManager().getAccount(transaction.getId());

        if (account != null && account.processTransaction(transaction)) {

            storeTransaction(transaction);
            return true;

        }

        return false;

    }

    /**
     *
     * @param transaction the {@link Transaction} to store in the {@link banking.Database}
     */
    public void storeTransaction(Transaction transaction) {

        try (Connection connection = bank.getDatabase().getConnection()) {

            PreparedStatement insert = connection.prepareStatement("INSERT INTO `transactions` values(?,?,?,?,?)");

            insert.setString(1, transaction.getId());
            insert.setDouble(2, transaction.getAmount());
            insert.setString(3, transaction.getType().name().toLowerCase());
            insert.setLong(4, transaction.getTimestamp());
            insert.setString(5, transaction.getComment());

            insert.execute();
            insert.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     *
     * @param account the account
     * @return a list of all the user's transactions
     */
    public List<Transaction> fetchTransactions(Account account) {

        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = bank.getDatabase().getConnection()) {

            PreparedStatement fetchTransactions = connection.prepareStatement("SELECT * FROM `transactions` WHERE id=?");

            fetchTransactions.setString(1, account.getId());

            ResultSet set = fetchTransactions.executeQuery();

            while (set.next())
                transactions.add(new Transaction(set));

            set.close();
            fetchTransactions.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return transactions;

    }

}
