import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ATMSystemTest {

    @Test
    void testAuthenticationSuccess() {
        ATMCard card = new ATMCard("1234567890", 1234);
        Authentication authentication = new Authentication(card);
        authentication.performOperation();
        // Add assertions to verify successful authentication
    }

    @Test
    void testAuthenticationFailure() {
        ATMCard card = new ATMCard("1234567890", 0000);
        Authentication authentication = new Authentication(card);
        authentication.performOperation();
        // Add assertions to verify authentication failure
    }

    @Test
    void testAuthenticationCardRetained() {
        ATMCard card = new ATMCard("1234567890", 0000);
        Authentication authentication = new Authentication(card);
        for (int i = 0; i <= 3; i++) {
            authentication.performOperation();
        }
        // Add assertions to verify card is retained
    }

    @Test
    void testBalanceDisplay() {
        Account account = new Account(1000);
        BalanceDisplay balanceDisplay = new BalanceDisplay(account);
        balanceDisplay.performOperation();
        // Add assertions to verify balance display
    }

    @Test
    void testCashWithdrawalSuccess() {
        Account account = new Account(1000);
        CashWithdrawal cashWithdrawal = new CashWithdrawal(account);
        cashWithdrawal.performOperation();
        // Add assertions to verify successful withdrawal
    }

    @Test
    void testCashWithdrawalInsufficientFunds() {
        Account account = new Account(100);
        CashWithdrawal cashWithdrawal = new CashWithdrawal(account);
        cashWithdrawal.performOperation();
        // Add assertions to verify insufficient funds
    }

    @Test
    void testDeposit() {
        Account account = new Account(1000);
        Deposit deposit = new Deposit(account);
        deposit.performOperation();
        // Add assertions to verify successful deposit
    }

    @Test
    void testPINChange() {
        ATMCard card = new ATMCard("1234567890", 1234);
        PINChange pinChange = new PINChange(card);
        pinChange.performOperation();
        // Add assertions to verify successful PIN change
    }

    @Test
    void testReceiptPrinting() {
        List<String> transactionHistory = new ArrayList<>();
        ReceiptPrinting receiptPrinting = new ReceiptPrinting(transactionHistory);
        receiptPrinting.performOperation();
        // Add assertions to verify receipt printing
    }

    @Test
    void testTransaction() {
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
        transactionHistory.add("Withdrawal: $100.00");
        transactionHistory.add("Deposit: $200.00");
        transactionHistory.add("Balance: $" + account.getBalance());

        // Print the receipt
        receiptPrinting.performOperation();
        // Add assertions to verify successful transaction execution
    }
}