# unit_test.py
import pytest
from code import ATMComponent, Leaf, Composite, Authentication, BalanceInquiry, CashWithdrawal, Deposit, PinChange, ReceiptPrinting

# Helper function to simulate user input
def mock_input(inputs):
    """
    Mocks the user input for testing purposes.

    Args:
      inputs: A list of inputs to be returned by the input() function.

    Returns:
      A function that simulates the input() function.
    """
    
    input_values = iter(inputs)

    def mock_input_function(prompt=""):
        """
        Simulates the input() function by returning values from the input_values iterator.

        Args:
          prompt (str, optional): The prompt to display to the user. Defaults to "".

        Returns:
          str: The next value from the input_values iterator.
        """
        print(prompt, end="")
        return next(input_values)
    
    return mock_input_function

# Test cases for ATMComponent
def test_ATMComponent_abstract_methods():
    with pytest.raises(TypeError):
        ATMComponent()

# Test cases for Leaf
def test_Leaf_initialization():
    leaf = Leaf("Test Leaf", "This is a test leaf")
    assert leaf.name == "Test Leaf"
    assert leaf.description == "This is a test leaf"

def test_Leaf_execute():
    leaf = Leaf("Test Leaf", "This is a test leaf")
    assert leaf.execute() is None  # Assuming execute() doesn't return anything

# Test cases for Composite
def test_Composite_initialization():
    composite = Composite("Test Composite", "This is a test composite")
    assert composite.name == "Test Composite"
    assert composite.description == "This is a test composite"
    assert composite.children == []

def test_Composite_add_and_remove():
    composite = Composite("Test Composite", "This is a test composite")
    leaf1 = Leaf("Leaf 1", "This is leaf 1")
    leaf2 = Leaf("Leaf 2", "This is leaf 2")
    composite.add(leaf1)
    composite.add(leaf2)
    assert len(composite.children) == 2
    composite.remove(leaf1)
    assert len(composite.children) == 1
    assert composite.children[0] == leaf2

def test_Composite_execute():
    composite = Composite("Test Composite", "This is a test composite")
    leaf1 = Leaf("Leaf 1", "This is leaf 1")
    leaf2 = Leaf("Leaf 2", "This is leaf 2")
    composite.add(leaf1)
    composite.add(leaf2)
    assert composite.execute() is None  # Assuming execute() doesn't return anything

# Test cases for Authentication
def test_Authentication_successful_login(monkeypatch):
    monkeypatch.setattr('builtins.input', mock_input(["1234567890", "1234"]))
    authentication = Authentication()
    authentication.execute()
    assert authentication.authenticated == True

def test_Authentication_failed_login(monkeypatch):
    monkeypatch.setattr('builtins.input', mock_input(["1234567890", "0000", "1234567890", "0000", "1234567890", "0000"]))
    authentication = Authentication()
    authentication.execute()
    assert authentication.authenticated == False

# Test cases for BalanceInquiry
def test_BalanceInquiry_execute():
    balance_inquiry = BalanceInquiry(1000.00)
    assert balance_inquiry.execute() is None  # Assuming execute() doesn't return anything

# Test cases for CashWithdrawal
def test_CashWithdrawal_successful_withdrawal(monkeypatch):
    monkeypatch.setattr('builtins.input', mock_input(["500.00"]))
    cash_withdrawal = CashWithdrawal(1000.00)
    cash_withdrawal.execute()
    assert cash_withdrawal.balance == 500.00

def test_CashWithdrawal_insufficient_funds(monkeypatch):
    monkeypatch.setattr('builtins.input', mock_input(["1500.00"]))
    cash_withdrawal = CashWithdrawal(1000.00)
    cash_withdrawal.execute()
    assert cash_withdrawal.balance == 1000.00

# Test cases for Deposit
def test_Deposit_execute(monkeypatch):
    monkeypatch.setattr('builtins.input', mock_input(["500.00"]))
    deposit = Deposit(1000.00)
    deposit.execute()
    assert deposit.balance == 1500.00

# Test cases for PinChange
def test_PinChange_successful_change(monkeypatch):
    monkeypatch.setattr('builtins.input', mock_input(["1234", "5678", "5678"]))
    pin_change = PinChange("1234")
    pin_change.execute()
    assert pin_change.pin == "5678"

def test_PinChange_incorrect_old_pin(monkeypatch):
    monkeypatch.setattr('builtins.input', mock_input(["0000", "5678", "5678"]))
    pin_change = PinChange("1234")
    pin_change.execute()
    assert pin_change.pin == "1234"

def test_PinChange_new_pins_mismatch(monkeypatch):
    monkeypatch.setattr('builtins.input', mock_input(["1234", "5678", "4321"]))
    pin_change = PinChange("1234")
    pin_change.execute()
    assert pin_change.pin == "1234"

# Test cases for ReceiptPrinting
def test_ReceiptPrinting_execute():
    receipt_printing = ReceiptPrinting()
    assert receipt_printing.execute() is None  # Assuming execute() doesn't return anything