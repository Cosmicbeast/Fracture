# Narrative: Ending Logic & Resolution

---

## 1. Overview of the Five Endings

*Paradox Log* features five mutually exclusive narrative endings resolved in Act VI. Each ending reflects a distinct philosophical answer to the central paradox of identity, survival, and moral responsibility.

```text
                                  ┌─────────────────────────────┐
                                  │   Act VI: Final Judgment    │
                                  └──────────────┬──────────────┘
                                                 │
         ┌──────────────────┬────────────────────┼───────────────────┬──────────────────┐
         ▼                  ▼                    ▼                   ▼                  ▼
    Ending 1:          Ending 2:            Ending 3:           Ending 4:          Ending 5:
   THE TYRANT        THE SACRIFICE          THE RESET          THE FRACTURE     THE RECONCILIATION
  (Pragmatism)        (Self-Erasure)        (Repetition)        (Entropy)           (Synthesis)
```

---

## 2. Ending Specifications

### Ending 1 — The Tyrant
- **Core Condition:** High `Tyrant Index`, moderate-to-high `Timeline Stability`, Past Self capitulates to the Future Self's doctrine.
  - *Thresholds:* `[PLANNED] (Not yet implemented / TBD)`
- **Narrative Meaning:** The Past Self accepts that moral purity is impossible in a shattered universe. The protagonist arms the temporal apparatus, step-for-step assuming the position of the Future Self to preserve the remnants of human civilization under iron temporal discipline.
- **Player Consequence:** Reality survives, but freedom and individuality are permanently suppressed. The loop remains intact, but stabilized.
- **Replayability Consequence:** Unlocks Codex records detailing the administrative structure of the Remnant Citadel.
- **Implementation Status:** `[PLANNED]` (State representation exists in `EndingState`; engine logic pending).

### Ending 2 — The Sacrifice
- **Core Condition:** Moderate `Tyrant Index`, high `Self-Trust`, possession of the `Chrono Blade`.
  - *Thresholds:* `[PLANNED] (Not yet implemented / TBD)`
- **Narrative Meaning:** The Past Self and Future Self agree that humanity's suffering is driven entirely by their mutual attempts to control the timeline. Using the Chrono Blade, they sever their own causal origin from spacetime, retroactively erasing themselves from history.
- **Player Consequence:** Reality continues without temporal interference. The catastrophe never occurs, but no one remembers the protagonist ever existed.
- **Replayability Consequence:** Unlocks "Echoes in the Static" log fragments in subsequent playthroughs.
- **Implementation Status:** `[PLANNED]`

### Ending 3 — The Reset
- **Core Condition:** High `Self-Trust`, low `Timeline Stability`, possession of the `Singularity Core`.
  - *Thresholds:* `[PLANNED] (Not yet implemented / TBD)`
- **Narrative Meaning:** Unable to bear the burden of tyranny or the finality of self-erasure, the player expends the Singularity Core to rewind universal coordinates back to Act I, clinging to the hope that a future loop might discover a better way.
- **Player Consequence:** The loop resets to iteration #4,113. Immediate failure is averted, but the eternal suffering of repetition continues.
- **Replayability Consequence:** Directly feeds into New Game+ mechanics, unlocking persistent timeline memories for the next run.
- **Implementation Status:** `[PLANNED]`

### Ending 4 — The Fracture
- **Core Condition:** `Timeline Stability` falls to `0` at any point during Act V or Act VI, or player actively shatters the Atemporal Core.
  - *Thresholds:* `Timeline Stability == 0` (Conceptual; engine triggers `[PLANNED]`)
- **Narrative Meaning:** The player refuses both compromise and duty. Causality tears apart at the seams. Spacetime shatters into billions of isolated, timeless moments.
- **Player Consequence:** Universal collapse. All living things drift in disconnected fragments of memory and entropy.
- **Replayability Consequence:** Unlocks the "Void Records" achievement and unique terminal dialogue upon restarting.
- **Implementation Status:** `[PLANNED]`

### Ending 5 — The Reconciliation (Hidden Ending)
- **Core Condition:** Precise equilibrium across all three global variables (`Tyrant Index`, `Timeline Stability`, `Self-Trust`), possession of all five relics, and discovery of secret memory fragments in Acts I–V.
  - *Thresholds:* `[PLANNED] (Not yet implemented / TBD)`
- **Narrative Meaning:** Past and Future achieve genuine psychological synthesis. Rather than one dominating the other, they merge their dual perspectives—combining the moral vision of the youth with the temporal mastery of the elder—to repair the timeline breach from both sides simultaneously.
- **Player Consequence:** The true canonical resolution. The paradox is dissolved without tyranny or erasure. Humanity steps into an open, unscripted future.
- **Replayability Consequence:** Unlocks the Master Codex, epilogue chronicles, and True Paradox Log archive.
- **Implementation Status:** `[PLANNED]`

---

## 3. Ending State Architecture

In the codebase, endings are represented within the authoritative `GameState`:

```kotlin
// Location: app/src/main/java/com/paradoxlog/app/game/state/GameState.kt
data class EndingState(
    val eligibleEnding: String? = null,
    val isResolved: Boolean = false,
    val endingSceneId: String? = null
)
```

The Game Rules Engine (`game/rules/` in Phase 3) will be responsible for evaluating thresholds against `GameState.variables` and `GameState.flags` to populate `eligibleEnding`. Zero ending calculation logic will ever reside in Composables or ViewModels.
