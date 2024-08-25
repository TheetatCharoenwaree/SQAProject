import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ATMTest {

    @Test
    void testAuthenticateSuccess() {
        ATM atm = new ATM();
        atm.addAccount("1234567890", "1234", 1000);

        atm.authenticate("1234567890", "1234");

        assertTrue(atm.accounts.containsKey("1234567890"));
    }

    @Test
    void testAuthenticateFailure() {
        ATM atm = new ATM();
        atm.addAccount("1234567890", "1234", 1000);

        atm.authenticate("1234567890", "1111");

        assertFalse(atm.accounts.containsKey("1234567890"));
    }

    @Test
    void testAuthenticateTooManyAttempts() {
        ATM atm = new ATM();
        atm.addAccount("1234567890", "1234", 1000);

        atm.authenticate("1234567890", "1111");
        atm.authenticate("1234567890", "2222");
        atm.authenticate("1234567890", "3333");

        assertFalse(atm.accounts.containsKey("1234567890"));
    }

    @Test
    void testDisplayBalance() {
        ATM atm = new ATM();
        atm.addAccount("1234567890", "1234", 1000);
        CheckingAccount account = (CheckingAccount) atm.accounts.get("1234567890");

        atm.authenticate("1234567890", "1234");

        assertEquals(1000, account.getBalance());
    }

    @Test
    void testWithdrawSuccess() {
        ATM atm = new ATM();
        atm.addAccount("1234567890", "1234", 1000);
        CheckingAccount account = (CheckingAccount) atm.accounts.get("1234567890");

        atm.authenticate("1234567890", "1234");
        account.withdraw(500);

        assertEquals(500, account.getBalance());
    }

    @Test
    void testWithdrawInsufficientFunds() {
        ATM atm = new ATM();
        atm.addAccount("1234567890", "1234", 1000);
        CheckingAccount account = (CheckingAccount) atm.accounts.get("1234567890");

        atm.authenticate("1234567890", "1234");
        account.withdraw(1500);

        assertEquals(1000, account.getBalance());
    }

    @Test
    void testDeposit() {
        ATM atm = new ATM();
        atm.addAccount("1234567890", "1234", 1000);
        CheckingAccount account = (CheckingAccount) atm.accounts.get("1234567890");

        atm.authenticate("1234567890", "1234");
        account.deposit(500);

        assertEquals(1500, account.getBalance());
    }

    @Test
    void testChangePIN() {
        ATM atm = new ATM();
        atm.addAccount("1234567890", "1234", 1000);
        CheckingAccount account = (CheckingAccount) atm.accounts.get("1234567890");

        atm.authenticate("1234567890", "1234");
        account.changePIN("1234", "5678");

        assertEquals("5678", account.getPIN());
    }

    @Test
    void testChangePINIncorrectOldPIN() {
        ATM atm = new ATM();
        atm.addAccount("1234567890", "1234", 1000);
        CheckingAccount account = (CheckingAccount) atm.accounts.get("1234567890");

        atm.authenticate("1234567890", "1234");
        account.changePIN("1111", "5678");

        assertEquals("1234", account.getPIN());
    }
}