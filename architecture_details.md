# 🏗 PARADOX LOG – Architecture Details

This document outlines the proposed technical architecture for building **Paradox Log** as an offline-first, mobile text-based RPG targeting both Android and iOS.

---

## 1. Core Tech Stack
To support both Android and iOS efficiently while maintaining a single codebase for the game's core logic and UI, the project uses a Kotlin-centric stack.

- **Language:** Kotlin
- **Frontend / UI:** [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
  - Allows building the text-heavy, scroll-based UI once and deploying it natively to both Android and iOS.
  - Excellent for handling dynamic text rendering and minimal animations as required by the design.
- **Dependency Injection:** [Koin](https://insert-koin.io/)
  - A lightweight, pragmatic dependency injection framework optimized for Kotlin Multiplatform.

---

## 2. Architecture Pattern

The game will utilize the **MVVM (Model-View-ViewModel)** architectural pattern to keep the narrative engine clean and decoupled from the UI.

- **Model:** Represents the data and business logic of the game (Current Act module, variables like `tyrant_index` and `timeline_stability`, and the save mechanisms).
- **View (Compose UI):** Purely declarative. It observes the UI State exposed by the ViewModel and renders it. It contains no game logic.
- **ViewModel (Narrative Engine):** Acts as the bridge. When a user taps a choice in the View, the ViewModel processes that action, updates the core Models (calculating consequences, saving state), and updates the observable `StateFlow` so the View can re-render.

---

## 3. Offline Persistence & Save System
Since the game requires local saving, persistence, and state tracking without a backend, the following tools will manage offline data:

- **Complex Game State / Save Slots:** [SQLDelight](https://cashapp.github.io/sqldelight/)
  - Generates type-safe Kotlin APIs from SQL statements.
  - Multiplatform compatible (works on Android SQLite and iOS native databases).
  - Perfect for storing acts, unlocked paths, past decisions, and save slots.
- **Simple Preferences / Settings:** **Jetpack DataStore** (Multiplatform)
  - Ideal for simple key-value pairs (e.g., UI theme preferences, text size, "Has finished tutorial" boolean flags).
- **State Serialization:** `kotlinx.serialization`
  - Used to easily convert Kotlin Data Classes representing narrative modules or lightweight save states into JSON text format.

---

## 4. Core System Mapping

How the core game systems map to the architecture:

| Game System | Technical Implementation |
|-------------|--------------------------|
| **Act Manager** | A Kotlin StateMachine or flow router in the ViewModel that loads the correct JSON/SQL narrative module based on the current save state. |
| **Choice Resolver** | Pure Kotlin domain logic. Evaluates requirements (e.g., `if (tyrant_index > 50)`) before emitting the choices to the Compose UI. |
| **Timeline State Tracker** | Global singleton variables managed via **Koin**, observed via Kotlin `StateFlow`, and persistently backed by **SQLDelight**. |
| **Save/Load System** | Triggers asynchronous writes to the local SQLDelight database after major choices (Auto-save). |
| **UI Rendering** | **Compose Multiplatform** `LazyColumn` for scrollable, text-heavy views and large tap targets for choices. |

---

## 5. Error Handling & Stability

- **Soft-lock prevention:** The ViewModel will always provide a fallback UI state or default choice if an act module fails to load or timeline stability conditions lead to a dead end.
- **Interrupt-safe saving:** Because save data is lightweight, Room/SQLDelight transactions will be extremely fast, ensuring that if the app is closed abruptly, the timeline state is preserved.
