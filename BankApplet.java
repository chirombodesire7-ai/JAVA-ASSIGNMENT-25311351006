
import java.applet.Applet;
import java.awt.Graphics;

public class BankApplet extends Applet {

    String accountHolder;
    String bankName;

    public void init() {

        accountHolder = getParameter("holder");
        bankName = getParameter("bank");

        if (accountHolder == null) {
            accountHolder = "Unknown";
        }

        if (bankName == null) {
            bankName = "Unknown";
        }
    }

    public void start() {
        System.out.println("Applet started.");
    }

    public void paint(Graphics g) {

        g.drawString("Account Holder: " + accountHolder, 30, 50);

        g.drawString("Bank Name: " + bankName, 30, 80);
    }

    public void stop() {
        System.out.println("Applet stopped.");
    }

    public void destroy() {
        System.out.println("Applet destroyed.");
    }
}
