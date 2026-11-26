
public class BankAccount {

    private double balance;
    private static final double LIMIT = 500000;

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public synchronized void deposit(double amount) {

        balance += amount;

        System.out.println("Deposited: MWK " + amount);
        System.out.println("Balance: MWK " + balance);

        notifyAll();
    }

    public synchronized void withdraw(double amount)
            throws InsufficientFundsException {

        if (amount <= 0 || amount > LIMIT) {
            throw new InsufficientFundsException("Invalid withdrawal amount.");
        }

        while (balance < amount) {

            System.out.println("Insufficient balance. Waiting...");

            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        balance -= amount;

        System.out.println("Withdrawn: MWK " + amount);
        System.out.println("Balance: MWK " + balance);
    }

    public synchronized double getBalance() {
        return balance;
    }
}

