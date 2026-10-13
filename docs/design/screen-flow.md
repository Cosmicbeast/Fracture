# Design: Screen Flow & User Journey

---

## 1. Master Screen Flow Diagram

```text
 ┌──────────────┐
 │ Splash Screen│  Cold start, initialization, telemetry check
 └──────┬───────┘
        │
        ▼
 ┌──────────────┐
 │  Main Menu   │  Start Journey, Resume Log, Access Codex, Settings
 └──────┬───────┘
        │
        ├──────────────────────┬──────────────────────┐
        ▼                      ▼                      ▼
 ┌──────────────┐       ┌──────────────┐       ┌──────────────┐
 │  Onboarding  │       │ SettingsView │       │ Codex/Memory │
 │(New Game Only│       │(Accessibility│       │(Archives/Logs│
 └──────┬───────┘       └──────────────┘       └──────────────┘
        │
        ▼
 ┌──────────────┐
 │ Story Screen │  Core narrative exposition and dialogue stream
 └──────┬───────┘
        │
        ▼
 ┌──────────────┐
 │ Choice View  │  Decision selection juncture
 └──────┬───────┘
        │
        ▼
 ┌──────────────┐
 │ Consequence  │  Subtle visual pulse / feedback on state shift
 └──────┬───────┘
        │
        ├─────────────────────────────────────────────┐
        ▼                                             ▼
 ┌──────────────┐                              ┌──────────────┐
 │ Next Scene   │ (Continues within Act)       │ Modal Drawers│ (Inventory, Timeline,
 └──────┬───────┘                              └──────────────┘  Telemetry inspection)
        │
        ▼
 ┌──────────────┐
 │Act Transition│  Act summary, timeline stability check
 └──────┬───────┘
        │
        ▼
 ┌──────────────┐
 │Ending Screen │  Final resolution (1 of 5 endings), credits, New Game+ unlock
 └──────────────┘
```

---

## 2. Screen Specifications & Implementation Status

| Screen / Destination | Narrative & Functional Role | Package Location | Status |
| :--- | :--- | :--- | :---: |
| **Splash Screen** | App bootstrap, loading content manifests, initializing application state. | `app/.../feature/splash/` | `[PLANNED]` |
| **Main Menu** | Starting new playthroughs, continuing saved `GameState`, accessing Codex. | `app/.../feature/mainmenu/` | `[PLANNED]` |
| **Onboarding** | Establishing the central premise and atmospheric calibration. | `app/.../feature/onboarding/` | `[PLANNED]` |
| **Story Screen** | Primary narrative canvas displaying prose, speaker headers, and dialogue. | `app/.../feature/story/` | `[PLANNED]` |
| **Choice View** | Inline or bottom-docked decision panel presenting validated choices. | `app/.../feature/choice/` | `[PLANNED]` |
| **Consequence Screen**| Brief atmospheric feedback indicating timeline adjustments. | `app/.../feature/consequence/` | `[PLANNED]` |
| **Inventory Drawer** | Minimalist inspection sheet reviewing acquired timeline artifacts. | `app/.../feature/inventory/` | `[PLANNED]` |
| **Timeline Review** | Chronological map displaying visited scenes, branch points, and stability. | `app/.../feature/timeline/` | `[PLANNED]` |
| **Paradox Log / Codex**| Archive reviewing discovered memory fragments and iteration records. | `app/.../feature/paradoxlog/`| `[PLANNED]` |
| **Act Transition** | Interstitial canvas displaying act progression and stability audit. | `app/.../feature/acttransition/` | `[PLANNED]` |
| **Ending Screen** | Narrative epilogue, resolution metrics, and New Game+ prompt. | `app/.../feature/ending/` | `[PLANNED]` |
| **Settings Screen** | Accessibility adjustments (font scale, contrast, haptics, motion). | `app/.../feature/settings/` | `[PLANNED]` |

---

## 3. Transition Philosophy

1. **Uninterrupted Immersion:** Transitions between reading and choosing occur on the same canvas without hard screen flashing or jarring cuts.
2. **Subtle Feedback:** When a choice triggers a consequence (e.g., `-10 Timeline Stability`), feedback is communicated through subtle ambient shifts (subtle screen flicker, haptic pulse, audio dissonance) rather than intrusive modal popups.
3. **Clean Resumption:** Resuming from the main menu immediately restores the player to the exact `currentSceneId` recorded in the authoritative `GameState`.
