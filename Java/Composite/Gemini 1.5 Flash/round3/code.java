import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Component interface
interface ATMComponent {
    void process(ATMContext context);
}

// Leaf components
class AuthenticateComponent implements ATMComponent {
    @Override
    public void process(ATMContext context) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your card number:");
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();

        if (context.authenticateUser(cardNumber, pin)) {
            System.out.println("Authentication successful.");
            context.setCurrentUser(cardNumber);
        } else {
            System.out.println("Incorrect card or PIN. Please try again.");
            context.incrementFailedAttempts();
            if (context.getFailedAttempts() >= 3) {
                System.out.println("Card retained. Please contact your bank.");
                context.resetFailedAttempts();
            }
        }
    }
}

class DisplayBalanceComponent implements ATMComponent {
    @Override
    public void process(ATMContext context) {
        System.out.println("Your current balance is: " + context.getBalance(context.getCurrentUser()));
    }
}

class WithdrawCashComponent implements ATMComponent {
    @Override
    public void process(ATMContext context) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the amount you wish to withdraw:");
        double amount = scanner.nextDouble();

        if (context.getBalance(context.getCurrentUser()) >= amount) {
            context.withdraw(context.getCurrentUser(), amount);
            System.out.println("Cash dispensed. Your new balance is: " + context.getBalance(context.getCurrentUser()));
        } else {
            System.out.println("Insufficient funds.");
        }
    }
}

class DepositCashComponent implements ATMComponent {
    @Override
    public void process(ATMContext context) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the amount you wish to deposit:");
        double amount = scanner.nextDouble();

        context.deposit(context.getCurrentUser(), amount);
        System.out.println("Cash deposited. Your new balance is: " + context.getBalance(context.getCurrentUser()));
    }
}

class ChangePINComponent implements ATMComponent {
    @Override
    public void process(ATMContext context) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your old PIN:");
        String oldPIN = scanner.nextLine();
        System.out.println("Enter your new PIN:");
        String newPIN = scanner.nextLine();
        System.out.println("Confirm your new PIN:");
        String confirmPIN = scanner.nextLine();

        if (newPIN.equals(confirmPIN) && context.authenticateUser(context.getCurrentUser(), oldPIN)) {
            context.changePIN(context.getCurrentUser(), newPIN);
            System.out.println("PIN changed successfully.");
        } else {
            System.out.println("Incorrect old PIN or new PIN mismatch.");
        }
    }
}

class PrintReceiptComponent implements ATMComponent {
    @Override
    public void process(ATMContext context) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to print a receipt? (y/n)");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("y")) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);

            System.out.println("====================");
            System.out.println("ATM Receipt");
            System.out.println("Date & Time: " + formattedDateTime);
            System.out.println("Card Number: " + context.getCurrentUser());
            System.out.println("Transaction Type: " + context.getTransactionType());
            System.out.println("Amount: " + context.getTransactionAmount());
            System.out.println("Balance: " + context.getBalance(context.getCurrentUser()));
            System.out.println("====================");

            // Implement logic to print receipt here (e.g., using a printer library)
        }
    }
}

// Composite component
class ATMMenuComponent implements ATMComponent {
    private Map<String, ATMComponent> menuItems = new HashMap<>();

    public ATMMenuComponent() {
        // Add menu items
        menuItems.put("1", new DisplayBalanceComponent());
        menuItems.put("2", new WithdrawCashComponent());
        menuItems.put("3", new DepositCashComponent());
        menuItems.put("4", new ChangePINComponent());
        menuItems.put("5", new PrintReceiptComponent());
    }

    @Override
    public void process(ATMContext context) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("ATM Menu:");
        System.out.println("1. Display Balance");
        System.out.println("2. Withdraw Cash");
        System.out.println("3. Deposit Cash");
        System.out.println("4. Change PIN");
        System.out.println("5. Print Receipt");
        System.out.println("0. Exit");
        System.out.println("Enter your choice:");
        String choice = scanner.nextLine();

        if (menuItems.containsKey(choice)) {
            menuItems.get(choice).process(context);
        } else if (choice.equals("0")) {
            System.out.println("Exiting ATM.");
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }
}

// Context class
class ATMContext {
    private Map<String, Double> accounts = new HashMap<>();
    private Map<String, String> pins = new HashMap<>();
    private String currentUser;
    private int failedAttempts;
    private String transactionType;
    private double transactionAmount;

    public ATMContext() {
        // Initialize accounts and PINs
        accounts.put("1234567890", 1000.0);
        pins.put("1234567890", "1234");
        // ... add more accounts and pins
    }

    public boolean authenticateUser(String cardNumber, String pin) {
        return pins.containsKey(cardNumber) && pins.get(cardNumber).equals(pin);
    }

    public void setCurrentUser(String cardNumber) {
        this.currentUser = cardNumber;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public double getBalance(String cardNumber) {
        return accounts.getOrDefault(cardNumber, 0.0);
    }

    public void withdraw(String cardNumber, double amount) {
        transactionType = "Withdrawal";
        transactionAmount = amount;
        accounts.put(cardNumber, accounts.get(cardNumber) - amount);
    }

    public void deposit(String cardNumber, double amount) {
        transactionType = "Deposit";
        transactionAmount = amount;
        accounts.put(cardNumber, accounts.get(cardNumber) + amount);
    }

    public void changePIN(String cardNumber, String newPIN) {
        pins.put(cardNumber, newPIN);
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void incrementFailedAttempts() {
        failedAttempts++;
    }

    public void resetFailedAttempts() {
        failedAttempts = 0;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }
}

public class ATMSystem {
    public static void main(String[] args) {
        ATMContext context = new ATMContext();
        ATMComponent authenticateComponent = new AuthenticateComponent();
        ATMComponent menuComponent = new ATMMenuComponent();

        // Authentication process
        authenticateComponent.process(context);

        // Main ATM menu
        if (context.getCurrentUser() != null) {
            menuComponent.process(context);
        }
    }
}