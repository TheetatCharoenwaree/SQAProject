import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// ATM class representing the Context
public class ATM {

    private static final int MAX_PIN_ATTEMPTS = 3;
    private Map<String, Account> accounts;
    private Account currentAccount;

    public ATM() {
        accounts = new HashMap<>();
        // Sample data
        accounts.put("1234567890", new Account("1234567890", "1234", 1000.0));
    }

    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.start();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the ATM");

        while (currentAccount == null) {
            System.out.print("Please insert your card (card number): ");
            String cardNumber = scanner.nextLine();

            System.out.print("Enter your PIN: ");
            String pin = scanner.nextLine();

            if (authenticateUser(cardNumber, pin)) {
                System.out.println("Authentication successful.");
                currentAccount = accounts.get(cardNumber);
            } else {
                System.out.println("Authentication failed. Please try again.");
            }
        }

        performTransactions(scanner);
    }

    private boolean authenticateUser(String cardNumber, String pin) {
        Account account = accounts.get(cardNumber);
        if (account != null) {
            int attempts = 0;
            while (attempts < MAX_PIN_ATTEMPTS) {
                if (account.getPin().equals(pin)) {
                    return true;
                } else {
                    attempts++;
                    System.out.println("Incorrect PIN. " + (MAX_PIN_ATTEMPTS - attempts) + " attempts remaining.");
                }
            }
        }
        return false;
    }

    private void performTransactions(Scanner scanner) {
        TransactionVisitor visitor = new TransactionVisitor(scanner, currentAccount);

        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    currentAccount.accept(visitor, "BalanceInquiry");
                    break;
                case 2:
                    currentAccount.accept(visitor, "CashWithdrawal");
                    break;
                case 3:
                    currentAccount.accept(visitor, "Deposit");
                    break;
                case 4:
                    currentAccount.accept(visitor, "PinChange");
                    break;
                case 5:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nSelect an option:");
        System.out.println("1. Balance Inquiry");
        System.out.println("2. Cash Withdrawal");
        System.out.println("3. Deposit");
        System.out.println("4. Change PIN");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }
}

// Account class representing the Element
class Account {
    private String cardNumber;
    private String pin;
    private double balance;

    public Account(String cardNumber, String pin, double balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public void accept(TransactionVisitor visitor, String operation) {
        visitor.visit(this, operation);
    }

    // Getters and setters
    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}

// TransactionVisitor class representing the Visitor
class TransactionVisitor {
    private Scanner scanner;
    private Account account;

    public TransactionVisitor(Scanner scanner, Account account) {
        this.scanner = scanner;
        this.account = account;
    }

    public void visit(Account account, String operation) {
        switch (operation) {
            case "BalanceInquiry":
                System.out.println("Your balance is: $" + account.getBalance());
                break;
            case "CashWithdrawal":
                System.out.print("Enter amount to withdraw: $");
                double amountToWithdraw = scanner.nextDouble();
                if (amountToWithdraw <= account.getBalance()) {
                    account.setBalance(account.getBalance() - amountToWithdraw);
                    System.out.println("Withdrawal successful. Please take your cash.");
                } else {
                    System.out.println("Insufficient funds.");
                }
                break;
            case "Deposit":
                System.out.print("Enter amount to deposit: $");
                double amountToDeposit = scanner.nextDouble();
                account.setBalance(account.getBalance() + amountToDeposit);
                System.out.println("Deposit successful.");
                break;
            case "PinChange":
                System.out.print("Enter old PIN: ");
                String oldPin = scanner.nextLine();
                if (oldPin.equals(account.getPin())) {
                    System.out.print("Enter new PIN: ");
                    String newPin1 = scanner.nextLine();
                    System.out.print("Confirm new PIN: ");
                    String newPin2 = scanner.nextLine();
                    if (newPin1.equals(newPin2)) {
                        account.setPin(newPin1);
                        System.out.println("PIN change successful.");
                    } else {
                        System.out.println("New PINs do not match.");
                    }
                } else {
                    System.out.println("Incorrect old PIN.");
                }
                break;
        }
    }
}