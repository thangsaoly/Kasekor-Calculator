To help you practice thinking like an Android engineer and prepare your development setup, we will detail the **"Kasekor Calculator" (កសិករគណនា)**. This app directly addresses the problem of farmers lacking market/profit awareness and getting squeezed by middlemen, but it scales the scope down to a realistic, offline-first Android app.  
By building this, you will master the core pillars of Modern Android Development (MAD): **Kotlin**, **Jetpack Compose**, **Room Database**, and **Clean Architecture (MVVM)**.

## **1. Project Overview & Objective**

The **Kasekor Calculator** is an offline-first tool designed for Cambodian farmers to log their seasonal cultivation expenses (seeds, fertilizer, labor, fuel) and expected yield. The app instantly calculates their exact **Break-Even Price** per kilogram.  
When a middleman arrives at the farm and offers a low price, the farmer can open the app, look at their baseline cost, and confidently negotiate or reject the offer to avoid selling at a financial loss.

## **2. Functional & Non-Functional Requirements**

### **Functional Requirements (What the app does)**

* **Manage Crop Cycles:** Users can start a new tracking cycle (e.g., "Rice Season Rainy 2026", "Mango Batch 2").  
* **Expense Input Categories:** Forms to input costs with specific categories: Seeds, Fertilizer/Pesticides, Equipment/Fuel, and Labor.  
* **Yield Prediction:** Input of expected harvest weight in Kilograms (kg) or Tons.  
* **Financial Insights Dashboard:** Displays the exact minimum price the farmer must sell at to break even, alongside potential profit margins based on different market prices.  
* **History Log:** Saved records of past seasons to compare whether expenses are rising or falling year-over-year.

### **Non-Functional Requirements (How the app behaves)**

* **100% Offline Capability:** Must function entirely without internet access, as cellular coverage in remote fields can be unreliable. Data must be saved locally using a Room SQLite database.  
* **Localization (Khmer-First):** The entire UI must default to the Khmer language with clear numerals.  
* **Performance:** UI elements must render seamlessly with zero lag on low-end, budget Android devices common in rural areas.  
* **Low Storage Footprint:** The APK size should remain under 10MB to make it easy to download or share via Bluetooth/Telegram.

## **3. Recommended Project Architecture & Structure**

To ensure your code is clean and scalable, you should follow the official Android recommendation: **Separation of Concerns** using a simplified Clean Architecture with MVVM (Model-View-ViewModel).  
Here is the exact folder structure you should set up in Android Studio:

```plaintext
com.saoly.kasekorcalculator/
│
├── data/                         # Data Layer (Database, Entities, Daos)
│   ├── local/
│   │   ├── dao/CropCycleDao.kt
│   │   ├── entity/CropCycleEntity.kt
│   │   └── KasekorDatabase.kt
│   └── repository/CropRepositoryImpl.kt
│
├── domain/                       # Business Logic Layer (Pure Kotlin)
│   ├── model/CropCycle.kt
│   └── repository/CropRepository.kt
│
├── ui/                           # Presentation Layer (Jetpack Compose UI)
│   ├── theme/                    # Color.kt, Type.kt, Theme.kt
│   ├── common/                   # Reusable components (KhmerTextField, PrimaryButton)
│   ├── home/                     # Home screen UI & ViewModel
│   ├── calculator/               # Calculation form screen & ViewModel
│   └── result/                   # Summary/Insights dashboard screen
│
└── MainActivity.kt               # App entry point & Navigation host
```

## **4. UX/UI Design & Behavior Dictionary**

Designing for rural users requires specific design patterns. Standard, tiny material components will cause user frustration.

### **Look and Feel (UI)**

* **Typography:** Use the **Kantumruy Pro** or **Hanuman** Google font for Khmer text. Set a minimum font size of `16.sp` for body text and `22.sp` for headers to accommodate older farmers.  
* **Color Palette:**  
  * *Primary:* Deep Agriculture Green (#2E7D32) to invoke trust and familiarity.  
  * *Secondary/Alert:* Warm Amber (#FFA000) for warnings when profit margins are negative.  
  * *Background:* Soft, high-contrast off-white (#F9F9F9) so the screen remains easily readable under direct, bright sunlight.  
* **Icon-Heavy UI:** Every text label must be accompanied by an unambiguous visual icon (e.g., 🌾 for Rice, 💧 for Fertilizer, 🚜 for Machinery, 💵 for Price).

### **Behavior & Interaction (UX)**

* **Massive Touch Targets:** All buttons and input fields must have a minimum height of `56.dp` so they are easy to press with weathered or calloused hands.  
* **Smart Numeric Keyboards:** When tapping any input field, the app must automatically trigger the decimal numeric keyboard (`keyboardType = KeyboardType.Number`), saving the user from switching keyboard layouts.

## **5. User Activity Flow & Screen Navigation**

Your app will use Jetpack Compose Navigation to handle the user journey through **three core screens**.

```mermaid
flowchart TD
    Splash["Splash Screen"] --> Home["Home Screen (History)"]
    Home -->|Tap "+" Button| Calc["Calculator Form Screen"]
    Calc -->|Tap "Calculate"| Result["Results Screen"]
    Result -->|Save & Return| Home
```

### **Screen 1: The Home Screen (Historical Feed)**

* **What it looks like:** A simple top bar with the app name in Khmer. The body contains a scrollable `LazyColumn` showing cards of past crop cycles. If empty, it displays a friendly graphic saying "មិនទាន់មានទិន្នន័យ (No data yet)".  
* **The Hero Element:** A large, floating action button (FAB) at the bottom right with a plus icon (`+`) to start a new calculation.

### **Screen 2: The Calculator Form (Data Input)**

* **What it looks like:** A step-by-step form layout.  
  1. *Field 1:* Crop name selection (Dropdown: ស្រូវ/Rice, ដំឡូង/Cassava, ស្វាយ/Mango).  
  2. *Field 2:* Expense categories with simple text fields where numbers are automatically formatted as Cambodian Riel (KHR) or USD ($).  
  3. *Field 3:* Expected yield (kg).  
* **Behavior:** As the user types, the app calculates the total cost on-the-fly at the bottom of the screen.

### **Screen 3: The Result Screen (The Shield)**

* **What it looks like:** A clean, uncluttered card layout that emphasizes the break-even number above everything else.  
* **The Core Logic:** The app processes the inputted data using the following mathematical logic:

$$\text{Total Expenses } (E_{\text{total}}) = \sum (\text{Seeds} + \text{Fertilizer} + \text{Labor} + \text{Fuel})$$

$$\text{Break-Even Price} = \frac{E_{\text{total}}}{\text{Expected Yield } (kg)}$$

* **The Visual Feedback:** If the user tests a potential buyer price, the app calculates the net profit:

$$\text{Net Profit} = (\text{Buyer Price} \times \text{Expected Yield}) - E_{\text{total}}$$

* If the Net Profit is greater than 0, the text turns **Green** (Profit). If it falls below 0, it turns **Red** (Loss), explicitly warning the farmer not to accept that trade.

### **Key Jetpack Compose APIs You Will Use**

To make this real, you will spend most of your time writing these Jetpack Compose elements:

1. `Scaffold`: To handle basic screen structures (TopBar, FloatingActionButton).  
2. `OutlinedTextField`: Customized with `leadingIcon` for your visual crop icons.  
3. `Card`: To display the clean calculation summaries.  
4. `rememberSaveable`: To keep form data safe even if the farmer rotates their phone.

Which part of this detailed project setup would you like to build out first: configuring the Room Database entities or designing the Jetpack Compose UI layout for the calculator screen?