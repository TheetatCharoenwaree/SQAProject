import pytest
from code import ATM, ATMCard, BalanceDisplayVisitor, CashWithdrawalVisitor, DepositVisitor, PINChangeVisitor

@pytest.fixture
def atm():
    card_data = {
        "1234567890123456": ("1111", 1000.00)
    }
    return ATM(card_data)

@pytest.fixture
def atm_card(atm):
    return ATMCard("1234567890123456", "1111", 1000.00)

def test_authenticate_success(atm):
    atm_card = atm.authenticate()
    assert atm_card.card_number == "1234567890123456"
    assert atm_card.pin == "1111"
    assert atm_card.balance == 1000.00

def test_authenticate_invalid_card(atm):
    atm_card = atm.authenticate()
    assert atm_card is None

def test_authenticate_incorrect_pin(atm):
    atm_card = atm.authenticate()
    assert atm_card is None

def test_balance_display(atm, atm_card):
    visitor = BalanceDisplayVisitor()
    atm.process_transaction(atm_card, visitor)
    # Assertions based on printed output are not possible with pytest,
    # so we need to modify the visitor to return the balance instead.
    # This is just an example, and for real testing, you'd need to mock the print function.
    assert visitor.visit_balance_display(atm_card) == 1000.00

def test_cash_withdrawal_sufficient_funds(atm, atm_card):
    visitor = CashWithdrawalVisitor()
    atm.process_transaction(atm_card, visitor)
    assert visitor.visit_cash_withdrawal(atm_card, 500.00) is None
    assert atm_card.balance == 500.00

def test_cash_withdrawal_insufficient_funds(atm, atm_card):
    visitor = CashWithdrawalVisitor()
    atm.process_transaction(atm_card, visitor)
    assert visitor.visit_cash_withdrawal(atm_card, 1500.00) is None
    assert atm_card.balance == 1000.00

def test_deposit(atm, atm_card):
    visitor = DepositVisitor()
    atm.process_transaction(atm_card, visitor)
    assert visitor.visit_deposit(atm_card, 200.00) is None
    assert atm_card.balance == 1200.00

def test_pin_change(atm, atm_card):
    visitor = PINChangeVisitor()
    atm.process_transaction(atm_card, visitor)
    # Mock input for the new pin and confirmation
    new_pin = "2222"
    visitor.visit_pin_change(atm_card, new_pin)
    assert atm_card.pin == new_pin