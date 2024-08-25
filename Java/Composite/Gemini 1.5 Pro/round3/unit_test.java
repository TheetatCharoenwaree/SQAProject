import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
// ... other imports 

class ATMSystemTest {

    private Account testAccount;
    private UserAuthentication authentication;
    private BalanceInquiry balanceInquiry;
    private CashWithdrawal cashWithdrawal;
    private Deposit deposit;
    private PINChange pinChange; 

    @BeforeEach
    void setUp() {
        testAccount = new Account("testCard", "1234", 1000.00);
        authentication = new UserAuthentication(testAccount);
        balanceInquiry = new BalanceInquiry(testAccount);
        cashWithdrawal = new CashWithdrawal(testAccount);
        deposit = new Deposit(testAccount);
        pinChange = new PINChange(testAccount);
    }

    // UserAuthentication Tests
    @Test
    void testAuthenticationSuccess() {
        // Simulate user input - correct PIN
        System.setIn(new ByteArrayInputStream("1234\n".getBytes())); 
        authentication.execute();
        // Assertions to verify successful authentication
    }

    @Test
    void testAuthenticationFailure() {
        // Simulate user input - incorrect PIN
        System.setIn(new ByteArrayInputStream("0000\n".getBytes())); 
        authentication.execute();
        // Assertions to verify failure (e.g., attemptsLeft reduced)
    }

    @Test
    void testAuthenticationCardRetained() {
        // Simulate multiple incorrect PIN attempts
        System.setIn(new ByteArrayInputStream("0000\n0000\n0000\n".getBytes()));
        // Call authentication.execute() multiple times (3 or more)
        for (int i = 0; i < 3; i++) {
            authentication.execute();
        }
        // This test should probably not directly call System.exit(0)
        // Instead, check for a state change in your authentication 
        // object that indicates the card is retained. 
    }

    // BalanceInquiry Tests 
    @Test
    void testBalanceInquiry() {
        balanceInquiry.execute();
        // Use System.setOut() and ByteArrayOutputStream to capture output 
        // and verify the displayed balance is correct
    }

    // CashWithdrawal Tests
    @Test
    void testSuccessfulWithdrawal() {
        System.setIn(new ByteArrayInputStream("500.00\n".getBytes()));
        cashWithdrawal.execute();
        // Assert that balance is updated correctly
        assertEquals(500.00, testAccount.getBalance(), 0.01);
    }

    @Test
    void testInsufficientFundsWithdrawal() {
        System.setIn(new ByteArrayInputStream("1500.00\n".getBytes()));
        cashWithdrawal.execute();
        // Assert that the balance remains unchanged 
        assertEquals(1000.00, testAccount.getBalance(), 0.01);
    }

    // Deposit Tests
    @Test
    void testDeposit() {
        System.setIn(new ByteArrayInputStream("250.00\n".getBytes()));
        deposit.execute();
        assertEquals(1250.00, testAccount.getBalance(), 0.01); 
    }

    // PINChange Tests 
    @Test
    void testSuccessfulPINChange() {
        // Simulate user input 
        System.setIn(new ByteArrayInputStream("1234\n5678\n5678\n".getBytes()));
        pinChange.execute();
        // Verify PIN has changed - you might need a way to get the PIN
        assertTrue(testAccount.verifyPin("5678")); 
    }

    @Test
    void testPINChangeMismatch() {
        System.setIn(new ByteArrayInputStream("1234\n5678\n1234\n".getBytes())); 
        pinChange.execute();
        // Verify PIN has not changed
        assertFalse(testAccount.verifyPin("5678"));
    }

    @Test
    void testPINChangeIncorrectOldPIN() {
        System.setIn(new ByteArrayInputStream("0000\n5678\n5678\n".getBytes())); 
        pinChange.execute();
        assertFalse(testAccount.verifyPin("5678"));
    }
}