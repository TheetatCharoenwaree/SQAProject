import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ATMTest {

    @Test
    void testAuthenticateSuccess() {
        Account account = new Account(1000, "1234");
        Card card = new Card("1234567890", account);
        ATM atm = new ATM(account);
        assertTrue(atm.authenticate(card, "1234"));
    }

    @Test
    void testAuthenticateFailure() {
        Account account = new Account(1000, "1234");
        Card card = new Card("1234567890", account);
        ATM atm = new ATM(account);
        assertFalse(atm.authenticate(card, "1111"));
    }

    @Test
    void testBalanceDisplay() {
        Account account = new Account(1000, "1234");
        ATM atm = new ATM(account);
        BalanceDisplay transaction = new BalanceDisplay();
        atm.performTransaction(transaction);
        assertEquals(1000, account.getBalance());
    }

    @Test
    void testCashWithdrawalSuccess() {
        Account account = new Account(1000, "1234");
        ATM atm = new ATM(account);
        CashWithdrawal transaction = new CashWithdrawal(500);
        atm.performTransaction(transaction);
        assertEquals(500, account.getBalance());
    }

    @Test
    void testCashWithdrawalFailure() {
        Account account = new Account(1000, "1234");
        ATM atm = new ATM(account);
        CashWithdrawal transaction = new CashWithdrawal(1500);
        atm.performTransaction(transaction);
        assertEquals(1000, account.getBalance());
    }

    @Test
    void testDeposit() {
        Account account = new Account(1000, "1234");
        ATM atm = new ATM(account);
        Deposit transaction = new Deposit(200);
        atm.performTransaction(transaction);
        assertEquals(1200, account.getBalance());
    }

    @Test
    void testPINChange() {
        Account account = new Account(1000, "1234");
        ATM atm = new ATM(account);
        PINChange transaction = new PINChange("5678");
        atm.performTransaction(transaction);
        assertEquals("5678", account.getPIN());
    }
}