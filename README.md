# Paradox Log

A minimalist, text-based time-paradox RPG for Android built with Kotlin, Jetpack Compose, and Clean Architecture.

---

## 1. Project Overview

- **Project Name:** Paradox Log
- **Genre:** Text-based Narrative RPG / Philosophical Sci-Fi
- **Platform:** Android (Min SDK: 26, Target SDK: 34)
- **Language & UI:** Kotlin, Jetpack Compose (Material 3 with iOS-inspired minimalist aesthetics)
- **Architecture:** Clean Architecture + MVVM (Strict unidirectional data flow)
- **Current Development Status:** `Phase 2 Completed (Data / Domain / State Foundation established; UI and Game Engine planned)`
- **Core Gameplay Concept:** A branching, decision-driven narrative structured across two alternating temporal perspectives (Past Self and Future Self), where decisions alter reality stability, character morality, and timeline continuity.
- **High-Level Objective:** Deliver an immersive, high-stakes interactive narrative examining the cost of preventing catastrophe, supported by a scalable, single-source-of-truth software architecture.

---

## 2. One-Paragraph Game Summary

In *Paradox Log*, the player navigates an unfolding temporal disaster by experiencing two interconnected timelines: a **Past Self** attempting to prevent a global timeline collapse, and a **Future Self** who has committed extreme, authoritarian acts to ensure humanity survives that very catastrophe. Through minimalist typography, intense decision trees, and persistent consequence mechanics, players confront the moral weight of their own future actions and decide whether to oppose, redeem, or become the necessary monster.

---

## 3. Core Concept

The core conflict of *Paradox Log* hinges on the temporal relationship between the **Past Self** and the **Future Self**. Rather than a standard time-travel story with external adversaries, the antagonist is the player's own future incarnation.

### The Central Paradox

> *"If your future self became a monster to prevent a catastrophe, would you stop them—even if stopping them causes the catastrophe?"*

Every action taken in the past directly recalculates the timeline stability and influences the psychological alignment of the future entity. Conversely, logs and warnings left by the future entity challenge the player's ethical boundaries in the past.

---

## 4. Core Gameplay Loop

```text
       ┌──────────────┐
       │     Read     │  Absorb narrative, dialogue, and atmospheric temporal cues
       └──────┬───────┘
              │
              ▼
       ┌──────────────┐
       │    Decide    │  Evaluate choices with visible/hidden conditions
       └──────┬───────┘
              │
              ▼
       ┌──────────────┐
       │ Consequence  │  Game rules evaluate choice deltas
       └──────┬───────┘
              │
              ▼
       ┌──────────────┐
       │ State Change │  Authoritative GameState mutation (Variables, Flags, Inventory)
       └──────┬───────┘
              │
              ▼
       ┌──────────────┐
       │   Continue   │  Transition to subsequent Scene or Act
       └──────┬───────┘
              │
              ▼
       ┌──────────────┐
       │   Discover   │  Uncover paradox logs, lore fragments, and diverging paths
       └──────┬───────┘
              │
              ▼
       ┌──────────────┐
       │Final Judgment│  Resolve 1 of 5 distinct narrative endings
       └──────────────┘
```

---

## 5. Major Systems Status

| System | Description | Status |
| :--- | :--- | :--- |
| **Domain Models** | Pure Kotlin models (`Act`, `Scene`, `Dialogue`, `Choice`, `Item`, `GameFlag`) | `[IMPLEMENTED]` |
| **Authoritative State** | Immutable `GameState` tracking variables, flags, inventory, timeline, and endings | `[IMPLEMENTED]` |
| **Content Parser** | `JsonContentParser` parsing raw JSON into domain models | `[IMPLEMENTED]` |
| **Content Repository** | `ContentRepository` contract and implementation for data access | `[IMPLEMENTED]` |
| **Save State Schema** | `game-data/save-schema.json` formalizing state persistence contracts | `[IMPLEMENTED]` |
| **Narrative Content** | Authoring files under `content/acts/` across Acts I–VI | `[PLANNED]` (Schemas established; text unpopulated) |
| **Game Engine** | Choice evaluation, condition checking, delta resolution, paradox triggers | `[IMPLEMENTED]` (`GameEngine`, `ConditionEvaluator`, `ConsequenceApplier`, `ProgressionHandler`, `EndingEvaluator`, `GameSaveManager`) |
| **UI & Screens** | Jetpack Compose minimalist screens (`StoryScreen`, `ChoiceView`, `TimelineView`) | `[PLANNED]` (Phase 4 milestone) |
| **Navigation** | AndroidX declarative navigation routing between features | `[PLANNED]` (Phase 4 milestone) |
| **Persistence Engine** | Room / DataStore implementation adhering to `save-schema.json` | `[PLANNED]` (Phase 5 milestone) |
| **Audio & Assets** | Audio manager, SFX playback, dynamic ambient soundtrack | `[PLANNED]` (Asset directories ready) |
| **New Game+** | Timeline branch retention, memory fragment carrying | `[PLANNED]` |

---

## 6. Technology Stack

- **Platform:** Native Android (Kotlin 1.9.24, Android Gradle Plugin 8.5.0)
- **Compile SDK:** 34 | **Target SDK:** 34 | **Min SDK:** 26
- **UI Framework:** Jetpack Compose (BOM 2024.05.00) + Material 3 (1.2.x)
- **Architecture Components:**
  - AndroidX Core KTX (1.13.1)
  - AndroidX Activity Compose (1.9.0)
  - AndroidX Lifecycle Runtime KTX (2.8.0)
  - Standard Android `org.json` for lightweight JSON deserialization
- **Testing:** JUnit 4 (4.13.2), AndroidX Test Ext JUnit (1.1.5), Espresso Core (3.5.1)
- **Design Paradigm:** Restrained Material 3, iOS-inspired spacing and hierarchy, low visual clutter, typography-first narrative presentation.

---

## 7. Repository Structure

```text
paradox-log/
├── README.md               # Primary project entry point and team lead overview
├── PRD.md                  # Product Requirements Document
├── GDD.md                  # Comprehensive Game Design Document
├── AI_RULES.md             # Strict engineering & AI contribution constraints
│
├── docs/                   # Full technical and design specifications
│   ├── architecture.md     # Technical architecture, pipelines, and data flows
│   ├── narrative/          # Narrative design, acts, choice matrix, character arcs, endings
│   └── design/             # Visual design system, screen flows, navigation, accessibility
│
├── content/                # CANONICAL AUTHORING SOURCE OF TRUTH (Narrative JSON)
│   ├── acts/ (act-01..06)  # Scenes, choices, and act metadata
│   ├── characters/         # Character definitions
│   ├── locations/          # Location profiles
│   ├── items/              # Item source definitions
│   ├── anomalies/          # Temporal anomaly definitions
│   ├── endings/            # Ending conditions source
│   └── dialogues/          # Master dialogue tables
│
├── game-data/              # GLOBAL CONFIGURATION & SCHEMAS
│   ├── variables.json      # Declarative definitions of Tyrant Index, Stability, Self-Trust
│   ├── flags.json          # Declarative registry of story flags
│   ├── quests.json         # Milestone/quest tracking definitions
│   ├── achievements.json   # Achievement definitions
│   └── save-schema.json    # Canonical JSON Schema for GameState persistence
│
├── app/                    # ANDROID APPLICATION MODULE
│   ├── build.gradle.kts    # Module dependencies and Android SDK configurations
│   ├── proguard-rules.pro  # Release obfuscation rules
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/paradoxlog/app/
│       │   │   ├── MainActivity.kt           # Minimal activity entry stub
│       │   │   ├── ParadoxLogApplication.kt  # Application entry stub
│       │   │   ├── core/                     # Shared design primitives, constants, utils
│       │   │   ├── domain/model/             # Pure Kotlin domain entities
│       │   │   ├── domain/repository/        # Repository contracts
│       │   │   ├── data/content/parser/      # JSON → Domain model parsers
│       │   │   ├── data/repository/          # Repository implementations
│       │   │   ├── game/state/               # Authoritative GameState container
│       │   │   ├── feature/                  # Thin UI features (Splash, Story, Timeline, etc.)
│       │   │   └── di/                       # Dependency injection modules
│       │   └── res/                          # Android resources (strings, drawables)
│       └── test/                             # Unit tests for domain, state, and parser
│
├── assets/                 # Raw source graphic assets (branding, illustrations, icons)
├── audio/                  # Raw source audio assets (music, ambience, sfx)
├── tests/                  # Root-level validation tests
├── build.gradle.kts        # Root Gradle build configuration
├── settings.gradle.kts     # Project name and module inclusion
└── gradle.properties       # JVM arguments and AndroidX flags
```

---

## 8. Development Status & Roadmap

```text
[Phase 1] Architecture Scaffolding  ───▶  COMPLETED (Directory structure & boundaries)
[Phase 2] Domain & State Foundation ───▶  COMPLETED (Domain models, GameState, Parser, Repo)
[Phase 3] Game Engine & Rules Engine───▶  COMPLETED (Choice evaluator, deltas, conditions, save manager, progression)
[Phase 4] Thin Jetpack Compose UI   ───▶  NEXT UP   (Typography, choice interaction, timeline)
[Phase 5] Persistence & Content Pack───▶  PLANNED   (Room/DataStore, build asset sync)
[Phase 6] Full Narrative Population ───▶  PLANNED   (Acts I–VI narrative authoring)
```

---

## 9. Getting Started

### Prerequisites
- Android Studio Iguana (2023.2.1) or newer / Android Studio Koala
- JDK 17 (Eclipse Temurin or OpenJDK recommended)
- Android SDK 34 with Build Tools 34.0.0

### Project Setup
1. Clone the repository to your local machine.
2. Open Android Studio and select **Open**, navigating to the `Paradox Log` root directory.
3. Allow Android Studio to sync the Gradle project.

### Known Environment & Gradle Wrapper Limitation
> **IMPORTANT BUILD NOTE:**
> In the current repository state, `gradlew` and `gradlew.bat` are placeholder shell scripts. The binary executable `gradle/wrapper/gradle-wrapper.jar` is intentionally omitted from source control to avoid committing unverified binary blobs without an external package manager.
> 
> Furthermore, on machines where `gradle` is not installed on the system `PATH`, executing `./gradlew` from the terminal will fail.
> 
> **Resolution:** When importing this project into Android Studio, allow Android Studio to generate a verified local wrapper or initialize the wrapper using an installed local Gradle distribution (`gradle wrapper --gradle-version 8.7`).

---

## 10. Contribution Rules

Before making any changes to the codebase, all developers and AI coding agents **MUST** read and adhere to:

👉 **[`AI_RULES.md`](file:///Users/ratnamsmac/Documents/Projects/Paradox%20Log/AI_RULES.md)**

### Core Architectural Mandates:
1. **Single Source of Truth:** `content/` is the sole narrative source. `GameState` is the sole runtime state container.
2. **Zero Duplication:** Never hardcode narrative copy or choices into Kotlin source code.
3. **Thin UI:** Jetpack Compose screens must never contain game rules, condition evaluations, JSON parsing, or persistence logic.
4. **Decoupled Engine:** The game engine evaluates *how* reality changes; content only describes *what* occurs.

---

## 11. Team Lead Quick Review

*A 2-minute executive brief for engineering leadership:*

### What is this?
A native Android text-based time-paradox RPG built with Kotlin and Jetpack Compose featuring an alternating Past Self / Future Self protagonist narrative.

### What is already implemented?
- **Domain Layer:** Pure Kotlin models for `Act`, `Scene`, `Dialogue`, `Choice`, `ChoiceCondition`, `ChoiceConsequence`, `Item`, and `GameFlag`.
- **State Layer:** Authoritative, immutable `GameState` holding the three core variables (`Tyrant Index`, `Timeline Stability`, `Self-Trust`), active `GameFlag` sets, inventory, timeline progression, and ending state.
- **Data Layer:** `JsonContentParser` and `ContentRepositoryImpl` providing verified `JSON → Domain Model` conversions.
- **Unit Tests:** Tests verifying model creation, state immutability, flag operations, parser fidelity, and repository behavior.

### What is currently being built?
Phase 3 (Game Engine & Rules Engine) is complete. Next is **Phase 4 (Thin Jetpack Compose UI & Navigation)**: implementing passive typography-first screens (`StoryScreen`, `ChoiceView`, `TimelineView`) and ViewModel connections.

### What is not implemented?
- Zero UI screens or Composables exist (no StoryScreen, MainMenu, or buttons).
- No narrative story text has been authored inside `content/` (schemas only).
- No database or disk persistence engine exists (schemas and memory serializers only).
- No Gradle wrapper binary exists in source control.

### What is the architecture?
Clean Architecture + MVVM with strict unidirectional flow:
`content/ → parser → domain models → repository → game engine → GameState → ViewModel → Compose UI`.

### What is the single source of truth?
- Narrative Authoring: `content/`
- Runtime Playthrough State: `app/.../game/state/GameState.kt`
- Game State Persistence Contract: `game-data/save-schema.json`

### What are the major risks?
1. **Gradle Binary Absence:** Terminal builds require Android Studio import or local Gradle to materialize `gradle-wrapper.jar`.
2. **UI Logic Bleed:** Future developers accidentally computing choices or conditions inside Compose instead of observing the rules engine.
3. **Content Duplication:** Copying story strings into Kotlin rather than reading from `content/`.

### What should the next developer work on?
Implement the pure **Jetpack Compose UI** (`app/src/main/java/com/paradoxlog/app/feature/`):
- `StoryScreen` & `StoryViewModel`: Observing `gameEngine.state` and rendering prose/dialogue.
- `ChoiceView`: Displaying choices and dispatching `OnChoiceSelected`.
- `NavigationGraph`: Declarative screen routing without gameplay rules.

### What should NOT be changed?
- Do not restructure directories or packages.
- Do not add gameplay logic or state containers to UI/ViewModels.
- Do not replace `GameFlag` with raw strings.
- Do not hardcode story dialogues into Kotlin.
