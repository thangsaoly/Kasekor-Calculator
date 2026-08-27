# Tech Stack & Environment Specifications 🛠️

## ⚙️ Core System Configuration

- **Platform:** Native Android
- **Language:** Kotlin `2.0.21`
- **Build System:** Gradle (Kotlin DSL `.gradle.kts`)
- **JDK:** Java 17
- **Compile SDK:** `35` (Android 15)
- **Min SDK:** `24` (Android 7.0 Nougat)
- **Target SDK:** `35`

---

## 📚 Libraries & Dependencies

```kotlin
// AndroidX Core & Lifecycle
libs.androidx.core.ktx                // Core KTX extensions
libs.androidx.lifecycle.runtime.ktx   // Lifecycle runtime
libs.androidx.lifecycle.viewmodel.compose // ViewModel for Compose integration

// Jetpack Compose BOM & UI
platform(libs.androidx.compose.bom)   // Bill of Materials
libs.androidx.ui                      // Core Compose UI
libs.androidx.ui.graphics             // Graphics engine
libs.androidx.ui.tooling.preview      // Preview annotations
libs.androidx.material3               // Material Design 3
libs.androidx.activity.compose        // ComponentActivity Compose entrypoint
libs.androidx.navigation.compose      // Navigation host

// Room Local Database
libs.androidx.room.runtime            // Room runtime engine
libs.androidx.room.ktx                // Room Coroutines & Flow support
libs.plugins.ksp                      // KSP (Kotlin Symbol Processing) for compiler

// Dependency Injection
libs.hilt.android                     // Dagger-Hilt runtime
libs.hilt.compiler                    // Hilt KSP compiler

// Icons
libs.androidx.material.icons.extended // Extended Material icons
```

---

## 🛠️ Build Commands Reference

```bash
# Build Debug APK
./gradlew assembleDebug

# Run Unit Tests
./gradlew test

# Clean & Rebuild
./gradlew clean assembleDebug
```
