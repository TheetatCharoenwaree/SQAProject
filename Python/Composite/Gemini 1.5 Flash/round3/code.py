from abc import ABC, abstractmethod
from random import randint
import datetime

class ATMComponent(ABC):
    @abstractmethod
    def process(self, user):
        pass

class ATM(ATMComponent):
    def __init__(self):
        self.user = None
        self.transactions = []
        self.components = [
            Authentication(self),
            BalanceDisplay(self),
            CashWithdrawal(self),
            Deposit(self),
            PINChange(self),
            ReceiptPrinting(self)
        ]

    def process(self, user):
        self.user = user
        print("Welcome to the ATM!")
        while True:
            print("\nSelect an option:")
            print("1. View Balance")
            print("2. Withdraw Cash")
            print("3. Deposit Cash")
            print("4. Change PIN")
            print("5. Print Receipt")
            print("6. Exit")

            choice = input("Enter your choice: ")

            if choice == '6':
                print("Thank you for using the ATM!")
                break

            try:
                choice = int(choice)
                if 1 <= choice <= 5:
                    self.components[choice - 1].process(user)
                    self.transactions.append(f"{self.user.name} - {self.components[choice - 1].__class__.__name__}")
                else:
                    print("Invalid choice. Please select a valid option.")
            except ValueError:
                print("Invalid input. Please enter a number.")

    def get_transactions(self):
        return self.transactions

class Authentication(ATMComponent):
    def __init__(self, atm):
        self.atm = atm

    def process(self, user):
        attempts = 0
        max_attempts = 3

        while attempts < max_attempts:
            pin = input("Enter your PIN: ")
            if pin == user.pin:
                print("Authentication successful.")
                return

            attempts += 1
            print(f"Incorrect PIN. {max_attempts - attempts} attempts remaining.")

        print("Card retained due to too many incorrect PIN attempts.")
        self.atm.user = None

class BalanceDisplay(ATMComponent):
    def __init__(self, atm):
        self.atm = atm

    def process(self, user):
        print(f"Your current balance is: {user.balance}")

class CashWithdrawal(ATMComponent):
    def __init__(self, atm):
        self.atm = atm

    def process(self, user):
        while True:
            try:
                amount = float(input("Enter the amount to withdraw: "))
                if amount > 0 and amount <= user.balance:
                    user.balance -= amount
                    print(f"Cash withdrawn successfully. Your new balance is: {user.balance}")
                    break
                else:
                    print("Invalid amount. Please enter a valid amount.")
            except ValueError:
                print("Invalid input. Please enter a number.")

class Deposit(ATMComponent):
    def __init__(self, atm):
        self.atm = atm

    def process(self, user):
        while True:
            try:
                amount = float(input("Enter the amount to deposit: "))
                if amount > 0:
                    user.balance += amount
                    print(f"Cash deposited successfully. Your new balance is: {user.balance}")
                    break
                else:
                    print("Invalid amount. Please enter a valid amount.")
            except ValueError:
                print("Invalid input. Please enter a number.")

class PINChange(ATMComponent):
    def __init__(self, atm):
        self.atm = atm

    def process(self, user):
        while True:
            old_pin = input("Enter your old PIN: ")
            if old_pin == user.pin:
                new_pin = input("Enter your new PIN: ")
                confirm_pin = input("Confirm your new PIN: ")
                if new_pin == confirm_pin:
                    user.pin = new_pin
                    print("PIN changed successfully.")
                    break
                else:
                    print("New PIN does not match confirmation. Please try again.")
            else:
                print("Incorrect old PIN. Please try again.")

class ReceiptPrinting(ATMComponent):
    def __init__(self, atm):
        self.atm = atm

    def process(self, user):
        print("\nReceipt:")
        print(f"Date: {datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')}")
        print(f"User: {user.name}")
        for transaction in self.atm.get_transactions():
            print(transaction)
        print(f"Remaining balance: {user.balance}")

class User:
    def __init__(self, name, pin, balance):
        self.name = name
        self.pin = pin
        self.balance = balance

# Example Usage
user = User("John Doe", "1234", 1000.00)
atm = ATM()
atm.process(user)