# Paradox Log — Project Roadmap

Canonical master implementation roadmap for *Paradox Log*.

> **Notice:** This document defines planned and pending project milestones, step dependencies, and scope boundaries. Historical implementation records and current build/test verifications are maintained in [`PROJECT_STATUS.md`](file:///Users/ratnamsmac/Documents/Projects/Paradox%20Log/PROJECT_STATUS.md). Authoritative sources remain:
> - Canonical Narrative: [`content/`](file:///Users/ratnamsmac/Documents/Projects/Paradox%20Log/content)
> - Declarative Schemas: [`game-data/`](file:///Users/ratnamsmac/Documents/Projects/Paradox%20Log/game-data)
> - Authoritative Runtime State: [`GameState.kt`](file:///Users/ratnamsmac/Documents/Projects/Paradox%20Log/app/src/main/java/com/paradoxlog/app/domain/model/GameState.kt)
> - Authoritative Engine & Loop: [`GameEngine.kt`](file:///Users/ratnamsmac/Documents/Projects/Paradox%20Log/app/src/main/java/com/paradoxlog/app/game/engine/GameEngine.kt)
> - Design & Specifications: [`docs/`](file:///Users/ratnamsmac/Documents/Projects/Paradox%20Log/docs), [`PRD.md`](file:///Users/ratnamsmac/Documents/Projects/Paradox%20Log/PRD.md), [`GDD.md`](file:///Users/ratnamsmac/Documents/Projects/Paradox%20Log/GDD.md), [`AI_RULES.md`](file:///Users/ratnamsmac/Documents/Projects/Paradox%20Log/AI_RULES.md)

---

## 1. Roadmap Purpose

The purpose of `PROJECT_ROADMAP.md` is to serve as the single source of truth for future development planning across all project phases. It answers:
- What phases exist and in what sequence?
- What specific steps belong to each phase?
- What has been verified as complete?
- What is the single currently approved **NEXT** implementation step?
- What remains pending in the backlog?
- What architectural dependencies gate each step?
- What work is explicitly deferred to prevent premature scope creep?

### Operational Separation
- **`PROJECT_STATUS.md`**: Historical implementation ledger, file change audits, local test execution limits, and current codebase state.
- **`PROJECT_ROADMAP.md`**: Planned, pending, and sequenced future deliverables, milestone gates, and dependency ordering.

---

## 2. Project Development Principles

1. **Single Source of Truth:** Never create competing state holders, duplicate narrative trees, or shadow domain models.
2. **Canonical Content Externalization:** Narrative prose, scene dialogues, and choice trees reside strictly in `content/`. Zero story content hardcoded in Kotlin.
3. **Pure State Unidirectionality:** `GameEngine` is the sole owner of mutable `GameState`. Compose UI and ViewModels are passive, read-only presentation projections.
4. **No Logic in UI:** Jetpack Compose never computes variable deltas, evaluates conditions, calculates timeline rules, or resolves endings.
5. **Obsidian Minimalist Design System:** Monochromatic OLED black foundation (`#0B0C0E`), typography-led visual hierarchy, 1.65 line-height ratio, restrained accent colors, zero persistent gameplay HUD clutter.
6. **Strict Bounded Execution:** Only one step is marked `NEXT` at any given time. Steps remain strictly bounded; future screens or features are never speculatively implemented.

---

## 3. Phase Overview

| Phase | Title | Status | Primary Focus |
| :--- | :--- | :---: | :--- |
| **Phase 1** | Architecture & Canonical Structure | **Complete** | Layered modular package structure and architectural boundaries |
| **Phase 2** | Domain & Content Foundation | **Complete** | Pure Kotlin models, `GameState`, JSON parsing, schemas |
| **Phase 3** | Game Engine & Rules | **Complete** | Pure condition evaluators, consequence applier, ending evaluator, `GameEngine` |
| **Phase 4** | UI & Presentation | **In Progress** | Jetpack Compose screens, design tokens, ViewModels, navigation |
| **Phase 5** | Persistence & Production Integration | **Pending** | File/DataStore save persistence, asset bundling pipeline |
| **Phase 6** | Narrative & Content Authoring | **Pending** | Full authoring of Acts I–VI canonical content in `content/` |
| **Phase 7** | Testing, Polish & Release | **Pending** | Automated test suite execution, accessibility, release build |

---

## 4. Phase 1 — Architecture & Canonical Structure

**Status:** COMPLETE

- **Purpose:** Establish clean separation between canonical narrative authoring, declarative schemas, documentation, and native Android application layers.
- **Established Structure:**
  - `content/`: Canonical authoring source of truth (Acts, Scenes, Choices JSON).
  - `game-data/`: Declarative JSON schemas (`content-schema.json`, `save-schema.json`).
  - `docs/`: Architecture, design system, narrative matrices, and screen flows.
  - `app/src/main/java/com/paradoxlog/app/`: Clean Architecture layers (`core`, `data`, `domain`, `game`, `feature`, `di`).

---

## 5. Phase 2 — Domain & Content Foundation

**Status:** COMPLETE

- **Purpose:** Define core business abstractions and runtime state models decoupled from the Android framework.
- **Key Deliverables:**
  - Domain models: `Act`, `Scene`, `Dialogue`, `Choice`, `ChoiceCondition`, `ChoiceConsequence`, `Item`, `GameFlag`.
  - Authoritative runtime container: `GameState` with immutable sub-states (`GameVariables`, `InventoryState`, `TimelineProgress`, `EndingState`).
  - Content parsing & repository: `JsonContentParser`, `ContentRepositoryImpl`.
  - JSON schemas: `content-schema.json`, `save-schema.json`.
  - Foundational unit test suites: `JsonContentParserTest`, `ContentRepositoryTest`.

---

## 6. Phase 3 — Game Engine & Rules

**Status:** COMPLETE

- **Purpose:** Implement stateless gameplay rule evaluators and authoritative state orchestration.
- **Key Deliverables:**
  - `ConditionEvaluator`: Pure, read-only evaluation of flags, variable thresholds, and required items.
  - `ConsequenceApplier`: Pure mathematical delta application with `[0, 100]` variable clamping.
  - `ProgressionHandler`: Pure scene transition and act completion handler.
  - `EndingEvaluator`: Isolated ending eligibility resolution (Act VI narrative thresholds remain `[TBD]`).
  - `GameSaveManager`: Stateless bidirectional 1:1 mapping against `save-schema.json`.
  - `DefaultGameEngine`: Central orchestration owning mutable `GameState` via `StateFlow<GameState>`.
  - Engine tests: `ConsequenceApplierTest`, `EndingEvaluatorTest`, `GameEngineTest`, `GameSaveManagerTest`.

---

## 7. Phase 4 — UI & Presentation

**Status:** IN PROGRESS (Steps 1–7 Complete; Step 8 Next; Steps 9–15 Pending)

### Step 1 — Compose / Design System Foundation
- **Status:** COMPLETE
- **Purpose:** Define typography, color tokens, and spacing for the minimalist obsidian aesthetic.
- **Depends on:** Phase 1, Phase 2
- **Key deliverables:** `Color.kt`, `Spacing.kt`, `Typography.kt`, `Theme.kt`.
- **Completion criteria:** Dark-only OLED baseline (`#0B0C0E`), 8dp grid spacing, 1.65 line-height ratio.

### Step 2 — ViewModel Contracts
- **Status:** COMPLETE
- **Purpose:** Establish unidirectional presentation contracts connecting engine state to Compose.
- **Depends on:** Step 1, Phase 3
- **Key deliverables:** `GameUiState.kt`, `GameUiEffect.kt`, `GameViewModel.kt`, `GameViewModelTest.kt`.
- **Completion criteria:** `GameUiState` is a passive projection; zero gameplay rules in ViewModel.

### Step 3 — Navigation Graph & Destination Contracts
- **Status:** COMPLETE
- **Purpose:** Establish type-safe navigation graph using immutable route parameters.
- **Depends on:** Step 2
- **Key deliverables:** `ParadoxDestination.kt`, `ParadoxNavHost.kt`, `ParadoxDestinationTest.kt`.
- **Completion criteria:** Route arguments carry primitive IDs only; zero game state objects in backstack.

### Step 4 — Story Screen
- **Status:** COMPLETE
- **Purpose:** Primary narrative reading canvas displaying dialogue, act/scene header, and typography-first prose.
- **Depends on:** Step 1, Step 2, Step 3
- **Key deliverables:** `StoryHeader.kt`, `NarrativeContent.kt`, `StoryScreen.kt`, `StoryScreenStateTest.kt`.
- **Completion criteria:** Clean reading canvas; zero invented fallback strings; no persistent HUD.

### Step 5 — Choice Component & Presentation Cleanup
- **Status:** COMPLETE
- **Purpose:** Render choices as interactive cards emitting `choiceId` without simulating consequence or navigation.
- **Depends on:** Step 4
- **Key deliverables:** `ChoiceItem.kt`, `ChoiceComponent.kt`, `UiLabels.kt`, `ChoiceComponentTest.kt`.
- **Completion criteria:** 56dp min height touch targets, disabled locked choices, strictly emits `choiceId`.

### Step 6 — Consequence Presentation
- **Status:** COMPLETE
- **Purpose:** Present brief visual feedback on choice consequence deltas before advancing.
- **Depends on:** Step 5
- **Key deliverables:** `ConsequenceUiState.kt`, `ConsequenceView.kt`, `activeConsequence` in `GameUiState`, `ConsequenceViewTest.kt`.
- **Completion criteria:** Displays only non-zero variable shifts from authoritative consequence; no consequence math in UI.

### Step 7 — Timeline Change / Log Integration
- **Status:** COMPLETE
- **Purpose:** Present authoritative timeline state-change confirmation resulting from player choices.
- **Depends on:** Step 6
- **Key deliverables:** `TimelineChangeUiState.kt`, `TimelineChangeView.kt`, `activeTimelineChange` in `GameUiState`, integration in `StoryScreen`, unit tests.
- **Completion criteria:** Confirms authoritative timeline state (`actId`, `sceneId`, recorded decision, node counts, stability); zero invented narrative descriptions or shadow history models.

### Step 8 — Exploration Screen
- **Status:** NEXT
- **Purpose:** Render environment points of interest and spatial investigation choices.
- **Depends on:** Step 7
- **Key deliverables:** `ExplorationUiState`, `ExplorationView`, navigation destination, unit tests.
- **Completion criteria:** Minimalist location inspection; dispatches choice selections to engine.

### Step 9 — Encounter Screen
- **Status:** PENDING
- **Purpose:** Render character interactions, tension indicators, and direct dialogue branches.
- **Depends on:** Step 8
- **Key deliverables:** `EncounterUiState`, `EncounterView`, unit tests.
- **Completion criteria:** Focused character dialogue canvas driven strictly by authoritative scene dialogue models.

### Step 10 — Timeline Node Graph View
- **Status:** PENDING
- **Purpose:** Read-only modal/screen displaying past visited nodes, decisions made, and divergent branches.
- **Depends on:** Step 9
- **Key deliverables:** `TimelineGraphUiState`, `TimelineGraphScreen`, unit tests.
- **Completion criteria:** Visualizes `GameState.timeline` without mutating or simulating branch paths.

### Step 11 — Inventory Drawer / Sheet
- **Status:** PENDING
- **Purpose:** Minimalist modal inspection sheet reviewing acquired timeline artifacts.
- **Depends on:** Step 10
- **Key deliverables:** `InventoryUiState`, `InventorySheet`, unit tests.
- **Completion criteria:** Renders `GameState.inventory` items loaded from `ContentRepository`.

### Step 12 — Act Transition Screen
- **Status:** PENDING
- **Purpose:** Interstitial canvas presented when an act is completed, auditing timeline stability before next act.
- **Depends on:** Step 11
- **Key deliverables:** `ActTransitionUiState`, `ActTransitionScreen`, unit tests.
- **Completion criteria:** Displayed strictly upon engine act completion; triggers `advanceAct`.

### Step 13 — Ending Screen
- **Status:** PENDING
- **Purpose:** Narrative epilogue canvas displaying final resolution metrics based on resolved ending state.
- **Depends on:** Step 12
- **Key deliverables:** `EndingUiState`, `EndingScreen`, unit tests.
- **Completion criteria:** Renders 1 of 5 canonical endings from `EndingEvaluator`; offers New Game+ restart.

### Step 14 — Settings Screen
- **Status:** PENDING
- **Purpose:** Accessibility and comfort controls (font scaling, haptics, motion toggle).
- **Depends on:** Step 13
- **Key deliverables:** `SettingsUiState`, `SettingsScreen`, unit tests.
- **Completion criteria:** Manages UI presentation preferences without altering `GameState`.

### Step 15 — UI & Navigation Test Pass
- **Status:** PENDING
- **Purpose:** Comprehensive integration pass across all navigation destinations and Compose flows.
- **Depends on:** Steps 1–14
- **Key deliverables:** End-to-end Compose navigation tests, accessibility audit.
- **Completion criteria:** Full navigation flow verified against canonical loop without backstack memory leaks.

---

## 8. Phase 5 — Persistence & Production Integration

**Status:** PENDING

- **Purpose:** Implement durable, schema-validated disk persistence and build-time content bundling.
- **Key Deliverables:**
  - File-backed save persistence implementing `GameSaveManager`.
  - Android DataStore / storage integration for active session state.
  - Production content packaging pipeline (bundling `content/` JSON into runtime assets).
  - Content loading integration for device runtime.
  - Save schema versioning and migration strategy `[TBD]`.
  - Automated persistence unit and integration tests.

---

## 9. Phase 6 — Narrative & Content Authoring

**Status:** PENDING

- **Purpose:** Author and validate the canonical six-act narrative content in `content/` JSON format.
- **Key Deliverables:**
  - Act I: Threshold (`act-01.json`) — Full scene tree and choice consequence calibration.
  - Act II: Fracture (`act-02.json`) `[TBD]`.
  - Act III: Divergence (`act-03.json`) `[TBD]`.
  - Act IV: Convergence (`act-04.json`) `[TBD]`.
  - Act V: Collapse (`act-05.json`) `[TBD]`.
  - Act VI: Resolution (`act-06.json`) `[TBD]`.
  - Characters, locations, timeline relics, paradox anomalies definitions.
  - Ending threshold calibration for all 5 philosophical outcomes `[TBD]`.
- **Authoring Rule:** `content/` is the sole source of truth. Narrative text is never authored or duplicated in Kotlin code.

---

## 10. Phase 7 — Testing, Polish & Release

**Status:** PENDING

- **Purpose:** Full quality assurance, accessibility compliance, performance profiling, and production readiness.
- **Key Deliverables:**
  - Full automated test suite execution across JVM and Android test harnesses.
  - Compose UI and accessibility verification (minimum 48dp touch targets, font scaling, contrast).
  - Memory leak and navigation backstack profiling.
  - Release build verification (minifyEnabled, ProGuard/R8 rules).
  - Regression testing against core game loop.
  - Content validation against `content-schema.json`.

---

## 11. Dependency Order

### Master Phase Dependencies
```text
Phase 1: Architecture & Canonical Structure
   ↓
Phase 2: Domain & Content Foundation
   ↓
Phase 3: Game Engine & Rules
   ↓
Phase 4: UI & Presentation
   ↓
Phase 5: Persistence & Production Integration
   ↓
Phase 6: Narrative & Content Authoring
   ↓
Phase 7: Testing, Polish & Release
```

### Phase 4 Canonical Gameplay Presentation Flow
```text
Story (Step 4)
  ↓
Choice (Step 5)
  ↓
Consequence (Step 6)
  ↓
Timeline Change (Step 7)
  ↓
Exploration (Step 8)
  ↓
Encounter (Step 9)
  ↓
Story (Step 4)
```

*Note: Auxiliary destinations (Timeline Node Graph, Inventory Sheet, Act Transition, Ending Screen, Settings) support this loop without replacing its core sequence.*

---

## 12. Current Next Step

- **NEXT:** Phase 4 — Step 8: Exploration Screen
- **Status:** Step 7 — Timeline Change Presentation / Log Integration is COMPLETE. Step 8 has not yet been implemented.
- **Prerequisites Complete:** Steps 1–7 (Compose Foundation through Timeline Change Presentation) are COMPLETE and approved.
- **Scope Restriction:** Implement strictly the Exploration Screen location/environment presentation layer. Do not implement Encounter Screen or subsequent roadmap destinations.

---

## 13. Pending Work Summary

```text
Phase 1 — Architecture & Canonical Structure: COMPLETE
Phase 2 — Domain & Content Foundation: COMPLETE
Phase 3 — Game Engine & Rules: COMPLETE

Phase 4 — UI & Presentation: IN PROGRESS
  [COMPLETE] Step 1: Compose / Design System Foundation
  [COMPLETE] Step 2: ViewModel Contracts
  [COMPLETE] Step 3: Navigation Graph & Destinations
  [COMPLETE] Step 4: Story Screen
  [COMPLETE] Step 5: Choice Component & Cleanup
  [COMPLETE] Step 6: Consequence Presentation
  [COMPLETE] Step 7: Timeline Change / Log Integration
  [NEXT]     Step 8: Exploration Screen
  [PENDING]  Step 9: Encounter Screen
  [PENDING]  Step 10: Timeline Node Graph View
  [PENDING]  Step 11: Inventory Drawer / Sheet
  [PENDING]  Step 12: Act Transition Screen
  [PENDING]  Step 13: Ending Screen
  [PENDING]  Step 14: Settings Screen
  [PENDING]  Step 15: UI & Navigation Test Pass

Phase 5 — Persistence & Production Integration: PENDING
  [PENDING] File-backed save persistence
  [PENDING] DataStore integration
  [PENDING] Build-time asset packaging pipeline
  [PENDING] Save versioning & migration strategy

Phase 6 — Narrative & Content Authoring: PENDING
  [PENDING] Acts II through VI authoring in content/
  [PENDING] Narrative ending threshold calibration
  [PENDING] Relic and anomaly definitions

Phase 7 — Testing, Polish & Release: PENDING
  [PENDING] Automated CI test suite execution
  [PENDING] Accessibility verification pass
  [PENDING] Release packaging and ProGuard optimization
```

---

## 14. Explicitly Deferred Work

To prevent scope creep and maintain architectural boundaries, the following work items are explicitly deferred and must **NOT** be implemented at this stage:
- **Encounter Screen (Step 9):** Deferred until Step 8 is complete and approved.
- **Timeline Node Graph View (Step 10):** Deferred until core presentation stages are complete.
- **Inventory Drawer / Relic Sheet (Step 11):** Deferred until Step 10 is complete.
- **Act Transition Screen (Step 12):** Deferred until act boundary requirements are reached.
- **Ending Screen (Step 13):** Deferred until ending content thresholds are calibrated.
- **Settings & Accessibility Preferences (Step 14):** Deferred to late UI phase.
- **File / DataStore Persistence (Phase 5):** Deferred until UI contracts are stabilized.
- **Narrative Prose Authoring for Acts II–VI (Phase 6):** Deferred until engine and UI pipelines are complete.
- **Production Release Profiling & Bundling (Phase 7):** Deferred until content and feature completion.

---

## 15. Roadmap Maintenance Rules

1. `PROJECT_STATUS.md` records what has actually happened (implementation ledger and verified status).
2. `PROJECT_ROADMAP.md` records what is planned, pending, and scheduled.
3. Never mark work complete until it is actually implemented and verified.
4. Never invent requirements to fill roadmap gaps; use `[TBD]` where decisions are not yet finalized.
5. Completed steps remain in the roadmap as historical milestones, but their detailed file changes and verification notes belong in `PROJECT_STATUS.md`.
6. Every approved implementation step must update both files:
   - `PROJECT_STATUS.md` $\rightarrow$ detailed implementation record
   - `PROJECT_ROADMAP.md` $\rightarrow$ milestone status transition
7. Only one implementation step must be marked **NEXT** at any given time.
8. Future steps remain **PENDING** until all upstream dependencies are satisfied.
9. Do not use the roadmap as a substitute for `PRD.md`, `GDD.md`, `docs/architecture.md`, or canonical `content/`.
10. All roadmap updates must preserve the permanent mandate: *No story, game state, rule, navigation route, or UI component should have multiple competing sources of truth.*
