# code.py
from abc import ABC, abstractmethod

class ATMComponent(ABC):
    @abstractmethod
    def execute(self):
        pass

class ATMCardReader(ATMComponent):
    def execute(self):
        print("Please insert your ATM card.")
        # Simulate card insertion and reading
        return True

class PINVerification(ATMComponent):
    def __init__(self, pin):
        self.pin = pin
        self.attempts = 0

    def execute(self):
        while self.attempts < 3:
            entered_pin = input("Please enter your PIN: ")
            if entered_pin == self.pin:
                print("PIN verified.")
                return True
            else:
                self.attempts += 1
                print(f"Incorrect PIN. {3 - self.attempts} attempts remaining.")
        print("Too many incorrect attempts. Card retained.")
        return False

class BalanceDisplay(ATMComponent):
    def __init__(self, balance):
        self.balance = balance

    def execute(self):
        print(f"Your current balance is: ${self.balance:.2f}")
        return True

class CashWithdrawal(ATMComponent):
    def __init__(self, balance):
        self.balance = balance

    def execute(self):
        amount = float(input("Enter the withdrawal amount: $"))
        if amount > self.balance:
            print("Insufficient funds.")
            return False
        else:
            self.balance -= amount
            print(f"Please take your cash: ${amount:.2f}")
            print(f"Your remaining balance is: ${self.balance:.2f}")
            return True

class Deposit(ATMComponent):
    def __init__(self, balance):
        self.balance = balance

    def execute(self):
        amount = float(input("Enter the deposit amount: $"))
        self.balance += amount
        print(f"Your new balance is: ${self.balance:.2f}")
        return True

class PINChange(ATMComponent):
    def __init__(self, pin):
        self.pin = pin

    def execute(self):
        old_pin = input("Enter your old PIN: ")
        if old_pin != self.pin:
            print("Incorrect old PIN.")
            return False
        new_pin = input("Enter your new PIN: ")
        confirm_pin = input("Confirm your new PIN: ")
        if new_pin == confirm_pin:
            self.pin = new_pin
            print("PIN changed successfully.")
            return True
        else:
            print("PIN confirmation mismatch.")
            return False

class ReceiptPrinting(ATMComponent):
    def execute(self):
        print("Do you want to print a receipt?")
        choice = input("Enter 'y' for Yes or 'n' for No: ")
        if choice.lower() == 'y':
            print("Printing receipt...")
            # Simulate receipt printing
            print("Receipt printed.")
            return True
        else:
            print("Receipt not printed.")
            return True

class ATMComposite(ATMComponent):
    def __init__(self):
        self.components = []

    def add(self, component):
        self.components.append(component)

    def execute(self):
        for component in self.components:
            success = component.execute()
            if not success:
                break  # Stop execution if any component fails

# Example usage:
if __name__ == "__main__":
    atm = ATMComposite()
    atm.add(ATMCardReader())
    atm.add(PINVerification("1234"))
    atm.add(BalanceDisplay(1000.00))
    atm.add(CashWithdrawal(1000.00))
    atm.add(Deposit(1000.00))
    atm.add(PINChange("1234"))
    atm.add(ReceiptPrinting())
    atm.execute()