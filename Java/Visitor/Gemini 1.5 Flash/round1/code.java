import java.util.HashMap;
import java.util.Map;

// Abstract ATM operation interface
interface ATMOperation {
    void execute(Account account);
}

// Concrete ATM operations
class BalanceDisplay implements ATMOperation {
    @Override
    public void execute(Account account) {
        System.out.println("Your current balance is: " + account.getBalance());
    }
}

class CashWithdrawal implements ATMOperation {
    private double amount;

    public CashWithdrawal(double amount) {
        this.amount = amount;
    }

    @Override
    public void execute(Account account) {
        if (account.getBalance() >= amount) {
            account.withdraw(amount);
            System.out.println("Cash withdrawn successfully.");
        } else {
            System.out.println("Insufficient funds.");
        }
    }
}

class Deposit implements ATMOperation {
    private double amount;

    public Deposit(double amount) {
        this.amount = amount;
    }

    @Override
    public void execute(Account account) {
        account.deposit(amount);
        System.out.println("Deposit successful.");
    }
}

class PINChange implements ATMOperation {
    private String newPIN;

    public PINChange(String newPIN) {
        this.newPIN = newPIN;
    }

    @Override
    public void execute(Account account) {
        // Simulate PIN verification (would involve database interaction in real scenario)
        if (newPIN.equals("correct_new_pin")) {
            account.setPIN(newPIN);
            System.out.println("PIN changed successfully.");
        } else {
            System.out.println("PIN change failed. Please try again.");
        }
    }
}

// Account class
class Account {
    private String cardNumber;
    private String PIN;
    private double balance;

    public Account(String cardNumber, String PIN, double balance) {
        this.cardNumber = cardNumber;
        this.PIN = PIN;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPIN() {
        return PIN;
    }

    public double getBalance() {
        return balance;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }
}

// ATM visitor interface
interface ATMVisitor {
    void visit(BalanceDisplay operation);
    void visit(CashWithdrawal operation);
    void visit(Deposit operation);
    void visit(PINChange operation);
}

// ATM class
class ATM {
    private Map<String, Account> accounts;

    public ATM() {
        accounts = new HashMap<>();
        // Initialize accounts for testing
        accounts.put("1234567890", new Account("1234567890", "1111", 1000));
    }

    // Simulate card and PIN verification
    public Account authenticate(String cardNumber, String PIN) {
        if (accounts.containsKey(cardNumber) && accounts.get(cardNumber).getPIN().equals(PIN)) {
            return accounts.get(cardNumber);
        } else {
            return null;
        }
    }

    // Accept ATM operation and execute it on the account
    public void processOperation(ATMOperation operation, Account account) {
        operation.execute(account);
    }

    // Accept ATM visitor and let it visit the operations
    public void accept(ATMVisitor visitor) {
        // Example operations
        visitor.visit(new BalanceDisplay());
        visitor.visit(new CashWithdrawal(500));
        visitor.visit(new Deposit(200));
        visitor.visit(new PINChange("new_pin"));
    }
}

// Example ATM visitor implementation
class PrintReceiptVisitor implements ATMVisitor {
    @Override
    public void visit(BalanceDisplay operation) {
        System.out.println("Balance Display operation");
    }

    @Override
    public void visit(CashWithdrawal operation) {
        System.out.println("Cash Withdrawal operation: " + operation.amount);
    }

    @Override
    public void visit(Deposit operation) {
        System.out.println("Deposit operation: " + operation.amount);
    }

    @Override
    public void visit(PINChange operation) {
        System.out.println("PIN Change operation");
    }
}

public class ATMSystem {
    public static void main(String[] args) {
        ATM atm = new ATM();

        // Simulate user authentication
        Account account = atm.authenticate("1234567890", "1111");
        if (account != null) {
            System.out.println("Authentication successful.");

            // Process operations
            atm.processOperation(new BalanceDisplay(), account);
            atm.processOperation(new CashWithdrawal(200), account);
            atm.processOperation(new Deposit(100), account);
            atm.processOperation(new PINChange("new_pin"), account);

            // Print receipt using Visitor pattern
            atm.accept(new PrintReceiptVisitor());
        } else {
            System.out.println("Authentication failed.");
        }
    }
}