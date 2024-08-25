import pytest
from code import ATM, Account, BalanceDisplayVisitor, CashWithdrawalVisitor, DepositVisitor, PINChangeVisitor, ReceiptPrintingVisitor

def test_add_account():
    atm = ATM()
    account = Account("1234567890", "1234", 1000)
    atm.add_account(account)
    assert atm.accounts["1234567890"] == account

def test_authenticate_success():
    atm = ATM()
    account = Account("1234567890", "1234", 1000)
    atm.add_account(account)
    authenticated_account = atm.authenticate("1234567890", "1234")
    assert authenticated_account == account

def test_authenticate_failure():
    atm = ATM()
    account = Account("1234567890", "1234", 1000)
    atm.add_account(account)
    authenticated_account = atm.authenticate("1234567890", "1111")
    assert authenticated_account is None

def test_balance_display():
    atm = ATM()
    account = Account("1234567890", "1234", 1000)
    atm.add_account(account)
    atm.process_transaction(account, BalanceDisplayVisitor())
    assert atm.get_transaction_history() == ["Balance: 1000.0"]

def test_cash_withdrawal_success():
    atm = ATM()
    account = Account("1234567890", "1234", 1000)
    atm.add_account(account)
    atm.process_transaction(account, CashWithdrawalVisitor(500))
    assert atm.get_transaction_history() == ["Withdrawal: 500.0"]
    assert account.balance == 500.0

def test_cash_withdrawal_failure():
    atm = ATM()
    account = Account("1234567890", "1234", 1000)
    atm.add_account(account)
    atm.process_transaction(account, CashWithdrawalVisitor(1500))
    assert atm.get_transaction_history() == ["Insufficient funds for withdrawal: 1500.0"]
    assert account.balance == 1000.0

def test_deposit():
    atm = ATM()
    account = Account("1234567890", "1234", 1000)
    atm.add_account(account)
    atm.process_transaction(account, DepositVisitor(200))
    assert atm.get_transaction_history() == ["Deposit: 200.0"]
    assert account.balance == 1200.0

def test_pin_change():
    atm = ATM()
    account = Account("1234567890", "1234", 1000)
    atm.add_account(account)
    atm.process_transaction(account, PINChangeVisitor("5678"))
    assert atm.get_transaction_history() == ["PIN changed successfully."]
    assert account.pin == "5678"

def test_receipt_printing():
    atm = ATM()
    account = Account("1234567890", "1234", 1000)
    atm.add_account(account)
    atm.process_transaction(account, BalanceDisplayVisitor())
    atm.process_transaction(account, CashWithdrawalVisitor(500))
    atm.process_transaction(account, DepositVisitor(200))
    atm.process_transaction(account, ReceiptPrintingVisitor())
    assert atm.get_transaction_history()[-1] == "Thank you!"