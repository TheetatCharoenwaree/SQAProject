import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

public class ATMTest {

    private ATM atm;

    @BeforeEach
    void setUp() {
        atm = new ATM();
    }

    @Test
    void testAuthenticateUser_Success() {
        assertTrue(atm.authenticateUser("1234567890", "1234"));
    }

    @Test
    void testAuthenticateUser_Failure() {
        assertFalse(atm.authenticateUser("1234567890", "0000"));
    }

    @Test
    void testAuthenticateUser_CardNotFound() {
        assertFalse(atm.authenticateUser("9999999999", "1111"));
    }

    @Test
    void testBalanceInquiry() {
        String input = "1234\n1\n5\n"; // PIN, Balance Inquiry, Exit
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        atm.start();

        System.setIn(System.in); // Reset System.in
    }

    @Test
    void testCashWithdrawal_Success() {
        String input = "1234\n2\n500\n5\n"; // PIN, Withdrawal, Amount, Exit
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        atm.start();

        System.setIn(System.in);
        assertEquals(500.0, atm.accounts.get("1234567890").getBalance());
    }

    @Test
    void testCashWithdrawal_InsufficientFunds() {
        String input = "1234\n2\n1500\n5\n"; // PIN, Withdrawal, Amount, Exit
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        atm.start();

        System.setIn(System.in);
        assertEquals(1000.0, atm.accounts.get("1234567890").getBalance());
    }

    @Test
    void testDeposit() {
        String input = "1234\n3\n200\n5\n"; // PIN, Deposit, Amount, Exit
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        atm.start();

        System.setIn(System.in);
        assertEquals(1200.0, atm.accounts.get("1234567890").getBalance());
    }

    @Test
    void testPinChange_Success() {
        String input = "1234\n4\n1234\n5678\n5678\n5\n"; // PIN, Change PIN, Old PIN, New PIN, Confirm, Exit
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        atm.start();

        System.setIn(System.in);
        assertEquals("5678", atm.accounts.get("1234567890").getPin());
    }

    @Test
    void testPinChange_IncorrectOldPin() {
        String input = "1234\n4\n0000\n5678\n5678\n5\n"; // Incorrect old PIN
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        atm.start();

        System.setIn(System.in);
        assertEquals("1234", atm.accounts.get("1234567890").getPin());
    }

    @Test
    void testPinChange_MismatchedNewPins() {
        String input = "1234\n4\n1234\n5678\n1234\n5\n"; // Mismatched new PINs
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        atm.start();

        System.setIn(System.in);
        assertEquals("1234", atm.accounts.get("1234567890").getPin());
    }
}