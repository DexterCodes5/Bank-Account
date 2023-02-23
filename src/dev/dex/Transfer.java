package dev.dex;

import static java.lang.Thread.currentThread;

public class Transfer implements Runnable {
    private NewBankAccount sourceAccount, destinationAccount;
    private double amount;

    Transfer(NewBankAccount sourceAccount, NewBankAccount destinationAccount, double amount) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
    }

    public void run() {
        while (!sourceAccount.transfer(destinationAccount, amount))
            continue;
        System.out.printf("%s completed\n", currentThread().getName());
    }
}
