
public class Withdrawer implements Runnable {

    private BankAccount account;

    public Withdrawer(BankAccount account) {
        this.account = account;
    }

    public void run() {

        try {
            for (int i = 1; i <= 5; i++) {

                Thread.sleep(1500);
                account.withdraw(80000);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
        }
    }
}
