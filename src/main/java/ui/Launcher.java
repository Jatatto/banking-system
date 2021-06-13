package ui;

import banking.CentralBank;

import java.io.IOException;

public class Launcher {

    public static CentralBank BANK;

    public static void main(String[] args) {

        try {
            BANK = new CentralBank();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
