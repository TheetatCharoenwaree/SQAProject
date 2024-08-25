import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ATMTest {
    private ATM atm;
    private Account account;

    @BeforeEach
    void setUp() {
        atm = new ATM();
        account = new Account("1234567890123456", "1234", 1000.0);
    }

    // Test Authentication
    @Test
    void testAuthenticate_Success() {
        String input = "1234567890123456\n1234\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        atm.start();
        System.setIn(System.in); // Reset to standard input
    }

    @Test
    void testAuthenticate_InvalidCardNumber() {
        String input = "invalid\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        atm.start();
        System.setIn(System.in); // Reset to standard input
    }

    @Test
    void testAuthenticate_IncorrectPin_MaxAttempts() {
        String input = "1234567890123456\nwrong\nwrong\nwrong\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        atm.start();
        System.setIn(System.in); // Reset to standard input
    }

    // Test Balance Inquiry
    @Test
    void testBalanceInquiry() {
        String input = "1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ATMVisitor visitor = new ATMVisitorImpl(account, new Scanner(System.in));
        visitor.visit(new BalanceInquiry());
        System.setIn(System.in); // Reset to standard input
    }

    // Test Cash Withdrawal
    @Test
    void testCashWithdrawal_Success() {
        String input = "200\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ATMVisitor visitor = new ATMVisitorImpl(account, new Scanner(System.in));
        visitor.visit(new CashWithdrawal());
        assertEquals(800.0, account.getBalance());
        System.setIn(System.in); // Reset to standard input
    }

    @Test
    void testCashWithdrawal_InsufficientFunds() {
        String input = "1500\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ATMVisitor visitor = new ATMVisitorImpl(account, new Scanner(System.in));
        visitor.visit(new CashWithdrawal());
        assertEquals(1000.0, account.getBalance());
        System.setIn(System.in); // Reset to standard input
    }

    // Test Cash Deposit
    @Test
    void testCashDeposit() {
        String input = "500\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ATMVisitor visitor = new ATMVisitorImpl(account, new Scanner(System.in));
        visitor.visit(new CashDeposit());
        assertEquals(1500.0, account.getBalance());
        System.setIn(System.in); // Reset to standard input
    }

    // Test PIN Change
    @Test
    void testPinChange_Success() {
        String input = "1234\n5678\n5678\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ATMVisitor visitor = new ATMVisitorImpl(account, new Scanner(System.in));
        visitor.visit(new PinChange());
        assertTrue(account.validatePin("5678"));
        System.setIn(System.in); // Reset to standard input
    }

    @Test
    void testPinChange_IncorrectOldPin() {
        String input = "wrong\n5678\n5678\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ATMVisitor visitor = new ATMVisitorImpl(account, new Scanner(System.in));
        visitor.visit(new PinChange());
        assertFalse(account.validatePin("5678"));
        System.setIn(System.in); // Reset to standard input
    }

    @Test
    void testPinChange_NewPinsMismatch() {
        String input = "1234\n5678\n1234\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ATMVisitor visitor = new ATMVisitorImpl(account, new Scanner(System.in));
        visitor.visit(new PinChange());
        assertFalse(account.validatePin("5678"));
        System.setIn(System.in); // Reset to standard input
    }
}