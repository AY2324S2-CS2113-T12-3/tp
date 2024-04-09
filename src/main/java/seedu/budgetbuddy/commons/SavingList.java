package seedu.budgetbuddy.commons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import java.util.stream.Collectors;

import seedu.budgetbuddy.Ui;
import seedu.budgetbuddy.exception.BudgetBuddyException;

public class SavingList {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    protected ArrayList<Saving> savings;
    protected ArrayList<String> categories;
    protected double initialAmount;
    Ui ui = new Ui();



    public SavingList() {
        this.savings = new ArrayList<>();
        this.categories = new ArrayList<>(Arrays.asList("Salary",
                "Investments", "Gifts", "Others"));
        this.initialAmount = 0;
    }

    public int size() {
        return savings.size();
    }

    public double getInitialAmount() {
        return this.initialAmount;
    }

    public ArrayList<Saving> getSavings() {
        return savings;
    }

    /**
     * Calculates the total savings amount by summing up the amounts of all savings.
     * Sets the initial amount to the calculated total savings.
     * Logs a severe error if an AssertionError occurs during the calculation.
     */
    public void findTotalSavings() {
        try {
            assert savings != null : "Savings list should not be null";

            double totalSavings = 0;
            for (int i = 0; i < savings.size(); i++) {
                Saving saving = savings.get(i);
                assert saving != null : "Saving object at index " + i + " is null";
                totalSavings += saving.getAmount();
            }

            this.initialAmount = totalSavings;
        } catch (AssertionError e) {
            LOGGER.log(Level.SEVERE, "Error occurred while calculating total savings", e);
        }
    }

    /**
     * Lists the savings, optionally filtered by category,
     * and calculates the remaining savings after deducting expenses.
     * Prints the initial savings amount, expenses deducted, and the remaining amount.
     *
     * @param filterCategory The category to filter savings by (optional). If null, all savings are listed.
     * @param expenseList    The ExpenseList object containing the expenses to deduct from savings.
     */
    public void listSavings(String filterCategory, ExpenseList expenseList) {
        LOGGER.info("Listing savings...");
        findTotalSavings();

        try {
            System.out.println(String.format("Current Currency: %s", DefaultCurrency.getDefaultCurrency()));
            System.out.println("Savings:");
            for (int i = 0; i < savings.size(); i++) {
                Saving saving = savings.get(i);
                if (filterCategory == null || saving.getCategory().equalsIgnoreCase(filterCategory)) {
                    System.out.print(i + 1 + " | ");
                    System.out.print("Category: " + saving.getCategory() + " | ");
                    System.out.println("Amount: $" + String.format("%.2f", saving.getAmount()) + " | ");
                }
            }
            ui.printDivider();
            System.out.println("Initial Savings Amount: $" + String.format("%.2f", initialAmount));
            System.out.println("Expenses Deducted: ");

            double totalExpenses = 0;
            for (Expense expense : expenseList.getExpenses()) {
                totalExpenses += expense.getAmount();
                System.out.println("$" + String.format("%.2f", expense.getAmount()) +
                        " spent on " + expense.getDescription() +
                        " on " + expense.getDateAdded());
            }
            ui.printDivider();

            double remainingAmount = calculateRemainingSavings(initialAmount, totalExpenses);
            if (remainingAmount < 0) {
                System.out.println("You are currently short on savings by: $" + String.format("%.2f", remainingAmount));
            } else {
                System.out.println("Overall Remaining Amount: $" + String.format("%.2f", remainingAmount));

            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while listing savings", e);
        }
    }

    /**
     * Calculates the remaining savings amount after deducting total expenses from the initial amount.
     *
     * @param initialAmount The initial amount of savings.
     * @param totalExpenses The total amount of expenses to be deducted.
     * @return The remaining savings amount after deducting total expenses.
     */
    public double calculateRemainingSavings(double initialAmount, double totalExpenses) {
        try {
            assert initialAmount >= 0 : "Initial amount should not be negative";
            assert totalExpenses >= 0 : "Total expenses should not be negative";

            return (initialAmount - totalExpenses);
        } catch (AssertionError e) {
            LOGGER.log(Level.SEVERE, "Assertion failed while calculating remaining savings", e);
        }
        return -1;
    }

    public void addSaving(String category, String amount) throws BudgetBuddyException{
        if (!categories.contains(category)) {
            throw new BudgetBuddyException("The category '" + category + "' is not listed.");
        }
        int amountInt = Integer.parseInt(amount);
        if (amountInt < 0) {
            try {
                throw new Exception("Savings should not be negative");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Saving saving = new Saving(category, amountInt);
        savings.add(saving);

        if (!categories.contains(category)) {
            categories.add(category);
        }
    }

    /**
     * Edits the saving entry at the specified index. This method updates the category and amount
     * of a saving object within the savings list. If the provided category doesn't exist or the index
     * is out of the valid range, it logs a warning and prints an error message without making changes.
     *
     * @param category The new category to which the saving entry will be updated.
     * @param index    The index of the saving entry in the list to be edited.
     * @param amount   The new amount of the saving entry.
     */
    public void editSaving(String category, int index, double amount) {
        LOGGER.info(String.format("Attempting to edit saving at index %d with category '%s' " +
                "and amount %.2f", index, category, amount));

        // Assert that the provided category is not null or empty
        assert category != null && !category.isEmpty() : "Category cannot be null or empty";

        // Assert that the index is within the valid bounds of the savings list
        assert index > 0 && index <= savings.size() : "Index is out of bounds";

        // Assert that the amount is non-negative
        assert amount >= 0 : "Amount cannot be negative";

        // Check if the category exists in the list of categories
        int categoryIndex = categories.indexOf(category);
        if (categoryIndex == -1) {
            LOGGER.warning("Invalid category: " + category);
            System.out.println("Invalid category.");
            return;
        }

        // Check if the index is within valid bounds
        if (index <= 0 || index > savings.size()) {
            LOGGER.warning(String.format("Invalid index: %d. Valid index range " +
                    "is 1 to %d.", index, savings.size()));
            System.out.println("Invalid index.");
            return;
        }

        Saving savingToEdit = null;
        try {
            // Retrieve the saving to edit
            savingToEdit = savings.get(index - 1);

            // Update the saving details
            savingToEdit.setCategory(category);
            savingToEdit.setAmount(amount);

            System.out.println("Saving edited successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error occurred while editing saving at index " + index, e);
            System.out.println("An error occurred during saving edition. Please try again.");

        }
    }

    // @@author Dheekshitha2
    public void reduceSavings(int index, double amount) {

        if (index >= 0 && index < savings.size()) {
            Saving saving = savings.get(index);
            if (saving.getAmount() >= amount) {
                saving.setAmount(saving.getAmount() - amount);
                System.out.println("Savings reduced successfully!");
            } else {
                System.out.println("Insufficient savings amount.");
            }
        } else {
            System.out.println("Invalid saving index.");
        }
    }

    /**
     * Analyzes and displays insights into the saved amounts across different categories.
     * It prints out the highest and lowest savings categories and lists categories with no savings.
     * A bar graph representing the distribution of savings is also displayed.
     */
    public void getSavingsInsights() {
        findTotalSavings(); // Make sure total savings are updated

        if (initialAmount == 0) {
            System.out.println("No savings to display.");
            return;
        }

        printSavingsDistribution();

        // Calculate the highest savings value
        double highestSavings = savings.stream()
                .mapToDouble(Saving::getAmount)
                .max().orElse(0);

        // Identify the categories with the highest savings
        List<String> highestCategories = savings.stream()
                .filter(s -> s.getAmount() == highestSavings)
                .map(Saving::getCategory)
                .collect(Collectors.toList());

        // Calculate the lowest savings value excluding the highest if it's the only value
        double lowestSavings = savings.stream()
                .filter(s -> !highestCategories.contains(s.getCategory()))
                .mapToDouble(Saving::getAmount)
                .min().orElse(0);

        // Identify the categories with the lowest savings, excluding those with no savings
        List<String> lowestCategories = savings.stream()
                .filter(s -> s.getAmount() == lowestSavings && lowestSavings != 0)
                .map(Saving::getCategory)
                .collect(Collectors.toList());

        // If lowestSavings is 0, then this list should be empty
        if (lowestSavings == 0) {
            lowestCategories.clear();
        }

        // Identify categories with no savings
        List<String> noSavingsCategories = categories.stream()
                .filter(c -> savings.stream().noneMatch(s -> s.getCategory().equals(c)))
                .collect(Collectors.toList());

        // Add categories with zero amount saved
        noSavingsCategories.addAll(savings.stream()
                .filter(s -> s.getAmount() == 0)
                .map(Saving::getCategory)
                .collect(Collectors.toList()));

        ui.printDivider();
        System.out.println("Highest Savings Category: " + formatCategoryList(highestCategories));
        System.out.println("Lowest Savings Category: " + formatCategoryList(lowestCategories));
        System.out.println("Categories with no savings added: " + formatCategoryList(noSavingsCategories));
        ui.printDivider();
    }

    /**
     * Calculates the sum of savings for each category.
     *
     * @return A map with the category as the key and the sum of savings in that category as the value.
     */
    private Map<String, Double> calculateSumsByCategory() {
        return savings.stream().collect(
                Collectors.groupingBy(
                        Saving::getCategory,
                        Collectors.summingDouble(Saving::getAmount)
                )
        );
    }

    /**
     * Formats a list of category names into a human-readable string with categories separated by commas.
     * The word "and" is inserted before the last category if there are two or more.
     * Returns "None" if the list is empty.
     *
     * @param categories The list of categories to format.
     * @return A formatted string of category names or "None" if the list is empty.
     */
    private String formatCategoryList(List<String> categories) {
        if (categories.isEmpty()) {
            return "None";
        } else {
            return String.join(", ", categories.subList(0, categories.size() - 1))
                    + (categories.size() > 1 ? " and " : "") + categories.get(categories.size() - 1);
        }
    }

    /**
     * Prints a distribution of savings as a horizontal bar graph.
     * Each category's bar length is proportional to its percentage of the total savings.
     */
    private void printSavingsDistribution() {
        Map<String, Double> sumsByCategory = calculateSumsByCategory();
        double totalSavings = sumsByCategory.values().stream().mapToDouble(Double::doubleValue).sum();

        for (String category : categories) {
            Double sum = sumsByCategory.getOrDefault(category, 0.0);
            double percentage = (sum / totalSavings) * 100;
            int barLength = (int) (percentage / (100.0 / 50)); // Assuming a bar max length of 50 characters
            String bar = "[" + "#".repeat(Math.max(0, barLength)) + "]";
            System.out.println(String.format("%-15s: %6.2f%% %s", category, percentage, bar));
        }
    }

}
