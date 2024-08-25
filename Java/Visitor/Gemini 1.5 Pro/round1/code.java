import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// ATM class representing the context
public class ATM {
    private Map<String, Account> accounts;
    private Account currentAccount;
    private int maxAttempts;
    private Scanner input;

    public ATM() {
        accounts = new HashMap<>();
        accounts.put("1234567890123456", new Account("1234567890123456", "1234", 1000.0));
        maxAttempts = 3;
        input = new Scanner(System.in);
    }

    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.start();
    }

    public void start() {
        System.out.println("Welcome to the ATM!");

        // Authentication
        if (authenticate()) {
            // User is authenticated
            System.out.println("Authentication successful.");
            showMenu();
        } else {
            // Authentication failed
            System.out.println("Authentication failed. Exiting...");
        }
    }

    private boolean authenticate() {
        System.out.print("Enter card number: ");
        String cardNumber = input.nextLine();

        if (accounts.containsKey(cardNumber)) {
            currentAccount = accounts.get(cardNumber);
            int attempts = 0;
            while (attempts < maxAttempts) {
                System.out.print("Enter PIN: ");
                String pin = input.nextLine();
                if (currentAccount.validatePin(pin)) {
                    return true;
                } else {
                    attempts++;
                    System.out.println("Incorrect PIN. Attempts remaining: " + (maxAttempts - attempts));
                }
            }
            System.out.println("Card retained due to exceeding maximum attempts.");
        } else {
            System.out.println("Invalid card number.");
        }
        return false;
    }

    private void showMenu() {
        ATMVisitor visitor = new ATMVisitorImpl(currentAccount, input);
        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Check Balance");
            System.out.println("2. Withdraw Cash");
            System.out.println("3. Deposit Cash");
            System.out.println("4. Change PIN");
            System.out.println("5. Exit");

            int choice = input.nextInt();
            input.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    visitor.visit(new BalanceInquiry());
                    break;
                case 2:
                    visitor.visit(new CashWithdrawal());
                    break;
                case 3:
                    visitor.visit(new CashDeposit());
                    break;
                case 4:
                    visitor.visit(new PinChange());
                    break;
                case 5:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}

// Account class
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

    // Getters and setters
    public String getCardNumber() {
        return cardNumber;
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
}

// Element interface in the Visitor pattern
interface ATMOperation {
    void accept(ATMVisitor visitor);
}

// Concrete element classes implementing ATMoperation
class BalanceInquiry implements ATMOperation {
    @Override
    public void accept(ATMVisitor visitor) {
        visitor.visit(this);
    }
}

class CashWithdrawal implements ATMOperation {
    @Override
    public void accept(ATMVisitor visitor) {
        visitor.visit(this);
    }
}

class CashDeposit implements ATMOperation {
    @Override
    public void accept(ATMVisitor visitor) {
        visitor.visit(this);
    }
}

class PinChange implements ATMOperation {
    @Override
    public void accept(ATMVisitor visitor) {
        visitor.visit(this);
    }
}

// Visitor interface
interface ATMVisitor {
    void visit(BalanceInquiry balanceInquiry);
    void visit(CashWithdrawal cashWithdrawal);
    void visit(CashDeposit cashDeposit);
    void visit(PinChange pinChange);
}

// Concrete visitor class implementing ATMVisitor
class ATMVisitorImpl implements ATMVisitor {
    private Account account;
    private Scanner input;

    public ATMVisitorImpl(Account account, Scanner input) {
        this.account = account;
        this.input = input;
    }

    @Override
    public void visit(BalanceInquiry balanceInquiry) {
        System.out.println("Your balance is: $" + account.getBalance());
    }

    @Override
    public void visit(CashWithdrawal cashWithdrawal) {
        System.out.print("Enter withdrawal amount: $");
        double amount = input.nextDouble();
        if (amount > 0 && amount <= account.getBalance()) {
            account.setBalance(account.getBalance() - amount);
            System.out.println("Withdrawal successful. Please take your cash.");
            System.out.println("Remaining balance: $" + account.getBalance());
        } else {
            System.out.println("Insufficient funds or invalid withdrawal amount.");
        }
    }

    @Override
    public void visit(CashDeposit cashDeposit) {
        System.out.print("Enter deposit amount: $");
        double amount = input.nextDouble();
        if (amount > 0) {
            account.setBalance(account.getBalance() + amount);
            System.out.println("Deposit successful.");
            System.out.println("New balance: $" + account.getBalance());
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    @Override
    public void visit(PinChange pinChange) {
        System.out.print("Enter old PIN: ");
        String oldPin = input.nextLine();
        if (account.validatePin(oldPin)) {
            System.out.print("Enter new PIN: ");
            String newPin1 = input.nextLine();
            System.out.print("Confirm new PIN: ");
            String newPin2 = input.nextLine();
            if (newPin1.equals(newPin2)) {
                account.setPin(newPin1);
                System.out.println("PIN changed successfully.");
            } else {
                System.out.println("New PINs do not match.");
            }
        } else {
            System.out.println("Incorrect old PIN.");
        }
    }
}