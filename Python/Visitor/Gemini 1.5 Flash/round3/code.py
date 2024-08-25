from abc import ABC, abstractmethod
from typing import List, Optional

class Account:
    def __init__(self, account_number: str, pin: str, balance: float = 0):
        self.account_number = account_number
        self.pin = pin
        self.balance = balance

    def get_balance(self) -> float:
        return self.balance

    def withdraw(self, amount: float) -> bool:
        if self.balance >= amount:
            self.balance -= amount
            return True
        return False

    def deposit(self, amount: float) -> None:
        self.balance += amount

    def change_pin(self, new_pin: str) -> None:
        self.pin = new_pin

class ATM:
    def __init__(self):
        self.accounts = {}  # Example: {"account_number": Account}
        self.max_pin_attempts = 3
        self.transaction_history = []

    def add_account(self, account: Account) -> None:
        self.accounts[account.account_number] = account

    def authenticate(self, account_number: str, pin: str) -> Optional[Account]:
        account = self.accounts.get(account_number)
        if account and account.pin == pin:
            return account
        return None

    def process_transaction(self, account: Account, visitor: "ATMVisitor") -> None:
        visitor.visit(account, self)

    def get_transaction_history(self) -> List[str]:
        return self.transaction_history

class ATMVisitor(ABC):
    @abstractmethod
    def visit(self, account: Account, atm: ATM) -> None:
        pass

class BalanceDisplayVisitor(ATMVisitor):
    def visit(self, account: Account, atm: ATM) -> None:
        balance = account.get_balance()
        atm.transaction_history.append(f"Balance: {balance}")
        print(f"Your current balance is: {balance}")

class CashWithdrawalVisitor(ATMVisitor):
    def __init__(self, amount: float):
        self.amount = amount

    def visit(self, account: Account, atm: ATM) -> None:
        if account.withdraw(self.amount):
            atm.transaction_history.append(f"Withdrawal: {self.amount}")
            print(f"You have withdrawn {self.amount}")
        else:
            atm.transaction_history.append(f"Insufficient funds for withdrawal: {self.amount}")
            print("Insufficient funds.")

class DepositVisitor(ATMVisitor):
    def __init__(self, amount: float):
        self.amount = amount

    def visit(self, account: Account, atm: ATM) -> None:
        account.deposit(self.amount)
        atm.transaction_history.append(f"Deposit: {self.amount}")
        print(f"You have deposited {self.amount}")

class PINChangeVisitor(ATMVisitor):
    def __init__(self, new_pin: str):
        self.new_pin = new_pin

    def visit(self, account: Account, atm: ATM) -> None:
        # Simulate PIN confirmation (you would typically ask for new pin twice)
        if True:  # Assuming confirmation is successful
            account.change_pin(self.new_pin)
            atm.transaction_history.append(f"PIN changed successfully.")
            print("PIN changed successfully.")
        else:
            atm.transaction_history.append(f"PIN change failed.")
            print("PIN change failed. Please try again.")

class ReceiptPrintingVisitor(ATMVisitor):
    def visit(self, account: Account, atm: ATM) -> None:
        print("Printing receipt...")
        print("-" * 20)
        for transaction in atm.get_transaction_history():
            print(transaction)
        print("-" * 20)
        print("Thank you!")

def main():
    atm = ATM()
    atm.add_account(Account("1234567890", "1234", 1000))

    while True:
        account_number = input("Enter your account number: ")
        pin = input("Enter your PIN: ")
        account = atm.authenticate(account_number, pin)

        if account:
            print("Authentication successful!")
            while True:
                print("\nChoose an option:")
                print("1. View balance")
                print("2. Withdraw cash")
                print("3. Deposit cash")
                print("4. Change PIN")
                print("5. Print receipt")
                print("6. Exit")

                choice = input("Enter your choice: ")

                if choice == '1':
                    atm.process_transaction(account, BalanceDisplayVisitor())
                elif choice == '2':
                    amount = float(input("Enter amount to withdraw: "))
                    atm.process_transaction(account, CashWithdrawalVisitor(amount))
                elif choice == '3':
                    amount = float(input("Enter amount to deposit: "))
                    atm.process_transaction(account, DepositVisitor(amount))
                elif choice == '4':
                    new_pin = input("Enter new PIN: ")
                    atm.process_transaction(account, PINChangeVisitor(new_pin))
                elif choice == '5':
                    atm.process_transaction(account, ReceiptPrintingVisitor())
                elif choice == '6':
                    print("Exiting...")
                    break
                else:
                    print("Invalid choice.")
        else:
            print("Authentication failed.")

if __name__ == "__main__":
    main()