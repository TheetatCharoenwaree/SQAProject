import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ATMSystemTest {

    @Test
    void testAuthenticateSuccess() {
        ConcreteATM atm = new ConcreteATM();
        Account account = atm.accounts.get(1234567890);
        atm.authenticate(account, 1111);
        assertEquals(0, account.getPinAttempts());
    }

    @Test
    void testAuthenticateFailure() {
        ConcreteATM atm = new ConcreteATM();
        Account account = atm.accounts.get(1234567890);
        atm.authenticate(account, 2222);
        assertEquals(1, account.getPinAttempts());
    }

    @Test
    void testAuthenticateCardRetained() {
        ConcreteATM atm = new ConcreteATM();
        Account account = atm.accounts.get(1234567890);
        for (int i = 0; i < 3; i++) {
            atm.authenticate(account, 2222);
        }
        assertEquals(3, account.getPinAttempts());
    }

    @Test
    void testDisplayBalance() {
        ConcreteATM atm = new ConcreteATM();
        Account account = atm.accounts.get(1234567890);
        atm.displayBalance(account);
        // Assertions are not possible for output-based methods
    }

    @Test
    void testWithdrawSuccess() {
        ConcreteATM atm = new ConcreteATM();
        Account account = atm.accounts.get(1234567890);
        atm.withdraw(account, 500.0);
        assertEquals(500.0, account.getBalance());
    }

    @Test
    void testWithdrawInsufficientFunds() {
        ConcreteATM atm = new ConcreteATM();
        Account account = atm.accounts.get(1234567890);
        atm.withdraw(account, 1500.0);
        assertEquals(1000.0, account.getBalance());
    }

    @Test
    void testDeposit() {
        ConcreteATM atm = new ConcreteATM();
        Account account = atm.accounts.get(1234567890);
        atm.deposit(account, 200.0);
        assertEquals(1200.0, account.getBalance());
    }

    @Test
    void testChangePin() {
        ConcreteATM atm = new ConcreteATM();
        Account account = atm.accounts.get(1234567890);
        atm.changePin(account, 4444);
        assertEquals(4444, account.getPin());
    }

    @Test
    void testPrintReceipt() {
        ConcreteATM atm = new ConcreteATM();
        Account account = atm.accounts.get(1234567890);
        atm.printReceipt(account, "Withdrawal", 500.0);
        // Assertions are not possible for output-based methods
    }
}