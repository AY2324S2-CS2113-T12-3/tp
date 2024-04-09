# Developer Guide


## Acknowledgements

{list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

## Design & implementation

### Budget Management

#### Implementation
The Budget Management feature allows users to set financial limits for the various categories and monitor their spending. 
This feature's objective is to give users the ability to stay within their financial goals and avoid overspending.

This feature is orchestrated by `ListBudgetCommand` and `SetBudgetCommand`, which are initialised by the `Parser` 
class. Below is a description of the key class attributes and methods involved in the budget setting and listing 
process:

##### Class Attributes for `SetBudgetCommand`:
| Class Attribute | Variable Type | Relevance                                                           |
|-----------------|---------------|---------------------------------------------------------------------|
| expenseList     | ExpenseList   | Object containing the list of expenses to check against set budgets |
| category        | String        | The category for which the budget is being set                      |
| budget          | double        | The budget amount to be set for the category                        |

##### Class Attributes for `ListBudgetCommand`:
| Class Attribute | Variable Type | Relevance                                                           |
|-----------------|---------------|---------------------------------------------------------------------|
| expenseList     | ExpenseList   | Object containing the list of expenses to check against set budgets |


Upon the call of the `execute()` method in `BudgetBuddy` using `command.execute()`, `SetBudgetCommand` will update the 
budget in `ExpenseList` using `setBudget`. Similarly, `ListBudgetCommand` will fetch and display all categories with 
their budgets using `getBudgets`, and highlight those that are above the set budget.

##### Key Methods used from `ExpenseList`
| Method                      | Return Type   | Relevance                                                          |
|-----------------------------|---------------|--------------------------------------------------------------------|
| setBudget(category, budget) | void          | Sets or updates the budget for a given category in the ExpenseList |
| getBudgets()                | List<Budget>  | Retrieves the list of all budgets set                              |

The `ListBudgetCommand`'s updated execution function now features an improved display that not only shows the budget, 
spent amount, and remaining balance but also clearly indicates when the budget has been exceeded. If the expenses 
surpass the budget, instead of showing a negative remaining balance, it displays "Exceeded", providing a straightforward
and immediate visual cue that the budget limits have been surpassed.

The "Categories above budget" section offers a concise table summarizing which categories have gone over the budget and
by what amount, making it easy for users to identify areas of concern.


#### Sequence diagrams

##### Setting a Budget
The following UML Sequence diagram shows how `SetBudgetCommand` works when a user sets a budget for a category in the
following format: `set budget c/<Category> b/<Amount>`

<img alt="SetBSD.png" height="400" src="SetBSD.png" width="700"/>


##### Printing Budgets
The following UML Sequence diagram shows how `ListBudgetCommand` works when a user checks the budget status with the
command: `print budget`

<img alt="ListBSD2.png" height="400" src="ListBSD2.png" width="700"/>

#### Class diagram
The class diagram below outlines the relationships between the classes involved in the Budget Management feature:

<img alt="ClassDiagram2.png" height="800" src="ClassDiagram2.png" width="700"/>

#### Activity diagram

The activity diagram provides an overview of the Budget Management feature's workflow:

<img alt="ActivityDiagram.png" height="600" src="ActivityDiagram.png" width="700"/>

#### Examples of usage

1. The user types `set budget c/food b/500` to set a budget of $500 for the food category. The Parser class creates a 
`SetBudgetCommand` object which calls `setBudget()` on the `ExpenseList` object.
2. To view budgets, the user enters `print budget`. The Parser class creates a `ListBudgetCommand` object. This command 
retrieves the budgets using `getBudgets()` and displays them, also indicating any categories that are over budget.

## 1. Introduction
Welcome to the Developer Guide for BudgetBuddy! This guide has been created to help you current and future 
developers of Budget understand how BudgetBuddy works and aid developers in easily adding new features, 
fix bugs. In this guide, it will go over the main parts of the app, how they work together, 
and why we made them that way.

## 2. Setup Guide
This section describes how to set up the coding environment, along with the tools needed to work on BudgetBuddy

### 2.1. Prerequisites
1. JDK 11
2. IntelliJ IDEA

## 3.Design

### 3.1 Architecture
The following diagram provides a rough overview of how BudgetBuddy is built

![Diagram of overview of BudgetBuddy](diagrams/Introduction.jpg)

`BudgetBuddy` is the main class of the application and directly interacts with the user. `BudgetBuddy` 
passes along the input into the Parser. The `Parser` creates a `CommandCreator` object depending on the user's input
. The `CommandCreator` object then creates the `Command` object. 
This `Command` object will be executed in `BudgetBuddy`. The `Command` object 
utilizes methods and the classes present in `Commons`, which will be explained in more 
detail in the following sections.

#### 3.2 Parser Class
The main functionality of the Parser Class is to determine the type of `CommandCreator` object to initialize. Using
Boolean Functions, the Parser Class determines this by what the user input starts with. 
After determining the type of `CommandCreator` object, the Parser initializes the `CommandCreator` object
with all its required parameters. 

Here are some examples :

| Boolean Method        | Checks if input starts with | Feature Requires   | Creates                                    |
|-----------------------|-----------------------------|--------------------|--------------------------------------------|
| isAddExpenseCommand() | add expense                 | input, ExpenseList | AddExpenseCommandCreator(input, expenses)  |
| isEditSavingCommand() | edit expense                | input, SavingList  | EditSavingsCommandCreator(input, savings)  |

#### 3.3 Ui Class
The Ui Class is used to print certain elements to the CLI. In particular, it consists of the Welcome Message,
Goodbye Message, Divider Lines and all the corresponding commands' command format.

#### 3.5 CommandCreator Class
The CommandCreator class has multiple subclasses, which corresponds to a specific function of the application.
Within the CommandCreator classes, it handles making sense of the user input, obtaining the relevant parameters, and finally
creating the `Command` class.

The superclass `CommandCreator` is an abstract class which is never instantiated. Where its createCommand() method is
overridden by its subclasses.

The association between the `Command` and `CommandCreator` can be seen in their names. E.g. `MenuCommandCreator`, would
create a `MenuCommand` class when its createCommand() method is called. Similarly, `FindExpensesCommandCreator` would
create a `FindCommand` class when its createCommand() method is called.

#### 3.4 Command Class
The Command class, similar to the CommandCreator class, contains multiple subclasses, all corresponding to a specific
function of the application. Stated in section`3.5 CommandCreator Class`, each subclass of the `Command` Object
is created by its associated `CommandCreator`. 

The superclass `Command` is an abstract class which is never instantiated. Where its execute() method is overridden
by its subclasses. What each Command class does when its execute() method is called would be discussed in more detail
in the Implementation section.

The following UML Sequence Diagram depicts the process of the process through which an input is gone through the application, up till the point
where the command gets executed :
![UML Sequence Diagram of Command](diagrams/sequence_diagram_command.jpg)

#### 3.5 Storage Class
The Storage Class handles the loading and saving of the features in BudgetBuddy. Different features are saved in
different files corresponding to their data type. 

### 3.6 Commons
The classes present in this group of `Commons` refers to a collection of classes used by multiple other components
. They represent data of the user's financial transactions, including expenses and savings, along with methods 
for organizing and managing this data.

##### 3.6.1 Transaction
This is an abstract class, which is the superclass for both the Expense and Saving Classes. It contains common variables
such as Currency, Category and Amount.

##### 3.6.2 Expense
This class holds details regarding an expense a user has. Within this class, it has 4 class-level variables :
`String category`, `LocalDate dateAdded` , `String description` and `Double amount`.

`String category` : This variable holds the category of the expense. It represents the type or classification 
of the expense as per the pre-defined categories ("Housing", "Groceries", "Utility", "Transport", "Entertainment").

`LocalDate dateAdded` : This variable holds the date when the expense was added or recorded. 
It is of type LocalDate, representing a date without a time zone. 
Storing the date of the expense allows users to track when each expense occurred, 
facilitating budget management and analysis over time.

`String description` : This variable holds a description of the expense. 
It provides additional details or information about the expense, 
such as what the expense was for or any relevant notes. 
Descriptions help users understand the context or purpose of each expense entry.

`Double amount` :  This variable holds the monetary amount of the expense. 
It represents the cost or value of the expense, typically in the currency used by the user. 
Storing the amount allows users to track how much money was spent on each expense, 
aiding in budgeting and financial planning.

##### 3.6.3 ExpenseList
This class represents a list of expenses. Within this class, it has 2 class-level variables :
`ArrayList<Expense> expenses` and `ArrayList<String> categories`, The variables and there relevance are as follows :

`ArrayList<Expense> expenses` : This variable holds a list of `Expense` objects. 
Each `Expense` object represents an expense incurred by the user. The list stores all the expenses entered by the user. 
Managing expenses in a list allows for easy retrieval, modification, and deletion of individual expenses. 
Additionally, it enables functionalities such as filtering, listing, and calculating total expenses.

`ArrayList<String> categories` : This variable holds a list of predefined expense categories. 
Each category represents a classification or grouping for expenses, such as "Housing," "Groceries," "Utility," etc. 
The list provides predefined options for users to select when adding or editing expenses. 
It helps organize expenses into meaningful groups, 
allowing users to track and analyze their spending habits across different expense categories.

This class also contains the methods to handle any user interactions with the list of expenses. The methods and a
brief explanation on their functionality is as follows :

* listExpenses(String filterCategory) :
  * The core functionality of this class. It lists a user's total expenses, optionally filtered by category as per user
  input.
  * The amounts being shown are dependent on the currency being used.
* calculateTotalExpenses() :
  * The method used to calculate all expenses found in the expense list.

##### 3.6.4 Saving
This class holds details regarding a saving a user has. Within this class, it has 3 class-level variables :
`String category`, `LocalDate dateAdded`, `Double amount`. The variables and their relevance
are as follows :

`String Category` : This variable holds the category of the saving. 
Similar to expenses, savings can also be categorized based on their purpose or intended use. 
Pre-defined categories include ("Salary", "Investments", "Gifts", or "Others"). 
Categorizing savings helps users allocate funds for different financial goals and track progress towards those goals.

`LocalDate dateAdded` : This variable holds the date when the saving was added or recorded. 
As with expenses, tracking the date of each saving allows users to monitor their saving habits over time. 
It provides a historical record of when savings were initiated, 
helping users understand their saving patterns and behaviors.

`Double amount` : This variable holds the monetary amount of the saving. 
It represents the value or sum of money saved by the user. 
The amount indicates how much money has been set aside or accumulated towards achieving a particular financial goal. 
Users can track their progress towards savings targets and 
monitor their overall financial health based on the amount saved.

##### 3.6.5 SavingList
This class represents a list of savings. Within this class, it has 2 class-level variables :
`ArrayList<Saving> savings` and `ArrayList<String> categories`, The variables and there relevance are as follows :

`ArrayList<Saving> savings`: This variable holds a list of `Saving` objects, 
where each object represents a saving made by the user. The list stores all the savings entered by the user. 
Managing savings in a list allows for easy retrieval, modification, and reduction of individual savings. 
Additionally, it enables functionalities such as listing savings, calculating remaining savings after 
deducting expenses, and adding new savings.

`ArrayList<String> categories`: This variable holds a list of predefined saving categories. 
Each category represents a classification or grouping for savings, such as "Salary," "Investments," "Gifts," etc. 
The list provides predefined options for users to select when adding or editing savings. 
It helps organize savings into meaningful groups, allowing users to track and 
manage their savings across different categories.

This class also contains the methods to handle any user interactions with the list of savings. The methods and a
brief explanation on their functionality is as follows :

* `listSavings(String filterCategory, ExpenseList expenseList)` :
  * The core functionality of this class. It prints the initial savings amount, expenses deducted, and the remaining 
  amount. 
  * It is able to print only the filtered category as per user input.
  * The amounts being shown is dependent on the currency being used.
* `findTotalSavings()` :
  * Calculates the total savings amount by summing up the amounts of all savings.
* `calculateRemainingSavings(double initialAmount, double totalExpenses)` :
  * Calculates the remaining savings amount after deducting total expenses from the initial amount.
  * Provides clarity on how much savings user has left to spend.

##### 3.6.6 RecurringExpenseList
This class represents a list of recurring expenses for the Recurring Expense feature. Within this class, it has 
1 class-level variable : `String name`. Which is used to store the name of the list. Given that its overall 
functionality is similar to ExpenseList class, it inherits the ExpenseList class.

##### 3.6.7 RecurringExpensesList
This class represents the list of all lists of recurring expenses for the Recurring Expense feature. Within this class,
it has only 1 class-level variable : `ArrayList<ExpenseList> recurringExpenses`. Which is used to store a list of
ExpenseList objects. This class contains all methods required for the overall Recurring Expense feature to work. 
The implementation of these methods would be discussed in further detail in the **Implementation** section.

For clarity, the follow Class Diagram depicts the associations between RecurringExpensesList, RecurringExpenseList and
ExpenseList.

![Class Diagram](diagrams/RecurringExpensesListClassDiagram.jpg)


##### 3.6.8 DefaultCurrency

##### 3.6.9 CurrencyConverter
The `CurrencyConverter` class provides functionality for converting amounts between different currencies. It includes two class-level variables:

`Map<Currency, Double> exchangeRates`: This variable represents a map where the keys are instances of 
the `Currency` class, and the values are conversion rates as `Double` values. 
The map stores exchange rates for various currencies relative to a base currency (in this case, Singapore Dollar, SGD). 
The exchange rates are initialized with default values for common currencies such as 
USD, EUR, JPY, KRW, MYR, CNY, and HKD.

The class includes several methods to handle currency conversion tasks:

* `convertAmount(double amount, Currency fromCurrency, Currency toCurrency)`: 
This method converts an amount from one currency to another using exchange rates stored in the `exchangeRates` map. 
It takes the original amount, the currency of the original amount (`fromCurrency`), 
and the target currency (`toCurrency`) as parameters and returns the converted amount. 
The method ensures that exchange rates are available for both currencies and that they are positive numbers.

* `convertExpenseCurrency(Currency newCurrency, ExpenseList expenses)`: 
This method converts the currency of expenses in a given `ExpenseList` to a specified new currency (`newCurrency`). 
It iterates through the expenses in the list, converts each expense amount to the new currency 
using the `convertAmount` method, and updates the expense amounts and currencies accordingly.

* `convertSavingCurrency(Currency newCurrency, SavingList savings)`: Similar to `convertExpenseCurrency`, 
this method converts the currency of savings in a given `SavingList` to a specified new currency (`newCurrency`). 
It iterates through the savings in the list, converts each saving amount to the new currency using the `convertAmount` 
method, and updates the saving amounts and currencies accordingly.

These methods facilitate currency conversion tasks by handling the conversion logic, validating input parameters, 
and logging relevant messages. They provide essential functionality for managing expenses and savings in different 
currencies within the budget management application.


## 4. Implementation

### Edit Expense Feature
The Edit Expense feature allows users to edit their previously added expenses, specifically the `category`, `amount`, 
and `description`. This feature is managed by the `EditExpenseCommand` class, which is initialized by the 
`Parser` class. Within the `EditExpenseCommand` object, 5 variables would have been initialized in the `Parser` class:
an `ExpenseList` object, `category`, `index`, `amount` and `description`. The relevance of these Class Attributes in 
`EditExpenseCommand` is as follows:

| Class Attribute | Variable Type | Relevance                                                             |
|-----------------|---------------|-----------------------------------------------------------------------|
| expenses        | ExpenseList   | ExpenseList Object containing the list of expenses that can be edited |
| category        | String        | The edited category for the expense in the specified index            |
| index           | Integer       | The index of the expense to be edited from `ExpenseList`              |
| amount          | Double        | The edited amount the expense in the specified index should be        |
| description     | String        | The edited description for the expense in the specified index         |

When the `execute()` method in `BudgetBuddy` is called via `command.execute()`, the `EditExpenseCommand` Object, 
utilizes the following method from the `ExpenseList` class to edit the expense.

| Method        | Return Type | Relevance                                                                                 |
|---------------|-------------|-------------------------------------------------------------------------------------------|
| editExpense() | void        | Edits the `category`, `amount` and `description` for the expense in the specified `index` |

The following UML Sequence diagram below shows how the Edit Expense Feature Command is executed when a user
inputs a valid edit expense command:

![EditExpenseSequence.png](team/EditExpenseSequence.png)

The following is a step by step explanation of the processes that occur for an example input:
`edit expense c/Transport i/2 a/40 d/GRAB`

1. The BudgetBuddy application receives the input string `edit expense c/Transport i/2 a/40 d/GRAB` and uses 
the `Parser` to interpret it.
2. The `Parser` splits the input into parts and constructs a `EditExpenseCommand` Object with the category 
(`c/Transport`), index (`i/2`), 
amount (`a/40`), and description (`d/GRAB`).
3. `Parser` returns this called `EditExpenseCommand` Object to `BudgetBuddy`.
4. The `BudgetBuddy` application calls `execute()` on the `EditExpenseCommand` object.
5. The `EditExpenseCommand` object calls `editExpense` on the `ExpenseList` with the provided parameters. 
6. The `ExpenseList` looks up the second expense in its list 
(as lists are zero-indexed, it uses index - 1 to access the correct item), and updates this expense’s 
category to "Transport," amount to 40.0, and description to "GRAB."
7. A message "Expense edited successfully." is printed to the console.


### Listing Feature (List Savings)

The Listing Savings Feature enables users to view their savings, potentially filtered by a specific category. This functionality is orchestrated by the `ListSavingsCommand` class, which is initialized by the `ListCommandCreator` class. Within the `ListSavingsCommand` object, the `ListCommandCreator` provides it with a `SavingList` object, an `ExpenseList` object, along with an optional `filterCategory`. The relevance of these class attributes in `ListSavingsCommand` is detailed in the following table:

| Class Attribute | Variable Type | Relevance                                                                           |
|-----------------|---------------|-------------------------------------------------------------------------------------|
| savings         | SavingList    | The `SavingList` object containing the list of savings to be displayed or filtered |
| expenses        | ExpenseList   | The `ExpenseList` object containing the list of expenses                            |
| filterCategory  | String        | The category to filter the savings by, if provided                                  |

When `BudgetBuddy` invokes the `execute()` method via `command.execute()`, the `ListSavingsCommand` object uses several methods from the `SavingList` class to perform its tasks:

| Method                      | Return Type        | Relevance                                                      |
|-----------------------------|--------------------|----------------------------------------------------------------|
| getSavings()                | ArrayList<Saving>  | Retrieves the list of all savings from the `SavingList`        |
| findTotalSavings()          | void               | Calculates the total amount of savings stored in `SavingList`  |
| listSavings()               | void               | Prints the savings, filtered by `filterCategory`, to the CLI   |
| calculateRemainingSavings() | double             | Calculates the remaining amount after deducting total expenses |

The Listing Savings feature follows these steps when a user inputs a command to list savings:
1. The user inputs `list savings [optional: filterCategory]`. This input is processed by the `Parser` class in `BudgetBuddy`, which creates a `ListSavingsCommand` object with `savings` set to the current `SavingList` and `filterCategory` to the user-specified category, if any.
2. The `Parser` returns this `ListSavingsCommand` object to `BudgetBuddy`, which calls `ListSavingsCommand.execute()`.
3. `execute()` calls `SavingList.listSavings(filterCategory, expenses)`, where the `filterCategory` is applied if provided.
4. Within `listSavings()`, the `findTotalSavings()` method is called first to calculate the initial total savings amount.
5. The `listSavings()` method continues by iterating through each `Saving` and printing those that match the `filterCategory` criteria.
6. After listing, the method calculates and displays the remaining savings by calling `calculateRemainingSavings(initialAmount, totalExpenses)`, accounting for any expenses deducted.
7. If the `filterCategory` is not provided, all savings are printed, and the total initial amount and remaining savings after expenses are displayed.

#### Sequence Diagram
The UML Sequence diagram for the Listing Savings feature would illustrate the interactions between the `User`, `BudgetBuddy`, `Parser`, `ListSavingsCommand`, and `SavingList` classes, showing the method calls and returns between these objects to complete the operation.
![Sequence diagram for List Expense Feature](diagrams/SavingList_SequenceDiagram.png)

### Listing feature (List Expenses)
The Listing Expenses Feature provides users with the ability to view their expenses, which can be filtered by category. The `ListExpenseCommand` class, generated by the `ListCommandCreator`, is responsible for this feature. The class utilizes the `ExpenseList` object to access and manipulate expense records, optionally applying a filter based on the category. The significance of the `ListExpenseCommand` class's attributes is outlined below:

| Class Attribute | Variable Type | Relevance                                                                         |
|-----------------|---------------|-----------------------------------------------------------------------------------|
| expenses        | ExpenseList   | Holds the list of expenses to be filtered and listed                              |
| filterCategory  | String        | The category to filter the expenses by (null if no filtering is needed)           |

Upon invoking the `execute()` method by `BudgetBuddy` through `command.execute()`, the `ListExpenseCommand` object calls upon several methods from the `ExpenseList` class to carry out its responsibilities:

| Method                   | Return Type | Relevance                                                              |
|--------------------------|-------------|------------------------------------------------------------------------|
| listExpenses()           | void        | Prints the expenses, filtered by `filterCategory`, to the command line |
| calculateTotalExpenses() | double      | Calculates the total expenses from the list of expenses                |

Here's an overview of the process flow when a user employs the Listing Expenses feature:
1. The user types `list expenses [optional: filterCategory]`. This command is parsed by the `Parser` class within `BudgetBuddy`, which then creates a `ListExpenseCommand` with `expenses` set to the current `ExpenseList` and `filterCategory` set to any specified by the user.
2. The `Parser` returns the `ListExpenseCommand` object to `BudgetBuddy`, which calls `ListExpenseCommand.execute()`.
3. The `execute()` method invokes `ExpenseList.listExpenses(filterCategory)`. If a `filterCategory` is provided, it will filter the expenses accordingly.
4. The `listExpenses()` method in `ExpenseList` iterates over the list of expenses and prints each one that matches the filter category criteria or all expenses if no filter is provided.
5. The method concludes by displaying the total expenses calculated using `calculateTotalExpenses()`.

#### Sequence Diagram
The sequence diagram for the Listing Expenses feature would illustrate the above steps, showing the interactions between the `User`, `BudgetBuddy`, `Parser`, `ListExpensesCommand`, and `ExpenseList` classes.
![Sequence diagram for List Expense Feature](diagrams/ExpenseList_SequenceDiagram.png)


### Currency Converter feature
The Currency Converter Feature allows users to convert the currency of expenses and savings. This feature is facilitated by the `ChangeCurrencyCommand` class, initialized by the `Parser` class with `CurrencyConverter`, `ExpenseList`, and `SavingList` objects, alongside the `newCurrency` to convert to. The importance of these class attributes is as follows:

| Class Attribute   | Variable Type          | Relevance                                                   |
|-------------------|------------------------|-------------------------------------------------------------|
| currencyConverter | CurrencyConverter      | The object responsible for currency conversion calculations |
| expenseList       | ExpenseList            | Contains the expenses whose currency will be converted      |
| savingList        | SavingList             | Contains the savings whose currency will be converted       |
| newCurrency       | Currency               | The new currency to which the amounts will be converted     | 
| exchangeRates     | Map<Currency, Double>  | Stores exchange rates with currencies as keys               |

When `BudgetBuddy` calls `command.execute()`, `ChangeCurrencyCommand` employs the following methods from `CurrencyConverter` to convert the currency of all financial records:

| Method                   | Return Type | Relevance                                                                 |
|--------------------------|-------------|---------------------------------------------------------------------------|
| convertExpenseCurrency() | void        | Converts the currency of each `Expense` object to `newCurrency`           |
| convertSavingCurrency()  | void        | Converts the currency of each `Saving` object to `newCurrency`            |
| convertAmount()          | double      | Converts an amount from one currency to another using the exchange rates  |

The Currency Converter feature also includes a mechanism for managing a default currency across the application, facilitated by the `DefaultCurrency` class. This enhancement allows for seamless conversion of financial records to a user-specified default currency.

The `DefaultCurrency` class is designed to maintain and update the application-wide default currency setting. It provides static methods to get and set the default currency:

| Method               | Return Type | Relevance                                                    |
|----------------------|-------------|--------------------------------------------------------------|
| getDefaultCurrency   | Currency    | Retrieves the current default currency for the application   |
| setDefaultCurrency   | void        | Updates the default currency to a new value                  |


Here's the step-by-step process when the user uses the Currency Converter feature:
1. The user inputs `change currency [newCurrencyCode]`. `Parser` processes this input and constructs a `ChangeCurrencyCommand` object with the necessary attributes.
2. The `ChangeCurrencyCommand` object is returned to `BudgetBuddy`, which calls `ChangeCurrencyCommand.execute()`.
3. `execute()` invokes `CurrencyConverter.convertExpenseCurrency(newCurrency, expenseList)` and `CurrencyConverter.convertSavingCurrency(newCurrency, savingList)`.
4. Within the `convertExpenseCurrency` and `convertSavingCurrency` call, the amounts of `Expense` or `Saving` objects are converted to the `newCurrency` using the `convertAmount` method.
5. The `DefaultCurrency.setDefaultCurrency(newCurrency)` method is called to update the application's default currency setting to `newCurrency`.
6. The `setAmount` and `setCurrency` methods of `ExpenseList` and `SavingList` are used to update the amounts and currency codes.
7. After successful conversion of savings and expenses, the default currency of the application is updated, reflecting the new choice across BudgetBuddy.


#### Sequence Diagram
![Sequence diagram for CurrencyConverter Feature](diagrams/CurrencyConverter_SequenceDiagram.png)

### Menu Feature
  
The menu feature is designed to allow users to view the relevant command formats by inputting the relevant menu
indexes. This feature is orchestrated by the `MenuCommand` class, which is initialized by the `MenuCommandCreator` 
class. Which is in turn, created by the `Parser` class. Within the `MenuCommand` object, the 
`MenuCommandCreator` would initialize one class-level variable `index` of type `String`. The relevance of
this class-level variable in `MenuCommand` is as follows

| Variable Name | Variable Type | Relevance                                              |
|---------------|---------------|--------------------------------------------------------|
| index         | int           | Refers to the corresponding item in the displayed menu |

For Clarity, the menu items and their corresponding indexes are as follows :

| index | Menu Item               |
|-------|-------------------------|
| Empty | Displays all Menu Items |
| 1     | Manage Expenses         |
| 2     | Manage Savings          |
| 3     | View Expenses           |
| 4     | View Savings            |
| 5     | Find Expenses           |
| 6     | Split Expenses          |
| 7     | Manage Recurring Bills  |
| 8     | Change Currency         |
| 9     | Manage Budget           |
| 10    | Get Graphical Insights  | 

Upon the call of the `execute()` method in BudgetBuddy using `command.execute()`, the `MenuCommand` object
utilizes methods from the `UI` class to display the relevant menu items. The utilized methods are as follows :

| methodName          | Return Type | Relevance                           |
|---------------------|-------------|-------------------------------------|
| showMenuTitles()    | void        | Prints all Menu Items               |
| showMenuItem(INDEX) | void        | Prints commands associated at INDEX |

The following UML Sequence Diagram shows how the MenuCommandCreator for Menu Commands work, NOTING that the Parser
has already detected that the user input is a menu command and has initialized a MenuCommandCreator object:
![Sequence Diagram for MenuCommandCreator for Menu Command](diagrams/sequenceDiagram-MenuCommandCreator.jpg)

The following UML Sequence Diagram shows the processes of the MenuCommand upon the call of its execute() command:
![Sequence Diagram for Menu Command](diagrams/sequenceDiagram_MenuCommand.jpg)

Given below is an example usage scenario and how the full Menu feature works :
1. The user types `menu 1`. This input passed from `BudgetBuddy` into `Parser#parseCommands()`.
2. Within the `Parser` , it determines that the input is a menu command from `isMenuCommand()`, and creates a new
`MenuCommandCreator` object.
3. The `Parser` then calls `MenuCommandCreator#createCommand()`
4. The checks for whether the input is valid, in particular whether it is a valid integer, 
along with obtaining the value of `index` is done in `MenuCommandCreator#handleMenuCommand`
5. `MenuCommandCreator` creates a constructor for `MenuCommand` with the parameter `1`, which in turn 
also constructs a new `Ui` object
6. `MenuCommandCreator` returns this created `MenuCommand` to `Parser`, which is then returned to `BudgetBuddy`
7. `BudgetBuddy` then calls `MenuCommand#execute()`
8. `execute()` then calls `Ui#showMenuItem(1)`
9. `showMenuItem()` in `Ui` then prints all commands for `case 1` which is for `Manage Expenses`

  
### Find Feature

The Find Feature allows users to search for expenses based on a specific criteria such as description, minimum amount
and maximum amount. This feature is orchestrated by the `FindExpensesCommand` class, which is created by the `FindExpensesCommandCreator`
, which is in turn created by the `Parser`. Within the `FindExpensesCommand` object, the `FindExpensesCommandCreator` 
would have initialized it with 4 variables, an `ExpenseList` object,  along with a `description`, `minAmount` , 
`maxAmount`. The relevance of these Class Attributes in `FindExpensesCommand` is as follows : 

| Class Attribute | Variable Type | Relevance                                                                 |
|-----------------|---------------|---------------------------------------------------------------------------|
| expenses        | ExpenseList   | ExpenseList Object containing the list of expenses which will be filtered |
| description     | String        | The description to match against expenses in `expenses`                   |
| minAmount       | Double        | The minimum amount matched expenses should be                             |
| maxAmount       | Double        | The **maximum** amount matched expenses should be                         |


Upon the call of the `execute()` method in `BudgetBuddy` using `command.execute()`,
the `FindExpensesCommand` Object, utilizes the following methods from the `ExpenseList` class in order to both 
obtain a new `ExpenseList` object containing the filtered expenses, along with printing them.

| Method           | Return Type        | Relevance                                                       |
|------------------|--------------------|-----------------------------------------------------------------|
| filterExpenses() | ArrayList<Expense> | Returns an ArrayList<Expense> containing all filtered expenses  |
| listExpenses()   | void               | Prints the filtered expenses obtained from `filterExpenses()`   |

The following UML Sequence diagram below shows how FindExpensesCommandCreator works to 
obtain the relevant inputs for the Find Feature, NOTING that the Parser has already determined the input to be a find :
expenses command, and has also created the FindExpensesCommandCreator.
![Sequence Diagram for FindFeatureCommandCreator](diagrams/sequenceDiagram-FindCommandCreator.jpg)

The following is a step-by-step explanation for the processes that occur before the FindExpensesCommand is created :
1. `BudgetBuddy` calls `Parser#parseCommand(input)` with `input` being the entire user input.
E.g `find expenses d/bruno`
2. Within the `Parser`, it will have determined that the `input` is a Find Command from the `isFindCommand(input)`.
3. The `Parser` then creates a `FindExpensesCommandCreator` object, initializing it with the overall Expense List and
the provided user input
4. The `Parser` then calls `FindExpensesCommandCreator#createCommand()`.
5. `FindExpensesCommandCreator#createCommand()` then calls `FindExpensesCommandCreator#handleFindExpensesCommand()`
6. Within `handleFindExpensesCommand(input)`, the first check would be the check for the existence of any combination of 
`d/ , morethan/ and lessthan/`. If none of these combinations were found, it immediately returns `null`
7. This is then followed by a second check `checkForDuplicateParameters()`, which checks for duplicates of parameters
in the user input. It duplicates are found, similarly, it immediately returns `null`.
8. If the checks in `4.` and `5` is passed, Three variables would be initialized.

    * | Variable Name | Variable Type |                                                              
      |---------------|---------------|
      | description   | String        | 
      | minAmount     | Double        |
      | maxAmount     | Double        |
9. Depending on which parameters were present, the corresponding input would be extracted and placed into each variable
using the `FindExpensesCommandCreator#parse*()`, where `*` represents the variable name we wish to obtain.
10. Should the values of `minAmount` and `maxAmount` not be empty,  a check is done to ensure `minAmount` is less than
or equals to `maxAmount`. If this check does not pass, the function immediately returns `null`
11. Finally, `FindExpensesCommandCreator#handleFindExpensesCommand()` creates and returns a 
`FindExpensesCommand` containing the extracted description, minAmount and maxAmount
12. `FindExpensesCommandCreator#createCommand()`, which is returned to, `Parser#parseCommand()`
, which is then returned to `BudgetBuddy`

The following UML Sequence diagram below shows how the Find Feature command works when a user provides a **valid**
find expenses command upon the call of its execute() method:

![Sequence diagram for Find Feature](diagrams/SequenceDiagram_FindExpenses.jpg)

The following is an example of the processes that occur when the user uses the find expenses feature:


**Important Note** : Although d/ , morethan/ and lessthan/ are optional parameters, the optional component would mean
user has left that option empty if not in use, e.t.c `find expenses d/ morethan/ lessthan/200`. Hence, 
unused parameters are treated a null variables instead.

1. The user types `find expenses d/bruno morethan/30 lessthan/200`. This input is passed through the `Parser`
class from `BudgetBuddy`, which constructs a `FindExpenseCommandCreator` Object. The `FindExpenseCommandCreator` then
creates a `FindExpenseCommand` object with its variables initialized to  with `expenses : current overall ExpenseList`,
`description : bruno`, `minAmount : 30`, `maxAmount : 200`. , by calling `FindExpenseCommandCreator#createCommand()`.
2. `Parser` returns this created `FindExpenseCommand` Object to `BudgetBuddy` and `BudgetBuddy` calls 
`FindExpenseCommand#execute()`
3. `execute()` is called, which initializes a variable `filteredExpenses` of type `ArrayList<Expense>`.
4. `execute()`then calls `ExpenseList#filterexpenses()`, which returns the filtered expenses based on the `description`,
`minAmount` and `maxAmount` into the `filteredExpenses` variable.
5.  If `filteredExpenses` is empty, "No Matching Expenses Found" is printed and `execute` ends here.
6. If `filteredExpenses` is not empty, `execute()` then initializes a new variable `filteredExpenseList` 
of type `ExpenseList` with `filteredExpenses` initialized as the `expenses` Class attribute.
7. Finally `execute()` calls `filteredExpenseList#listexpenses()` to print filtered expenses into the CLI.

### Recurring Expenses Feature
The Recurring Expenses feature allows users to create list(s) of expenses, where each list can be added to
the overall expenses in a single command. This feature includes the creation of a list of expenses, the viewing of
all/each list of expenses and the removal of each list of expenses. All functions are orchestrated by the 
`RecurringExpenseCommand` class, which would have been created by the `RecurringExpenseCommandCreator`, which is in turn
created by the `Parser` class. When `RecurringExpenseCommand#execute()` is called by `BudgetBuddy`, it utilizes methods
present in `ExpenseList`, `RecurringExpenseList` and `RecurringExpensesList` to facilitate the relevant features.

Within the RecurringExpenseCommand, the following variables would be initialized :

| Variable        | Variable Type | Relevance                                                                      |                                                           
|-----------------|---------------|--------------------------------------------------------------------------------|
| overallExpenses | ExpenseList   | Refer to the overall Expense List storing all of User's Expenses               |
| initialListName | String        | Used as the name of the new list that will be created                          |
| commandType     | String        | Type of RecurringExpenseCommand. E.g. `newlist`, `viewlists`, ...              |
| listNumber      | int           | Refers to the List Number of a recurring expense list shown during `viewlists` |
| category        | String        | Category of the Expense to be added when using `newexpense`                    |
| amount          | Double        | Amount of Expense to be added when using `newexpense`                          |
| description     | String        | Description of Expense to be added when using `newexpense`                     |

When viewing the code, you would notice that there are 5 different constructors in `RecurringExpensesCommand`. These
constructors correspond to the different `commandTypes` present. Each constructor would initialize only the required
parameters for the specified `commandTypes`.

A switch statement in `RecurringExpensesCommand` is used, where it runs the corresponding function according to the
`commandType`. The following is the `commandType`, class-level methods used and methods utilized from other classes 
when `RecurringExpensesCommand#execute()` is called

| commandType  | Calls Method                     | Uses Methods  From                                                                                               |                                                       
|--------------|----------------------------------|------------------------------------------------------------------------------------------------------------------|
| newlist      | addNewList()                     | `RecurringExpensesList#addNewRecurringList()`                                                                    |
| viewlists    | printList()                      | `RecurringExpensesList#printAllRecurringLists()`                                                                 |
| removelist   | removeList()                     | `RecurringExpensesList#removeList()`                                                                             |
| newexpense   | addExpenseToList()               | `RecurringExpensesList#getExpenseListAtListNumber()`, `ExpenseList#addExpense()`                                 |
| addrec       | addRecurringExpensesToExpenses() | `RecurringExpensesList#getExpenseListAtListNumber()`, `ExpenseList#getExpenses()`, `AddExpenseCommand#execute()` |
| viewexpenses | printExpensesAtIndex             | `RecurringExpensesList#getExpenseListAtListNumber()` , `ExpenseList#listExpenses()`                              |                             |

From the table above, most commandTypes have a fairly straight forward process of calling a single method from the relevant classes. However,
the `addrec` commandType would be the most complicated to follow, given that it utilized 3 methods from three different classes. The following
is a UML sequence diagram to illustrate the implementation of the addRecurringExpensesToExpenses() method in `RecurringExpenseCommand`

![Sequence Diagram for addRecurringExpensesToExpenses()](diagrams/sequenceDiagram_RecurringExpenses.jpg)

The following is an example of the processes that occur when the user uses the rec addrec command :
1. The user types `rec addrec 1`. This input is passed through the `Parser`
class from `BudgetBuddy`, which constructs a `RecurringExpenseCommandCreator`
2. `RecurringExpenseCommandCreator` identifies that the command type is `addrec`, obtains all the relevant parameters,
and uses the constructor `RecurringExpenseCommand(1, expensesList, overallExpenses, addrec)`. Note that 
`expensesList` here is the overall list containing all lists of recurring expenses and `overallExpenses` is the user's
overall expenses.
3. The created `RecurringExpenseCommand` is returned to the `Parser`, which is then returned to `BudgetBuddy`.
4. `BudgetBuddy` calls `RecurringExpenseCommand#execute()`
5. In `execute()`, `RecurringExpenseCommand` identifies it needs to perform a `addrec` operation from its 
`commandType` and calls its own `addRecurringExpensesToExpenses()`
6. The first check is passed as the listNumber is a valid number. If the listNumber is invalid, an error message is printed,
and the method would have ended here.
7. The `expenseList` we wish to add into the `overallExpenses` is obtained utilizing `RecurringExpensesList#getExpenseListAtListNumber(listNumber)`
where `listNumber` is `1`.
8. Next the `ArrayList<Expense> expenses` is extracted by utilizing `ExpenseList#getExpenses()` from our extracted `expenseList`
9. Lastly, a for loop is utilized, extracting the `category`, `amount` and `description` of all the expenses present in `expenses`
and adding them one by one into the `overallExpenses`. This is done so by creating a new `AddExpenseCommand` with the relevant parameters and executing it. FOr more details regarding
this `AddExpenseCommand`, do refer to the `Implementation` section for `AddExpenseCommand`.
10. Finally, a success message is printed to the User.


### Setting Budget Feature
The Set Budget feature allows users to allocate a specific budget to various categories. This feature is managed by the
SetBudgetCommand class, which is instantiated by the SetBudgetCommandCreator as a result of the Parser class
interpretation. Within the SetBudgetCommand object, the following variables are initialized:

| Variable    | Variable Type | Relevance                                                               |                                                           
|-------------|---------------|-------------------------------------------------------------------------|
| expenseList | ExpenseList   | The ExpenseList object containing all the categories to set budgets for |
| category    | String        | The category for which the budget is to be set                          |
| budget      | double        | The financial limit allocated to the specified category                 |

When the execute() method is called via command.execute(), the SetBudgetCommand utilizes methods from the ExpenseList
class to apply the budget:

| Method      | Return Type | Relevance                                                |                                                           
|-------------|-------------|----------------------------------------------------------|
| expenseList | ExpenseList | Sets the budget for a specific category within the list  |

The UML Sequence diagram below illustrates the execution flow of the Set Budget Feature when a user inputs a valid 
command to set a budget:
![SeqDiagramBudget.png](SeqDiagramBudget.png)

The sequence of operations for an example input, `set budget c/Transport b/500`, is as follows:
1. BudgetBuddy receives the user input and utilizes the Parser to decipher it.
2. The Parser identifies the key components of the input (category and budget) and constructs a SetBudgetCommand object with the identified category (Transport) and budget (500).
3. The Parser then hands over the SetBudgetCommand object to BudgetBuddy.
4. BudgetBuddy invokes the execute() method on the SetBudgetCommand object.
5. The SetBudgetCommand object calls the setBudget() method on the ExpenseList, passing in the category and budget amount.
6. The ExpenseList updates or creates a budget allocation for the specified category with the provided amount.
7. A confirmation message is displayed in the console indicating the budget has been successfully set or updated.


## 5. Product scope

### Target user profile
This product is for users who can type fast, and wishes to handle and track their current and future
expenses on a singular platform.

### Value proposition
BudgetBuddy is faster and more efficient way to track and calculate current and future expenses if a user is able to
type fast. It also provides the ability to deal with finances on a singular platform.

## 6. User Stories

## User Stories

| Version | As a ...          | I want to ...                                                   | So that I can ...                                                |
|---------|-------------------|-----------------------------------------------------------------|------------------------------------------------------------------|
| v1.0    | user              | be able to view my expenses                                     | track my prior expenditures and plan future expenses accordingly |
| v1.0    | user              | be able to view my savings                                      | plan my budget accordingly                                       |
| v1.0    | user              | be able to view my expenses by  their relevant categories       | control my spending                                              |
| v1.0    | user              | be able to identify my largest savings category                 | allocate necessary saved funds                                   |
| v1.0    | user              | add expenses                                                    | track my spending                                                |
| v1.0    | user              | Categorise my expenses                                          | manage my finances more efficiently                              |
| v1.0    | user              | Edit or delete expenses                                         | remove any incorrectly added items                               |
| v1.0    | user              | allocate saved funds                                            | know how much I will have left after expenses                    |
| v1.0    | user              | be able to find expenses by description                         | know the expenses i have that is associated with the description |
| v1.0    | user              | be able to find expenses more than a certain amount             | know what my deemed larger expenses are                          |
| v1.0    | user              | be able to find expenses less than a certain amount             | know what my deemed lower expenses are                           |
| v1.0    | User              | See what commands i can use                                     | I know how to use the application                                |
| v2.0    | user              | Plan my budget                                                  | Avoid overspending                                               |
| v2.0    | frequent traveler | log my expenses in multiple currencies                          | accurately track my expenses across different countries          |
| v2.0    | user              | add multiple expenses at once                                   | Add common expenditures i have monthly at one shot               |
| v2.0    | user              | have multiple lists of recurring expenses                       | separate associated recurring expenses together                  |
| v2.0    | user              | view what expenses i have in each of my recurring expenses list | know what expenses i have put into each list                     |
| v2.0    | user              | remove a list from my recurring expenses list                   | remove underutilized lists or wrongly added lists                |


## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}

### Add Expense Feature

#### Implementation

The Add Expense Feature allows users to add expenses to different categories. `AddExpenseCommand` class enables this feature, 
after initialized by the `Parser` class. Within the `AddExpense` object, the `Parser` would have initialized it with
4 variables, an `ExpenseList` object,  along with a `category`, `amount` , `description`. 
The relevance of these Class Attributes in `AddExpenseCommand` is as follows : 

| Class Attribute | Variable Type | Relevance                                         |
|-----------------|---------------|---------------------------------------------------|
| expenses        | ExpenseList   | ExpenseList Object containing the list of expenses|
| category        | String        | The category that the `expense` belongs to        |
| amount          | String        | The amount spent                                  |
| description     | String        | The description of the expense                    |


Upon the call of the `execute()` method in `BudgetBuddy` using `command.execute()`,
the `AddExpenseCommand` Object utilizes the following method from the `ExpenseList` class to add it to the existing
list of `expenses` matching against the corresponding `category`.

| Method       | Return Type | Relevance                                       |
|--------------|-------------|-------------------------------------------------|
| addExpense() | void        | Add expense to the existing list of `expenses`  |

The following UML Sequence diagram shows how the Parser works to obtain the relevant inputs for the Add Expense Feature :
![Sequence Diagram for Parser for Add Expense Feature](docs\diagram\sequenceDiagram_AddExpense.png)

The following is a step-by-step explanation for the Parser for the Find Feature :
1. `BudgetBuddy` calls `Parser#parseCommand(input)` with `input` being the entire user input.
E.g `add expense c/Transport a/20 d/EZ-Link Top Up`
2. Within the `Parser`, it will have determined that the `input` is a Find Command from the `isAddExpenseCommand(input)`
function. 
3. The `Parser` then self calls the method `handleAddExpenseCommand(input)` with the `input` still being the entire
user input.
4. Within `AddExpenseCommand(input)`, the first check would be the check for the existence of any combination of 
`c/ , a/ and d/`. If none of these combinations were found, it immediately returns `null`. 
5. If the checks in `4.` is passed, Three variables would be initialized.

    * | Variable Name | Variable Type |                                                              
      |---------------|---------------|
      | category      | String        | 
      | amount     | String        |
      | description   | String        |
6. Depending on which parameters were present, the corresponding input would be extracted and placed into each variable
using the `Parser#extractDetailsForAdd(input, "parameter")`
7. Finally, `Parser#handleAddExpenseCommand()` returns a `AddExpensesCommand` to `Parser#parseCommand()`, which is
then returned to `BudgetBuddy`

### Add Savings Feature

#### Implementation

The Add Savings Feature allows users to add savings to different categories. `AddSavingCommandCreator` class intialises the `AddSavingCommand`, after initialised by the `Parser` class. Within the `AddSavings` object, the `Parser` would have initialized it with
4 variables, a `SavingList` object,  along with a `category`, `amount`. 
The relevance of these Class Attributes in `AddExpenseCommand` is as follows : 

| Class Attribute | Variable Type | Relevance                                         |
|-----------------|---------------|---------------------------------------------------|
| savings         | SavingList    | SavingList Object containing the list of savings  |
| category        | String        | The category that the `expense` belongs to        |
| amount          | String        | The amount spent                                  |

Upon the call of the `execute()` method in `BudgetBuddy` using `command.execute()`,
the `AddSavingCommand` Object utilizes the following method from the `SavingList` class to add it to the existing
list of `savings` matching against the corresponding `category`.

| Method       | Return Type | Relevance                                       |
|--------------|-------------|-------------------------------------------------|
| addSaving()  | void        | Add savings to the existing list of `savings`   |

The following UML Sequence diagram shows how the Parser works to obtain the relevant inputs for the Add Expense Feature :
![Sequence Diagram for Parser for Add Expense Feature](docs\diagram\sequenceDiagram_AddSavings.png)

The following is a step-by-step explanation for the Parser for the Find Feature :
1. `BudgetBuddy` calls `Parser#parseCommand(input)` with `input` being the entire user input.
E.g `add savings c/Allowance a/20`
2. Within the `Parser`, it will have determined that the `input` is a Find Command from the `isAddSavingsCommand(input)`
function. 
3. The `Parser` then self calls the method `handleAddExpenseCommand(input)` with the `input` still being the entire
user input.
4. Within `AddExpenseCommand(input)`, the first check would be the check for the existence of any combination of 
`c/ , and a/`. If none of these combinations were found, it immediately returns `null`. 
5. If the checks in `4.` is passed, two variables would be initialized.

    * | Variable Name | Variable Type |                                                              
      |---------------|---------------|
      | category      | String        | 
      | amount        | String        |
6. Depending on which parameters were present, the corresponding input would be extracted and placed into each variable
using the `Parser#extractDetailsForAdd(input, "parameter")`
7. Finally, `Parser#handleAddExpenseCommand()` intialises a `AddExpensesCommandCreator` which then returns `AddSavingCommand` to `Parser#parseCommand()`, which is then returned to `BudgetBuddy`.
