# test_atm.py
from unittest.mock import patch
import pytest
from code import ATM, Account, BalanceInquiry, CashWithdrawal, Deposit, PINChange, TransactionVisitor

@pytest.fixture
def atm_with_account():
    atm = ATM()
    account = Account("1234567890123456", "1234", 1000)
    atm.add_account(account)
    return atm, account

def test_balance_inquiry(atm_with_account, capsys):
    atm, account = atm_with_account
    visitor = TransactionVisitor(atm, account)
    BalanceInquiry().accept(visitor)
    captured = capsys.readouterr()
    assert captured.out == "Your balance is: $1000.00\n"

def test_cash_withdrawal_sufficient_funds(atm_with_account, capsys):
    atm, account = atm_with_account
    visitor = TransactionVisitor(atm, account)
    CashWithdrawal(500).accept(visitor)
    captured = capsys.readouterr()
    assert captured.out == "Withdrew $500.00. New balance: $500.00\n"

def test_cash_withdrawal_insufficient_funds(atm_with_account, capsys):
    atm, account = atm_with_account
    visitor = TransactionVisitor(atm, account)
    CashWithdrawal(1500).accept(visitor)
    captured = capsys.readouterr()
    assert captured.out == "Insufficient funds.\n"

def test_deposit(atm_with_account, capsys):
    atm, account = atm_with_account
    visitor = TransactionVisitor(atm, account)
    Deposit(500).accept(visitor)
    captured = capsys.readouterr()
    assert captured.out == "Deposited $500.00. New balance: $1500.00\n"

def test_pin_change_success(atm_with_account, capsys):
    atm, account = atm_with_account
    visitor = TransactionVisitor(atm, account)
    PINChange("1234", "5678").accept(visitor)
    captured = capsys.readouterr()
    assert captured.out == "PIN changed successfully.\n"
    assert account.pin == "5678"

def test_pin_change_incorrect_old_pin(atm_with_account, capsys):
    atm, account = atm_with_account
    visitor = TransactionVisitor(atm, account)
    PINChange("wrong_pin", "5678").accept(visitor)
    captured = capsys.readouterr()
    assert captured.out == "Incorrect old PIN.\n"
    assert account.pin == "1234"

def test_verify_pin_success(atm_with_account):
    atm, account = atm_with_account
    assert account.verify_pin("1234") is True

def test_verify_pin_failure(atm_with_account):
    atm, account = atm_with_account
    assert account.verify_pin("wrong_pin") is False

def test_verify_pin_card_retained(atm_with_account, capsys):
    atm, account = atm_with_account
    for _ in range(3):
        account.verify_pin("wrong_pin")
    captured = capsys.readouterr()
    assert "Card retained due to too many incorrect PIN attempts." in captured.out

def test_dispense_cash(atm_with_account, capsys):
    atm, account = atm_with_account
    atm.dispense_cash(500)
    captured = capsys.readouterr()
    assert captured.out == "Dispensing $500.00 cash...\n"