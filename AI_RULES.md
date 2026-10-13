# Paradox Log — AI & Developer Architecture Rules

> **PERMANENT CORE MANDATE:**
> **No story, game state, rule, navigation route, or UI component should have multiple competing sources of truth.**

---

## 1. Architectural Integrity Rules

1. **Do Not Restructure:**
   Do not rename, relocate, merge, or create alternative copies of the established package structure (`core`, `data`, `domain`, `game`, `feature`, `di`, `content`, `game-data`).
2. **Single Responsibility:**
   Every directory, layer, and class has one clear responsibility. Do not create multi-purpose dumping grounds (e.g., generic `utils` folders containing business logic).
3. **No Parallel Architectures:**
   Never introduce alternative state managers, duplicate repositories, or shadow models. Always extend the established architecture.

---

## 2. Content & Narrative Rules

1. **`content/` Is Canonical:**
   The external `content/` directory is the **sole** authoring source of truth for all acts, scenes, dialogues, choices, characters, and items.
2. **Never Duplicate in Kotlin:**
   Never hardcode story dialogue, scene text, or choice trees inside Kotlin classes, ViewModels, or Composables.
3. **No Runtime Asset Duplication:**
   Do not manually duplicate JSON files into `app/src/main/assets/` or `res/`. Runtime packaging must occur via the automated build pipeline.

---

## 3. Game State Rules

1. **Authoritative State Container:**
   `app/src/main/java/com/paradoxlog/app/game/state/GameState.kt` is the **only** authoritative runtime state representation.
2. **No Competing States:**
   Do not create separate state containers (e.g., `StoryState`, `ChoiceState`, `TimelineState`, `EndingState`) that duplicate variables, flags, or coordinates belonging to `GameState`.
3. **Immutability:**
   `GameState` is strictly immutable. State transitions are achieved exclusively by emitting new copied instances through the game engine.
4. **Single-Source Global Variables:**
   `Tyrant Index`, `Timeline Stability`, and `Self-Trust` must be read from and written to `GameState.variables` exclusively.

---

## 4. UI Layer Rules (Jetpack Compose)

1. **Thin UI Mandate:**
   Composables and ViewModels are strictly passive presentation and event-forwarding mechanisms:
   ```text
   UI displays state  ──▶  User selects choice  ──▶  ViewModel forwards event to Game Engine
   ```
2. **Strict UI Prohibitions:**
   Jetpack Compose screens and ViewModels must **NEVER**:
   - Calculate or mutate game variables directly.
   - Evaluate choice conditions or consequences.
   - Contain story branching or ending eligibility logic.
   - Parse JSON or read raw content files.
   - Directly invoke SQLite, Room, DataStore, or file persistence.

---

## 5. Game Logic & Rules Engine Rules

1. **Rules Isolation:**
   All gameplay evaluations (choice availability, variable delta calculations, paradox backlashes, ending resolutions) belong strictly in `app/src/main/java/com/paradoxlog/app/game/rules/` and `game/engine/`.
2. **Content vs. Rules Distinction:**
   - **Content describes WHAT happens** (declarative IDs, text, destination pointers).
   - **Rules determine HOW the game responds** (mathematical adjustments, condition gating, ending eligibility).

---

## 6. Repository & Data Layer Rules

1. **Content Retrieval Only:**
   Repositories (`ContentRepository`) exist solely to provide access to domain content (`Act`, `Scene`, `Item`).
2. **No State Ownership:**
   Repositories must not own `GameState`, mutate playthrough variables, or evaluate narrative rules.

---

## 7. Parser Rules

1. **Pure Transformation:**
   Parsers (`ContentParser`, `JsonContentParser`) must strictly perform:
   ```text
   JSON  ──▶  Domain Model
   ```
2. **No Business Logic:**
   Parsers must never evaluate conditions, apply consequences, or alter runtime game state.

---

## 8. Documentation & Status Rules

1. **Keep Documentation Synchronized:**
   Whenever architecture, models, or contracts change, immediately update the corresponding documentation files.
2. **Explicit Status Labels:**
   Always mark features with exact status tags: `[IMPLEMENTED]`, `[PARTIALLY IMPLEMENTED]`, `[PLANNED]`, `[TBD]`, or `[BLOCKED]`. Never claim a feature is implemented when it is only designed.
3. **Zero Stale Documentation:**
   Remove obsolete descriptions and never leave contradictory statements across files.

---

## 9. Dependency Discipline

1. **No Gratuitous Libraries:**
   Do not add libraries for convenience. Prefer standard Kotlin, standard Android APIs (`org.json`), and established AndroidX libraries.
2. **No External Game Engines:**
   Do not import third-party game frameworks or narrative scripting engines. *Paradox Log* runs on its own pure Kotlin game engine.

---

## 10. AI Coding Agent Mandatory Behaviors

All automated AI coding assistants must adhere to the following workflow:

1. **Inspect Before Coding:** Always read existing models, interfaces, and architecture docs before modifying or generating code.
2. **Reuse Existing Abstractions:** Always utilize existing domain models (`Act`, `Scene`, `Choice`, `GameFlag`, `GameState`). Never create duplicate or parallel representations.
3. **Never Speculate or Overengineer:** Implement only what is explicitly requested for the current milestone. Do not generate speculative classes or unused generic abstractions.
4. **Stop and Ask on Structural Conflicts:** If a requested task would require breaking the single source of truth or reorganizing packages, stop and request clarification.
5. **Never Silently Relocate Files:** Preserve established file locations.
6. **Zero Content Invention:** Do not write fictional story dialogues or scene text in code; content authoring belongs in `content/`.
7. **Honest Build Reporting:** Never claim code compiles or tests pass unless the build and test suites were actually executed and verified in the active shell environment.
