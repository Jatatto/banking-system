package com.jakehonea.banking.accounts;

import com.jakehonea.banking.CentralBank;

public class Account {

    private final CentralBank bank;

    public Account(String id, CentralBank bank) {

        this.bank = bank;

    }

}
