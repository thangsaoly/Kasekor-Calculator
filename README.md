# Kasekor Calculator (កសិករគណនា) 🌾

[![Android SDK](https://img.shields.io/badge/SDK-35%20(Android%2015)-brightgreen.svg)](https://developer.android.com/about/versions/15)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue.svg)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-green.svg)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-orange.svg)](https://developer.android.com/topic/architecture)
[![License](https://img.shields.io/badge/License-MIT-purple.svg)](LICENSE)

**Kasekor Calculator** is an offline-first Android application designed specifically for Cambodian farmers to calculate their exact crop cultivation expenses, yield predictions, and **Break-Even Market Selling Prices**.

By providing instant financial visibility without requiring an internet connection, Kasekor Calculator empowers farmers to negotiate fairly with middlemen, protect their profit margins, and avoid selling crops at a financial loss.

---

## 📸 Key Features

- 🌾 **Crop Cycle Tracking:** Manage and record costs for individual agricultural seasons (e.g., Rice, Cassava, Mango).
- 💰 **Categorized Expense Logging:** Track inputs across Seeds, Fertilizer & Pesticides, Fuel & Machinery, and Manual Labor.
- ⚖️ **Instant Break-Even Calculation:** Real-time calculation of minimum sell price per kilogram or ton.
- 📊 **Profit/Loss Negotiation Visualizer:** Test middleman offer prices with live color-coded feedback (Green for profit, Red for loss).
- 📱 **100% Offline Persistence:** Powered by local Room SQLite database for rural areas without internet coverage.
- 🇰🇭 **Khmer-First UX:** High-contrast UI, Khmer numeric & typography formatting, massive 56dp touch targets.

---

## 🏗️ Architecture & Tech Stack

The application follows Google's recommended **Modern Android Development (MAD)** standards and **Clean Architecture with MVVM**:

```
com.saoly.kasekorcalculator/
├── data/           # Data Layer (Room Entities, DAOs, Repositories)
├── domain/         # Domain Layer (Business Models, Repository Interfaces)
└── ui/             # Presentation Layer (Jetpack Compose Screens & ViewModels)
```

- **Language:** Kotlin 2.0+
- **UI Framework:** Jetpack Compose + Material 3
- **Dependency Injection:** Hilt (Dagger-Hilt)
- **Local Database:** Room Database with KSP (Kotlin Symbol Processing)
- **Async Programming:** Kotlin Coroutines & StateFlow
- **Navigation:** Jetpack Compose Navigation

---

## 📁 Repository Documentation

Comprehensive project documentation is structured across dedicated directories:

### 📖 [`/docs`](file:///home/saoly/Documents/AndroidStudioProjects/Kasekor%20Calculator/docs)
- 🏛️ [**Architecture Guide**](file:///home/saoly/Documents/AndroidStudioProjects/Kasekor%20Calculator/docs/ARCHITECTURE.md) — Layer details, data flow, Room schema, and Hilt setup.
- 📱 [**User Guide**](file:///home/saoly/Documents/AndroidStudioProjects/Kasekor%20Calculator/docs/USER_GUIDE.md) — Screen walkthroughs, UX design principles, and Khmer accessibility.
- 🧮 [**Calculation Logic**](file:///home/saoly/Documents/AndroidStudioProjects/Kasekor%20Calculator/docs/CALCULATION_LOGIC.md) — Mathematical formulas for break-even, net profit, and yield conversions.

### 🧠 [`/context`](file:///home/saoly/Documents/AndroidStudioProjects/Kasekor%20Calculator/context)
- 🎯 [**Project Context**](file:///home/saoly/Documents/AndroidStudioProjects/Kasekor%20Calculator/context/PROJECT_CONTEXT.md) — Target audience, social impact, problem statement, and offline constraints.
- 🛠️ [**Tech Stack & Tooling**](file:///home/saoly/Documents/AndroidStudioProjects/Kasekor%20Calculator/context/TECH_STACK.md) — Exact dependency versions, build configuration, and Gradle setup.
- 📏 [**Development Guidelines**](file:///home/saoly/Documents/AndroidStudioProjects/Kasekor%20Calculator/context/DEVELOPMENT_GUIDELINES.md) — Code style, Compose best practices, color palettes, and touch target rules.

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio:** Ladybug (2024.2.1) or newer
- **JDK:** Java 17
- **Android SDK:** Compile SDK 35, Minimum SDK 24 (Android 7.0+)

### Building from Command Line

```bash
# Clone the repository
git clone https://github.com/saoly/Kasekor-Calculator.git
cd "Kasekor Calculator"

# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test
```

---

## 📄 License

This project is open-source under the [MIT License](LICENSE).
