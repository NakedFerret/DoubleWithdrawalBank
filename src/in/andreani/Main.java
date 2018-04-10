package in.andreani;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // We're going to create a bunch of bank accounts
        // Each bank account will have $200 and each thread will
        // go through the list of accounts and withdraw $200 if possible

        BankAccount[] bankAccounts = new BankAccount[1_000_000];
        for(int i = 0; i < bankAccounts.length; i++) {
            bankAccounts[i] = new BankAccount(200);
        }

        // Threads share the bank accounts
        Thread t1 = new WithdrawThread(bankAccounts);
        Thread t2 = new WithdrawThread(bankAccounts);

        // Each thread will run independently of each other and the main thread
        t1.start();
        t2.start();

        // Wait for these threads to finish
        t1.join();
        t2.join();

        // Let's see how many of these accounts have less than $0!
        int accountsWithAProblem = 0;
        for(BankAccount account: bankAccounts) {
            if (account.amount() < 0) {
                accountsWithAProblem++;
            }
        }

        System.out.println("Banks with less than $0: " + accountsWithAProblem);
    }
}

class BankAccount {
    private long amount;

    public BankAccount(int amount) {
        this.amount = amount;
    }

    public long amount() {
        return this.amount;
    }

    public void withdraw(int amount) {
        this.amount -= amount;
    }
}

class WithdrawThread extends Thread {
    private BankAccount[] bankAccounts;

    public WithdrawThread(BankAccount[] bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    @Override
    public void run() {
        for (BankAccount account: this.bankAccounts) {
            // We'll try to withdraw 2 dollars. We'll check the amount first
            if (account.amount() >= 200) {
                account.withdraw(200);
            }
        }
    }
}
