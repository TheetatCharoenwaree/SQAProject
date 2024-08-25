import pytest
from code import ATM, Transaction, PrintSlip

class MockATM:
    def __init__(self):
        self.balance = 1000
        self.pin_attempts = 0
        self.max_attempts = 3

    def authenticate(self, pin):
        if pin == "1234":
            self.pin_attempts = 0
            return True
        else:
            self.pin_attempts += 1
            if self.pin_attempts >= self.max_attempts:
                return False
            else:
                return False

    def withdraw(self, amount):
        if amount <= self.balance:
            self.balance -= amount
            return True
        else:
            return False

    def deposit(self, amount):
        self.balance += amount
        return True

def test_atm_authenticate_success():
    atm = MockATM()
    assert atm.authenticate("1234") == True

def test_atm_authenticate_failure():
    atm = MockATM()
    assert atm.authenticate("1111") == False

def test_atm_authenticate_block():
    atm = MockATM()
    for _ in range(4):
        atm.authenticate("1111")
    assert atm.authenticate("1111") == False

def test_atm_withdraw_success():
    atm = MockATM()
    assert atm.withdraw(500) == True

def test_atm_withdraw_failure():
    atm = MockATM()
    assert atm.withdraw(1500) == False

def test_atm_deposit_success():
    atm = MockATM()
    assert atm.deposit(500) == True

def test_transaction_withdraw_success(monkeypatch):
    atm = MockATM()
    transaction_details = {'transaction': 'Withdrawal', 'amount': 500, 'date': '2023-12-12', 'time': '10:10:10'}
    monkeypatch.setattr('builtins.input', lambda _: '500')
    transaction = Transaction(atm, 'withdraw')
    transaction.execute()
    assert atm.balance == 500
    assert transaction.transaction_details == transaction_details

def test_transaction_withdraw_failure(monkeypatch):
    atm = MockATM()
    transaction_details = {'transaction': 'Withdrawal', 'amount': 1500, 'date': '2023-12-12', 'time': '10:10:10'}
    monkeypatch.setattr('builtins.input', lambda _: '1500')
    transaction = Transaction(atm, 'withdraw')
    transaction.execute()
    assert atm.balance == 1000
    assert transaction.transaction_details == transaction_details

def test_transaction_deposit_success(monkeypatch):
    atm = MockATM()
    transaction_details = {'transaction': 'Deposit', 'amount': 500, 'date': '2023-12-12', 'time': '10:10:10'}
    monkeypatch.setattr('builtins.input', lambda _: '500')
    transaction = Transaction(atm, 'deposit')
    transaction.execute()
    assert atm.balance == 1500
    assert transaction.transaction_details == transaction_details

def test_transaction_check_balance(monkeypatch):
    atm = MockATM()
    transaction_details = {'transaction': 'Balance Inquiry', 'amount': 0, 'date': '2023-12-12', 'time': '10:10:10'}
    transaction = Transaction(atm, 'check_balance')
    transaction.execute()
    assert transaction.transaction_details == transaction_details

def test_print_slip():
    atm = MockATM()
    transaction_details = {'transaction': 'Withdrawal', 'amount': 500, 'date': '2023-12-12', 'time': '10:10:10'}
    print_slip = PrintSlip(atm, transaction_details)
    print_slip.execute()
