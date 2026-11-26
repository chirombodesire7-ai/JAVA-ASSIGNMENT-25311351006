
public class Depositor extends Thread {

    private BankAccount account;

    public Depositor(BankAccount account) {
        this.account = account;
    }

    public void run() {

        try {
            for (int i = 1; i <= 5; i++) {

                Thread.sleep(1000);
                account.deposit(100000);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

