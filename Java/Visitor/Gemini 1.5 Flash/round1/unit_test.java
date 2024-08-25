import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ATMTest {

    @Test
    void authenticate_validCredentials_shouldReturnAccount() {
        ATM atm = new ATM();
        Account account = atm.authenticate("1234567890", "1111");
        assertNotNull(account);
        assertEquals("1234567890", account.getCardNumber());
        assertEquals("1111", account.getPIN());
    }

    @Test
    void authenticate_invalidCardNumber_shouldReturnNull() {
        ATM atm = new ATM();
        Account account = atm.authenticate("1234567891", "1111");
        assertNull(account);
    }

    @Test
    void authenticate_invalidPIN_shouldReturnNull() {
        ATM atm = new ATM();
        Account account = atm.authenticate("1234567890", "1112");
        assertNull(account);
    }

    @Test
    void processOperation_balanceDisplay_shouldDisplayBalance() {
        ATM atm = new ATM();
        Account account = atm.authenticate("1234567890", "1111");
        assertNotNull(account);

        // Capture System.out output
        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        atm.processOperation(new BalanceDisplay(), account);

        // Verify output
        String output = ((ByteArrayOutputStream) System.out.out().stream()).toString();
        assertTrue(output.contains("Your current balance is: 1000.0"));
    }

    @Test
    void processOperation_cashWithdrawal_sufficientFunds_shouldWithdrawAndUpdateBalance() {
        ATM atm = new ATM();
        Account account = atm.authenticate("1234567890", "1111");
        assertNotNull(account);

        // Capture System.out output
        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        atm.processOperation(new CashWithdrawal(200), account);

        // Verify output
        String output = ((ByteArrayOutputStream) System.out.out().stream()).toString();
        assertTrue(output.contains("Cash withdrawn successfully."));

        // Verify balance update
        assertEquals(800, account.getBalance());
    }

    @Test
    void processOperation_cashWithdrawal_insufficientFunds_shouldDisplayAlert() {
        ATM atm = new ATM();
        Account account = atm.authenticate("1234567890", "1111");
        assertNotNull(account);

        // Capture System.out output
        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        atm.processOperation(new CashWithdrawal(1200), account);

        // Verify output
        String output = ((ByteArrayOutputStream) System.out.out().stream()).toString();
        assertTrue(output.contains("Insufficient funds."));

        // Verify balance remains unchanged
        assertEquals(1000, account.getBalance());
    }

    @Test
    void processOperation_deposit_shouldDepositAndUpdateBalance() {
        ATM atm = new ATM();
        Account account = atm.authenticate("1234567890", "1111");
        assertNotNull(account);

        // Capture System.out output
        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        atm.processOperation(new Deposit(100), account);

        // Verify output
        String output = ((ByteArrayOutputStream) System.out.out().stream()).toString();
        assertTrue(output.contains("Deposit successful."));

        // Verify balance update
        assertEquals(1100, account.getBalance());
    }

    @Test
    void processOperation_pinChange_validNewPIN_shouldUpdatePIN() {
        ATM atm = new ATM();
        Account account = atm.authenticate("1234567890", "1111");
        assertNotNull(account);

        // Capture System.out output
        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        atm.processOperation(new PINChange("correct_new_pin"), account);

        // Verify output
        String output = ((ByteArrayOutputStream) System.out.out().stream()).toString();
        assertTrue(output.contains("PIN changed successfully."));

        // Verify PIN update
        assertEquals("correct_new_pin", account.getPIN());
    }

    @Test
    void processOperation_pinChange_invalidNewPIN_shouldDisplayError() {
        ATM atm = new ATM();
        Account account = atm.authenticate("1234567890", "1111");
        assertNotNull(account);

        // Capture System.out output
        System.setOut(new PrintStream(new ByteArrayOutputStream()));

        atm.processOperation(new PINChange("invalid_new_pin"), account);

        // Verify output
        String output = ((ByteArrayOutputStream) System.out.out().stream()).toString();
        assertTrue(output.contains("PIN change failed. Please try again."));

        // Verify PIN remains unchanged
        assertEquals("1111", account.getPIN());
    }

    @Test
    void accept_shouldVisitAllOperations() {
        ATM atm = new ATM();
        ATMVisitor visitor = new TestATMVisitor();

        atm.accept(visitor);

        assertTrue(visitor.balanceDisplayVisited);
        assertTrue(visitor.cashWithdrawalVisited);
        assertTrue(visitor.depositVisited);
        assertTrue(visitor.pinChangeVisited);
    }

    private static class TestATMVisitor implements ATMVisitor {
        boolean balanceDisplayVisited = false;
        boolean cashWithdrawalVisited = false;
        boolean depositVisited = false;
        boolean pinChangeVisited = false;

        @Override
        public void visit(BalanceDisplay operation) {
            balanceDisplayVisited = true;
        }

        @Override
        public void visit(CashWithdrawal operation) {
            cashWithdrawalVisited = true;
        }

        @Override
        public void visit(Deposit operation) {
            depositVisited = true;
        }

        @Override
        public void visit(PINChange operation) {
            pinChangeVisited = true;
        }
    }
}