package com.jakehonea.banking;

import com.jakehonea.banking.transactions.TransactionManager;

import java.io.File;
import java.io.IOException;

public class CentralBank {

    private final Database database;
    private final TransactionManager transactionManager;

    public CentralBank()
            throws IOException {

        this.database = new Database(new File(getClass().getClassLoader().getResource("database.txt").getPath()));

        this.transactionManager = new TransactionManager(this);

    }

    public Database getDatabase() {
        return database;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public static void main(String[] args) {

        try {
            new CentralBank();
        } catch (IOException e) {
            // failed to load
        }

    }

}
