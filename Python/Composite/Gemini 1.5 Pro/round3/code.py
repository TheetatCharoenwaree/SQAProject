# code.py
from abc import ABC, abstractmethod

class ATMComponent(ABC):
    """
    Abstract base class for all ATM components (Leaf and Composite).
    """
    @abstractmethod
    def execute(self):
        pass


class Leaf(ATMComponent):
    """
    Represents leaf objects in the Composite pattern (individual actions).
    """
    def __init__(self, name, description):
        self.name = name
        self.description = description

    def execute(self):
        print(f"Executing: {self.name} - {self.description}")


class Composite(ATMComponent):
    """
    Represents composite objects in the Composite pattern (groups of actions).
    """
    def __init__(self, name, description):
        self.name = name
        self.description = description
        self.children = []

    def add(self, component):
        self.children.append(component)

    def remove(self, component):
        self.children.remove(component)

    def execute(self):
        print(f"Executing: {self.name} - {self.description}")
        for child in self.children:
            child.execute()


class Authentication(Leaf):
    def __init__(self):
        super().__init__("Authentication", "Verifying card and PIN")


class BalanceDisplay(Leaf):
    def __init__(self):
        super().__init__("Balance Display", "Showing account balance")


class CashWithdrawal(Composite):
    def __init__(self):
        super().__init__("Cash Withdrawal", "Withdrawing money from account")
        self.add(Leaf("Amount Selection", "Select withdrawal amount"))
        self.add(Leaf("Balance Verification", "Check if funds are sufficient"))
        self.add(Leaf("Dispensing Cash", "Provide cash to the user"))


class Deposit(Composite):
    def __init__(self):
        super().__init__("Deposit", "Depositing money into account")
        self.add(Leaf("Cash Reception", "Receive cash from user"))
        self.add(Leaf("Money Counting", "Count deposited cash"))
        self.add(Leaf("Balance Update", "Update account balance"))


class PinChange(Composite):
    def __init__(self):
        super().__init__("PIN Change", "Changing account PIN")
        self.add(Leaf("Old PIN Verification", "Verify old PIN"))
        self.add(Leaf("New PIN Entry", "Enter new PIN twice"))
        self.add(Leaf("PIN Update", "Store new PIN"))


class ReceiptPrinting(Leaf):
    def __init__(self):
        super().__init__("Receipt Printing", "Printing transaction details")


class ATMSystem:
    """
    The main ATM system that uses the Composite pattern to organize operations.
    """
    def __init__(self):
        self.root = Composite("ATM Operations", "Root of ATM operations")
        self.root.add(Authentication())
        self.root.add(BalanceDisplay())
        self.root.add(CashWithdrawal())
        self.root.add(Deposit())
        self.root.add(PinChange())
        self.root.add(ReceiptPrinting())

    def perform_transaction(self):
        self.root.execute()


# Example usage:
if __name__ == "__main__":
    atm = ATMSystem()
    atm.perform_transaction()