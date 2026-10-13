# Paradox Log — Technical Architecture Specification

> **PERMANENT ARCHITECTURAL MANDATE:**
> **No story, game state, rule, navigation route, or UI component should have multiple competing sources of truth.**

---

## 1. Architecture Overview

*Paradox Log* is built on Clean Architecture and MVVM principles, enforcing strict separation of concerns, unidirectional data flow, and complete decoupling of narrative content, game rules, and user interface.

```
┌────────────────────────────────────────────────────────┐
│                   UI Layer (feature/)                  │  Passive Jetpack Compose UI
└───────────────────────────┬────────────────────────────┘
                            │ Dispatches User Actions
                            ▼
┌────────────────────────────────────────────────────────┐
│                ViewModel (feature/*/ViewModel)         │  StateFlow exposure, UI state holder
└───────────────────────────┬────────────────────────────┘
                            │ Requests Game Action
                            ▼
┌────────────────────────────────────────────────────────┐
│               Game Engine Layer (game/)                │  Evaluates conditions, rules, deltas
└─────────────┬────────────────────────────┬─────────────┘
              │ Owns & Mutates             │ Reads Definitions
              ▼                            ▼
┌───────────────────────────┐  ┌─────────────────────────┐
│     GameState (game/state)│  │   Domain Layer (domain/) │ Pure Kotlin Entities & Repositories
└───────────────────────────┘  └───────────▲─────────────┘
                                           │ Maps to
                               ┌───────────┴─────────────┐
                               │     Data Layer (data/)  │ Parsers, Data Sources, Repositories
                               └───────────▲─────────────┘
                                           │ Reads
                               ┌───────────┴─────────────┐
                               │   Canonical Content     │ content/ (Authoring Source of Truth)
                               └─────────────────────────┘
```

---

## 2. Layer Responsibilities

### 2.1 `app/src/main/java/com/paradoxlog/app/core/`
- Shared foundational infrastructure: design system primitives, common string constants, and general utility helpers.
- **Rule:** Must remain strictly agnostic of feature-specific game rules.

### 2.2 `app/src/main/java/com/paradoxlog/app/domain/`
- Pure Kotlin business abstractions:
  - `domain/model/`: Pure data classes (`Act`, `Scene`, `Dialogue`, `Choice`, `ChoiceCondition`, `ChoiceConsequence`, `Item`, `GameFlag`).
  - `domain/repository/`: Data retrieval interfaces (`ContentRepository`).
  - `domain/usecase/`: Pure business use cases `[PLANNED]`.
- **Rule:** Zero Android framework, Compose, or persistence dependencies.

### 2.3 `app/src/main/java/com/paradoxlog/app/data/`
- Data retrieval, file reading, and content parsing:
  - `data/content/parser/`: Deserializes JSON into domain models (`JsonContentParser`).
  - `data/repository/`: Implements domain repositories (`ContentRepositoryImpl`).
  - `data/local/`: Handles local disk persistence and SQLite/Room/DataStore integrations `[PLANNED]`.
- **Rule:** Contains zero game rules, choice evaluation logic, or mutable playthrough state.

### 2.4 `app/src/main/java/com/paradoxlog/app/game/`
- Runtime game engine and business logic:
  - `game/state/`: Owns the single authoritative `GameState` data container.
  - `game/engine/`: Orchestrates gameplay execution, scene navigation, and game loop `[PLANNED]`.
  - `game/rules/`: Evaluates choice conditions, calculates variable deltas, checks paradox limits `[PLANNED]`.
  - `game/save/`: Serializes and restores `GameState` according to `save-schema.json` `[PLANNED]`.
- **Rule:** Completely decoupled from UI. Can run independently in headless unit tests.

### 2.5 `app/src/main/java/com/paradoxlog/app/feature/`
- Feature-specific UI screens and ViewModels (`splash`, `mainmenu`, `story`, `choice`, `timeline`, `inventory`, `ending`).
- **Rule:** Passive presentation only. Observes `StateFlow<GameState>` and emits user action events.

### 2.6 `app/src/main/java/com/paradoxlog/app/di/`
- Dependency injection composition roots (`AppModule`, `DataModule`, `GameModule`).

---

## 3. Content Pipeline & Build-Time Packaging

The canonical narrative source is the external `content/` tree. The pipeline from authoring to runtime is:

```text
content/ (Authoring Source of Truth: Acts, Scenes, Choices JSON)
   │
   ▼
[Build-Time Packaging / Asset Pipeline]
   │ Validates schemas and bundles into app assets during build
   ▼
Runtime Content Loader (data/content/)
   │
   ▼
Content Parser (data/content/parser/JsonContentParser)
   │
   ▼
Domain Models (domain/model/Act, Scene, Choice)
```

- **Pipeline Mandate:** Never copy story text into Kotlin files. The build process packages canonical JSON files so the runtime loader can ingest them dynamically.

---

## 4. Runtime System Pipeline

```text
Content (JSON)
   └──> Content Parser (data/content/parser)
         └──> Domain Models (domain/model)
               └──> Repository (data/repository)
                     └──> Game Engine (game/engine)
                           └──> Game State (game/state)
                                 └──> ViewModel (feature/*/ViewModel)
                                       └──> Compose UI (feature/*/Screen)
```

---

## 5. Gameplay Execution Pipeline

```text
Act
 └──> Scene
       └──> Dialogue
             └──> Choice
                   └──> Condition Check (Evaluated by Game Rules)
                         └──> Consequence Calculation (Deltas applied)
                               └──> Immutable GameState Mutation (game/state)
                                     └──> Target Scene Transition
```

---

## 6. Runtime State Ownership

The **Game Engine** (`game/state/GameState.kt`) is the **sole** owner of mutable runtime state.

### Managed State:
1. **Global Variables (`GameVariables`):**
   - `tyrantIndex`: Authoritarian metric.
   - `timelineStability`: Reality cohesion metric.
   - `selfTrust`: Psychological alignment metric.
2. **Flags (`flags: Set<GameFlag>`):** Dynamic persistent event triggers.
3. **Inventory (`inventory: InventoryState`):** Set of acquired item IDs.
4. **Timeline Progress (`timeline: TimelineProgress`):** Completed acts, visited scenes, decision history.
5. **Ending State (`ending: EndingState`):** Active ending eligibility and resolution status.

**Architectural Law:** No screen, ViewModel, or repository may hold duplicate playthrough state. ViewModels only expose `StateFlow<GameState>`.

---

## 7. Data vs. Logic Separation

| Dimension | Content Layer (`content/`) | Game Rules Layer (`game/rules/`) |
| :--- | :--- | :--- |
| **Question** | **WHAT** happens? | **HOW** does the system respond? |
| **Responsibilities** | Scene text, choice text, pointers to next scenes, declarative consequence descriptors. | Evaluating if conditions pass, calculating exact variable deltas, evaluating ending eligibility, triggering paradox collapses. |
| **Location** | `content/acts/*.json` | `app/src/main/java/.../game/rules/` |
| **UI Bleed Prevention** | Composables never read raw JSON. | Composables never evaluate rules or modify variables. |

---

## 8. Dependency Direction

Dependencies flow strictly inward toward the domain core:

```text
feature (UI)  ──▶  domain (Core Models & Repository Interfaces)
     │                     ▲
     ▼                     │
game (Engine & State) ─────┤
     │                     │
     ▼                     │
data (Persistence & Parser)─┘
```

- `domain` depends on **nothing**.
- `game` depends only on `domain`.
- `data` depends on `domain`.
- `feature` depends on `domain` and `game/state`.
- **Prohibited:** `domain` depending on `data` or `feature`; `game` depending on `feature`.

---

## 9. Persistence Boundary

- **Contract:** Serialized state must strictly conform to [`game-data/save-schema.json`](file:///Users/ratnamsmac/Documents/Projects/Paradox%20Log/game-data/save-schema.json).
- **Ownership:** Persistence operations are managed jointly by `game/save/` and `data/local/`.
- **Restoration:** Loading a save file reconstitutes the exact `GameState` container. UI screens receive the restored state through StateFlow.

---

## 10. Known Architectural Risks & Mitigations

1. **Content Packaging Synchronization:**
   - *Risk:* Developers manually editing `app/src/main/assets` and causing divergence from `content/`.
   - *Mitigation:* Establish an automated Gradle build task that synchronizes and validates `content/` during compilation.
2. **Schema Drift:**
   - *Risk:* New fields added to JSON without updating domain models or `save-schema.json`.
   - *Mitigation:* Rigorous unit tests verifying that all content schemas serialize and deserialize cleanly through `JsonContentParser`.
3. **UI Logic Bleed:**
   - *Risk:* Developers writing choice checks or variable calculations directly in Composables.
   - *Mitigation:* Strict code reviews and linting rules enforcing the Thin UI policy.
4. **Gradle Wrapper Limitation:**
   - *Risk:* Command-line builds fail because `gradle-wrapper.jar` is omitted.
   - *Mitigation:* Import project via Android Studio to initialize the verified wrapper binary.
