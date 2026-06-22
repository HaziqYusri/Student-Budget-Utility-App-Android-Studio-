# Student Budget Utility

Student Budget Utility is an Android budgeting app designed for international students who need a quick way to monitor their spending, remaining funds, currency display, and budget cycle.

## Main Features

- Monthly budget overview
- Quick add expenses by category
- Custom expense amounts
- Collapsible transaction history
- Delete transactions
- Persistent settings using DataStore
- Persistent transactions using DataStore
- Editable currency conversion rates
- Budget cycle countdown
- Monthly statistics screen
- Category spending breakdown
- Budget status and spending insights

## Target Users

This app is designed for international students who manage money across different currencies and need simple, at-a-glance budgeting support.

## Architecture

The project uses a structured Android architecture:

- `screens` for main app screens
- `components` for reusable Jetpack Compose UI components
- `viewmodel` for app state and business logic
- `model` for data models
- `data` for sample data and DataStore repository
- `util` for reusable calculation and formatting utilities

## Technologies Used

- Kotlin
- Jetpack Compose
- Material 3
- ViewModel
- DataStore Preferences
- Android Studio
- GitHub version control

## Current Limitations

- Currency conversion rates are manually editable placeholders.
- Transactions are saved using DataStore JSON storage rather than Room database.
- Live exchange-rate API integration is planned as a future improvement.

## Future Improvements

- Live currency exchange API
- Room database for larger transaction history
- Export monthly reports
- Charts for spending trends
- Notifications for budget warnings
- Dark/light theme setting