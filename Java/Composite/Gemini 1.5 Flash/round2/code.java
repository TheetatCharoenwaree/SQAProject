import java.util.HashMap;
import java.util.Map;

// Abstract Component - Bank Account Component
abstract class BankAccountComponent {
    protected String accountNumber;

    public BankAccountComponent(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public abstract void displayBalance();
    public abstract void withdraw(double amount);
    public abstract void deposit(double amount);
    public abstract void changePIN(String oldPIN, String newPIN);
    public abstract String getAccountNumber();
}

// Leaf - Checking Account
class CheckingAccount extends BankAccountComponent {
    private double balance;
    private String pin;

    public CheckingAccount(String accountNumber, String pin, double initialBalance) {
        super(accountNumber);
        this.balance = initialBalance;
        this.pin = pin;
    }

    @Override
    public void displayBalance() {
        System.out.println("Account Balance: " + balance);
    }

    @Override
    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            System.out.println("Withdrawal successful. New balance: " + balance);
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
        System.out.println("Deposit successful. New balance: " + balance);
    }

    @Override
    public void changePIN(String oldPIN, String newPIN) {
        if (oldPIN.equals(this.pin)) {
            this.pin = newPIN;
            System.out.println("PIN changed successfully.");
        } else {
            System.out.println("Incorrect old PIN.");
        }
    }

    public double getBalance() {
        return balance;
    }

    public String getPIN() {
        return pin;
    }
}

// Composite - ATM
class ATM {
    private Map<String, BankAccountComponent> accounts;
    private int pinAttempts;

    public ATM() {
        accounts = new HashMap<>();
        pinAttempts = 0;
    }

    public void addAccount(String accountNumber, String pin, double initialBalance) {
        accounts.put(accountNumber, new CheckingAccount(accountNumber, pin, initialBalance));
    }

    public void authenticate(String accountNumber, String pin) {
        BankAccountComponent account = accounts.get(accountNumber);
        if (account != null && account.getPIN().equals(pin)) {
            System.out.println("Authentication successful.");
            pinAttempts = 0; // Reset attempts
            processTransactions(account);
        } else {
            pinAttempts++;
            if (pinAttempts >= 3) {
                System.out.println("Card retained. Too many incorrect PIN attempts.");
            } else {
                System.out.println("Incorrect PIN. Attempts remaining: " + (3 - pinAttempts));
            }
        }
    }

    private void processTransactions(BankAccountComponent account) {
        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Display Balance");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Change PIN");
            System.out.println("5. Exit");

            int choice = 0;
            try {
                choice = Integer.parseInt(System.console().readLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    account.displayBalance();
                    break;
                case 2:
                    System.out.print("Enter withdrawal amount: ");
                    try {
                        double amount = Double.parseDouble(System.console().readLine());
                        account.withdraw(amount);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount. Please enter a number.");
                    }
                    break;
                case 3:
                    System.out.print("Enter deposit amount: ");
                    try {
                        double amount = Double.parseDouble(System.console().readLine());
                        account.deposit(amount);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount. Please enter a number.");
                    }
                    break;
                case 4:
                    System.out.print("Enter old PIN: ");
                    String oldPIN = System.console().readLine();
                    System.out.print("Enter new PIN: ");
                    String newPIN = System.console().readLine();
                    System.out.print("Confirm new PIN: ");
                    String confirmPIN = System.console().readLine();
                    if (newPIN.equals(confirmPIN)) {
                        account.changePIN(oldPIN, newPIN);
                    } else {
                        System.out.println("New PINs do not match.");
                    }
                    break;
                case 5:
                    System.out.println("Exiting ATM.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}