import pytest
from code import ATMComposite, Authentication, BalanceInquiry, Withdrawal, Deposit, PINChange, PrintReceipt, User

class MockUser:
    def __init__(self, card_number, pin, balance):
        self.card_number = card_number
        self.pin = pin
        self.balance = balance

@pytest.fixture
def atm():
    atm = ATMComposite()
    atm.add(Authentication())
    atm.add(BalanceInquiry())
    atm.add(Withdrawal())
    atm.add(Deposit())
    atm.add(PINChange())
    atm.add(PrintReceipt())
    return atm

def test_authentication_success(atm):
    user = MockUser("1234567890", "1234", 1000)
    assert atm.components[0].process(user) == True

def test_authentication_failure(atm):
    user = MockUser("1234567890", "1111", 1000)
    assert atm.components[0].process(user) == False

def test_authentication_max_attempts(atm):
    user = MockUser("1234567890", "1111", 1000)
    for _ in range(atm.components[0].max_attempts):
        atm.components[0].process(user)
    with pytest.raises(ValueError) as excinfo:
        atm.components[0].process(user)
    assert "Card seized due to exceeding attempts." in str(excinfo.value)

def test_balance_inquiry(atm):
    user = MockUser("1234567890", "1234", 1000)
    atm.components[1].process(user)

def test_withdrawal_sufficient_funds(atm, monkeypatch):
    user = MockUser("1234567890", "1234", 1000)
    monkeypatch.setattr('builtins.input', lambda _: "500")
    atm.components[2].process(user)
    assert user.balance == 500

def test_withdrawal_insufficient_funds(atm, monkeypatch):
    user = MockUser("1234567890", "1234", 1000)
    monkeypatch.setattr('builtins.input', lambda _: "1500")
    atm.components[2].process(user)
    assert user.balance == 1000

def test_deposit(atm, monkeypatch):
    user = MockUser("1234567890", "1234", 1000)
    monkeypatch.setattr('builtins.input', lambda _: "200")
    atm.components[3].process(user)
    assert user.balance == 1200

def test_pin_change_success(atm, monkeypatch):
    user = MockUser("1234567890", "1234", 1000)
    monkeypatch.setattr('builtins.input', lambda _: "1234")
    monkeypatch.setattr('builtins.input', lambda _: "5678")
    monkeypatch.setattr('builtins.input', lambda _: "5678")
    atm.components[4].process(user)
    assert user.pin == "5678"

def test_pin_change_incorrect_old_pin(atm, monkeypatch):
    user = MockUser("1234567890", "1234", 1000)
    monkeypatch.setattr('builtins.input', lambda _: "1111")
    atm.components[4].process(user)
    assert user.pin == "1234"

def test_pin_change_mismatched_new_pins(atm, monkeypatch):
    user = MockUser("1234567890", "1234", 1000)
    monkeypatch.setattr('builtins.input', lambda _: "1234")
    monkeypatch.setattr('builtins.input', lambda _: "5678")
    monkeypatch.setattr('builtins.input', lambda _: "9012")
    atm.components[4].process(user)
    assert user.pin == "1234"

def test_print_receipt(atm):
    user = MockUser("1234567890", "1234", 1000)
    atm.components[5].process(user)

