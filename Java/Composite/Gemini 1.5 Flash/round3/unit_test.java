import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ATMSystemTest {

    @Test
    void testAuthenticateUser_Success() {
        ATMContext context = new ATMContext();
        assertTrue(context.authenticateUser("1234567890", "1234"));
    }

    @Test
    void testAuthenticateUser_Failure() {
        ATMContext context = new ATMContext();
        assertFalse(context.authenticateUser("1234567890", "1111"));
    }

    @Test
    void testGetBalance() {
        ATMContext context = new ATMContext();
        assertEquals(1000.0, context.getBalance("1234567890"));
    }

    @Test
    void testWithdraw_Success() {
        ATMContext context = new ATMContext();
        context.withdraw("1234567890", 500.0);
        assertEquals(500.0, context.getBalance("1234567890"));
        assertEquals("Withdrawal", context.getTransactionType());
        assertEquals(500.0, context.getTransactionAmount());
    }

    @Test
    void testWithdraw_InsufficientFunds() {
        ATMContext context = new ATMContext();
        context.withdraw("1234567890", 1500.0);
        assertEquals(1000.0, context.getBalance("1234567890"));
    }

    @Test
    void testDeposit() {
        ATMContext context = new ATMContext();
        context.deposit("1234567890", 200.0);
        assertEquals(1200.0, context.getBalance("1234567890"));
        assertEquals("Deposit", context.getTransactionType());
        assertEquals(200.0, context.getTransactionAmount());
    }

    @Test
    void testChangePIN_Success() {
        ATMContext context = new ATMContext();
        context.changePIN("1234567890", "5678");
        assertTrue(context.authenticateUser("1234567890", "5678"));
    }

    @Test
    void testChangePIN_Failure() {
        ATMContext context = new ATMContext();
        context.changePIN("1234567890", "5678");
        assertFalse(context.authenticateUser("1234567890", "1234"));
    }

    @Test
    void testIncrementFailedAttempts() {
        ATMContext context = new ATMContext();
        context.incrementFailedAttempts();
        assertEquals(1, context.getFailedAttempts());
    }

    @Test
    void testResetFailedAttempts() {
        ATMContext context = new ATMContext();
        context.incrementFailedAttempts();
        context.resetFailedAttempts();
        assertEquals(0, context.getFailedAttempts());
    }
}