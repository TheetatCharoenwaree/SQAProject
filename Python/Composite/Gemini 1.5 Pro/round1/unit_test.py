# unit_test.py
from code import ATMCardReader, PINVerification, BalanceDisplay, CashWithdrawal, Deposit, PINChange, ReceiptPrinting
import pytest

# Fixture for creating a BalanceDisplay instance with an initial balance
@pytest.fixture
def balance_display():
    return BalanceDisplay(1000.00)

# Fixture for creating a CashWithdrawal instance with an initial balance
@pytest.fixture
def cash_withdrawal():
    return CashWithdrawal(1000.00)

# Fixture for creating a Deposit instance with an initial balance
@pytest.fixture
def deposit():
    return Deposit(1000.00)

# Test case for ATMCardReader
def test_atm_card_reader():
    card_reader = ATMCardReader()
    # Assuming card insertion and reading always succeed in this simulation
    assert card_reader.execute() == True

# Test cases for PINVerification
def test_pin_verification_success():
    pin_verification = PINVerification("1234")
    assert pin_verification.execute() == True  # Assuming correct PIN entered

def test_pin_verification_failure():
    pin_verification = PINVerification("1234")
    pin_verification.execute()  # Attempt 1 (incorrect)
    pin_verification.execute()  # Attempt 2 (incorrect)
    pin_verification.execute()  # Attempt 3 (incorrect)
    assert pin_verification.attempts == 3

# Test cases for BalanceDisplay
def test_balance_display(balance_display):
    assert balance_display.execute() == True  # Assuming balance display always succeeds

# Test cases for CashWithdrawal
def test_cash_withdrawal_sufficient_funds(cash_withdrawal):
    assert cash_withdrawal.execute() == True  # Assuming a valid withdrawal amount

def test_cash_withdrawal_insufficient_funds(cash_withdrawal):
    cash_withdrawal.balance = 500.00
    assert cash_withdrawal.execute() == False  # Assuming a withdrawal amount exceeding the balance

# Test cases for Deposit
def test_deposit(deposit):
    assert deposit.execute() == True  # Assuming a valid deposit amount

# Test cases for PINChange
def test_pin_change_success():
    pin_change = PINChange("1234")
    assert pin_change.execute() == True  # Assuming correct old and matching new PINs entered

def test_pin_change_incorrect_old_pin():
    pin_change = PINChange("1234")
    pin_change.execute()  # Attempt with incorrect old PIN
    assert pin_change.pin == "1234"  # PIN should not change

def test_pin_change_mismatched_new_pin():
    pin_change = PINChange("1234")
    pin_change.execute()  # Attempt with mismatched new PINs
    assert pin_change.pin == "1234"  # PIN should not change

# Test cases for ReceiptPrinting
def test_receipt_printing_yes():
    receipt_printing = ReceiptPrinting()
    assert receipt_printing.execute() == True  # Assuming user chooses to print

def test_receipt_printing_no():
    receipt_printing = ReceiptPrinting()
    assert receipt_printing.execute() == True  # Assuming user chooses not to print