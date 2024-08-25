import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;

class ATMTest {

    @Test
    void testAuthenticationSuccess() {
        String input = "1234567890\n1234\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ATM atm = new ATM();
        atm.start(); // Start will attempt authentication

        // If authentication is successful, currentAccount will be set
        assertNotNull(atm.currentAccount);
        System.setIn(System.in); // Resetting System.in
    }

    @Test
    void testAuthenticationFailure() {
        String input = "1234567890\nwrongpin\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ATM atm = new ATM();
        atm.start();

        // Authentication fails, currentAccount should remain null
        assertNull(atm.currentAccount);
        System.setIn(System.in);
    }

    @Test
    void testCheckBalance() {
        // Simulate user input for authentication and balance check
        String input = "1234567890\n1234\n1\n5\n"; // Authenticate, Check Balance, Exit
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ATM atm = new ATM();
        atm.start();

        // Since we are not asserting output, ensure code runs without exceptions
        System.setIn(System.in);
    }

    @Test
    void testWithdrawSuccess() {
        String input = "1234567890\n1234\n2\n1\n500\n5\n"; 
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ATM atm = new ATM();
        atm.start(); 

        assertEquals(500.00, atm.currentAccount.getBalance()); 
        System.setIn(System.in); 
    }


    @Test
    void testWithdrawInsufficientFunds() {
        String input = "1234567890\n1234\n2\n1\n1500\n5\n"; // Attempt to withdraw more than balance
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ATM atm = new ATM();
        atm.start();

        System.setIn(System.in);
    }


    @Test
    void testDeposit() {
        String input = "1234567890\n1234\n3\n2\n500\n5\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ATM atm = new ATM();
        atm.start();

        assertEquals(1500.00, atm.currentAccount.getBalance()); 
        System.setIn(System.in); 
    }

  
    @Test
    void testChangePinSuccess() {
        String input = "1234567890\n1234\n4\n3\n1234\n5678\n5678\n5\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ATM atm = new ATM();
        atm.start(); 

        assertTrue(atm.currentAccount.validatePin("5678")); 
        System.setIn(System.in); 
    }

    @Test
    void testChangePinMismatch() {
        String input = "1234567890\n1234\n4\n3\n1234\n5678\n1234\n5\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ATM atm = new ATM();
        atm.start();

        System.setIn(System.in);
    }
}