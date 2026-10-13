# Paradox Log — Product Requirements Document (PRD)

---

## 1. Product Vision

*Paradox Log* is a minimalist, narrative-driven time-paradox RPG for Android that explores the ethical complexity of determinism, free will, and personal sacrifice. By placing the player in control of two alternating temporal incarnations of the same individual—one trying to prevent a future collapse, the other having become a ruthless authoritarian to survive it—the game transforms temporal paradox from a puzzle mechanic into an intimate psychological inquiry.

---

## 2. Problem & Market Opportunity

### The Problem
Most time-travel games treat temporal mechanics as superficial branching trees, undo buttons, or external puzzles. Choices frequently feel cosmetic, consequences are isolated into localized dialogue variations, and antagonists are externalized villains rather than reflections of the protagonist's own decisions.

### The Opportunity
There is a passionate audience for focused, contemplative, high-stakes narrative games (such as *A Dark Room*, *Lifeline*, and *80 Days*) that prioritize text elegance, deep systemic agency, and moral ambiguity over bloated graphics and gameplay padding. *Paradox Log* fills this space by offering a premium, iOS-inspired minimalist aesthetic on native Android with a mathematically consistent, single-state temporal consequence engine.

---

## 3. Target Experience

- **Tone:** Intellectual, melancholic, cerebral, claustrophobic, ethically demanding.
- **Pacing:** Thoughtful reading punctuated by high-tension choice junctures where consequences carry irreversible permanence.
- **Atmosphere:** Minimalist stark typography, deep negative space, restrained ambient soundscapes, and intense psychological focus.
- **Player Emotion:** Dread of complicity, moral paralysis, curiosity regarding temporal causality, and personal responsibility.

---

## 4. Core Player Question

> *"If your future self became a monster to prevent a catastrophe, would you stop them—even if stopping them causes the catastrophe?"*

---

## 5. Core Product Pillars

1. **Meaningful Choices:** Every choice alters global variables, sets persistent temporal flags, or modifies inventory. No choices exist merely for cosmetic filler.
2. **Permanent Consequences:** Decisions propagate across acts; an action taken in Act I alters the structural reality and moral posture encountered in Act V.
3. **Identity & Duplicity:** The protagonist is both hero and tyrant. The player must reconcile the ideals of the youth with the horrifying calculations of the survivor.
4. **Time Paradox Mechanics:** Time is not an infinite sandbox; it possesses a measured stability metric. Excess divergence triggers catastrophic reality collapse.
5. **Moral Ambiguity:** No clear "good" or "evil" alignment meters. The Tyrant Index measures ruthless pragmatism, not cartoon villainy.
6. **High Replayability:** Distinct narrative branches, divergent act pathways, five mutually exclusive endings, and a New Game+ timeline memory layer.

---

## 6. Core Features & Status

| Feature | Description | Status |
| :--- | :--- | :--- |
| **Dual-Perspective Narrative** | Alternating perspectives between Past Self (Acts I, III, V) and Future Self (Acts II, IV, VI) | `[PLANNED]` (Architecture supports; content pending) |
| **Global State Variables** | Quantitative metrics: Tyrant Index, Timeline Stability, Self-Trust | `[IMPLEMENTED]` (Domain & state models complete) |
| **Dynamic Narrative Flags** | Type-safe persistent narrative event flags (`GameFlag`) | `[IMPLEMENTED]` |
| **Inventory & Relic System** | 5 core timeline artifacts (`Chrono Blade`, `Aion Compass`, etc.) | `[PARTIALLY IMPLEMENTED]` (Models ready; logic pending) |
| **Timeline Consequence Engine** | Mathematical consequence application calculating variable deltas | `[IMPLEMENTED]` (`ConsequenceApplier`, `ConditionEvaluator`, `GameEngine`) |
| **Multiple Endings** | 5 distinct philosophical outcomes based on final state | `[PARTIALLY IMPLEMENTED]` (`EndingEvaluator` & collapse interface; Act VI thresholds `[TBD]`) |
| **Timeline Review / Log** | Visual chronology tracking visited nodes and decision points | `[PLANNED]` (Data state exists; UI pending) |
| **New Game+ Loop** | Unlocked memory fragments and persistent temporal awareness | `[PLANNED]` |

---

## 7. User Experience Goals

- **Typography-First Minimalism:** Narrative copy and decision prompts are the primary visual heroes. Zero superfluous ornamentation.
- **Low Visual Noise:** Ample whitespace, absence of cluttered dashboards, minimal card borders, and high legibility.
- **Calm & Deliberate Interaction:** Generous touch targets, subtle micro-animations on state transitions, and no intrusive flashing UI elements.
- **Zero Information Redundancy:** Data shown on screen must be essential to current agency; players are never overwhelmed with duplicate stat bars.

---

## 8. Non-Goals

- **Not an Action Game:** Zero real-time combat, reflex checks, or arcade mini-games.
- **Not a 3D/Heavy Asset Title:** No high-polygon models, pre-rendered CGI cutscenes, or heavy graphical shaders.
- **Not a Live-Service / Free-to-Play Product:** Zero microtransactions, gacha mechanics, energy bars, push notifications, or ad networks.
- **Not a Standard "Branch-and-Forget" Visual Novel:** Divergences must mathematically affect the global reality parameters, not just alter immediate character sprite dialogue.

---

## 9. Product Requirements

### 9.1 Functional Requirements
- **FR-01:** System must maintain an authoritative runtime `GameState` holding `variables`, `flags`, `inventory`, `timeline`, and `ending`.
- **FR-02:** System must gate player choices using declarative conditions (`ChoiceCondition`) checking flags, variable thresholds, and required items.
- **FR-03:** Choice selection must apply `ChoiceConsequence` deltas to produce a new immutable `GameState`.
- **FR-04:** Content must be loaded and parsed from external canonical JSON definitions under `content/` without modifying code.
- **FR-05:** State persistence must strictly conform to `game-data/save-schema.json`.

### 9.2 UX & Design Requirements
- **UX-01:** UI must conform to modern Material 3 design tokens while adopting an iOS-inspired clean typographic hierarchy.
- **UX-02:** Dialogue must support fluid reading with smooth, discrete transition into choice selection.
- **UX-03:** Visual feedback on state changes (e.g., Timeline Stability drop) must be subtle and atmospheric.
- **UX-04:** Full support for accessibility: dynamic text scaling, minimum 48dp touch targets, high contrast ratios.

### 9.3 Technical Requirements
- **TR-01:** Android SDK Min 26, Target 34.
- **TR-02:** UI completely built using Jetpack Compose; no legacy Android XML layouts.
- **TR-03:** Strict Clean Architecture: zero UI or Android framework dependencies in the `domain` or `game` modules.
- **TR-04:** Unidirectional data flow: UI emits user events to ViewModel; ViewModel triggers Game Engine; UI observes `StateFlow<GameState>`.

### 9.4 Content Requirements
- **CR-01:** Six complete narrative acts with structured scenes, choices, and dialogue trees.
- **CR-02:** Canonical content authored solely within `content/acts/`, `content/characters/`, `content/items/`, and `content/endings/`.

---

## 10. Success Criteria

1. **Architectural Integrity:** Zero circular dependencies, zero duplicate state models, and 100% adherence to single-source-of-truth guidelines.
2. **Player Agency Perception:** Playtesters report that choices feel impactful, morally challenging, and temporally consequential.
3. **Stability & Performance:** Constant 60 FPS UI rendering on low-tier Android devices (API 26+), with cold boot to initial interaction under 1.5 seconds.
4. **Narrative Completion:** All five narrative endings are reachable and deterministically verifiable based on documented player decisions.

---

## 11. Document Hierarchy & Source of Truth

- **`PRD.md` (This Document):** Source of truth for product vision, player requirements, feature scope, and success criteria.
- **`README.md`:** Primary project entry point and high-level team overview.
- **`GDD.md`:** Source of truth for game mechanics, act progression, tools, and narrative design.
- **`AI_RULES.md`:** Authoritative constraints for engineering and AI coding agents.
- **`docs/architecture.md`:** Source of truth for technical software architecture and data pipelines.
- **`content/`:** Canonical authoring source of truth for narrative dialogue and story JSON.
- **`game-data/`:** Canonical source of truth for global variable schemas and save state contracts.
- **Kotlin Source (`app/src/`):** Authoritative software implementation.

