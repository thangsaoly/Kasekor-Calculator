# Architecture & Technical Design 🏛️

**Kasekor Calculator** adheres strictly to **Clean Architecture** principles combined with the **MVVM (Model-View-ViewModel)** pattern, enforcing clear **Separation of Concerns** across Data, Domain, and Presentation layers.

---

## 🏗️ System Overview

```mermaid
graph TD
    subgraph Presentation Layer [UI Layer - Jetpack Compose]
        HomeScreen[HomeScreen & HomeViewModel]
        CalcScreen[CalculatorScreen & CalculatorViewModel]
        ResultScreen[ResultScreen & ResultViewModel]
    end

    subgraph Domain Layer [Pure Business Logic - Kotlin]
        CropModel[CropCycle Domain Model]
        CropRepoInterface[CropRepository Interface]
    end

    subgraph Data Layer [Data & Persistence - Room / SQLite]
        CropRepoImpl[CropRepositoryImpl]
        Dao[CropCycleDao]
        Entity[CropCycleEntity]
        DB[(Room SQLite DB)]
    end

    HomeScreen -->|Observes StateFlow| HomeViewModel
    CalcScreen -->|Observes StateFlow| CalculatorViewModel
    ResultScreen -->|Observes StateFlow| ResultViewModel

    HomeViewModel -->|Calls| CropRepoInterface
    CalculatorViewModel -->|Calls| CropRepoInterface
    ResultViewModel -->|Calls| CropRepoInterface

    CropRepoImpl ..|> CropRepoInterface
    CropRepoImpl --> Dao
    Dao --> DB
    Dao --> Entity
    CropRepoImpl -->|Mappers| CropModel
```

---

## 📦 Layer Breakdown

### 1. Domain Layer (`com.saoly.kasekorcalculator.domain`)
- **Purity:** Standard Kotlin only. Zero dependencies on Android SDK, Jetpack Compose, or Room.
- **`model/CropCycle.kt`**: Domain entity representing a full cultivation record.
- **`repository/CropRepository.kt`**: Interface defining data contracts (`getAllCropCycles()`, `insertCropCycle()`, `getCropCycleById()`).

### 2. Data Layer (`com.saoly.kasekorcalculator.data`)
- **Persistence:** Local SQLite via **Room Database**.
- **`local/entity/CropCycleEntity.kt`**: Room database entity annotated with `@Entity(tableName = "crop_cycles")`.
- **`local/dao/CropCycleDao.kt`**: Data Access Object with reactive Kotlin `Flow` queries.
- **`local/KasekorDatabase.kt`**: Room Database instance.
- **`repository/CropRepositoryImpl.kt`**: Implements `CropRepository`, mapping database entities to domain models.

### 3. Presentation Layer (`com.saoly.kasekorcalculator.ui`)
- **UI Framework:** **Jetpack Compose** with Material 3 components.
- **Architecture:** Unidirectional Data Flow (UDF) with `ViewModel` and `StateFlow`.
- **Packages:**
  - `ui/home/`: `HomeScreen.kt`, `HomeViewModel.kt` (Historical cultivation feed & analytics overview).
  - `ui/calculator/`: `CalculatorScreen.kt`, `CalculatorViewModel.kt` (Dynamic expense entry form).
  - `ui/result/`: `ResultScreen.kt`, `ResultViewModel.kt` (Break-even dashboard & price negotiation shield).
  - `ui/common/`: Reusable components (`KhmerTextField`, `PrimaryButton`).
  - `ui/theme/`: Visual theme, typography (`Kantumruy Pro`), and color definitions.

---

## ⚡ Data Flow & Reactive State Management

1. **State Observation:** UI screens collect state reactively using `collectAsStateWithLifecycle()` from ViewModels.
2. **User Actions:** User interactions trigger intent methods in ViewModel (e.g., `onCalculateClicked()`, `onPriceChanged()`).
3. **Async Processing:** ViewModels execute domain operations inside `viewModelScope` using Kotlin **Coroutines**.
4. **Local Updates:** Repository updates local Room database, which automatically emits updated data via `Flow<List<CropCycle>>`.

---

## 💉 Dependency Injection (Hilt)

Dependency injection is managed globally via **Dagger-Hilt**:
- **Application Class:** `@HiltAndroidApp` handles component lifecycle.
- **Database Module:** Provides `KasekorDatabase` and `CropCycleDao` as `@Singleton`.
- **Repository Module:** Binds `CropRepositoryImpl` to `CropRepository`.
- **ViewModels:** Annotated with `@HiltViewModel` and injected via `@Inject constructor`.
