package dev.dex;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

import static java.lang.Thread.currentThread;

public class NewBankAccount {
    private double balance;
    private String accountNumber;
    private Lock lock = new ReentrantLock();

    NewBankAccount(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public boolean withdraw(double amount) {
        try {
            if (lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
                System.out.println(currentThread().getName() + " acquired the lock");

                try {
                    // Simulate database access
                    Thread.sleep(100);

                    balance -= amount;
                    System.out.printf("%s: Withdraw %f\n", currentThread().getName(), amount);
                    return true;
                }
                finally {
                    lock.unlock();
                    System.out.println(currentThread().getName() + " released the lock");
                }
            }
        }
        catch (InterruptedException e) {

        }

        return false;
    }

    public boolean deposit(double amount) {
        try {
            if (lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
                System.out.println(currentThread().getName() + " acquired the lock");
                try {
                    // Simulate database access
                    Thread.sleep(100);

                    balance += amount;
                    System.out.printf("%s: Deposited %f\n", currentThread().getName(), amount);
                    return true;
                }  finally {
                    lock.unlock();
                    System.out.println(currentThread().getName() + " released the lock");
                }
            }
        }
        catch (InterruptedException e) {

        }
        return false;
    }

    public boolean transfer(NewBankAccount destinationAccount, double amount) {
        if (withdraw(amount)) {
            if (destinationAccount.deposit(amount)) {
                return true;
            }
            else {
                // The deposit failed. Refund the money back into the account.
                System.out.printf("%s: Destination account busy. Refunding money\n",
                        currentThread().getName());
                deposit(amount);
            }
        }

        return false;
    }
}
