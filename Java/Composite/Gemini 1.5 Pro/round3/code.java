import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Component Interface
interface ATMComponent {
    void execute();
}

// Leaf: Account
class Account {
    private String cardNumber;
    private String pin;
    private double balance;

    public Account(String cardNumber, String pin, double balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public boolean verifyPin(String pin) {
        return this.pin.equals(pin);
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void changePin(String newPin) {
        this.pin = newPin;
    }
}

// Composite: ATM Operations
class ATMOperation implements ATMComponent {
    private String name;
    private List<ATMComponent> components = new ArrayList<>();

    public ATMOperation(String name) {
        this.name = name;
    }

    public void addComponent(ATMComponent component) {
        components.add(component);
    }

    public void removeComponent(ATMComponent component) {
        components.remove(component);
    }

    @Override
    public void execute() {
        System.out.println("---- " + name + " ----");
        for (ATMComponent component : components) {
            component.execute();
        }
    }
}

// Leaf: User Authentication
class UserAuthentication implements ATMComponent {
    private Account account;
    private int attemptsLeft = 3;

    public UserAuthentication(Account account) {
        this.account = account;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter PIN: ");
        String enteredPin = scanner.nextLine();

        if (account.verifyPin(enteredPin)) {
            System.out.println("Authentication successful.");
            attemptsLeft = 3; 
        } else {
            attemptsLeft--;
            System.out.println("Incorrect PIN. Attempts left: " + attemptsLeft);

            if (attemptsLeft == 0) {
                System.out.println("Card retained. Please contact your bank.");
                System.exit(0);
            }
        }
    }
}

// Leaf: Balance Inquiry
class BalanceInquiry implements ATMComponent {
    private Account account;

    public BalanceInquiry(Account account) {
        this.account = account;
    }

    @Override
    public void execute() {
        System.out.println("Balance: $" + account.getBalance());
    }
}

// Leaf: Cash Withdrawal
class CashWithdrawal implements ATMComponent {
    private Account account;

    public CashWithdrawal(Account account) {
        this.account = account;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter withdrawal amount: $");
        double amount = scanner.nextDouble();

        if (account.withdraw(amount)) {
            System.out.println("Please collect your cash.");
        } else {
            System.out.println("Insufficient funds.");
        }
    }
}
// Leaf: Deposit
class Deposit implements ATMComponent {
    private Account account;

    public Deposit(Account account) {
        this.account = account;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter deposit amount: $");
        double amount = scanner.nextDouble();

        account.deposit(amount);
        System.out.println("Deposit successful.");
    }
}

// Leaf: PIN Change
class PINChange implements ATMComponent {
    private Account account;

    public PINChange(Account account) {
        this.account = account;
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter old PIN: ");
        String oldPin = scanner.nextLine();

        if (account.verifyPin(oldPin)) {
            System.out.print("Enter new PIN: ");
            String newPin1 = scanner.nextLine();
            System.out.print("Confirm new PIN: ");
            String newPin2 = scanner.nextLine();

            if (newPin1.equals(newPin2)) {
                account.changePin(newPin1);
                System.out.println("PIN changed successfully.");
            } else {
                System.out.println("New PINs do not match.");
            }
        } else {
            System.out.println("Incorrect old PIN.");
        }
    }
}
// Leaf: Receipt Printing
class ReceiptPrinting implements ATMComponent {
    // ... (Simplified for brevity)
    @Override
    public void execute() {
        System.out.println("Printing receipt...");
        // Add logic to print transaction details
    }
}

// Client
public class ATM {
    public static void main(String[] args) {
        Account userAccount = new Account("1234567890", "1234", 1000.00);

        ATMComponent authentication = new UserAuthentication(userAccount);
        ATMComponent balanceInquiry = new BalanceInquiry(userAccount);
        ATMComponent cashWithdrawal = new CashWithdrawal(userAccount);
        ATMComponent deposit = new Deposit(userAccount);
        ATMComponent pinChange = new PINChange(userAccount);
        ATMComponent printReceipt = new ReceiptPrinting();

        // Create Composite Operations
        ATMOperation loginOperation = new ATMOperation("Login");
        loginOperation.addComponent(authentication);

        ATMOperation transactionOperation = new ATMOperation("Transactions");
        transactionOperation.addComponent(balanceInquiry);
        transactionOperation.addComponent(cashWithdrawal);
        transactionOperation.addComponent(deposit);
        transactionOperation.addComponent(pinChange);

        ATMOperation completeOperation = new ATMOperation("Complete Transaction");
        completeOperation.addComponent(printReceipt); 

        // Main ATM Loop
        Scanner scanner = new Scanner(System.in);

        while (true) {
            loginOperation.execute(); // Perform authentication

            System.out.println("\nSelect Transaction:");
            System.out.println("1. Balance Inquiry");
            System.out.println("2. Cash Withdrawal");
            System.out.println("3. Deposit");
            System.out.println("4. PIN Change");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    transactionOperation.execute();
                    break;
                case 2:
                    transactionOperation.execute();
                    break;
                case 3:
                    transactionOperation.execute();
                    break;
                case 4:
                    transactionOperation.execute(); 
                    break;
                case 5:
                    System.out.println("Thank you for using the ATM.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }

            // After each transaction, offer receipt printing
            System.out.println("\nDo you want to print a receipt? (Yes/No)");
            scanner.nextLine(); // consume newline
            String receiptChoice = scanner.nextLine().toLowerCase();
            if (receiptChoice.equals("yes")) {
                completeOperation.execute();
            }
        }
    }
}