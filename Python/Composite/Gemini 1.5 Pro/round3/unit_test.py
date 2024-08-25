# unit_test.py
import pytest
from code import ATMComponent, Leaf, Composite, Authentication, BalanceDisplay, \
    CashWithdrawal, Deposit, PinChange, ReceiptPrinting, ATMSystem

# Helper function to count children recursively
def count_children(component):
    count = 0
    if isinstance(component, Composite):
        count += len(component.children)
        for child in component.children:
            count += count_children(child)
    return count

# Test cases for ATMComponent
def test_ATMComponent_abstract():
    with pytest.raises(TypeError):
        ATMComponent()

# Test cases for Leaf
def test_Leaf_creation():
    leaf = Leaf("Test Leaf", "Description")
    assert leaf.name == "Test Leaf"
    assert leaf.description == "Description"

def test_Leaf_execute():
    leaf = Leaf("Test Leaf", "Description")
    # Check if execute() prints the expected output
    # (You might need to redirect stdout to capture the output for assertion)
    leaf.execute() 

# Test cases for Composite
def test_Composite_creation():
    composite = Composite("Test Composite", "Description")
    assert composite.name == "Test Composite"
    assert composite.description == "Description"
    assert len(composite.children) == 0

def test_Composite_add_remove():
    composite = Composite("Test Composite", "Description")
    leaf1 = Leaf("Leaf 1", "Description 1")
    leaf2 = Leaf("Leaf 2", "Description 2")
    composite.add(leaf1)
    composite.add(leaf2)
    assert len(composite.children) == 2
    composite.remove(leaf1)
    assert len(composite.children) == 1
    assert composite.children[0] == leaf2

def test_Composite_execute():
    composite = Composite("Test Composite", "Description")
    leaf1 = Leaf("Leaf 1", "Description 1")
    leaf2 = Leaf("Leaf 2", "Description 2")
    composite.add(leaf1)
    composite.add(leaf2)
    # Check if execute() prints the expected output
    # (You might need to redirect stdout to capture the output for assertion)
    composite.execute()

# Test cases for Concrete ATM Components
def test_Authentication():
    auth = Authentication()
    assert isinstance(auth, Leaf)
    assert auth.name == "Authentication"

def test_BalanceDisplay():
    display = BalanceDisplay()
    assert isinstance(display, Leaf)
    assert display.name == "Balance Display"

def test_CashWithdrawal():
    withdrawal = CashWithdrawal()
    assert isinstance(withdrawal, Composite)
    assert withdrawal.name == "Cash Withdrawal"
    assert count_children(withdrawal) == 3

def test_Deposit():
    deposit = Deposit()
    assert isinstance(deposit, Composite)
    assert deposit.name == "Deposit"
    assert count_children(deposit) == 3

def test_PinChange():
    pin_change = PinChange()
    assert isinstance(pin_change, Composite)
    assert pin_change.name == "PIN Change"
    assert count_children(pin_change) == 3

def test_ReceiptPrinting():
    receipt = ReceiptPrinting()
    assert isinstance(receipt, Leaf)
    assert receipt.name == "Receipt Printing"

# Test cases for ATMSystem
def test_ATMSystem_creation():
    atm = ATMSystem()
    assert isinstance(atm.root, Composite)
    assert atm.root.name == "ATM Operations"
    assert count_children(atm.root) == 6  # 6 direct children

def test_ATMSystem_perform_transaction():
    atm = ATMSystem()
    # Check if perform_transaction() prints the expected output
    # (You might need to redirect stdout to capture the output for assertion)
    atm.perform_transaction() 

# Add more test cases as needed to achieve 100% coverage