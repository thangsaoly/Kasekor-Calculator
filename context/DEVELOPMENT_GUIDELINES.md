# Development Guidelines & Engineering Standards 📏

This document outlines the coding standards, UI rules, and architectural guidelines enforced across the **Kasekor Calculator** codebase.

---

## 🎨 UI & UX Coding Principles

1. **Khmer Language First:** Default all user-facing strings to Khmer (`res/values/strings.xml`).
2. **Typography Standards:** Use Google **Kantumruy Pro** font family.
   - Body Text: $\ge 16.\text{sp}$
   - Titles / Headers: $\ge 22.\text{sp}$
3. **Massive Touch Targets:** Ensure all interactive elements (Buttons, TextFields, Cards) have a minimum touch target height of `56.dp`.
4. **Keyboard Automation:** Explicitly assign `keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)` on numeric text fields.
5. **High Contrast Color Palette:**
   - Primary: `#2E7D32` (Agriculture Deep Green)
   - Secondary / Profit: `#4CAF50` (Green Accent)
   - Loss Warning: `#E53935` (High Contrast Red)
   - Background: `#F9F9F9` (High Contrast Off-White)

---

## 🏛️ Clean Architecture & Code Organization

1. **Layer Dependency Direction:**
   $$\text{UI Layer} \longrightarrow \text{Domain Layer} \longleftarrow \text{Data Layer}$$
   - UI and Data layers depend on Domain. Domain depends on **nothing**.
2. **Immutability First:** State models must use `data class` with read-only (`val`) properties.
3. **State Management:**
   - Use `StateFlow` in ViewModels.
   - Collect state in Compose screens using `collectAsStateWithLifecycle()`.
4. **Resource Management:** Never hardcode strings or dimensions in `@Composable` functions; reference `stringResource()` and theme tokens.

---

## 🧪 Testing Guidelines

- **Domain Layer:** Unit test all break-even and profit/loss calculation formulas in `test/`.
- **Data Layer:** In-memory Room database testing for `CropCycleDao`.
- **Target Coverage:** Maintain $\ge 80\%$ test coverage on business logic.
