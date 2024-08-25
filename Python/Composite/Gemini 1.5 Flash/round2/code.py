from abc import ABC, abstractmethod

class ATMComponent(ABC):
    @abstractmethod
    def execute(self):
        pass

class ATM(ATMComponent):
    def __init__(self):
        self.balance = 1000
        self.pin_attempts = 0
        self.max_attempts = 3

    def execute(self):
        print("Welcome to the ATM")
        while True:
            print("\nChoose an option:")
            print("1. Check balance")
            print("2. Withdraw")
            print("3. Deposit")
            print("4. Change PIN")
            print("5. Exit")

            choice = input("Enter your choice: ")

            if choice == "1":
                self.check_balance()
            elif choice == "2":
                self.withdraw()
            elif choice == "3":
                self.deposit()
            elif choice == "4":
                self.change_pin()
            elif choice == "5":
                print("Thank you for using the ATM!")
                break
            else:
                print("Invalid choice. Please try again.")

    def check_balance(self):
        print(f"Your current balance is: {self.balance}")

    def withdraw(self):
        amount = int(input("Enter the amount to withdraw: "))
        if amount <= self.balance:
            self.balance -= amount
            print(f"You have withdrawn {amount}.")
            print(f"Your remaining balance is: {self.balance}")
        else:
            print("Insufficient funds.")

    def deposit(self):
        amount = int(input("Enter the amount to deposit: "))
        self.balance += amount
        print(f"You have deposited {amount}.")
        print(f"Your current balance is: {self.balance}")

    def change_pin(self):
        old_pin = input("Enter your current PIN: ")
        if self.authenticate(old_pin):
            new_pin = input("Enter your new PIN: ")
            confirm_pin = input("Confirm your new PIN: ")
            if new_pin == confirm_pin:
                self.pin = new_pin
                print("Your PIN has been changed successfully.")
            else:
                print("PINs do not match. Please try again.")
        else:
            print("Invalid PIN. Please try again.")

    def authenticate(self, pin):
        if pin == "1234":
            self.pin_attempts = 0
            return True
        else:
            self.pin_attempts += 1
            if self.pin_attempts >= self.max_attempts:
                print("Card blocked due to excessive incorrect PIN attempts.")
                return False
            else:
                print("Incorrect PIN. Please try again.")
                return False

class PrintSlip(ATMComponent):
    def __init__(self, atm, transaction_details):
        self.atm = atm
        self.transaction_details = transaction_details

    def execute(self):
        print("\n--- Transaction Slip ---")
        print(f"Date: {self.transaction_details['date']}")
        print(f"Time: {self.transaction_details['time']}")
        print(f"Transaction: {self.transaction_details['transaction']}")
        print(f"Amount: {self.transaction_details['amount']}")
        print(f"Balance: {self.atm.balance}")
        print("-----------------------")

class Transaction(ATMComponent):
    def __init__(self, atm, transaction_type):
        self.atm = atm
        self.transaction_type = transaction_type
        self.transaction_details = {}

    def execute(self):
        if self.transaction_type == "withdraw":
            amount = int(input("Enter the amount to withdraw: "))
            self.transaction_details['transaction'] = "Withdrawal"
            self.transaction_details['amount'] = amount
            self.transaction_details['date'] = "2023-12-12"
            self.transaction_details['time'] = "10:10:10"
            if amount <= self.atm.balance:
                self.atm.balance -= amount
                print(f"You have withdrawn {amount}.")
                print(f"Your remaining balance is: {self.atm.balance}")
            else:
                print("Insufficient funds.")
        elif self.transaction_type == "deposit":
            amount = int(input("Enter the amount to deposit: "))
            self.transaction_details['transaction'] = "Deposit"
            self.transaction_details['amount'] = amount
            self.transaction_details['date'] = "2023-12-12"
            self.transaction_details['time'] = "10:10:10"
            self.atm.balance += amount
            print(f"You have deposited {amount}.")
            print(f"Your current balance is: {self.atm.balance}")
        elif self.transaction_type == "check_balance":
            self.transaction_details['transaction'] = "Balance Inquiry"
            self.transaction_details['amount'] = 0
            self.transaction_details['date'] = "2023-12-12"
            self.transaction_details['time'] = "10:10:10"
            print(f"Your current balance is: {self.atm.balance}")

def main():
    atm = ATM()
    atm.execute()

if __name__ == "__main__":
    main()