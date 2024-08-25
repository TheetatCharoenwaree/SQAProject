import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ATMTest {

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new Account("1234567890", "1234", BigDecimal.valueOf(1000.00));
    }

    // Account Class Tests
    @Test
    void testVerifyPIN_Success() {
        assertTrue(testAccount.verifyPIN("1234"));
        assertEquals(0, testAccount.getAttempts()); // Attempts reset on success
    }

    @Test
    void testVerifyPIN_Failure() {
        assertFalse(testAccount.verifyPIN("0000"));
        assertEquals(1, testAccount.getAttempts());
    }

    @Test
    void testVerifyPIN_CardRetained() {
        testAccount.setAttempts(3); // Simulate exceeding attempts
        assertFalse(testAccount.verifyPIN("1234")); // Even with correct PIN
        assertTrue(testAccount.isCardRetained());
    }

    @Test
    void testGetBalance() {
        assertEquals(BigDecimal.valueOf(1000.00), testAccount.getBalance());
    }

    @Test
    void testWithdraw_Success() {
        assertTrue(testAccount.withdraw(200.00));
        assertEquals(BigDecimal.valueOf(800.00), testAccount.getBalance());
    }

    @Test
    void testWithdraw_InsufficientFunds() {
        assertFalse(testAccount.withdraw(1500.00));
        assertEquals(BigDecimal.valueOf(1000.00), testAccount.getBalance()); // Balance unchanged
    }

    @Test
    void testDeposit() {
        testAccount.deposit(500.00);
        assertEquals(BigDecimal.valueOf(1500.00), testAccount.getBalance());
    }

    @Test
    void testUpdatePIN() {
        testAccount.updatePIN("5678");
        assertTrue(testAccount.verifyPIN("5678")); // Verify with the new PIN
    }

    // ATM Interaction Tests (Using System.in redirection for simulation)
    @Test
    void testATMActions() {
        String input = "1234\n" +              // Correct PIN
                       "1\n" +               // Select Check Balance
                       "2\n" +               // Select Withdraw Cash
                       "200.00\n" +          // Withdraw amount
                       "3\n" +               // Select Deposit Funds
                       "300.00\n" +          // Deposit amount
                       "4\n" +               // Select Change PIN
                       "1234\n" +          // Old PIN
                       "5678\n" +          // New PIN
                       "5678\n";             // Confirm new PIN

        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Run the ATM (replace with your main method call if needed)
        ATM.main(new String[]{});

        // Add assertions based on expected output or state changes
        // (Example: Check if balance is updated correctly)
    }
}