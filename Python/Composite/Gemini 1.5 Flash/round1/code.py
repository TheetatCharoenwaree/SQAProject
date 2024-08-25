from abc import ABC, abstractmethod

class ATMComponent(ABC):
    @abstractmethod
    def process(self, user):
        pass

class Authentication(ATMComponent):
    def __init__(self, max_attempts=3):
        self.max_attempts = max_attempts
        self.attempts = 0

    def process(self, user):
        if self.attempts >= self.max_attempts:
            raise ValueError("Card seized due to exceeding attempts.")
        
        # Simulate card and PIN verification
        if user.card_number == "1234567890" and user.pin == "1234":
            print("Authentication successful!")
            self.attempts = 0
            return True
        else:
            self.attempts += 1
            print("Authentication failed. Please try again.")
            return False

class BalanceInquiry(ATMComponent):
    def process(self, user):
        print(f"Your current balance is: {user.balance}")

class Withdrawal(ATMComponent):
    def process(self, user):
        amount = int(input("Enter withdrawal amount: "))
        if amount > user.balance:
            print("Insufficient funds!")
        else:
            user.balance -= amount
            print(f"Withdrawal successful. New balance: {user.balance}")

class Deposit(ATMComponent):
    def process(self, user):
        amount = int(input("Enter deposit amount: "))
        user.balance += amount
        print(f"Deposit successful. New balance: {user.balance}")

class PINChange(ATMComponent):
    def process(self, user):
        old_pin = input("Enter old PIN: ")
        if old_pin != user.pin:
            print("Incorrect old PIN.")
            return
        
        new_pin1 = input("Enter new PIN: ")
        new_pin2 = input("Confirm new PIN: ")
        if new_pin1 == new_pin2:
            user.pin = new_pin1
            print("PIN changed successfully.")
        else:
            print("PINs do not match.")

class PrintReceipt(ATMComponent):
    def process(self, user):
        print("Printing receipt...")

class ATMComposite(ATMComponent):
    def __init__(self):
        self.components = []

    def add(self, component):
        self.components.append(component)

    def remove(self, component):
        self.components.remove(component)

    def process(self, user):
        for component in self.components:
            component.process(user)

class User:
    def __init__(self, card_number, pin, balance):
        self.card_number = card_number
        self.pin = pin
        self.balance = balance

def main():
    user = User("1234567890", "1234", 1000)

    atm = ATMComposite()
    atm.add(Authentication())
    atm.add(BalanceInquiry())
    atm.add(Withdrawal())
    atm.add(Deposit())
    atm.add(PINChange())
    atm.add(PrintReceipt())

    while True:
        print("\nATM Menu:")
        print("1. Authentication")
        print("2. Balance Inquiry")
        print("3. Withdrawal")
        print("4. Deposit")
        print("5. Change PIN")
        print("6. Print Receipt")
        print("7. Exit")

        choice = input("Enter your choice: ")

        if choice == '1':
            if atm.components[0].process(user):
                continue
        elif choice == '2':
            atm.components[1].process(user)
        elif choice == '3':
            atm.components[2].process(user)
        elif choice == '4':
            atm.components[3].process(user)
        elif choice == '5':
            atm.components[4].process(user)
        elif choice == '6':
            atm.components[5].process(user)
        elif choice == '7':
            break
        else:
            print("Invalid choice.")

if __name__ == "__main__":
    main()