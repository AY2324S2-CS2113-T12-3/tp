package seedu.budgetbuddy.command;

import seedu.budgetbuddy.commons.CurrencyConverter;
import seedu.budgetbuddy.commons.DefaultCurrency;
import seedu.budgetbuddy.commons.ExpenseList;
import seedu.budgetbuddy.commons.SavingList;
import seedu.budgetbuddy.commons.RecurringExpensesList;

import java.util.Currency;

public class ChangeCurrencyCommand extends Command {

    private Currency newCurrency;
    private SavingList savings;
    private ExpenseList expenses;
    private RecurringExpensesList recurringExpensesList;
    private CurrencyConverter currencyConverter;

    public ChangeCurrencyCommand(Currency newCurrency, SavingList savings, ExpenseList expenses,
                                 RecurringExpensesList recurringExpensesList, CurrencyConverter currencyConverter) {
        this.newCurrency = newCurrency;
        this.savings = savings;
        this.expenses = expenses;
        this.recurringExpensesList = recurringExpensesList;
        this.currencyConverter = currencyConverter;
    }

    @Override
    public void execute() {
        currencyConverter.convertSavingCurrency(newCurrency, savings);
        currencyConverter.convertExpenseCurrency(newCurrency, expenses);
        currencyConverter.convertRecurringExpensesCurrency(newCurrency, recurringExpensesList);
        currencyConverter.convertBudgetCurrency(newCurrency, expenses);
        DefaultCurrency.setDefaultCurrency(newCurrency);
    }
}
