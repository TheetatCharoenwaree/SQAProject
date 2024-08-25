from abc import ABC, abstractmethod
from typing import List, Tuple, Dict

class ATMCard:
    def __init__(self, card_number: str, pin: str, balance: float):
        self.card_number = card_number
        self.pin = pin
        self.balance = balance

    def get_balance(self) -> float:
        return self.balance

    def update_balance(self, amount: float):
        self.balance += amount

    def change_pin(self, new_pin: str):
        self.pin = new_pin

class ATMVisitor(ABC):
    @abstractmethod
    def visit_balance_display(self, atm_card: ATMCard):
        pass

    @abstractmethod
    def visit_cash_withdrawal(self, atm_card: ATMCard, amount: float):
        pass

    @abstractmethod
    def visit_deposit(self, atm_card: ATMCard, amount: float):
        pass

    @abstractmethod
    def visit_pin_change(self, atm_card: ATMCard, new_pin: str):
        pass

class BalanceDisplayVisitor(ATMVisitor):
    def visit_balance_display(self, atm_card: ATMCard):
        print(f"Your current balance is: {atm_card.get_balance()}")

    def visit_cash_withdrawal(self, atm_card: ATMCard, amount: float):
        pass

    def visit_deposit(self, atm_card: ATMCard, amount: float):
        pass

    def visit_pin_change(self, atm_card: ATMCard, new_pin: str):
        pass

class CashWithdrawalVisitor(ATMVisitor):
    def visit_balance_display(self, atm_card: ATMCard):
        pass

    def visit_cash_withdrawal(self, atm_card: ATMCard, amount: float):
        if atm_card.get_balance() >= amount:
            atm_card.update_balance(-amount)
            print(f"Cash withdrawn: {amount}. Your new balance is: {atm_card.get_balance()}")
        else:
            print("Insufficient funds.")

    def visit_deposit(self, atm_card: ATMCard, amount: float):
        pass

    def visit_pin_change(self, atm_card: ATMCard, new_pin: str):
        pass

class DepositVisitor(ATMVisitor):
    def visit_balance_display(self, atm_card: ATMCard):
        pass

    def visit_cash_withdrawal(self, atm_card: ATMCard, amount: float):
        pass

    def visit_deposit(self, atm_card: ATMCard, amount: float):
        atm_card.update_balance(amount)
        print(f"Deposited amount: {amount}. Your new balance is: {atm_card.get_balance()}")

    def visit_pin_change(self, atm_card: ATMCard, new_pin: str):
        pass

class PINChangeVisitor(ATMVisitor):
    def visit_balance_display(self, atm_card: ATMCard):
        pass

    def visit_cash_withdrawal(self, atm_card: ATMCard, amount: float):
        pass

    def visit_deposit(self, atm_card: ATMCard, amount: float):
        pass

    def visit_pin_change(self, atm_card: ATMCard, new_pin: str):
        # Assuming a basic pin confirmation system for demonstration
        confirm_pin = input("Confirm new PIN: ")
        if new_pin == confirm_pin:
            atm_card.change_pin(new_pin)
            print("PIN changed successfully.")
        else:
            print("PIN confirmation failed.")

class ATM:
    def __init__(self, card_data: Dict[str, Tuple[str, float]]):
        self.card_data = card_data
        self.max_pin_attempts = 3

    def authenticate(self) -> ATMCard:
        card_number = input("Enter your card number: ")
        if card_number not in self.card_data:
            print("Invalid card number.")
            return None

        pin = input("Enter your PIN: ")
        correct_pin = self.card_data[card_number][0]
        attempts = 0
        while attempts < self.max_pin_attempts:
            if pin == correct_pin:
                return ATMCard(card_number, correct_pin, self.card_data[card_number][1])
            else:
                attempts += 1
                pin = input("Incorrect PIN. Please try again: ")
        print("Card retained due to too many incorrect PIN attempts.")
        return None

    def process_transaction(self, atm_card: ATMCard, visitor: ATMVisitor):
        visitor.visit_balance_display(atm_card)
        visitor.visit_cash_withdrawal(atm_card, 0) # Placeholder for potential withdrawal
        visitor.visit_deposit(atm_card, 0) # Placeholder for potential deposit
        visitor.visit_pin_change(atm_card, "") # Placeholder for potential PIN change

    def run(self):
        atm_card = self.authenticate()
        if atm_card:
            while True:
                print("\nChoose an option:")
                print("1. Balance Display")
                print("2. Cash Withdrawal")
                print("3. Deposit")
                print("4. Change PIN")
                print("5. Exit")

                choice = input("Enter your choice: ")

                if choice == '1':
                    self.process_transaction(atm_card, BalanceDisplayVisitor())
                elif choice == '2':
                    amount = float(input("Enter withdrawal amount: "))
                    self.process_transaction(atm_card, CashWithdrawalVisitor())
                elif choice == '3':
                    amount = float(input("Enter deposit amount: "))
                    self.process_transaction(atm_card, DepositVisitor())
                elif choice == '4':
                    new_pin = input("Enter new PIN: ")
                    self.process_transaction(atm_card, PINChangeVisitor())
                elif choice == '5':
                    print("Thank you for using the ATM.")
                    break
                else:
                    print("Invalid choice.")

if __name__ == "__main__":
    # Sample card data: card number, pin, balance
    card_data = {
        "1234567890123456": ("1111", 1000.00),
        "9876543210123456": ("2222", 500.00)
    }
    atm = ATM(card_data)
    atm.run()