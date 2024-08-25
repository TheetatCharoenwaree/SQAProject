import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ATMSystemTest {

    @Test
    void testBalanceDisplay() {
        Account account = new Account(1000.00, "1111");
        BalanceDisplay display = new BalanceDisplay(account);
        display.execute();
        assertEquals(1000.00, account.getBalance());
    }

    @Test
    void testCashWithdrawal() {
        Account account = new Account(1000.00, "1111");
        CashWithdrawal withdrawal = new CashWithdrawal(account);
        withdrawal.execute();
        assertEquals(900.00, account.getBalance()); // Assuming withdrawal amount is 100.00
    }

    @Test
    void testCashWithdrawalInsufficientFunds() {
        Account account = new Account(100.00, "1111");
        CashWithdrawal withdrawal = new CashWithdrawal(account);
        withdrawal.execute();
        assertEquals(100.00, account.getBalance());
    }

    @Test
    void testDeposit() {
        Account account = new Account(1000.00, "1111");
        Deposit deposit = new Deposit(account);
        deposit.execute();
        assertEquals(1200.00, account.getBalance()); // Assuming deposit amount is 200.00
    }

    @Test
    void testPINChange() {
        Account account = new Account(1000.00, "1111");
        PINChange change = new PINChange(account);
        change.execute();
        assertEquals("1234", account.getPIN()); // Assuming new PIN is "1234"
    }
}import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ATMSystemTest {

    @Test
    void testBalanceDisplay() {
        Account account = new Account(1000.00, "1111");
        BalanceDisplay display = new BalanceDisplay(account);
        display.execute();
        assertEquals(1000.00, account.getBalance());
    }

    @Test
    void testCashWithdrawal() {
        Account account = new Account(1000.00, "1111");
        CashWithdrawal withdrawal = new CashWithdrawal(account);
        withdrawal.execute();
        assertEquals(900.00, account.getBalance()); // Assuming withdrawal amount is 100.00
    }

    @Test
    void testCashWithdrawalInsufficientFunds() {
        Account account = new Account(100.00, "1111");
        CashWithdrawal withdrawal = new CashWithdrawal(account);
        withdrawal.execute();
        assertEquals(100.00, account.getBalance());
    }

    @Test
    void testDeposit() {
        Account account = new Account(1000.00, "1111");
        Deposit deposit = new Deposit(account);
        deposit.execute();
        assertEquals(1200.00, account.getBalance()); // Assuming deposit amount is 200.00
    }

    @Test
    void testPINChange() {
        Account account = new Account(1000.00, "1111");
        PINChange change = new PINChange(account);
        change.execute();
        assertEquals("1234", account.getPIN()); // Assuming new PIN is "1234"
    }
}