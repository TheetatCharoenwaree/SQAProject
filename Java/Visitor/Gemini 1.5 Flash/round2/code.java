// ATM.java
public class ATM {
    private Account account;
    private ATMVisitor visitor;

    public ATM(Account account) {
        this.account = account;
        this.visitor = new ATMVisitor(account);
    }

    public boolean authenticate(Card card, String pin) {
        if (card.getAccount().equals(account) && account.verifyPIN(pin)) {
            return true;
        }
        return false;
    }

    public void performTransaction(Transaction transaction) {
        transaction.accept(visitor);
    }

    public void printReceipt(Transaction transaction) {
        System.out.println("Receipt:");
        System.out.println("Date: " + transaction.getDate());
        System.out.println("Time: " + transaction.getTime());
        System.out.println("Transaction: " + transaction.getDescription());
        System.out.println("Amount: " + transaction.getAmount());
        System.out.println("Balance: " + account.getBalance());
    }
}

// Account.java
public class Account {
    private double balance;
    private String pin;

    public Account(double balance, String pin) {
        this.balance = balance;
        this.pin = pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getPIN() {
        return pin;
    }

    public void setPIN(String pin) {
        this.pin = pin;
    }

    public boolean verifyPIN(String enteredPIN) {
        return enteredPIN.equals(pin);
    }
}

// Card.java
public class Card {
    private String cardNumber;
    private Account account;

    public Card(String cardNumber, Account account) {
        this.cardNumber = cardNumber;
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}

// Transaction.java
public abstract class Transaction {
    private String description;
    private double amount;
    private String date;
    private String time;

    public Transaction(String description, double amount) {
        this.description = description;
        this.amount = amount;
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        this.time = new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public abstract void accept(Visitor visitor);
}

// BalanceDisplay.java
public class BalanceDisplay extends Transaction {
    public BalanceDisplay() {
        super("Balance Display", 0);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

// CashWithdrawal.java
public class CashWithdrawal extends Transaction {
    public CashWithdrawal(double amount) {
        super("Cash Withdrawal", amount);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

// Deposit.java
public class Deposit extends Transaction {
    public Deposit(double amount) {
        super("Deposit", amount);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

// PINChange.java
public class PINChange extends Transaction {
    private String newPIN;

    public PINChange(String newPIN) {
        super("PIN Change", 0);
        this.newPIN = newPIN;
    }

    public String getNewPIN() {
        return newPIN;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

// Visitor.java
public interface Visitor {
    void visit(BalanceDisplay transaction);
    void visit(CashWithdrawal transaction);
    void visit(Deposit transaction);
    void visit(PINChange transaction);
}

// ATMVisitor.java
public class ATMVisitor implements Visitor {
    private Account account;

    public ATMVisitor(Account account) {
        this.account = account;
    }

    @Override
    public void visit(BalanceDisplay transaction) {
        System.out.println("Your balance is: " + account.getBalance());
    }

    @Override
    public void visit(CashWithdrawal transaction) {
        if (account.getBalance() >= transaction.getAmount()) {
            account.setBalance(account.getBalance() - transaction.getAmount());
            System.out.println("Cash withdrawn successfully.");
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    @Override
    public void visit(Deposit transaction) {
        account.setBalance(account.getBalance() + transaction.getAmount());
        System.out.println("Deposit successful.");
    }

    @Override
    public void visit(PINChange transaction) {
        // Implement PIN change logic with confirmation
        account.setPIN(transaction.getNewPIN());
        System.out.println("PIN changed successfully.");
    }
}