# Calculation & Mathematical Logic 🧮

This document outlines the core business logic and mathematical calculations executed by **Kasekor Calculator**.

---

## 1. Total Cultivation Expenses ($E_{\text{total}}$)

The total expense for a cultivation cycle is the sum of all individual expense categories:

$$E_{\text{total}} = E_{\text{seed}} + E_{\text{fertilizer}} + E_{\text{equipment}} + E_{\text{labor}}$$

Where:
- $E_{\text{seed}}$ = Cost of seeds / seedlings
- $E_{\text{fertilizer}}$ = Cost of fertilizers, pesticides, and chemicals
- $E_{\text{equipment}}$ = Fuel, machinery rental, and tools
- $E_{\text{labor}}$ = Hired labor and operational wages

---

## 2. Break-Even Price ($P_{\text{be}}$)

The Break-Even Price represents the minimum selling price per kilogram (or ton) at which the farmer neither makes a profit nor incurs a loss:

$$P_{\text{be}} = \frac{E_{\text{total}}}{Y_{\text{kg}}}$$

Where:
- $E_{\text{total}}$ = Total Cultivation Expenses (in KHR or USD)
- $Y_{\text{kg}}$ = Expected Yield in Kilograms ($\text{kg}$)

---

## 3. Net Profit / Loss Simulation ($\text{Profit}$)

When a farmer inputs a buyer's offered price per unit ($P_{\text{buyer}}$), the net financial outcome is calculated as:

$$\text{Profit} = (P_{\text{buyer}} \times Y_{\text{kg}}) - E_{\text{total}}$$

Alternatively:

$$\text{Profit} = (P_{\text{buyer}} - P_{\text{be}}) \times Y_{\text{kg}}$$

### Visual Status Rules

$$\text{Status} = \begin{cases} \text{PROFIT (Green)}, & \text{if } P_{\text{buyer}} > P_{\text{be}} \\ \text{BREAK-EVEN (Yellow)}, & \text{if } P_{\text{buyer}} = P_{\text{be}} \\ \text{LOSS (Red)}, & \text{if } P_{\text{buyer}} < P_{\text{be}} \end{cases}$$

---

## 4. Currencies & Yield Units

Each crop cycle is entered in one **currency** (KHR, the default, or USD) and one **yield unit** (kg, the default, or ton). Amounts are never converted between currencies.

- Costs are stored in the cycle's currency.
- Yield is always stored in **kg**: $Y_{\text{kg}} = Y_{\text{entered}} \times 1000$ when entered in tons.
- The buyer price is stored **per kg** and shown per the cycle's unit: $P_{\text{ton}} = P_{\text{kg}} \times 1000$.
- The formulas above therefore always run in kg; only display and input are converted.

A net profit smaller than half of the smallest displayed amount (0.5 ៛, or $0.005) is shown as break-even.

---

## 5. Edge Cases & Safeguards

| Scenario | Handled By | Outcome |
| :--- | :--- | :--- |
| **Yield = 0 kg** | Domain Model / ViewModel Validation | Prevents Division by Zero (`ArithmeticException`). Displays error: *"សូមបញ្ចូលទិន្នផល" (Please enter yield)* |
| **Negative or Invalid Amounts** | `CalculatorViewModel` validation | Rejected with an error message; thousands separators (`1,500,000`) are accepted |
| **Currency Formatting** | `ui/common/Formatters.kt` | KHR as whole riel (`1,500,000 រៀល`); USD with cents (`$1,234.50`), and 3 decimals for per-unit prices under $1 (`$0.275`) |
