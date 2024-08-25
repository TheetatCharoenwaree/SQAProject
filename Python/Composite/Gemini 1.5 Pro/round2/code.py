# code.py
from abc import ABC, abstractmethod

class ATMComponent(ABC):
    @abstractmethod
    def execute(self):
        pass

class Leaf(ATMComponent):
    def __init__(self, name, description):
        self.name = name
        self.description = description

    def execute(self):
        print(f"Executing Leaf: {self.name} - {self.description}")

class Composite(ATMComponent):
    def __init__(self, name, description):
        self.name = name
        self.description = description
        self.children = []

    def add(self, component):
        self.children.append(component)

    def remove(self, component):
        self.children.remove(component)

    def execute(self):
        print(f"Executing Composite: {self.name} - {self.description}")
        for child in self.children:
            child.execute()

class Authentication(Leaf):
    def __init__(self):
        super().__init__("Authentication", "Verifies user credentials")
        self.attempts = 0
        self.max_attempts = 3
        self.authenticated = False

    def execute(self):
        while self.attempts < self.max_attempts:
            card_number = input("Insert card number: ")
            pin = input("Enter PIN: ")
            # Simulate PIN verification
            if card_number == "1234567890" and pin == "1234":
                self.authenticated = True
                print("Authentication successful.")
                break
            else:
                self.attempts += 1
                print(f"Invalid credentials. {self.max_attempts - self.attempts} attempts remaining.")
        if not self.authenticated:
            print("Card retained due to too many incorrect attempts.")

class BalanceInquiry(Leaf):
    def __init__(self, balance):
        super().__init__("Balance Inquiry", "Displays account balance")
        self.balance = balance

    def execute(self):
        print(f"Current balance: ${self.balance:.2f}")

class CashWithdrawal(Leaf):
    def __init__(self, balance):
        super().__init__("Cash Withdrawal", "Withdraws cash from account")
        self.balance = balance

    def execute(self):
        amount = float(input("Enter withdrawal amount: "))
        if amount <= self.balance:
            self.balance -= amount
            print(f"Cash withdrawn: ${amount:.2f}")
            print(f"Remaining balance: ${self.balance:.2f}")
        else:
            print("Insufficient funds.")

class Deposit(Leaf):
    def __init__(self, balance):
        super().__init__("Deposit", "Deposits cash into account")
        self.balance = balance

    def execute(self):
        amount = float(input("Enter deposit amount: "))
        self.balance += amount
        print(f"Cash deposited: ${amount:.2f}")
        print(f"Updated balance: ${self.balance:.2f}")

class PinChange(Leaf):
    def __init__(self, pin):
        super().__init__("PIN Change", "Changes account PIN")
        self.pin = pin

    def execute(self):
        old_pin = input("Enter old PIN: ")
        if old_pin == self.pin:
            new_pin = input("Enter new PIN: ")
            confirm_pin = input("Confirm new PIN: ")
            if new_pin == confirm_pin:
                self.pin = new_pin
                print("PIN changed successfully.")
            else:
                print("New PINs do not match.")
        else:
            print("Incorrect old PIN.")

class ReceiptPrinting(Leaf):
    def __init__(self):
        super().__init__("Receipt Printing", "Prints transaction receipt")

    def execute(self):
        print("Printing receipt...")

# Creating the ATM system composite
atm = Composite("ATM", "Automated Teller Machine")

# Adding components to the ATM system
atm.add(Authentication())
atm.add(BalanceInquiry(1000.00))
atm.add(CashWithdrawal(1000.00))
atm.add(Deposit(1000.00))
atm.add(PinChange("1234"))
atm.add(ReceiptPrinting())

# Executing the ATM system
atm.execute()