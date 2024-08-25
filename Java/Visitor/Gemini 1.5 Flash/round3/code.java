import java.util.HashMap;
import java.util.Map;

// Account class
class Account {
    private int accountNumber;
    private int pin;
    private double balance;
    private int pinAttempts;

    public Account(int accountNumber, int pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.pinAttempts = 0;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public int getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getPinAttempts() {
        return pinAttempts;
    }

    public void incrementPinAttempts() {
        this.pinAttempts++;
    }

    public void resetPinAttempts() {
        this.pinAttempts = 0;
    }

    public void changePin(int newPin) {
        this.pin = newPin;
    }
}

// ATM interface
interface ATM {
    void authenticate(Account account, int pin);
    void displayBalance(Account account);
    void withdraw(Account account, double amount);
    void deposit(Account account, double amount);
    void changePin(Account account, int newPin);
    void printReceipt(Account account, String transactionType, double amount);
}

// Concrete ATM implementation
class ConcreteATM implements ATM {
    private Map<Integer, Account> accounts;

    public ConcreteATM() {
        accounts = new HashMap<>();
        // Initialize accounts (replace with your data source)
        accounts.put(1234567890, new Account(1234567890, 1111, 1000.0));
        accounts.put(9876543210, new Account(9876543210, 2222, 500.0));
    }

    @Override
    public void authenticate(Account account, int pin) {
        if (account.getPin() == pin) {
            account.resetPinAttempts();
            System.out.println("Authentication successful.");
        } else {
            account.incrementPinAttempts();
            System.out.println("Incorrect PIN. Attempts remaining: " + (3 - account.getPinAttempts()));
            if (account.getPinAttempts() >= 3) {
                System.out.println("Card retained.");
            }
        }
    }

    @Override
    public void displayBalance(Account account) {
        System.out.println("Your current balance: $" + account.getBalance());
    }

    @Override
    public void withdraw(Account account, double amount) {
        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            System.out.println("Cash withdrawn: $" + amount);
            printReceipt(account, "Withdrawal", amount);
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    @Override
    public void deposit(Account account, double amount) {
        account.setBalance(account.getBalance() + amount);
        System.out.println("Cash deposited: $" + amount);
        printReceipt(account, "Deposit", amount);
    }

    @Override
    public void changePin(Account account, int newPin) {
        account.changePin(newPin);
        System.out.println("PIN changed successfully.");
    }

    @Override
    public void printReceipt(Account account, String transactionType, double amount) {
        // Replace with your receipt printing logic
        System.out.println("----------------------");
        System.out.println("Transaction: " + transactionType);
        System.out.println("Amount: $" + amount);
        System.out.println("Balance: $" + account.getBalance());
        System.out.println("----------------------");
    }
}

// Visitor interface for transaction operations
interface TransactionVisitor {
    void visit(Account account, ATM atm);
}

// Concrete visitors for different transactions
class BalanceVisitor implements TransactionVisitor {
    @Override
    public void visit(Account account, ATM atm) {
        atm.displayBalance(account);
    }
}

class WithdrawalVisitor implements TransactionVisitor {
    private double amount;

    public WithdrawalVisitor(double amount) {
        this.amount = amount;
    }

    @Override
    public void visit(Account account, ATM atm) {
        atm.withdraw(account, amount);
    }
}

class DepositVisitor implements TransactionVisitor {
    private double amount;

    public DepositVisitor(double amount) {
        this.amount = amount;
    }

    @Override
    public void visit(Account account, ATM atm) {
        atm.deposit(account, amount);
    }
}

class ChangePinVisitor implements TransactionVisitor {
    private int newPin;

    public ChangePinVisitor(int newPin) {
        this.newPin = newPin;
    }

    @Override
    public void visit(Account account, ATM atm) {
        atm.changePin(account, newPin);
    }
}

// ATM system
public class ATMSystem {
    private ConcreteATM atm;

    public ATMSystem() {
        atm = new ConcreteATM();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        Account currentAccount = null;

        while (true) {
            System.out.println("\nATM Menu:");
            System.out.println("1. Insert Card");
            System.out.println("2. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.print("Enter your account number: ");
                int accountNumber = scanner.nextInt();
                scanner.nextLine();

                if (atm.accounts.containsKey(accountNumber)) {
                    currentAccount = atm.accounts.get(accountNumber);

                    while (true) {
                        System.out.println("\nATM Options:");
                        System.out.println("1. Balance Inquiry");
                        System.out.println("2. Withdraw Cash");
                        System.out.println("3. Deposit Cash");
                        System.out.println("4. Change PIN");
                        System.out.println("5. Logout");

                        choice = scanner.nextInt();
                        scanner.nextLine();

                        switch (choice) {
                            case 1:
                                atm.authenticate(currentAccount, currentAccount.getPin());
                                if (currentAccount.getPinAttempts() < 3) {
                                    new BalanceVisitor().visit(currentAccount, atm);
                                }
                                break;
                            case 2:
                                atm.authenticate(currentAccount, currentAccount.getPin());
                                if (currentAccount.getPinAttempts() < 3) {
                                    System.out.print("Enter withdrawal amount: $");
                                    double amount = scanner.nextDouble();
                                    scanner.nextLine();
                                    new WithdrawalVisitor(amount).visit(currentAccount, atm);
                                }
                                break;
                            case 3:
                                atm.authenticate(currentAccount, currentAccount.getPin());
                                if (currentAccount.getPinAttempts() < 3) {
                                    System.out.print("Enter deposit amount: $");
                                    double amount = scanner.nextDouble();
                                    scanner.nextLine();
                                    new DepositVisitor(amount).visit(currentAccount, atm);
                                }
                                break;
                            case 4:
                                atm.authenticate(currentAccount, currentAccount.getPin());
                                if (currentAccount.getPinAttempts() < 3) {
                                    System.out.print("Enter new PIN (4 digits): ");
                                    int newPin = scanner.nextInt();
                                    scanner.nextLine();
                                    new ChangePinVisitor(newPin).visit(currentAccount, atm);
                                }
                                break;
                            case 5:
                                currentAccount = null;
                                System.out.println("Logged out.");
                                break;
                            default:
                                System.out.println("Invalid option.");
                        }
                        if (choice == 5) {
                            break;
                        }
                    }
                } else {
                    System.out.println("Invalid account number.");
                }
            } else if (choice == 2) {
                System.out.println("Exiting ATM system.");
                break;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    public static void main(String[] args) {
        ATMSystem atmSystem = new ATMSystem();
        atmSystem.run();
    }
}