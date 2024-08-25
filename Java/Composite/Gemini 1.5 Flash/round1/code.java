import java.util.HashMap;
import java.util.Map;

// Abstract Component (ATM component)
abstract class ATMComponent {
    protected String name;

    public ATMComponent(String name) {
        this.name = name;
    }

    public abstract void execute();

    public abstract String getName();
}

// Leaf Component (Basic ATM operations)
class BalanceDisplay extends ATMComponent {
    private Account account;

    public BalanceDisplay(Account account) {
        super("Balance Display");
        this.account = account;
    }

    @Override
    public void execute() {
        System.out.println("Your current balance is: " + account.getBalance());
    }

    @Override
    public String getName() {
        return name;
    }
}

class CashWithdrawal extends ATMComponent {
    private Account account;

    public CashWithdrawal(Account account) {
        super("Cash Withdrawal");
        this.account = account;
    }

    @Override
    public void execute() {
        double amount = getWithdrawalAmount();
        if (account.getBalance() >= amount) {
            account.withdraw(amount);
            System.out.println("Cash withdrawn successfully. Your new balance is: " + account.getBalance());
        } else {
            System.out.println("Insufficient funds!");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    // Mock method for getting withdrawal amount (replace with user input)
    private double getWithdrawalAmount() {
        return 100.00;
    }
}

class Deposit extends ATMComponent {
    private Account account;

    public Deposit(Account account) {
        super("Deposit");
        this.account = account;
    }

    @Override
    public void execute() {
        double amount = getDepositAmount();
        account.deposit(amount);
        System.out.println("Deposit successful. Your new balance is: " + account.getBalance());
    }

    @Override
    public String getName() {
        return name;
    }

    // Mock method for getting deposit amount (replace with user input)
    private double getDepositAmount() {
        return 200.00;
    }
}

class PINChange extends ATMComponent {
    private Account account;

    public PINChange(Account account) {
        super("PIN Change");
        this.account = account;
    }

    @Override
    public void execute() {
        String newPIN = getNewPIN();
        if (newPIN != null) {
            account.setPIN(newPIN);
            System.out.println("PIN changed successfully.");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    // Mock method for getting new PIN (replace with user input)
    private String getNewPIN() {
        return "1234";
    }
}

// Composite Component (ATM Menu)
class ATMMenu extends ATMComponent {
    private Map<String, ATMComponent> operations;

    public ATMMenu() {
        super("ATM Menu");
        operations = new HashMap<>();
    }

    public void addOperation(ATMComponent operation) {
        operations.put(operation.getName(), operation);
    }

    @Override
    public void execute() {
        System.out.println("ATM Menu:");
        for (String operationName : operations.keySet()) {
            System.out.println(operationName);
        }

        String choice = getMenuChoice();
        if (operations.containsKey(choice)) {
            operations.get(choice).execute();
        } else {
            System.out.println("Invalid choice!");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    // Mock method for getting user menu choice (replace with user input)
    private String getMenuChoice() {
        return "Cash Withdrawal";
    }
}

// Account class (Simulates account data)
class Account {
    private double balance;
    private String PIN;

    public Account(double balance, String PIN) {
        this.balance = balance;
        this.PIN = PIN;
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

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String newPIN) {
        this.PIN = newPIN;
    }
}

// Example usage:
public class ATMSystem {
    public static void main(String[] args) {
        Account account = new Account(1000.00, "1111");
        ATMMenu menu = new ATMMenu();
        menu.addOperation(new BalanceDisplay(account));
        menu.addOperation(new CashWithdrawal(account));
        menu.addOperation(new Deposit(account));
        menu.addOperation(new PINChange(account));
        menu.execute();
    }
}