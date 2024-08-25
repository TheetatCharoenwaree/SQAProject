import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Component interface for ATM operations
interface ATMComponent {
    void performOperation();
}

// Leaf component: Authentication
class Authentication implements ATMComponent {
    private final ATMCard card;
    private int attempts;
    private final int maxAttempts = 3;

    public Authentication(ATMCard card) {
        this.card = card;
        this.attempts = 0;
    }

    @Override
    public void performOperation() {
        if (card.isCardValid()) {
            if (authenticatePIN()) {
                System.out.println("Authentication successful.");
            } else {
                attempts++;
                if (attempts >= maxAttempts) {
                    System.out.println("Too many incorrect attempts. Card retained.");
                    card.retainCard();
                } else {
                    System.out.println("Incorrect PIN. Attempts remaining: " + (maxAttempts - attempts));
                }
            }
        } else {
            System.out.println("Invalid card.");
        }
    }

    private boolean authenticatePIN() {
        // Simulate PIN verification
        return card.getPin() == 1234;
    }
}

// Leaf component: Balance Display
class BalanceDisplay implements ATMComponent {
    private final Account account;

    public BalanceDisplay(Account account) {
        this.account = account;
    }

    @Override
    public void performOperation() {
        System.out.println("Current balance: $" + account.getBalance());
    }
}

// Leaf component: Cash Withdrawal
class CashWithdrawal implements ATMComponent {
    private final Account account;

    public CashWithdrawal(Account account) {
        this.account = account;
    }

    @Override
    public void performOperation() {
        // Simulate amount selection and withdrawal logic
        double amount = 100; // Replace with actual amount input
        if (account.getBalance() >= amount) {
            account.withdraw(amount);
            System.out.println("$" + amount + " withdrawn successfully.");
        } else {
            System.out.println("Insufficient funds.");
        }
    }
}

// Leaf component: Deposit
class Deposit implements ATMComponent {
    private final Account account;

    public Deposit(Account account) {
        this.account = account;
    }

    @Override
    public void performOperation() {
        // Simulate cash deposit logic
        double amount = 200; // Replace with actual amount input
        account.deposit(amount);
        System.out.println("$" + amount + " deposited successfully.");
    }
}

// Leaf component: PIN Change
class PINChange implements ATMComponent {
    private final ATMCard card;

    public PINChange(ATMCard card) {
        this.card = card;
    }

    @Override
    public void performOperation() {
        // Simulate PIN change logic
        int newPIN = 5678; // Replace with actual new PIN input
        card.changePIN(newPIN);
        System.out.println("PIN changed successfully.");
    }
}

// Leaf component: Receipt Printing
class ReceiptPrinting implements ATMComponent {
    private final List<String> transactionHistory;

    public ReceiptPrinting(List<String> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    @Override
    public void performOperation() {
        // Simulate receipt printing logic
        System.out.println("Printing receipt...");
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions to print.");
        } else {
            for (String transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
    }
}

// Composite component: Transaction
class Transaction implements ATMComponent {
    private final List<ATMComponent> operations = new ArrayList<>();

    public void addOperation(ATMComponent operation) {
        operations.add(operation);
    }

    @Override
    public void performOperation() {
        for (ATMComponent operation : operations) {
            operation.performOperation();
        }
    }
}

// Data class for ATM card
class ATMCard {
    private final String cardNumber;
    private int pin;
    private boolean isCardValid;
    private boolean isCardRetained;

    public ATMCard(String cardNumber, int pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.isCardValid = true;
        this.isCardRetained = false;
    }

    public boolean isCardValid() {
        return isCardValid;
    }

    public int getPin() {
        return pin;
    }

    public void changePIN(int newPIN) {
        this.pin = newPIN;
    }

    public void retainCard() {
        this.isCardRetained = true;
    }
}

// Data class for Account
class Account {
    private double balance;

    public Account(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    public void deposit(double amount) {
        balance += amount;
    }
}

public class ATMSystem {
    public static void main(String[] args) {
        // Create an ATM card and account
        ATMCard card = new ATMCard("1234567890", 1234);
        Account account = new Account(1000);

        // Create ATM components
        Authentication authentication = new Authentication(card);
        BalanceDisplay balanceDisplay = new BalanceDisplay(account);
        CashWithdrawal cashWithdrawal = new CashWithdrawal(account);
        Deposit deposit = new Deposit(account);
        PINChange pinChange = new PINChange(card);

        // Create a transaction history list
        List<String> transactionHistory = new ArrayList<>();

        // Create a receipt printing component
        ReceiptPrinting receiptPrinting = new ReceiptPrinting(transactionHistory);

        // Create a transaction
        Transaction transaction = new Transaction();
        transaction.addOperation(authentication);
        transaction.addOperation(balanceDisplay);
        transaction.addOperation(cashWithdrawal);
        transaction.addOperation(deposit);
        transaction.addOperation(pinChange);

        // Perform the transaction
        transaction.performOperation();

        // Add transaction details to the history
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        transactionHistory.add("Date: " + dtf.format(now));
        transactionHistory.add("Withdrawal: $100.00");
        transactionHistory.add("Deposit: $200.00");
        transactionHistory.add("Balance: $" + account.getBalance());

        // Print the receipt
        receiptPrinting.performOperation();
    }
}