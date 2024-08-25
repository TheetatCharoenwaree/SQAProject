import pytest
import datetime
from code import ATM, BalanceInquiryVisitor, CashWithdrawalVisitor, DepositVisitor, PINChangeVisitor

@pytest.fixture
def atm():
    atm = ATM()
    atm.add_user("1234567890", "1111", 1000)
    return atm

def test_authenticate_success(atm):
    assert atm.authenticate("1234567890", "1111")

def test_authenticate_failure(atm):
    assert not atm.authenticate("1234567890", "1234")

def test_authenticate_max_attempts(atm):
    atm.max_pin_attempts = 2
    assert not atm.authenticate("1234567890", "1234")

def test_balance_inquiry(atm):
    visitor = BalanceInquiryVisitor("1111")
    atm.process_transaction("1234567890", visitor)
    assert visitor.get_transaction_details() == "Balance Inquiry: 1000"

def test_cash_withdrawal_success(atm):
    visitor = CashWithdrawalVisitor("1111", 500)
    atm.process_transaction("1234567890", visitor)
    assert visitor.get_transaction_details() == "Cash Withdrawal: 500"
    assert atm.users["1234567890"]["balance"] == 500

def test_cash_withdrawal_failure(atm):
    visitor = CashWithdrawalVisitor("1111", 1500)
    atm.process_transaction("1234567890", visitor)
    assert visitor.get_transaction_details() is None
    assert atm.users["1234567890"]["balance"] == 1000

def test_deposit(atm):
    visitor = DepositVisitor("1111", 200)
    atm.process_transaction("1234567890", visitor)
    assert visitor.get_transaction_details() == "Deposit: 200"
    assert atm.users["1234567890"]["balance"] == 1200

def test_pin_change_success(atm, monkeypatch):
    monkeypatch.setattr('builtins.input', lambda _: "2222")
    visitor = PINChangeVisitor("1111", "2222")
    atm.process_transaction("1234567890", visitor)
    assert visitor.get_transaction_details() == "PIN Change"
    assert atm.users["1234567890"]["pin"] == "2222"

def test_pin_change_failure(atm, monkeypatch):
    monkeypatch.setattr('builtins.input', lambda _: "3333")
    visitor = PINChangeVisitor("1111", "2222")
    atm.process_transaction("1234567890", visitor)
    assert visitor.get_transaction_details() is None
    assert atm.users["1234567890"]["pin"] == "1111"

def test_print_receipt(atm):
    visitor = BalanceInquiryVisitor("1111")
    visitor.receipt_flag = True
    atm.process_transaction("1234567890", visitor)
    # Asserting receipt printing is tricky as it involves printing to the console
    # You can use a mocking library like 'mock' to capture the printed output.