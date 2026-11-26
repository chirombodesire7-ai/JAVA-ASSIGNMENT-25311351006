
import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        try {

            System.out.print("Enter account holder's name: ");
            String name = input.nextLine();

            System.out.print("Enter opening balance: MWK ");
            double openingBalance = input.nextDouble();

            if (openingBalance < 0) {
                System.out.println("Opening balance cannot be negative.");
                return;
            }

            BankAccount account = new BankAccount(openingBalance);

            System.out.println("Account Holder: " + name);

            Depositor depositor = new Depositor(account);

            Withdrawer withdrawerTask = new Withdrawer(account);
            Thread withdrawer = new Thread(withdrawerTask);

            depositor.setPriority(Thread.MAX_PRIORITY);
            withdrawer.setPriority(Thread.MIN_PRIORITY);

            depositor.start();
            withdrawer.start();

            depositor.join();
            withdrawer.join();

            System.out.println("Final Balance: MWK " + account.getBalance());

        } catch (InputMismatchException e) {
            System.out.println("Invalid input.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

        } finally {
            System.out.println("Thank you for using the banking system.");
            input.close();
        }
    }
}

