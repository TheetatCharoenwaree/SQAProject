import pytest
from code import ATM, ATMTransactionVisitor

# Fixture to create an ATM object for testing
@pytest.fixture
def atm():
    atm = ATM()
    atm.card_number = "1234567890"
    atm.pin = "1234"
    return atm

# Fixture to create an ATM visitor object for testing
@pytest.fixture
def visitor(atm):
    return ATMTransactionVisitor(atm)

# Test cases for ATM methods

def test_authenticate_user_success(atm):
    assert atm.authenticate_user() == True

def test_authenticate_user_failure(atm):
    atm.pin = "wrong_pin"
    assert atm.authenticate_user() == False

def test_check_balance(atm, capsys):
    atm.check_balance()
    captured = capsys.readouterr()
    assert "Your balance is: $1000.00" in captured.out

def test_withdraw_cash_sufficient_funds(atm):
    atm.withdraw_cash(500)
    assert atm.balance == 500

def test_withdraw_cash_insufficient_funds(atm):
    atm.withdraw_cash(1500)
    assert atm.balance == 1000

def test_deposit_cash(atm):
    atm.deposit_cash(500)
    assert atm.balance == 1500

def test_change_pin_success(atm):
    atm.change_pin()
    assert atm.pin == "1234"

def test_change_pin_incorrect_old_pin(atm):
    atm.change_pin()
    assert atm.pin == "1234"

def test_change_pin_mismatch(atm):
    atm.change_pin()
    assert atm.pin == "1234"

# Test cases for ATM visitor methods

def test_visit_card_reader(visitor, monkeypatch):
    monkeypatch.setattr('builtins.input', lambda _: "1234567890")
    visitor.visit_card_reader(visitor.atm.card_reader)
    assert visitor.atm.card_number == "1234567890"

def test_visit_pin_pad(visitor, monkeypatch):
    monkeypatch.setattr('builtins.input', lambda _: "1234")
    visitor.visit_pin_pad(visitor.atm.pin_pad)
    assert visitor.atm.pin == "1234"

def test_visit_display(visitor, capsys):
    visitor.atm.set_display_message("Test message")
    visitor.visit_display(visitor.atm.display)
    captured = capsys.readouterr()
    assert "Test message" in captured.out

def test_visit_cash_dispenser(visitor, monkeypatch):
    monkeypatch.setattr('builtins.input', lambda _: "500")
    visitor.visit_cash_dispenser(visitor.atm.cash_dispenser)
    assert visitor.atm.balance == 500

def test_visit_deposit_slot(visitor, monkeypatch):
    monkeypatch.setattr('builtins.input', lambda _: "500")
    visitor.visit_deposit_slot(visitor.atm.deposit_slot)
    assert visitor.atm.balance == 1500

def test_visit_receipt_printer(visitor, capsys):
    visitor.atm.set_receipt("Test receipt")
    visitor.visit_receipt_printer(visitor.atm.receipt_printer)
    captured = capsys.readouterr()
    assert "Test receipt" in captured.out