package com.company.Porterfield;

import java.util.concurrent.*;

class CheckAccount {
    private Semaphore perm = new Semaphore(1, true);
    private int bal;

    public CheckAccount(int initBalance)
    {
        bal = initBalance;
    }

    synchronized public int withdraw(int amount)
    {
        try {
            perm.acquire();
        } catch (InterruptedException e) {
            return bal;
        }
        if (amount <= bal)
        {
            try {
                Thread.sleep((int) (Math.random() * 200));
            }
            catch (InterruptedException ie) {
            }

            bal -= amount;
        }

        perm.release();
        return bal;
    }
}

class AccountHolder implements Runnable {
    private String name;
    private CheckAccount acc;

    AccountHolder(String name, CheckAccount account) {
        this.name = name;
        this.acc = account;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(name + " tries to withdraw $10, balance: " +
                    acc.withdraw(10));
        }

    }

}

public class Main {
    public static void main(String[] args) {
        CheckAccount account = new CheckAccount(100);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(new AccountHolder("Wife", account));
        executor.submit(new AccountHolder("Husband", account));

    }
}
