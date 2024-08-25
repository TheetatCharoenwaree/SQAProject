from abc import ABC, abstractmethod

# Define an abstract base class for ATM elements
class ATLElement(ABC):
    @abstractmethod
    def accept(self, visitor):
        pass

# Define concrete ATM elements
class CardReader(ATLElement):
    def accept(self, visitor):
        visitor.visit_card_reader(self)

class PinPad(ATLElement):
    def accept(self, visitor):
        visitor.visit_pin_pad(self)

class Display(ATLElement):
    def accept(self, visitor):
        visitor.visit_display(self)

class CashDispenser(ATLElement):
    def accept(self, visitor):
        visitor.visit_cash_dispenser(self)

class DepositSlot(ATLElement):
    def accept(self, visitor):
        visitor.visit_deposit_slot(self)

class ReceiptPrinter(ATLElement):
    def accept(self, visitor):
        visitor.visit_receipt_printer(self)

# Define the abstract visitor class
class ATMVisitor(ABC):
    @abstractmethod
    def visit_card_reader(self, element):
        pass

    @abstractmethod
    def visit_pin_pad(self, element):
        pass

    @abstractmethod
    def visit_display(self, element):
        pass

    @abstractmethod
    def visit_cash_dispenser(self, element):
        pass

    @abstractmethod
    def visit_deposit_slot(self, element):
        pass

    @abstractmethod
    def visit_receipt_printer(self, element):
        pass

# Define a concrete visitor for ATM transactions
class ATMTransactionVisitor(ATMVisitor):
    def __init__(self, atm):
        self.atm = atm

    def visit_card_reader(self, element):
        # Logic for card reading
        card_number = input("Please insert your card (Enter card number): ")
        self.atm.set_card_number(card_number)

    def visit_pin_pad(self, element):
        # Logic for PIN input
        pin = input("Please enter your PIN: ")
        self.atm.set_pin(pin)

    def visit_display(self, element):
        # Logic for displaying information
        print(self.atm.get_display_message())

    def visit_cash_dispenser(self, element):
        # Logic for cash dispensing
        amount = float(input("Enter withdrawal amount: "))
        self.atm.withdraw_cash(amount)

    def visit_deposit_slot(self, element):
        # Logic for cash deposit
        amount = float(input("Enter deposit amount: "))
        self.atm.deposit_cash(amount)

    def visit_receipt_printer(self, element):
        # Logic for printing receipts
        print("Printing receipt...")
        print(self.atm.get_receipt())

# Define the ATM class
class ATM:
    def __init__(self):
        self.card_reader = CardReader()
        self.pin_pad = PinPad()
        self.display = Display()
        self.cash_dispenser = CashDispenser()
        self.deposit_slot = DepositSlot()
        self.receipt_printer = ReceiptPrinter()
        self.card_number = None
        self.pin = None
        self.balance = 1000  # Initial balance
        self.display_message = "Welcome!"
        self.receipt = ""

    def set_card_number(self, card_number):
        self.card_number = card_number

    def set_pin(self, pin):
        self.pin = pin

    def get_display_message(self):
        return self.display_message

    def set_display_message(self, message):
        self.display_message = message

    def get_receipt(self):
        return self.receipt

    def set_receipt(self, receipt):
        self.receipt = receipt

    def authenticate_user(self):
        # Logic for user authentication
        if self.card_number == "1234567890" and self.pin == "1234":
            self.set_display_message("Authentication successful!")
            return True
        else:
            self.set_display_message("Invalid card or PIN.")
            return False

    def check_balance(self):
        self.set_display_message(f"Your balance is: ${self.balance:.2f}")

    def withdraw_cash(self, amount):
        if amount > self.balance:
            self.set_display_message("Insufficient funds.")
        else:
            self.balance -= amount
            self.set_display_message(f"Withdrawal successful. Your new balance is: ${self.balance:.2f}")
            self.set_receipt(f"Withdrawal of ${amount:.2f}\nNew balance: ${self.balance:.2f}")

    def deposit_cash(self, amount):
        self.balance += amount
        self.set_display_message(f"Deposit successful. Your new balance is: ${self.balance:.2f}")
        self.set_receipt(f"Deposit of ${amount:.2f}\nNew balance: ${self.balance:.2f}")

    def change_pin(self):
        old_pin = input("Enter your old PIN: ")
        if old_pin != self.pin:
            self.set_display_message("Incorrect old PIN.")
            return

        new_pin = input("Enter your new PIN: ")
        confirm_pin = input("Confirm your new PIN: ")
        if new_pin == confirm_pin:
            self.pin = new_pin
            self.set_display_message("PIN changed successfully.")
        else:
            self.set_display_message("PIN mismatch.")

    def perform_transaction(self, visitor):
        visitor.visit_card_reader(self.card_reader)
        visitor.visit_pin_pad(self.pin_pad)
        if self.authenticate_user():
            while True:
                visitor.visit_display(self.display)
                choice = input("Select an option:\n1. Check balance\n2. Withdraw cash\n3. Deposit cash\n4. Change PIN\n5. Exit\n")

                if choice == '1':
                    self.check_balance()
                elif choice == '2':
                    visitor.visit_cash_dispenser(self.cash_dispenser)
                elif choice == '3':
                    visitor.visit_deposit_slot(self.deposit_slot)
                elif choice == '4':
                    self.change_pin()
                elif choice == '5':
                    self.set_display_message("Thank you for using our ATM!")
                    break
                else:
                    self.set_display_message("Invalid option selected.")

                visitor.visit_display(self.display)
                if input("Do you want to print a receipt? (y/n): ").lower() == 'y':
                    visitor.visit_receipt_printer(self.receipt_printer)
        else:
            self.set_display_message("Authentication failed. Please try again.")

# Example usage:
if __name__ == "__main__":
    atm = ATM()
    visitor = ATMTransactionVisitor(atm)
    atm.perform_transaction(visitor)