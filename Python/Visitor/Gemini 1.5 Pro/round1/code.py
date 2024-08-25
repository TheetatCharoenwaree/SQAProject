from abc import ABC, abstractmethod

# Visitor interface
class ATMVisitor(ABC):

    @abstractmethod
    def visit_balance_inquiry(self, balance_inquiry):
        pass

    @abstractmethod
    def visit_cash_withdrawal(self, cash_withdrawal):
        pass

    @abstractmethod
    def visit_deposit(self, deposit):
        pass

    @abstractmethod
    def visit_pin_change(self, pin_change):
        pass

# Concrete visitor for handling transactions
class TransactionVisitor(ATMVisitor):

    def __init__(self, atm, account):
        self.atm = atm
        self.account = account

    def visit_balance_inquiry(self, balance_inquiry):
        print(f"Your balance is: ${self.account.balance:.2f}")

    def visit_cash_withdrawal(self, cash_withdrawal):
        amount = cash_withdrawal.amount
        if self.account.balance >= amount:
            self.account.balance -= amount
            self.atm.dispense_cash(amount)
            print(f"Withdrew ${amount:.2f}. New balance: ${self.account.balance:.2f}")
        else:
            print("Insufficient funds.")

    def visit_deposit(self, deposit):
        amount = deposit.amount
        self.account.balance += amount
        print(f"Deposited ${amount:.2f}. New balance: ${self.account.balance:.2f}")

    def visit_pin_change(self, pin_change):
        if self.account.verify_pin(pin_change.old_pin):
            self.account.change_pin(pin_change.new_pin)
            print("PIN changed successfully.")
        else:
            print("Incorrect old PIN.")

# ATM Elements (Visitable)
class ATMElement(ABC):

    @abstractmethod
    def accept(self, visitor: ATMVisitor):
        pass

class BalanceInquiry(ATMElement):
    def accept(self, visitor: ATMVisitor):
        visitor.visit_balance_inquiry(self)

class CashWithdrawal(ATMElement):
    def __init__(self, amount):
        self.amount = amount

    def accept(self, visitor: ATMVisitor):
        visitor.visit_cash_withdrawal(self)

class Deposit(ATMElement):
    def __init__(self, amount):
        self.amount = amount

    def accept(self, visitor: ATMVisitor):
        visitor.visit_deposit(self)

class PINChange(ATMElement):
    def __init__(self, old_pin, new_pin):
        self.old_pin = old_pin
        self.new_pin = new_pin

    def accept(self, visitor: ATMVisitor):
        visitor.visit_pin_change(self)

# Account class
class Account:
    def __init__(self, card_number, pin, balance=0):
        self.card_number = card_number
        self.pin = pin
        self.balance = balance
        self.attempts_left = 3

    def verify_pin(self, pin):
        if self.attempts_left > 0:
            if self.pin == pin:
                self.attempts_left = 3
                return True
            else:
                self.attempts_left -= 1
                print(f"Incorrect PIN. Attempts left: {self.attempts_left}")
                if self.attempts_left == 0:
                    print("Card retained due to too many incorrect PIN attempts.")
                return False
        else:
            print("Card is blocked. Please contact your bank.")
            return False

    def change_pin(self, new_pin):
        self.pin = new_pin

# ATM class
class ATM:
    def __init__(self):
        self.accounts = {}

    def add_account(self, account):
        self.accounts[account.card_number] = account

    def authenticate_user(self, card_number, pin):
        account = self.accounts.get(card_number)
        if account and account.verify_pin(pin):
            return account
        return None

    def dispense_cash(self, amount):
        # Logic for physically dispensing cash
        print(f"Dispensing ${amount:.2f} cash...")

    def run(self):
        card_number = input("Insert card (enter card number): ")
        pin = input("Enter PIN: ")
        account = self.authenticate_user(card_number, pin)
        if account:
            visitor = TransactionVisitor(self, account)
            while True:
                print("\nChoose an option:")
                print("1. Balance Inquiry")
                print("2. Cash Withdrawal")
                print("3. Deposit")
                print("4. PIN Change")
                print("5. Exit")
                choice = input("Enter choice: ")

                if choice == '1':
                    BalanceInquiry().accept(visitor)
                elif choice == '2':
                    amount = float(input("Enter amount to withdraw: "))
                    CashWithdrawal(amount).accept(visitor)
                elif choice == '3':
                    amount = float(input("Enter amount to deposit: "))
                    Deposit(amount).accept(visitor)
                elif choice == '4':
                    old_pin = input("Enter old PIN: ")
                    new_pin = input("Enter new PIN: ")
                    confirm_pin = input("Confirm new PIN: ")
                    if new_pin == confirm_pin:
                        PINChange(old_pin, new_pin).accept(visitor)
                    else:
                        print("New PINs do not match.")
                elif choice == '5':
                    print("Thank you for using the ATM!")
                    break
                else:
                    print("Invalid choice. Please try again.")
        else:
            print("Authentication failed.")


if __name__ == "__main__":
    atm = ATM()
    atm.add_account(Account("1234567890123456", "1234", 1000))
    atm.run()