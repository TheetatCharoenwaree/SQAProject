# atm.py
from abc import ABC, abstractmethod

class ATMPartVisitor(ABC):
    """Visitor interface for ATM operations."""

    @abstractmethod
    def visit_card_reader(self, card_reader):
        pass

    @abstractmethod
    def visit_pin_pad(self, pin_pad):
        pass

    @abstractmethod
    def visit_display(self, display):
        pass

    @abstractmethod
    def visit_cash_dispenser(self, cash_dispenser):
        pass

    @abstractmethod
    def visit_deposit_slot(self, deposit_slot):
        pass

    @abstractmethod
    def visit_receipt_printer(self, receipt_printer):
        pass

class ATM:
    """Context class representing the ATM."""

    def __init__(self):
        self.card_reader = CardReader()
        self.pin_pad = PinPad()
        self.display = Display()
        self.cash_dispenser = CashDispenser()
        self.deposit_slot = DepositSlot()
        self.receipt_printer = ReceiptPrinter()
        self.user_account = None

    def accept(self, visitor):
        """Accepts a visitor to perform operations."""
        visitor.visit_card_reader(self.card_reader)
        visitor.visit_pin_pad(self.pin_pad)
        visitor.visit_display(self.display)
        visitor.visit_cash_dispenser(self.cash_dispenser)
        visitor.visit_deposit_slot(self.deposit_slot)
        visitor.visit_receipt_printer(self.receipt_printer)

# Concrete element classes representing ATM parts
class CardReader:
    def read_card(self):
        """Simulates reading the ATM card."""
        card_number = input("Please insert your card (enter card number): ")
        return card_number

class PinPad:
    def get_pin(self):
        """Simulates getting the PIN from the user."""
        pin = input("Please enter your PIN: ")
        return pin

class Display:
    def show_message(self, message):
        """Simulates displaying a message on the ATM screen."""
        print(message)

    def show_balance(self, balance):
        """Simulates displaying the account balance."""
        print(f"Your current balance is: ${balance:.2f}")

class CashDispenser:
    def dispense_cash(self, amount):
        """Simulates dispensing cash."""
        print(f"Dispensing ${amount:.2f}")

class DepositSlot:
    def receive_deposit(self):
        """Simulates receiving a cash deposit."""
        try:
            amount = float(input("Enter deposit amount: $"))
            if amount <= 0:
                raise ValueError("Invalid deposit amount.")
            return amount
        except ValueError as e:
            print(f"Error: {e}")
            return 0

class ReceiptPrinter:
    def print_receipt(self, transaction_details):
        """Simulates printing a receipt."""
        if transaction_details:
            print("----- Receipt -----")
            for key, value in transaction_details.items():
                print(f"{key}: {value}")
            print("--------------------")
        else:
            print("No transaction details to print.")

# Concrete visitor classes for specific ATM operations
class AuthenticationVisitor(ATMPartVisitor):
    """Handles user authentication."""

    def __init__(self, atm, accounts):
        self.atm = atm
        self.accounts = accounts
        self.attempts_remaining = 3

    def visit_card_reader(self, card_reader):
        self.card_number = card_reader.read_card()

    def visit_pin_pad(self, pin_pad):
        while self.attempts_remaining > 0:
            pin = pin_pad.get_pin()
            if self.authenticate(self.card_number, pin):
                self.atm.display.show_message("Authentication successful!")
                break
            else:
                self.attempts_remaining -= 1
                self.atm.display.show_message(
                    f"Incorrect PIN. {self.attempts_remaining} attempts remaining."
                )
        else:
            self.atm.display.show_message("Card retained. Please contact your bank.")

    def visit_display(self, display):
        # Not used in authentication
        pass
    def visit_cash_dispenser(self, cash_dispenser):
        # Not used in authentication
        pass
    def visit_deposit_slot(self, deposit_slot):
        # Not used in authentication
        pass
    def visit_receipt_printer(self, receipt_printer):
        # Not used in authentication
        pass
    def authenticate(self, card_number, pin):
        """Simulates user authentication."""
        account = self.accounts.get(card_number)
        if account and account.pin == pin:
            self.atm.user_account = account
            return True
        return False

class BalanceInquiryVisitor(ATMPartVisitor):
    """Handles balance inquiry."""
    def __init__(self, atm):
        self.atm = atm
    def visit_display(self, display):
        if self.atm.user_account:
            display.show_balance(self.atm.user_account.balance)
        else:
            display.show_message("Please authenticate first.")
    def visit_card_reader(self, card_reader):
        # Not used in balance inquiry
        pass
    def visit_pin_pad(self, pin_pad):
        # Not used in balance inquiry
        pass
    def visit_cash_dispenser(self, cash_dispenser):
        # Not used in balance inquiry
        pass
    def visit_deposit_slot(self, deposit_slot):
        # Not used in balance inquiry
        pass
    def visit_receipt_printer(self, receipt_printer):
        # Not used in balance inquiry
        pass
class WithdrawalVisitor(ATMPartVisitor):
    """Handles cash withdrawals."""
    def __init__(self, atm):
        self.atm = atm
        self.amount = 0

    def visit_display(self, display):
        if self.atm.user_account:
            while True:
                try:
                    self.amount = float(input("Enter withdrawal amount: $"))
                    if self.amount <= 0:
                        raise ValueError("Invalid withdrawal amount.")
                    if self.amount > self.atm.user_account.balance:
                        raise ValueError("Insufficient funds.")
                    break
                except ValueError as e:
                    display.show_message(f"Error: {e}")
        else:
            display.show_message("Please authenticate first.")
    def visit_cash_dispenser(self, cash_dispenser):
        if self.atm.user_account and self.amount > 0:
            cash_dispenser.dispense_cash(self.amount)
            self.atm.user_account.balance -= self.amount
            self.atm.display.show_balance(self.atm.user_account.balance)
    def visit_card_reader(self, card_reader):
        # Not used in withdrawal
        pass
    def visit_pin_pad(self, pin_pad):
        # Not used in withdrawal
        pass
    def visit_deposit_slot(self, deposit_slot):
        # Not used in withdrawal
        pass
    def visit_receipt_printer(self, receipt_printer):
        if self.atm.user_account and self.amount > 0:
            transaction_details = {
                "Transaction": "Withdrawal",
                "Amount": f"${self.amount:.2f}",
                "Balance": f"${self.atm.user_account.balance:.2f}",
            }
            receipt_printer.print_receipt(transaction_details)
class DepositVisitor(ATMPartVisitor):
    """Handles cash deposits."""
    def __init__(self, atm):
        self.atm = atm
        self.amount = 0
    def visit_display(self, display):
        # Not used in deposit (deposit amount handled by DepositSlot)
        pass
    def visit_deposit_slot(self, deposit_slot):
        if self.atm.user_account:
            self.amount = deposit_slot.receive_deposit()
            if self.amount > 0:
                self.atm.user_account.balance += self.amount
                self.atm.display.show_balance(self.atm.user_account.balance)
        else:
            self.atm.display.show_message("Please authenticate first.")
    def visit_card_reader(self, card_reader):
        # Not used in deposit
        pass
    def visit_pin_pad(self, pin_pad):
        # Not used in deposit
        pass
    def visit_cash_dispenser(self, cash_dispenser):
        # Not used in deposit
        pass
    def visit_receipt_printer(self, receipt_printer):
        if self.atm.user_account and self.amount > 0:
            transaction_details = {
                "Transaction": "Deposit",
                "Amount": f"${self.amount:.2f}",
                "Balance": f"${self.atm.user_account.balance:.2f}",
            }
            receipt_printer.print_receipt(transaction_details)
class PinChangeVisitor(ATMPartVisitor):
    """Handles PIN change operations."""

    def __init__(self, atm):
        self.atm = atm

    def visit_pin_pad(self, pin_pad):
        if self.atm.user_account:
            while True:
                old_pin = pin_pad.get_pin()
                if old_pin == self.atm.user_account.pin:
                    break
                else:
                    self.atm.display.show_message("Incorrect PIN. Please try again.")
            new_pin = pin_pad.get_pin()
            confirm_pin = pin_pad.get_pin()
            if new_pin == confirm_pin:
                self.atm.user_account.pin = new_pin
                self.atm.display.show_message("PIN changed successfully!")
            else:
                self.atm.display.show_message("PINs do not match. Please try again.")
        else:
            self.atm.display.show_message("Please authenticate first.")
    def visit_card_reader(self, card_reader):
        # Not used in PIN change
        pass

    def visit_display(self, display):
        # Messages handled within visit_pin_pad
        pass

    def visit_cash_dispenser(self, cash_dispenser):
        # Not used in PIN change
        pass

    def visit_deposit_slot(self, deposit_slot):
        # Not used in PIN change
        pass

    def visit_receipt_printer(self, receipt_printer):
        # No receipt for PIN change
        pass

# Example account data
class Account:
    def __init__(self, card_number, pin, balance):
        self.card_number = card_number
        self.pin = pin
        self.balance = balance
# Example Usage
if __name__ == "__main__":
    accounts = {
        "1234567890123456": Account("1234567890123456", "1234", 1000.00),
        # Add more accounts as needed
    }
    atm = ATM()
    while True:
        atm.display.show_message(
            "\nWelcome to the ATM!"
            "\nPlease choose an option:"
            "\n1. Authenticate"
            "\n2. Balance Inquiry"
            "\n3. Withdrawal"
            "\n4. Deposit"
            "\n5. Change PIN"
            "\n6. Exit"
        )
        choice = input("Enter your choice: ")
        if choice == "1":
            auth_visitor = AuthenticationVisitor(atm, accounts)
            atm.accept(auth_visitor)
        elif choice == "2":
            balance_visitor = BalanceInquiryVisitor(atm)
            atm.accept(balance_visitor)
        elif choice == "3":
            withdraw_visitor = WithdrawalVisitor(atm)
            atm.accept(withdraw_visitor)
        elif choice == "4":
            deposit_visitor = DepositVisitor(atm)
            atm.accept(deposit_visitor)
        elif choice == "5":
            pin_change_visitor = PinChangeVisitor(atm)
            atm.accept(pin_change_visitor)
        elif choice == "6":
            atm.display.show_message("Thank you for using the ATM. Goodbye!")
            break
        else:
            atm.display.show_message("Invalid choice. Please try again.")