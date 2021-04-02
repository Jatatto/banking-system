package com.jakehonea.banking.transactions;

import java.util.Arrays;

public enum TransactionType {

    WITHDRAW,
    DEPOSIT,
    TRANSFER;

    @Override
    public String toString() {

        return name().toLowerCase();

    }

    public static TransactionType from(String label) {

        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(label))
                .findFirst()
                .orElse(null);

    }

}