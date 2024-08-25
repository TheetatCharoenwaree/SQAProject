# test_atm.py
import pytest
from code import ATM, Account, AuthenticationVisitor, BalanceInquiryVisitor, WithdrawalVisitor, DepositVisitor, PinChangeVisitor
class MockCardReader:
    def __init__(self, card_number):
        self.card_number = card_number

    def read_card(self):
        return self.card_number


class MockPinPad:
    def __init__(self, pin):
        self.pin = pin

    def get_pin(self):
        return self.pin


class MockDisplay:
    def __init__(self):
        self.messages = []

    def show_message(self, message):
        self.messages.append(message)

    def show_balance(self, balance):
        self.messages.append(f"Your current balance is: ${balance:.2f}")


class MockCashDispenser:
    def __init__(self):
        self.dispensed_amount = 0

    def dispense_cash(self, amount):
        self.dispensed_amount += amount


class MockDepositSlot:
    def __init__(self, deposit_amount=0):
        self.deposit_amount = deposit_amount

    def receive_deposit(self):
        return self.deposit_amount


class MockReceiptPrinter:
    def __init__(self):
        self.printed_details = []

    def print_receipt(self, transaction_details):
        self.printed_details.append(transaction_details)

@pytest.fixture
def atm_with_account():
    atm = ATM()
    accounts = {
        "1234567890123456": Account("1234567890123456", "1234", 1000.00),
    }
    atm.card_reader = MockCardReader("1234567890123456")
    atm.pin_pad = MockPinPad("1234")
    atm.display = MockDisplay()
    atm.cash_dispenser = MockCashDispenser()
    atm.deposit_slot = MockDepositSlot()
    atm.receipt_printer = MockReceiptPrinter()
    auth_visitor = AuthenticationVisitor(atm, accounts)
    atm.accept(auth_visitor)
    return atm, accounts

def test_authentication_success(atm_with_account):
    atm, accounts = atm_with_account
    assert atm.user_account is not None
    assert atm.user_account.card_number == "1234567890123456"

def test_authentication_incorrect_pin(atm_with_account):
    atm, accounts = atm_with_account
    atm.pin_pad = MockPinPad("wrong_pin")
    auth_visitor = AuthenticationVisitor(atm, accounts)
    atm.accept(auth_visitor)
    assert atm.user_account is None

def test_balance_inquiry(atm_with_account):
    atm, accounts = atm_with_account
    balance_visitor = BalanceInquiryVisitor(atm)
    atm.accept(balance_visitor)
    assert atm.display.messages[-1] == "Your current balance is: $1000.00"

def test_withdrawal_success(atm_with_account):
    atm, accounts = atm_with_account
    withdraw_visitor = WithdrawalVisitor(atm)
    atm.display = MockDisplay() # Reset display messages
    atm.accept(withdraw_visitor)
    assert atm.cash_dispenser.dispensed_amount == 200.00
    assert atm.user_account.balance == 800.00
    assert atm.display.messages[-1] == "Your current balance is: $800.00"
    assert atm.receipt_printer.printed_details[-1]["Transaction"] == "Withdrawal"

def test_withdrawal_insufficient_funds(atm_with_account):
    atm, accounts = atm_with_account
    withdraw_visitor = WithdrawalVisitor(atm)
    atm.display = MockDisplay() # Reset display messages
    atm.accept(withdraw_visitor)
    assert atm.cash_dispenser.dispensed_amount == 0
    assert atm.user_account.balance == 1000.00
    assert "Insufficient funds" in atm.display.messages

def test_deposit_success(atm_with_account):
    atm, accounts = atm_with_account
    atm.deposit_slot = MockDepositSlot(300.00)  # Simulate a $300 deposit
    deposit_visitor = DepositVisitor(atm)
    atm.accept(deposit_visitor)
    assert atm.user_account.balance == 1300.00
    assert atm.display.messages[-1] == "Your current balance is: $1300.00"
    assert atm.receipt_printer.printed_details[-1]["Transaction"] == "Deposit"

def test_pin_change_success(atm_with_account):
    atm, accounts = atm_with_account
    atm.pin_pad = MockPinPad("1234") # For old PIN
    pin_change_visitor = PinChangeVisitor(atm)
    atm.accept(pin_change_visitor)
    assert atm.user_account.pin == "5678"

def test_pin_change_mismatch(atm_with_account):
    atm, accounts = atm_with_account
    atm.pin_pad = MockPinPad("1234") # For old PIN
    pin_change_visitor = PinChangeVisitor(atm)
    atm.accept(pin_change_visitor)
    assert atm.user_account.pin == "1234" # PIN unchanged

# Add more tests for other scenarios and edge cases