package com.jakehonea;

import com.jakehonea.banking.CentralBank;
import com.jakehonea.banking.accounts.Account;

import java.io.IOException;

public class Launcher {

    public static CentralBank BANK;

    public static void main(String[] args) {

        try {
            BANK = new CentralBank();
            Account account = BANK.getAccountManager().getAccount("Jake");
            new GraphUI(account, BANK.getTransactionManager().fetchTransactions(account));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
