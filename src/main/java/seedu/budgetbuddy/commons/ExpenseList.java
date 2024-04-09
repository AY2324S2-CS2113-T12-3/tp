package seedu.budgetbuddy.commons;

import seedu.budgetbuddy.Ui;
import seedu.budgetbuddy.exception.BudgetBuddyException;

import java.util.Arrays;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;


public class ExpenseList {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    protected ArrayList<Expense> expenses;
    protected ArrayList<String> categories = new ArrayList<>(Arrays.asList("Housing",
            "Groceries", "Utility", "Transport", "Entertainment", "Others"));
    protected List<Budget> budgets;

    Ui ui = new Ui();

    public ExpenseList(ArrayList<Expense> expenses) {
        this.expenses = expenses;
        this.budgets = new ArrayList<>();

    }

    public ExpenseList() {
        this.expenses = new ArrayList<>();
        this.budgets = new ArrayList<>();
    }

    public int size() {
        return expenses.size();
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public List<String> getCategories() {
        return this.categories;
    }

    public List<Budget> getBudgets() {
        return this.budgets;
    }


    /**
     * Filters this.expenses based on the provided description, minimum amount and maximum amount.
     * This method uses Java streams to perform a case-insensitive search for the description
     * , and filters expenses to include the range specified by the minAmount and maxAmount.
     *
     * @param description The description to match against the description of the Expense object
     * @param minAmount The minimum amount value of the Expense object
     * @param maxAmount The minimum amount value of the Expense object
     *
     * @return An ArrayList of Expense object containing all filtered
     *         Expense which match the provided parameters provided
     */
    public ArrayList<Expense> filterExpenses(String description, Double minAmount, Double maxAmount) {
        assert minAmount == null || maxAmount == null || minAmount <= maxAmount
                : "Minimum Amount must be smaller than or equals to Max Amount if both are not null";

        LOGGER.log(Level.INFO, "Start Filtering expenses based on description : " + description + " minAmount : "
                + minAmount + " maxAmount : " + maxAmount);

        String descriptionInLowerCase = description.toLowerCase();
        ArrayList<Expense> filteredExpenses = new ArrayList<>(this.expenses.stream()
                .filter(expense -> (expense.getDescription()
                        .toLowerCase().contains(descriptionInLowerCase)))
                .filter(expense -> (minAmount == null || expense.getAmount() > minAmount))
                .filter(expense -> (maxAmount == null || expense.getAmount() < maxAmount))
                .collect(Collectors.toList()));

        LOGGER.log(Level.INFO, "Ending filtering and returning filtered expenses");
        return filteredExpenses;

    }

    /**
     * Lists expenses based on the provided filter category.
     * If no filter category is specified, all expenses are listed.
     *
     * @param filterCategory the category by which to filter the expenses (optional)
     */
    public void listExpenses(String filterCategory) {
        LOGGER.info("Listing expenses...");

        try {
            System.out.println(String.format("Current Currency: %s\n", DefaultCurrency.getDefaultCurrency()));

            System.out.println("Expenses:");
            for (int i = 0; i < expenses.size(); i++) {
                Expense expense = expenses.get(i);

                // Checks for null expenses
                if (expense == null) {
                    LOGGER.warning("Expense object at index " + i + " is null");
                    continue;
                }

                if (filterCategory == null || expense.getCategory().equalsIgnoreCase(filterCategory)) {
                    System.out.print(i+1 + " | ");
                    System.out.print("Date: " + expense.getDateAdded() + " | ");
                    System.out.print("Category: " + expense.getCategory() + " | ");
                    System.out.print("Amount: $" + String.format("%.2f", expense.getAmount()) + " | ");
                    System.out.println("Description: " + expense.getDescription() + " | ");
                }
            }
            ui.printDivider();
            System.out.println("Overall Total Expenses: $" + String.format("%.2f", calculateTotalExpenses()));

            // Assertion: Check if total expenses calculation is correct
            double totalExpenses = calculateTotalExpenses();
            assert totalExpenses >= 0 : "Total expenses should be non-negative";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while listing expenses.", e);
        }
    }

    /**
     * Calculates the total expenses from the list of expenses.
     * Negative expense amounts are considered invalid.
     *
     * @return The total expenses.
     * @throws IllegalArgumentException If any expense amount is negative.
     */
    public double calculateTotalExpenses() {
        double totalExpenses = 0;
        try {
            for (Expense expense: expenses) {
                if (expense.getAmount() < 0) {
                    throw new IllegalArgumentException("Expenses should not be negative");
                }
                totalExpenses += expense.getAmount();
            }
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.WARNING, "Negative expense amount detected", e);
        }

        // Assertion: Check if total expenses is non-negative
        assert totalExpenses >= 0 : "Total expenses should be non-negative";

        return totalExpenses;
    }

    //@@author Zhang Yangda
    public void addExpense(String category, String amount, String description) throws BudgetBuddyException {
        assert category != null : "Category should not be null";
        assert amount != null : "Amount should not be null";
        assert description != null : "Description should not be null";

        if (!categories.contains(category)) {
            throw new BudgetBuddyException("The category '" + category + "' is not listed.");
        }
        double amountAsDouble;
        try {
            amountAsDouble = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            throw new BudgetBuddyException("Invalid amount format. Amount should be a number.");
        }

        if (amountAsDouble < 0) {
            throw new BudgetBuddyException("Expenses should not be negative.");
        }

        // Check against the budget before adding the expense
        Budget budgetForCategory = budgets.stream()
                .filter(budget -> budget.getCategory().equalsIgnoreCase(category))
                .findFirst()
                .orElse(null);

        if (budgetForCategory != null) {
            double totalSpent = expenses.stream()
                    .filter(expense -> expense.getCategory().equalsIgnoreCase(category))
                    .mapToDouble(Expense::getAmount)
                    .sum();
            double projectedTotal = totalSpent + amountAsDouble;

            if (projectedTotal > budgetForCategory.getBudget()) {
                ui.printDivider();
                System.out.println("Warning: Adding this expense will exceed your budget for " + category);
                ui.printDivider();

                // Replace with actual user confirmation in your application context
                if (!ui.getUserConfirmation()) {
                    System.out.println("Expense not added due to budget constraints.");
                    return; // Exit without adding the expense
                }
            }
        }

        Expense expense = new Expense(category, amountAsDouble, description);
        expenses.add(expense);

    }

    /**
     * Edits an expense entry in the expenses list at the specified index. Updates the category,
     * amount, and description of the expense.
     *
     * @param category    The new category to assign to the expense entry.
     * @param index       The index in the list where the expense entry is located.
     * @param amount      The new amount to assign to the expense entry.
     * @param description The new description to assign to the expense entry.
     */
    public void editExpense(String category, int index, double amount, String description) {
        LOGGER.info(String.format("Attempting to edit expense at index %d with category '%s', " +
                "amount %.2f, and description '%s'", index, category, amount, description));

        // Assert that the provided category is not null or empty
        assert category != null && !category.isEmpty() : "Category cannot be null or empty";
        // Assert that the index is within the valid bounds of the expenses list
        assert index > 0 && index <= expenses.size() : "Index is out of bounds";
        // Assert that the amount is non-negative
        assert amount >= 0 : "Amount cannot be negative";
        // Assert that the description is not null.
        assert description != null : "Description cannot be null";

        // Check if the category exists in the list of categories
        int categoryIndex = categories.indexOf(category);
        if (categoryIndex == -1) {
            LOGGER.warning("Invalid category: " + category);
            System.out.println("Invalid category.");
            return;
        }

        // Check if the index is within valid bounds
        if (index <= 0 || index > expenses.size()) {
            LOGGER.warning("Invalid index: " + index);
            System.out.println("Invalid index.");
            return;
        }

        try {
            // Retrieve the expense to edit
            Expense expenseToEdit = expenses.get(index - 1);

            // Update the expense details
            expenseToEdit.setCategory(category);
            expenseToEdit.setAmount(amount);
            expenseToEdit.setDescription(description);

            LOGGER.info("Expense at index " + index + " edited successfully. New details: " +
                    expenseToEdit.toString());
            System.out.println("Expense edited successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error editing expense at index " + index, e);
        }
    }

    public void deleteExpense(int index){
        if (index >= 0 && index < expenses.size()){
            expenses.remove(index);
            System.out.println("Expense deleted successfully!");
        } else {
            System.out.println("Invalid expense index.");
        }
    }

    public String getName() {
        return "placeholder";
    }

    public void setBudget(String category, double budget) {
        LOGGER.info("Setting budget - Category: " + category + ", Budget: $" + budget);

        for (Budget b : budgets) {
            if (b.getCategory().equalsIgnoreCase(category)) {
                LOGGER.info("Updating budget for category: " + category);
                b.setBudget(budget);
                System.out.println("Updated budget for " + category + " to $" + budget);
                return;
            }
        }

        LOGGER.info("Creating new budget for category: " + category);
        budgets.add(new Budget(category, budget));
        System.out.println("Budget Added: " + category + " of $" + budget);
    }

    /**
     * Retrieves and prints the budget for a specified category and lists all the expenses under that category.
     * The expenses are sorted from the highest to the lowest amount, displaying the amount and what percentage
     * of the total budget each expense constitutes.
     *
     * @param inputCategory The category for which to retrieve and print the budget and expenses.
     */
    public void getBudgetAndListExpensesForCategory(String inputCategory) {
        // Trim the input and replace multiple internal spaces with a single space
        String normalizedCategory = inputCategory.trim().replaceAll("\\s+", " ");

        Budget budgetForCategory = budgets.stream()
                .filter(budget -> budget.getCategory().equalsIgnoreCase(normalizedCategory))
                .findFirst()
                .orElse(null);

        if (budgetForCategory == null) {
            System.out.println("No budget set for " + normalizedCategory);
            return;
        }

        double budgetAmount = budgetForCategory.getBudget();
        System.out.println("Budget for " + normalizedCategory + ": $" + budgetAmount);

        List<Expense> expensesForCategory = expenses.stream()
                .filter(expense -> expense.getCategory().equalsIgnoreCase(normalizedCategory))
                .sorted(Comparator.comparingDouble(Expense::getAmount).reversed())
                .collect(Collectors.toList());

        if (expensesForCategory.isEmpty()) {
            System.out.println("No expenses recorded for " + normalizedCategory);
            return;
        }

        System.out.printf("%-20s | %-15s | %-15s%n", "Expense", "Amount", "% of Budget");
        ui.printDivider();

        for (Expense expense : expensesForCategory) {
            double amount = expense.getAmount();
            double percentOfBudget = (amount / budgetAmount) * 100;
            System.out.printf("%-20s | $%-14.2f | %-14.2f%%%n", expense.getDescription(), amount, percentOfBudget);
            ui.printDivider();
        }
    }


    /**
     * Calculates and prints a distribution of expenses in various categories as a horizontal bar graph.
     * It also identifies and prints the categories with the highest and lowest expenses,
     * as well as categories where no expenses have been added.
     */
    public void getExpenseInsights() {
        double totalExpenses = calculateTotalExpenses();
        if (totalExpenses == 0) {
            System.out.println("No expenses to display.");
            return;
        }

        Map<String, Double> sumsByCategory = new HashMap<>();
        for (Expense expense : expenses) {
            sumsByCategory.merge(expense.getCategory(), expense.getAmount(), Double::sum);
        }

        // Calculate the highest expense amount
        double highestExpense = Collections.max(sumsByCategory.values());

        // Identify the categories with the highest expenses
        List<String> highestCategories = sumsByCategory.entrySet().stream()
                .filter(entry -> entry.getValue().equals(highestExpense))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Calculate the lowest expense amount excluding the highest if it's the only value
        double lowestExpense = sumsByCategory.entrySet().stream()
                .filter(entry -> entry.getValue() > 0 && !highestCategories.contains(entry.getKey()))
                .mapToDouble(Map.Entry::getValue)
                .min().orElse(Double.MAX_VALUE);

        // Identify the categories with the lowest expenses
        List<String> lowestCategories = sumsByCategory.entrySet().stream()
                .filter(entry -> entry.getValue() > 0 && entry.getValue().equals(lowestExpense))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // If lowestExpense is Double.MAX_VALUE, then this list should be empty
        if (lowestExpense == Double.MAX_VALUE) {
            lowestCategories.clear();
        }

        // Identify categories with no expenses
        List<String> noExpenseCategories = categories.stream()
                .filter(category -> !sumsByCategory.containsKey(category) || sumsByCategory.get(category) == 0)
                .collect(Collectors.toList());

        ui.printDivider();
        printExpensesDistribution(sumsByCategory, totalExpenses);
        ui.printDivider();

        System.out.println("Highest Expense Category: " + formatCategoryList(highestCategories));
        System.out.println("Lowest Expense Category: " + formatCategoryList(lowestCategories));
        System.out.println("Categories with no expenses added: " + formatCategoryList(noExpenseCategories));
        ui.printDivider();
    }

    /**
     * Prints the distribution of expenses in a bar graph format.
     * Each category's expenses are represented by a percentage and a visual bar made of hashes ('#').
     *
     * @param sumsByCategory A map containing the sum of expenses for each category.
     * @param totalExpenses  The total amount of expenses, used to calculate the percentage for each category.
     */
    private void printExpensesDistribution(Map<String, Double> sumsByCategory, double totalExpenses) {
        // Find the maximum percentage to scale the bars
        double maxPercentage = sumsByCategory.values().stream()
                .mapToDouble(amount -> (amount / totalExpenses) * 100)
                .max()
                .orElse(100);

        // Calculate percentages and build bars
        for (String category : categories) {
            Double sum = sumsByCategory.getOrDefault(category, 0.0);
            double percentage = (sum / totalExpenses) * 100;
            int barLength = (int) (percentage / (maxPercentage / 50));
            String bar = "[" + "#".repeat(Math.max(0, barLength)) + "]";
            System.out.println(String.format("%-15s: %6.2f%% %s", category, percentage, bar));
        }
    }

    /**
     * Formats a list of categories into a string. If the list contains more than one category,
     * they are joined by commas, with "and" before the last category. If the list is empty,
     * returns "None".
     *
     * @param categories The list of category names to be formatted.
     * @return A string representing the formatted categories or "None" if the list is empty.
     */
    private String formatCategoryList(List<String> categories) {
        if (categories.isEmpty()) {
            return "None";
        } else {
            return String.join(", ", categories.subList(0, categories.size() - 1))
                    + (categories.size() > 1 ? " and " : "") + categories.get(categories.size() - 1);
        }
    }

}
