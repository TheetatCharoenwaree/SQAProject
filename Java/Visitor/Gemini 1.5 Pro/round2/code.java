import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// ATM class representing the context
public class ATM {
    private Map<String, Account> accounts;
    private Account currentAccount;
    private Scanner input;

    public ATM() {
        accounts = new HashMap<>();
        // Sample accounts for demonstration
        accounts.put("1234567890", new Account("1234567890", "1234", 1000.00));
        accounts.put("9876543210", new Account("9876543210", "4321", 500.00));
        input = new Scanner(System.in);
    }

    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.start();
    }

    public void start() {
        System.out.println("Welcome to the ATM!");
        while (true) {
            if (currentAccount == null) {
                authenticateUser();
            } else {
                showMenu();
            }
        }
    }

    private void authenticateUser() {
        System.out.print("Enter card number: ");
        String cardNumber = input.nextLine();
        System.out.print("Enter PIN: ");
        String pin = input.nextLine();

        Account account = accounts.get(cardNumber);
        if (account != null && account.validatePin(pin)) {
            currentAccount = account;
            System.out.println("Authentication successful!\n");
        } else {
            System.out.println("Invalid card number or PIN.\n");
        }
    }

    private void showMenu() {
        System.out.println("\nSelect an option:");
        System.out.println("1. Check Balance");
        System.out.println("2. Withdraw Cash");
        System.out.println("3. Deposit Cash");
        System.out.println("4. Change PIN");
        System.out.println("5. Exit");

        int choice = input.nextInt();
        input.nextLine(); // Consume newline character

        // Using a Visitor to handle operations
        ATMVisitor visitor = new ATMOperationVisitor(currentAccount, input);
        switch (choice) {
            case 1:
                currentAccount.accept(visitor);
                break;
            case 2:
                currentAccount.accept(visitor);
                break;
            case 3:
                currentAccount.accept(visitor);
                break;
            case 4:
                currentAccount.accept(visitor);
                break;
            case 5:
                System.out.println("Thank you for using the ATM. Goodbye!");
                System.exit(0);
            default:
                System.out.println("Invalid option. Please try again.\n");
        }
    }
}

// Account class representing the element
class Account {
    private String cardNumber;
    private String pin;
    private double balance;

    public Account(String cardNumber, String pin, double balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public boolean validatePin(String pin) {
        return this.pin.equals(pin);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    // Accepting the visitor
    public void accept(ATMVisitor visitor) {
        visitor.visit(this);
    }
}

// Visitor interface
interface ATMVisitor {
    void visit(Account account);
}

// Concrete visitor implementing ATM operations
class ATMOperationVisitor implements ATMVisitor {
    private Account account;
    private Scanner input;

    public ATMOperationVisitor(Account account, Scanner input) {
        this.account = account;
        this.input = input;
    }

    @Override
    public void visit(Account account) {
        System.out.println("\nCurrent balance: $" + account.getBalance());
        System.out.println("\nSelect an operation:");
        System.out.println("1. Withdraw");
        System.out.println("2. Deposit");
        System.out.println("3. Change PIN");
        System.out.println("4. Back to Main Menu");

        int choice = input.nextInt();
        input.nextLine(); // Consume newline character

        switch (choice) {
            case 1:
                withdraw();
                break;
            case 2:
                deposit();
                break;
            case 3:
                changePin();
                break;
            case 4:
                // Back to main menu
                break;
            default:
                System.out.println("Invalid option. Please try again.\n");
        }
    }

    private void withdraw() {
        System.out.print("Enter withdrawal amount: $");
        double amount = input.nextDouble();
        input.nextLine(); // Consume newline character
        if (amount > 0 && amount <= account.getBalance()) {
            account.setBalance(account.getBalance() - amount);
            System.out.println("Withdrawal successful. Please take your cash.\n");
        } else {
            System.out.println("Insufficient funds or invalid amount.\n");
        }
    }

    private void deposit() {
        System.out.print("Enter deposit amount: $");
        double amount = input.nextDouble();
        input.nextLine(); // Consume newline character
        if (amount > 0) {
            account.setBalance(account.getBalance() + amount);
            System.out.println("Deposit successful.\n");
        } else {
            System.out.println("Invalid amount.\n");
        }
    }

    private void changePin() {
        System.out.print("Enter old PIN: ");
        String oldPin = input.nextLine();
        if (account.validatePin(oldPin)) {
            System.out.print("Enter new PIN: ");
            String newPin1 = input.nextLine();
            System.out.print("Confirm new PIN: ");
            String newPin2 = input.nextLine();

            if (newPin1.equals(newPin2)) {
                account.setPin(newPin1);
                System.out.println("PIN changed successfully.\n");
            } else {
                System.out.println("New PINs do not match.\n");
            }
        } else {
            System.out.println("Incorrect old PIN.\n");
        }
    }
}