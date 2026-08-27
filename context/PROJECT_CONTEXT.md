# Project Context & Problem Statement 🎯

## 🌾 Background & Problem Statement

In rural Cambodia, millions of smallholder farmers cultivate crops like rice (ស្រូវ), cassava (ដំឡូង), and mangoes (ស្វាយ). However, many face severe financial losses due to asymmetrical market information:

1. **Middleman Squeeze:** Middlemen arrive directly at farms during harvest time and offer low prices on a take-it-or-leave-it basis.
2. **Lack of Cost Transparency:** Farmers often do not calculate their cumulative costs (seeds, fertilizer, fuel, labor) before negotiating, making it impossible to know if an offer covers their cost of production.
3. **Connectivity Deficit:** Cellular network coverage in agricultural fields is spotty or non-existent.

---

## 🎯 Solution: Kasekor Calculator (កសិករគណនា)

**Kasekor Calculator** is a lightweight, offline-first mobile tool that bridges this information gap by allowing farmers to track seasonal costs and instantly generate their **Break-Even Price per Kilogram**.

---

## 🔒 Core Operational Constraints

1. **100% Offline Autonomy:** All features, data storage, and calculations must function locally without any cloud or network connection.
2. **Rural Accessibility:** Designed for budget, entry-level Android smartphones (`minSdk 24` / Android 7.0+).
3. **App Footprint:** APK size must remain under 10MB for easy sharing via Bluetooth or local messaging apps.
4. **Zero Latency:** Computation and UI rendering must execute instantly with zero UI lag.
