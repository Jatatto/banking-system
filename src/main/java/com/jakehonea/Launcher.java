package com.jakehonea;

import com.jakehonea.banking.CentralBank;
import com.jakehonea.ui.AccountInformationUI;

import java.io.IOException;

public class Launcher {

    public static void main(String[] args) {

        try {
            new AccountInformationUI(new CentralBank().getAccountManager().getAccount("Jake"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
