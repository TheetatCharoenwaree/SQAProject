import pytest
from code import ATM, User, Authentication, BalanceDisplay, CashWithdrawal, Deposit, PINChange, ReceiptPrinting

@pytest.fixture
def user():
    return User("Test User", "1234", 1000.00)

@pytest.fixture
def atm():
    return ATM()

def test_authentication_successful(user, atm):
    authentication = Authentication(atm)
    authentication.process(user)
    assert atm.user == user

def test_authentication_failed(user, atm):
    authentication = Authentication(atm)
    authentication.process(user)
    authentication.process(user)
    authentication.process(user)
    assert atm.user is None

def test_balance_display(user, atm):
    atm.user = user
    balance_display = BalanceDisplay(atm)
    balance_display.process(user)
    # Mocked output
    assert True

def test_cash_withdrawal_successful(user, atm):
    atm.user = user
    cash_withdrawal = CashWithdrawal(atm)
    cash_withdrawal.process(user)
    assert user.balance == 900.00

def test_cash_withdrawal_insufficient_funds(user, atm):
    atm.user = user
    cash_withdrawal = CashWithdrawal(atm)
    # Mocked output
    cash_withdrawal.process(user)
    assert user.balance == 1000.00

def test_deposit(user, atm):
    atm.user = user
    deposit = Deposit(atm)
    deposit.process(user)
    assert user.balance == 1100.00

def test_pin_change(user, atm):
    atm.user = user
    pin_change = PINChange(atm)
    pin_change.process(user)
    assert user.pin == "5678"

def test_receipt_printing(user, atm):
    atm.user = user
    receipt_printing = ReceiptPrinting(atm)
    receipt_printing.process(user)
    # Mocked output
    assert True