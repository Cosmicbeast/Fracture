# Narrative: Choice Architecture & Matrix

---

## 1. Choice Classification System

In *Paradox Log*, player decisions are categorized into three distinct operational tiers to balance narrative granularity with systemic causality:

```text
┌─────────────────┐      ┌─────────────────────┐      ┌─────────────────────┐
│  Local Choice   │      │  Persistent Choice  │      │   Timeline Choice   │
├─────────────────┤      ├─────────────────────┤      ├─────────────────────┤
│ Immediate scene │ ──▶  │ Cross-act flags &   │ ──▶  │ Major state shifts &│
│ feedback only   │      │ scene branching     │      │ ending eligibility  │
└─────────────────┘      └─────────────────────┘      └─────────────────────┘
```

### 1.1 Local Choice
- **Scope:** Confined entirely within the active `Scene`.
- **Primary Effect:** Determines immediate dialogue responses, uncovers optional lore text, or provides atmospheric exposition.
- **Systemic Impact:** Minor or zero variable deltas; does not set persistent cross-act flags.
- **Implementation:** Governed directly by `Scene.choices` pointing to localized target scenes within the same act.

### 1.2 Persistent Choice
- **Scope:** Cross-act narrative branching.
- **Primary Effect:** Sets or clears persistent `GameFlag` identifiers and adds or removes relics from `InventoryState`.
- **Systemic Impact:** Unlocks or locks entire scenes, alternate dialogue branches, or character survival states in subsequent acts.
- **Implementation:** Evaluated via `ChoiceCondition.requiredFlags` and `ChoiceCondition.forbiddenFlags` in later acts.

### 1.3 Timeline Choice
- **Scope:** Global reality architecture and ending determination.
- **Primary Effect:** High-stakes moral dilemmas occurring at the climax of each act and in Act VI.
- **Systemic Impact:** Imposes significant shifts across `Tyrant Index`, `Timeline Stability`, and `Self-Trust`. Can trigger early paradox collapses if stability reaches zero.
- **Implementation:** Evaluated via `ChoiceConsequence` and tracked in `EndingState`.

---

## 2. Systemic Influence Dimensions

Every choice in the game interacts with one or more systemic dimensions:

| Dimension | Managed By | Influence Mechanism | Status |
| :--- | :--- | :--- | :---: |
| **Tyrant Index** | `GameState.variables.tyrantIndex` | Increased by ruthless, authoritarian, and emotionally detached actions. Decreased by compassionate or self-sacrificing choices. | `[IMPLEMENTED]` (Model exists; numerical deltas `[TBD]`) |
| **Timeline Stability** | `GameState.variables.timelineStability` | Depleted by actions that contradict established history, cause temporal paradoxes, or strain physical containment. Reaching `0` causes collapse. | `[IMPLEMENTED]` (Model exists; numerical deltas `[TBD]`) |
| **Self-Trust** | `GameState.variables.selfTrust` | Bolstered when Past Self aligns with Future Self's rationale; diminished by ideological conflict or mutual sabotage. | `[IMPLEMENTED]` (Model exists; numerical deltas `[TBD]`) |
| **Persistent Flags** | `GameState.flags: Set<GameFlag>` | Boolean milestones recording specific plot decisions (e.g., `terminal_accessed`, `survivors_saved`). | `[IMPLEMENTED]` |
| **Inventory Relics** | `GameState.inventory.itemIds` | Acquisition of key temporal artifacts required to unlock special conditions. | `[IMPLEMENTED]` |
| **Ending Eligibility** | `GameState.ending.eligibleEnding` | Cumulative gating determining which of the 5 endings can be executed in Act VI. | `[PLANNED]` |

---

## 3. Conceptual Choice Matrix (Acts I–VI)

*Note: In accordance with architectural guidelines, exact balancing numbers are marked `[TBD]` to prevent premature gameplay balancing.*

| Act | Scene Context | Choice Description | Type | Systemic Impact | Status |
| :---: | :--- | :--- | :---: | :--- | :---: |
| **I** | Core Breach Alert | Execute immediate quarantine protocol | Persistent | `+TyrantIndex`, `+TimelineStability`, sets `quarantine_enforced` | `[TBD]` |
| **I** | Core Breach Alert | Delay quarantine to evacuate staff | Persistent | `-TyrantIndex`, `-TimelineStability`, sets `staff_evacuated` | `[TBD]` |
| **I** | Council Chamber | Conceal transmission from council | Local | `+SelfTrust`, sets `transmission_concealed` | `[TBD]` |
| **I** | Council Chamber | Transmit telemetry across network | Local | `-SelfTrust`, sets `council_alerted` | `[TBD]` |
| **II** | Citadel Pylon Defense | Neutralize dissidents with temporal force | Timeline | `++TyrantIndex`, `+TimelineStability`, sets `dissidents_purged` | `[TBD]` |
| **II** | Citadel Pylon Defense | Negotiate passage with dissidents | Timeline | `--TyrantIndex`, `--TimelineStability`, sets `dissidents_spared` | `[TBD]` |
| **III** | Vault Infiltration | Claim Chrono Blade | Persistent | Adds `chrono_blade`, sets `causality_severed` | `[TBD]` |
| **III** | Vault Infiltration | Claim Aion Compass | Persistent | Adds `aion_compass`, sets `vectors_mapped` | `[TBD]` |
| **IV** | Sector 4 Catastrophe | Authorize sacrificial sector purge | Timeline | `+++TyrantIndex`, `+TimelineStability`, sets `sector_purged` | `[TBD]` |
| **IV** | Sector 4 Catastrophe | Attempt rescue against telemetry | Timeline | `---TyrantIndex`, `---TimelineStability`, sets `purge_refused` | `[TBD]` |
| **V** | Singularity Chamber | Arm the Paradox Gauntlet | Persistent | Adds `paradox_gauntlet`, `+SelfTrust` | `[TBD]` |
| **V** | Singularity Chamber | Reject the gauntlet; defy the loop | Persistent | `-SelfTrust`, `--TimelineStability` | `[TBD]` |
| **VI** | Final Confrontation | Embrace the Tyrant Doctrine | Timeline | Resolves **The Tyrant** Ending | `[PLANNED]` |
| **VI** | Final Confrontation | Sever both incarnations from reality | Timeline | Resolves **The Sacrifice** Ending | `[PLANNED]` |
| **VI** | Final Confrontation | Overload Core to restart loop | Timeline | Resolves **The Reset** Ending | `[PLANNED]` |
| **VI** | Final Confrontation | Let stability fail; fracture spacetime | Timeline | Resolves **The Fracture** Ending | `[PLANNED]` |
| **VI** | Final Confrontation | Synthesize Past and Future alignment | Timeline | Resolves **The Reconciliation** (Hidden) | `[PLANNED]` |
