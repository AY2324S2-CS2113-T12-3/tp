package seedu.budgetbuddy.commandcreator;

import seedu.budgetbuddy.commons.SavingList;
import seedu.budgetbuddy.command.Command;
import seedu.budgetbuddy.command.ReduceSavingCommand;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReduceSavingCommandCreator extends CommandCreator{
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private SavingList savings;
    private String input;

    public ReduceSavingCommandCreator(SavingList savings, String input){
        this.savings = savings;
        this.input = input;
    }

    public Command handleReduceSavingCommand(SavingList savings, String input) {
        LOGGER.log(Level.INFO, "Processing handleReduceSavingCommand");

        assert savings != null : "Savings list cannot be null";
        assert input != null : "Input string cannot be null";

        String description = input.replace("reduce", "").trim();

        // Regular expressions to identify the flags and extract the corresponding values
        Pattern indexPattern = Pattern.compile("i/\\s*(\\d+)\\s*");
        Pattern amountPattern = Pattern.compile("a/\\s*(-?\\d+(\\.\\d+)?)\\s*");

        Matcher indexMatcher = indexPattern.matcher(description);
        Matcher amountMatcher = amountPattern.matcher(description);

        if(indexMatcher.find() && amountMatcher.find()) {
            try {
                String indexToReduceAsString = indexMatcher.group(1);
                String amountToReduceAsString = amountMatcher.group(1);

                int indexToReduce = Integer.parseInt(indexToReduceAsString) - 1;
                double amountToReduce = Double.parseDouble(amountToReduceAsString);

                // Validate the index range and that the amount is positive.
                if (indexToReduce < 0 || indexToReduce >= savings.size()) {
                    LOGGER.log(Level.WARNING, "Index is out of bounds.");
                    System.out.println("Error: Index is out of bounds.");
                    return null;
                }
                if (amountToReduce <= 0) {
                    LOGGER.log(Level.WARNING, "Amount must be a positive value.");
                    System.out.println("Error: Amount must be a positive value.");
                    return null;
                }

                LOGGER.log(Level.INFO, "Successfully processed ReduceSavingCommand!");
                return new ReduceSavingCommand(savings, indexToReduce, amountToReduce);
            } catch (NumberFormatException e){
                LOGGER.log(Level.SEVERE, "Index and amount must be valid numbers.");
                // Catch and handle incorrect number formats for index or amount.
                System.out.println("Error: Index and amount must be valid numbers.");
                return null;
            }
        } else {
            LOGGER.log(Level.WARNING, "Invalid command format. Expected format: reduce i/<index> a/<amount>");
            // Handle the case where the input does not contain the required markers.
            System.out.println("Error: Invalid command format. Expected format: reduce i/<index> a/<amount>");
            return null;
        }
    }

    @Override
    public Command createCommand(){
        return handleReduceSavingCommand(savings, input);
    }

}
