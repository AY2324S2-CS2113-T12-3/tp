# Jasra Zainab's Project Portfolio Page

## Project: Budget Buddy
BudgetBuddy is a streamlined finance-tracking application designed for efficient management of current and future
expenses on a single platform. BudgetBuddy simplifies expense tracking and calculations, making financial management
both quick and intuitive. With budgeting features implemented, Budget Buddy is a well-rounded financial management
application that is user-friendly.

### Summary of Contributions
Given below are my contributions to the project

#### New Feature: Added the ability to edit expenses [#26](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/26) [#37](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/37) [#46](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/46) [#58](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/58) [#59](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/59)
- **What it does:** Allows users to edit expenses that have already been added. Users have to specify the index of the
  expense they want to edit, and they can edit the Category, Amount and Description of the expense.
- **Justification:** This feature enables to users to correct any mistakes they may have made while adding an expense.
  Thus, improving the apps accuracy in managing finances since users are able to update the most correct information.

#### New Feature: Added the ability to edit savings [#26](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/26) [#37](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/37) [#46](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/46) [#58](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/58) [#59](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/59)
- **What it does:** Allows users to edit savings that have already been added. Users have to category of the saving
  they want to edit, and they can edit the amount of the saving by specifying the amount they wish to edit it to.
- **Justification:** This feature enables to users to correct any mistakes they may have made while adding a saving.
  Thus, improving the apps accuracy in managing finances since users are able to update the most correct information.

#### New Feature: Added the ability to save and load expenses and savings [#65](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/65) [#215](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/215) 
- **What it does:** This feature allows users to save the state of their financial data such as their expenses and
  savings added and load them back at their convenience. Information such as the Index, Category, Amount and Description
  will be saved. This ensures continuity in financial tracking even after closing the application.
- **Justification:** This enhancement is critical for maintaining the integrity of financial records over multiple
  sessions. Users can confidently close BudgetBuddy, knowing they can pick up exactly where they left off, making
  financial management more seamless and user-friendly.

#### New Feature: Added the ability to get graphical insights for Expenses and Savings [#111](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/111)
- **What it does:** This feature presents a visual representation of users' financial data, displaying expenses and
  savings as horizontal bar graphs. It gives users a quick and clear picture of their spending and saving patterns,
  enabling them to identify the largest and smallest categories at a glance.
- **Justification:** The visual summary of expenses and savings helps users better understand their financial habits
  without delving into the details. The ability to see which categories take up most of their budget and where they are
  saving effectively can be crucial for making informed decisions about financial planning. This addition enhances the
  user experience by providing a more interactive and engaging way to engage with their financial data.
- **Highlights:** The feature includes a neat, aligned graphical output where each category is represented by a
  proportional bar filled with hash symbols (#). It also includes key insights like the highest and lowest expense and
  saving categories, as well as any categories that haven't been added to, ensuring users have a comprehensive view of
  their financial status.
- **Usage:** Users can access this graphical summary by entering specific commands to retrieve insights on their
  expenses or savings. The system will then calculate and display the information in an easy-to-read bar graph format
  within the command line interface, eliminating the need for external tools or visualisation software.

#### Code Contributed
[RepoSense Link](https://nus-cs2113-ay2324s2.github.io/tp-dashboard/?search=jasraa&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2024-02-23&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other)

#### Enhancements to existing features
1. Wrote Junit tests for Edit Expenses, Edit Savings, Get Expenses Insight and Get Savings Insights
2. Implemented Bug fixes for "Edit Expenses", "Edit Savings", "Storage", "Get Expenses Insights" and
   "Get Savings Insights"

#### Contributions to the UG
Added documentation for the features `edit expense`, `edit savings`, `get expenses insight`
and `get savings insights` [#48](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/48) [#112](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/112)

#### Contributions to the DG
Added diagrams and documentation for the features `edit expense`, `edit savings`, `get expenses insight`
and `get savings insights` [#78](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/78) [#223](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/223) [#226](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/226)

#### Community
1. Communicated with teammates for ideation and enhancement of existing features.
2. Added reviews to teammate's Pull Request [#217](https://github.com/AY2324S2-CS2113-T12-3/tp/pull/217) to help them improve their program and features.
2. Provided DG Peer Review Comments for another team. [CS2113-T15-3 SplitLiang](https://github.com/nus-cs2113-AY2324S2/tp/pull/47)
3. Reported bugs for another team during PE-D. [CS2113-T15-1 LongAh](https://github.com/AY2324S2-CS2113-T15-1/tp/releases)