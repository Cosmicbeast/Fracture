# Paradox Log — Game Design Document (GDD)

---

## 1. Game Overview

- **Title:** Paradox Log
- **Genre:** Text-Based Time-Paradox RPG
- **Theme:** Philosophical Science Fiction, Temporal Determinism, Moral Pragmatism
- **Platform:** Native Android (Jetpack Compose)
- **Design Stance:** Minimalist, typography-centered, systemic narrative consequence engine.

*Paradox Log* puts the player in control of two temporal manifestations of the same person across an alternating six-act structure. As the Past Self, the player attempts to prevent a catastrophic timeline collapse. As the Future Self, the player has survived that collapse by adopting brutal, authoritarian methods to preserve humanity's remaining timeline remnants. The choices made in both timelines converge toward a definitive final judgment.

---

## 2. Core Gameplay Loop

```text
 ┌─────────────────────────────────────────────────────────────┐
 │ 1. Narrative Presentation (Dialogue, Scene Setting, Logs)   │
 └──────────────────────────────┬──────────────────────────────┘
                                │
                                ▼
 ┌─────────────────────────────────────────────────────────────┐
 │ 2. Decision Juncture (Player evaluates available choices)   │
 │    - Evaluates visible text, conditions, and costs          │
 └──────────────────────────────┬──────────────────────────────┘
                                │
                                ▼
 ┌─────────────────────────────────────────────────────────────┐
 │ 3. Consequence Evaluation (Rules Engine executes choice)    │
 │    - Mutates Tyrant Index, Timeline Stability, Self-Trust   │
 │    - Sets or clears dynamic persistent GameFlags            │
 │    - Updates Inventory items and relic states               │
 └──────────────────────────────┬──────────────────────────────┘
                                │
                                ▼
 ┌─────────────────────────────────────────────────────────────┐
 │ 4. Timeline Propagation (Next Scene / Act Transition)       │
 │    - Logs decision in TimelineProgress map                  │
 │    - Checks for paradox triggers or ending conditions       │
 └──────────────────────────────┬──────────────────────────────┘
                                │
                                ▼
 ┌─────────────────────────────────────────────────────────────┐
 │ 5. Final Act Resolution (1 of 5 philosophical endings)      │
 └─────────────────────────────────────────────────────────────┘
```

---

## 3. Player Perspectives

The game utilizes a dual-protagonist structure where both perspectives belong to the same soul at different chronological milestones:

1. **The Past Self (The Architect of Intent):**
   - **Playable In:** Act I, Act III, Act V
   - **Mindset:** Idealistic, desperate, inquisitive, terrified of moral compromise.
   - **Focus:** Uncovering the root cause of the imminent timeline breach, investigating paradox anomalies, and attempting to fix reality without sacrificing humanity.

2. **The Future Self (The Sovereign of Necessity):**
   - **Playable In:** Act II, Act IV, Act VI
   - **Mindset:** Cynical, calculated, pragmatic, emotionally detached, authoritarian.
   - **Focus:** Managing the fragile remnant of the shattered timeline, eliminating destabilizing temporal anomalies, and neutralizing the reckless interference of their own past self.

---

## 4. Six-Act Structure

```
Act I (Past Self)   ──▶ Act II (Future Self)  ──▶ Act III (Past Self)
       │                        │                        │
       ▼                        ▼                        ▼
Act IV (Future Self) ──▶ Act V (Past Self)    ──▶ Act VI (Confrontation)
```

### Act I — The Consequence
- **Protagonist:** Past Self
- **Setting:** Sector 0 Chrono-Facility (Hours prior to initial breach)
- **Narrative Purpose:** Introduce the baseline world, the initial timeline anomaly, and the mysterious authoritarian messages arriving from the future.
- **Major Events:** Discovery of temporal telemetry showing an impending universal collapse; interception of an encrypted transmission signed by the protagonist's own biometric cipher.
- **Major Decisions:** Whether to trust the encrypted warnings, override safety protocols, or alert the supervisory council.
- **Consequences:** Determines the initial baseline of **Self-Trust** and sets initial facility security flags.
- **Important Reveals:** The entity commanding the quarantine protocol in the future is the protagonist's future self.

### Act II — The First Interference
- **Protagonist:** Future Self
- **Setting:** The Remnant Citadel (Decades post-collapse)
- **Narrative Purpose:** Shift perspective to the Future Self dealing with the cascading fallout caused by the Past Self's actions in Act I.
- **Major Events:** Detecting a timeline ripple caused by Act I decisions; enforcing severe rationing and quarantine to prevent the sector from dissolving into void.
- **Major Decisions:** Suppress an uprising of timeline refugees or sacrifice temporal stability to grant them safe passage.
- **Consequences:** Directly alters **Tyrant Index** and impacts **Timeline Stability**.
- **Important Reveals:** The Future Self's harshness is motivated by mathematical calculations showing that compassion accelerates entropy.

### Act III — The Correction
- **Protagonist:** Past Self
- **Setting:** Sub-orbital Relay Station / The Chrono-Vaults
- **Narrative Purpose:** The Past Self attempts an aggressive timeline correction to undo the atrocities witnessed in Future Self's logs.
- **Major Events:** Infiltration of the core temporal vaults to retrieve the Aion Compass; encountering paradox phantoms left by failed previous loops.
- **Major Decisions:** Whether to destroy the facility core to halt temporal research or steal the temporal tools to attempt surgical intervention.
- **Consequences:** Sets key inventory flags (`chrono_blade`, `aion_compass`); tests **Timeline Stability**.
- **Important Reveals:** Every attempt to prevent the future has already occurred in previous iterations of the log.

### Act IV — The Origin
- **Protagonist:** Future Self
- **Setting:** The Nexus Spire (During the apex of the Catastrophe)
- **Narrative Purpose:** Flashback / chronicle revealing the exact moment the Future Self made the choice to abandon idealism and become the monster.
- **Major Events:** A catastrophic paradox failure where sparing a single life resulted in the erasure of millions; the birth of the "Tyrant" doctrine.
- **Major Decisions:** Re-enact the pivotal historical execution or seek an untested third alternative.
- **Consequences:** Sets permanent character alignment flags; establishes ending eligibility branches.
- **Important Reveals:** The Future Self did not choose tyranny out of malice, but because mercy reliably collapsed the timeline.

### Act V — The Necessary Monster
- **Protagonist:** Past Self
- **Setting:** The Singularity Chamber (Point of no return)
- **Narrative Purpose:** The Past Self discovers that all historical records point to them becoming the very tyrant they detest.
- **Major Events:** Confrontation with temporal projections of the future; discovery of the Codex of Regression; realizing the timeline demands a sacrifice.
- **Major Decisions:** Accept the mantle of the future tyrant to preserve existence, or reject it entirely knowing it may dissolve reality.
- **Consequences:** Extreme delta shifts across all three global variables; final locking of ending paths.
- **Important Reveals:** The loop cannot be resolved by standard choice logic; it requires an ideological alignment.

### Act VI — The Final Judgment
- **Protagonist:** Convergence (Past Self and Future Self face-to-face)
- **Setting:** The Atemporal Core / The Final Horizon
- **Narrative Purpose:** Climax and resolution. The player reconciles or destroys the two incarnations based on the entire cumulative playthrough state.
- **Major Events:** Direct philosophical confrontation between Past Self and Future Self; reading of the complete Paradox Log.
- **Major Decisions:** Final choice executing 1 of 5 philosophical endings.
- **Consequences:** Permanent narrative resolution and timeline settlement.
- **Important Reveals:** The true origin of the initial anomaly.

---

## 5. Global State Variables

The game engine tracks three core systemic variables that define character morality, world state, and ending eligibility:

| Variable | Type | Range | Conceptual Role |
| :--- | :--- | :--- | :--- |
| **Tyrant Index** | `Int` | `0` to `100+` | Measures ruthless pragmatism, authoritarian enforcement, and willingness to sacrifice individuals for timeline survival. |
| **Timeline Stability** | `Int` | `0` to `100` | Quantifies the physical integrity of the active reality. Reaching `0` triggers catastrophic paradox failure. |
| **Self-Trust** | `Int` | `0` to `100` | Measures psychological coherence and alignment between Past Self and Future Self. High trust enables reconciliation; low trust breeds mutual destruction. |

*Note: Specific ending threshold boundaries are `[PLANNED]` for Phase 3 calibration and are not arbitrarily hardcoded.*

---

## 6. Tools & Artifacts

Players acquire and utilize five key timeline relics throughout the narrative:

1. **Chrono Blade:**
   - *Narrative Purpose:* A weapon forged from stabilized temporal shear.
   - *Gameplay Role:* Capable of severing causality links, eliminating temporal anomalies, or executing irreversible character eliminations.
2. **Aion Compass:**
   - *Narrative Purpose:* A sensory instrument that reads timeline divergence vectors.
   - *Gameplay Role:* Unlocks hidden decision options by detecting the stability cost of choices in advance.
3. **Paradox Gauntlet:**
   - *Narrative Purpose:* Handwear that absorbs local paradox backlashes.
   - *Gameplay Role:* Allows the player to make extreme choices without immediately crashing Timeline Stability.
4. **Codex of Regression:**
   - *Narrative Purpose:* The complete compiled record of previous failed timeline loops.
   - *Gameplay Role:* Grants access to retroactive lore, secret flags, and alternate dialogue branches.
5. **Singularity Core:**
   - *Narrative Purpose:* The beating heart of the original temporal breach.
   - *Gameplay Role:* Key component required to trigger global resets or total timeline reunification in Act VI.

---

## 7. Timeline Mechanics

- **Causal Propagation:** Decisions made in Past Self acts create environmental changes in subsequent Future Self acts.
- **Temporal Paradoxes:** If the Past Self destroys an artifact or location that the Future Self relies upon, a paradox alert is triggered, reducing Timeline Stability.
- **Controlled Convergence:** While individual scenes branch dramatically, the timeline architecture utilizes controlled convergence points (Act boundaries) to prevent exponential combinatorial explosion while preserving consequence memory via `GameFlag`.

---

## 8. Choice Types

1. **Local Choice:**
   - Immediate scene consequence only. Alters dialogue flow, reveals local lore, or produces minor variable adjustments.
2. **Persistent Choice:**
   - Sets permanent `GameFlag` instances and creates cross-act consequences that unlock or lock future scenes.
3. **Timeline Choice:**
   - High-impact juncture occurring at act climaxes. Produces major deltas across `Tyrant Index`, `Timeline Stability`, and `Self-Trust`, directly determining ending trajectories.

---

## 9. Endings

The narrative resolves into five distinct endings based on the final values of the global variables and key story flags:

| Ending | Title | Core Philosophical Theme | Status |
| :---: | :--- | :--- | :---: |
| **1** | **The Tyrant** | The player fully embraces authoritarian necessity; reality is preserved through total temporal control. | `[PLANNED]` |
| **2** | **The Sacrifice** | The player erases both incarnations to save reality, vanishing from the timeline entirely. | `[PLANNED]` |
| **3** | **The Reset** | The player expends the Singularity Core to rewind time to the beginning, trapped in an eternal loop. | `[PLANNED]` |
| **4** | **The Fracture** | Timeline Stability collapses completely; reality shatters into infinite disconnected shards. | `[PLANNED]` |
| **5** | **The Reconciliation** | *Hidden Ending.* Perfect balance between Self-Trust and Stability; Past and Future merge into a unified, free reality. | `[PLANNED]` |

---

## 10. Replayability Systems

- **Branching Decision Matrix:** Over 50 distinct choice paths across the narrative.
- **Paradox Logs:** Collecting hidden logs throughout scenes unlocks fragments of the Codex of Regression.
- **New Game+ (Temporal Memory):** Starting a subsequent playthrough retains unlocked lore entries and flags, enabling unique dialogue options and earlier access to The Reconciliation ending.

---

## 11. Design Philosophy & Themes

- **Determinism vs. Agency:** Can knowledge of the future truly allow one to change it, or is the effort to prevent a fate the very mechanism that causes it?
- **The Moral Cost of Survival:** Is a preserved world worth living in if preserving it requires sacrificing every moral principle?
- **Identity Across Time:** Are we the same person if trauma, decades of survival, and differing burdens alter every perspective we hold?

---

## 12. Document Hierarchy & Source of Truth

- **`GDD.md` (This Document):** Source of truth for game design, act mechanics, variables, and systems.
- **`PRD.md`:** Source of truth for product vision, functional scope, and player requirements.
- **`README.md`:** Project entry point and high-level team summary.
- **`AI_RULES.md`:** Authoritative constraints for engineering and AI coding agents.
- **`docs/architecture.md`:** Technical software architecture and data pipelines.
- **`docs/narrative/`:** Detailed scene matrices, character dynamics, and ending specifications.
- **`docs/design/`:** UI/UX design system, screen flows, and navigation.
- **`content/`:** Canonical narrative authoring JSON.
- **`game-data/`:** Declarative game configuration schemas.
- **Kotlin Source (`app/src/`):** Authoritative software implementation.

