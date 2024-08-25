from abc import ABC, abstractmethod
from typing import List
import datetime

class ATM:
    def __init__(self):
        self.users = {}  # {card_number: {"pin": pin, "balance": balance}}
        self.max_pin_attempts = 3

    def add_user(self, card_number, pin, balance):
        self.users[card_number] = {"pin": pin, "balance": balance}

    def authenticate(self, card_number, pin, attempts=0):
        user = self.users.get(card_number)
        if user and user["pin"] == pin:
            return True
        elif attempts >= self.max_pin_attempts:
            print("Card retained due to too many incorrect PIN attempts.")
            return False
        else:
            print("Incorrect PIN. Please try again.")
            return self.authenticate(card_number, pin, attempts + 1)

    def process_transaction(self, card_number, visitor):
        if self.authenticate(card_number, visitor.get_pin()):
            user = self.users[card_number]
            visitor.execute(user)
            if visitor.should_print_receipt():
                self.print_receipt(user, visitor.get_transaction_details())

    def print_receipt(self, user, transaction_details):
        print("-" * 20)
        print(f"Transaction Details: {transaction_details}")
        print(f"Date: {datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
        print(f"Balance: {user['balance']}")
        print("-" * 20)

class ATMVisitor(ABC):
    @abstractmethod
    def execute(self, user):
        pass

    @abstractmethod
    def get_pin(self):
        pass

    @abstractmethod
    def should_print_receipt(self):
        pass

    @abstractmethod
    def get_transaction_details(self):
        pass

class BalanceInquiryVisitor(ATMVisitor):
    def __init__(self, pin):
        self.pin = pin
        self.receipt_flag = False
        self.transaction_details = None

    def execute(self, user):
        print(f"Your current balance is: {user['balance']}")
        self.transaction_details = f"Balance Inquiry: {user['balance']}"

    def get_pin(self):
        return self.pin

    def should_print_receipt(self):
        return self.receipt_flag

    def get_transaction_details(self):
        return self.transaction_details

class CashWithdrawalVisitor(ATMVisitor):
    def __init__(self, pin, amount):
        self.pin = pin
        self.amount = amount
        self.receipt_flag = True
        self.transaction_details = None

    def execute(self, user):
        if user['balance'] >= self.amount:
            user['balance'] -= self.amount
            print(f"Cash withdrawn: {self.amount}")
            self.transaction_details = f"Cash Withdrawal: {self.amount}"
        else:
            print("Insufficient funds.")

    def get_pin(self):
        return self.pin

    def should_print_receipt(self):
        return self.receipt_flag

    def get_transaction_details(self):
        return self.transaction_details

class DepositVisitor(ATMVisitor):
    def __init__(self, pin, amount):
        self.pin = pin
        self.amount = amount
        self.receipt_flag = True
        self.transaction_details = None

    def execute(self, user):
        user['balance'] += self.amount
        print(f"Amount deposited: {self.amount}")
        self.transaction_details = f"Deposit: {self.amount}"

    def get_pin(self):
        return self.pin

    def should_print_receipt(self):
        return self.receipt_flag

    def get_transaction_details(self):
        return self.transaction_details

class PINChangeVisitor(ATMVisitor):
    def __init__(self, old_pin, new_pin):
        self.old_pin = old_pin
        self.new_pin = new_pin
        self.receipt_flag = True
        self.transaction_details = None

    def execute(self, user):
        if user['pin'] == self.old_pin:
            if self.new_pin == self.confirm_new_pin():
                user['pin'] = self.new_pin
                print("PIN changed successfully.")
                self.transaction_details = "PIN Change"
            else:
                print("New PIN mismatch.")
        else:
            print("Incorrect old PIN.")

    def get_pin(self):
        return self.old_pin

    def should_print_receipt(self):
        return self.receipt_flag

    def get_transaction_details(self):
        return self.transaction_details

    def confirm_new_pin(self):
        return input("Confirm new PIN: ")