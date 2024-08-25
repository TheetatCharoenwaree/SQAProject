import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Component Interface for ATM Operations
interface ATMComponent {
    void performOperation();
}

// Leaf: Concrete ATM Operation - Check Balance
class CheckBalance implements ATMComponent {
    private final Account account;

    public CheckBalance(Account account) {
        this.account = account;
    }

    @Override
    public void performOperation() {
        System.out.println("Your account balance is: $" + account.getBalance());
    }
}

// Leaf: Concrete ATM Operation - Withdraw Cash
class WithdrawCash implements ATMComponent {
    private final Account account;

    public WithdrawCash(Account account) {
        this.account = account;
    }

    @Override
    public void performOperation() {
        System.out.print("Enter withdrawal amount: $");
        double amount = ATM.scanner.nextDouble();
        if (account.withdraw(amount)) {
            System.out.println("Please collect your cash.");
        } else {
            System.out.println("Insufficient funds.");
        }
    }
}

// Leaf: Concrete ATM Operation - Deposit Funds
class DepositFunds implements ATMComponent {
    private final Account account;

    public DepositFunds(Account account) {
        this.account = account;
    }

    @Override
    public void performOperation() {
        System.out.print("Enter deposit amount: $");
        double amount = ATM.scanner.nextDouble();
        account.deposit(amount);
        System.out.println("Your deposit is successful.");
    }
}

// Leaf: Concrete ATM Operation - Change PIN
class ChangePIN implements ATMComponent {
    private final Account account;

    public ChangePIN(Account account) {
        this.account = account;
    }

    @Override
    public void performOperation() {
        System.out.print("Enter old PIN: ");
        String oldPIN = ATM.scanner.nextLine();
        if (account.verifyPIN(oldPIN)) {
            System.out.print("Enter new PIN: ");
            String newPIN1 = ATM.scanner.nextLine();
            System.out.print("Confirm new PIN: ");
            String newPIN2 = ATM.scanner.nextLine();
            if (newPIN1.equals(newPIN2)) {
                account.updatePIN(newPIN1);
                System.out.println("PIN changed successfully.");
            } else {
                System.out.println("PIN mismatch. Please try again.");
            }
        } else {
            System.out.println("Incorrect PIN.");
        }
    }
}

// Composite: ATM Transaction
class ATMTransaction implements ATMComponent {
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

// Account Class
class Account {
    private final String cardNumber;
    private String pin;
    private BigDecimal balance;
    private int attempts;
    private boolean cardRetained;

    public Account(String cardNumber, String pin, BigDecimal balance) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
        this.attempts = 0;
        this.cardRetained = false;
    }

    public boolean verifyPIN(String pin) {
        if (cardRetained) {
            System.out.println("Your card is retained. Please contact your bank.");
            return false;
        }

        if (this.pin.equals(pin)) {
            attempts = 0; // Reset attempts on successful PIN entry
            return true;
        } else {
            attempts++;
            if (attempts >= 3) {
                cardRetained = true;
                System.out.println("Your card is retained. Please contact your bank.");
            } else {
                System.out.println("Incorrect PIN. Attempts remaining: " + (3 - attempts));
            }
            return false;
        }
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean withdraw(double amount) {
        if (balance.compareTo(BigDecimal.valueOf(amount)) >= 0) {
            balance = balance.subtract(BigDecimal.valueOf(amount));
            System.out.println("Withdrawal successful. Remaining balance: $" + balance);
            return true;
        }
        return false;
    }

    public void deposit(double amount) {
        balance = balance.add(BigDecimal.valueOf(amount));
        System.out.println("Deposit successful. New balance: $" + balance);
    }

    public void updatePIN(String newPIN) {
        this.pin = newPIN;
    }
}

// Receipt Class
class Receipt {
    private final LocalDateTime timestamp;
    private final String transactionType;
    private final BigDecimal amount;
    private final BigDecimal remainingBalance;

    public Receipt(String transactionType, BigDecimal amount, BigDecimal remainingBalance) {
        this.timestamp = LocalDateTime.now();
        this.transactionType = transactionType;
        this.amount = amount;
        this.remainingBalance = remainingBalance;
    }

    public void printReceipt() {
        System.out.println("-----------------------------");
        System.out.println("Transaction Receipt");
        System.out.println("-----------------------------");
        System.out.println("Date: " + timestamp);
        System.out.println("Transaction: " + transactionType);
        if (amount != null) {
            System.out.println("Amount: $" + amount);
        }
        System.out.println("Remaining Balance: $" + remainingBalance);
        System.out.println("-----------------------------");
    }
}

// ATM Class
public class ATM {
    public static java.util.Scanner scanner = new java.util.Scanner(System.in);

    public static void main(String[] args) {
        // Create an account
        Account userAccount = new Account("1234567890", "1234", BigDecimal.valueOf(1000.00));

        // ATM Operations
        ATMComponent checkBalance = new CheckBalance(userAccount);
        ATMComponent withdrawCash = new WithdrawCash(userAccount);
        ATMComponent depositFunds = new DepositFunds(userAccount);
        ATMComponent changePIN = new ChangePIN(userAccount);

        // Main ATM Transaction
        ATMTransaction atmTransaction = new ATMTransaction();
        atmTransaction.addOperation(checkBalance);
        atmTransaction.addOperation(withdrawCash);
        atmTransaction.addOperation(depositFunds);
        atmTransaction.addOperation(changePIN);

        // Simulate ATM interaction
        System.out.println("Welcome to the ATM");
        System.out.print("Enter your PIN: ");
        String enteredPIN = scanner.nextLine();

        if (userAccount.verifyPIN(enteredPIN)) {
            System.out.println("PIN verified.");
            atmTransaction.performOperation();
        } else {
            System.out.println("Authentication failed.");
        }
    }
}