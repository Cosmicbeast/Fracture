# AGENTS.md — Fracture: The Tyrant Paradox (Paradox Log)

> **This file is authoritative guidance for any AI agent working on this project.**
> Read this file in full before making any changes.

---

## 🎯 Project Identity

**Fracture** (working title: *Paradox Log*, subtitle: *The Tyrant Paradox*) is a **narrative-driven, text-based RPG** for mobile platforms. It is NOT a visual novel, NOT an action game, NOT a puzzle game. It is an **interactive fiction engine** with deep branching narrative, consequence tracking, and philosophical storytelling.

### Core Design Pillars (NEVER deviate from these)

1. **Choice Has Weight** — every player decision must have meaningful, persistent consequences
2. **Perspective Changes Meaning** — the same event looks different through Past Self vs Future Self
3. **Control Is Not Neutral** — exerting control always has a cost
4. **There Is No Perfect Timeline** — no "golden path" or objectively correct ending

### The Central Question

> "What are you willing to become to protect the future?"

---

## 🏗 Technology Stack

| Layer | Technology |
|-------|-----------|
| **Language** | Kotlin (Kotlin Multiplatform) |
| **UI Framework** | Compose Multiplatform (JetBrains) |
| **Design System** | Material3 (dark theme only) |
| **Platforms** | Android (primary), iOS (secondary, must compile) |
| **Build System** | Gradle with Version Catalog (`libs.versions.toml`) |
| **Min Android SDK** | 24 |
| **Serialization** | kotlinx-serialization (for save system) |

### Critical Constraints

- **ALL game logic and UI must live in `shared/src/commonMain/`** — this is a KMP project, platform-specific code is only for platform APIs (storage, platform identity)
- **Never add Android-only dependencies to `commonMain`** — if it imports `android.*`, it belongs in `androidMain`
- **iOS must always compile** — even if primary development targets Android, never break iOS compilation
- **Dark theme only** — this game has no light mode. The aesthetic is atmospheric, moody, and immersive

---

## 📁 Project Structure

```
Fracture/
├── shared/                                     # KMP shared module (ALL game code goes here)
│   └── src/
│       ├── commonMain/kotlin/org/example/project/
│       │   ├── App.kt                          # Root composable with navigation
│       │   ├── app/                            # Application bootstrap & lifecycle
│       │   │   ├── MainApplication.kt
│       │   │   └── GameLauncher.kt
│       │   ├── core/                           # Core data structures and domains (folder.md)
│       │   │   ├── GameState.kt
│       │   │   ├── Act.kt
│       │   │   ├── Scene.kt
│       │   │   ├── Choice.kt
│       │   │   ├── Ending.kt
│       │   │   └── enums/
│       │   │       ├── Perspective.kt
│       │   │       ├── AnomalyType.kt
│       │   │       └── ToolType.kt
│       │   ├── data/                           # Narrative and content data (folder.md)
│       │   │   ├── acts/
│       │   │   │   ├── act01_future/
│       │   │   │   ├── act02_past/
│       │   │   │   ├── act03_future/
│       │   │   │   ├── act04_past/
│       │   │   │   ├── act05_future/
│       │   │   │   └── act06_past/
│       │   │   ├── dialogue/
│       │   │   ├── choices/
│       │   │   ├── anomalies/
│       │   │   ├── tools/
│       │   │   └── endings/
│       │   ├── engine/                         # Narrative & game engine (folder.md)
│       │   │   ├── ActManager.kt
│       │   │   ├── DialogueEngine.kt
│       │   │   ├── ChoiceResolver.kt
│       │   │   ├── TimelineStateTracker.kt
│       │   │   ├── EndingResolver.kt
│       │   │   └── anomaly/
│       │   │       ├── AnomalyProcessor.kt
│       │   │       └── AnomalyRules.kt
│       │   ├── ui/                             # UI and presentation components (folder.md)
│       │   │   ├── TextRenderer.kt
│       │   │   ├── ChoiceView.kt
│       │   │   ├── InputHandler.kt
│       │   │   ├── ScrollController.kt
│       │   │   ├── screens/                    # Full screens (TitleScreen, NarrativeScreen, SettingsScreen)
│       │   │   ├── components/                 # Reusable Compose widgets (HUD, BottomBar, Panels)
│       │   │   └── theme/
│       │   │       ├── Colors.kt
│       │   │       ├── Fonts.kt
│       │   │       └── Theme.kt
│       │   ├── save/                           # Save & persistence system (folder.md)
│       │   │   ├── SaveManager.kt
│       │   │   ├── SaveSlot.kt
│       │   │   ├── SaveSerializer.kt
│       │   │   ├── SaveValidator.kt
│       │   │   └── PlatformStorage.kt          # expect/actual storage backend
│       │   ├── config/                         # Game constants & balance rules (folder.md)
│       │   │   ├── GameConstants.kt
│       │   │   ├── BalanceRules.kt
│       │   │   └── FeatureFlags.kt
│       │   └── util/                           # Utilities (folder.md)
│       │       ├── Logger.kt
│       │       ├── JsonLoader.kt
│       │       ├── TimeUtils.kt
│       │       └── ValidationUtils.kt
│       ├── androidMain/                        # Android platform-specific implementations (expect/actual)
│       ├── iosMain/                            # iOS platform-specific implementations (expect/actual)
│       ├── commonTest/kotlin/org/example/project/ # Tests aligned with folder.md
│       │   ├── engine/
│       │   ├── save/
│       │   ├── state/
│       │   └── ui/
│       └── composeResources/                   # Images, fonts, drawables
├── androidApp/                                 # Android app shell (launches shared App)
├── iosApp/                                     # iOS app shell (launches shared App)
├── design_references/                          # UI reference screenshots (Eldrum-inspired)
├── README.md                                   # Game overview and narrative design
├── game_design.md                              # Game design pillars and philosophy
├── technical_readme.md                         # Technical architecture overview
├── player_readme.md                            # Player-facing description
├── folder.md                                   # Folder structure documentation (authoritative)
└── AGENTS.md                                   # Guidance for AI agents
```

### Where to Put Code (Strict Alignment with `folder.md`)

| Package / Directory | Purpose & Contents (as defined in `folder.md`) |
|---------------------|------------------------------------------------|
| `core/` | Core models: `GameState.kt`, `Act.kt`, `Scene.kt`, `Choice.kt`, `Ending.kt` |
| `core/enums/` | Fixed domains: `Perspective.kt`, `AnomalyType.kt`, `ToolType.kt` |
| `data/acts/` | Per-act content modules (`act01_future/`, `act02_past/`, etc.) |
| `data/dialogue/`, `data/choices/`, etc. | Dialogue registries, choice definitions, tools, endings |
| `engine/` | `ActManager.kt`, `DialogueEngine.kt`, `ChoiceResolver.kt`, `TimelineStateTracker.kt`, `EndingResolver.kt` |
| `engine/anomaly/` | `AnomalyProcessor.kt`, `AnomalyRules.kt` |
| `ui/` | Composable views: `TextRenderer.kt`, `ChoiceView.kt`, `InputHandler.kt`, `ScrollController.kt`, `screens/`, `components/` |
| `ui/theme/` | `Colors.kt`, `Fonts.kt`, `Theme.kt` |
| `save/` | `SaveManager.kt`, `SaveSlot.kt`, `SaveSerializer.kt`, `SaveValidator.kt`, `PlatformStorage.kt` |
| `config/` | `GameConstants.kt`, `BalanceRules.kt`, `FeatureFlags.kt` |
| `util/` | `Logger.kt`, `JsonLoader.kt`, `TimeUtils.kt`, `ValidationUtils.kt` |
| `commonTest/` | Test suites mirroring `engine/`, `save/`, `state/`, and `ui/` |

---

## 🎮 Game Systems Reference

### Three Core State Variables

These are tracked globally and persist across all 6 acts. They drive dialogue availability, event triggers, and endings.

| Variable | Range | What It Tracks |
|----------|-------|---------------|
| `tyrantIndex` | 0–100 | How authoritarian the Future Self becomes |
| `timelineStability` | 0–100 | How broken/fractured the world is |
| `selfTrust` | 0–100 | Alignment between Past and Future selves |

### Dual-Protagonist System

| Perspective | Played In Acts | Tone |
|-------------|---------------|------|
| **Future Self** | Act I, III, V | Pragmatic, burdened, controlling |
| **Past Self** | Act II, IV, VI | Idealistic, uncertain, reactive |

### Act Structure

| Act | Perspective | Theme |
|-----|------------|-------|
| I | Future Self | Consequences of past choices |
| II | Past Self | First interference & free will |
| III | Future Self | Damage control & escalation |
| IV | Past Self | Seeds of collapse |
| V | Future Self | Edge of tyranny |
| VI | Past Self | Final judgment & endings |

### Narrative Tools (unlocked per-act)

Tools are **narrative modifiers**, not numeric stats. They change how the player reads and interprets choices.

| Tool | Represents | Effect |
|------|-----------|--------|
| Chrono Blade | Awareness | Reveals future consequences in dialogue |
| Aion Compass | Control | Highlights stability-based choices |
| Paradox Gauntlet | Power | Absorbs/suppresses anomalies |
| Codex of Regression | Regret | Allows limited rewrites of past events |
| Singularity Core | Acceptance | Final convergence tool |

### Endings

Multiple endings, determined by final values of Tyrant Index, Timeline Stability, Self-Trust, and key irreversible choices:
- Kill the Future Self
- Merge Past and Future
- Sacrifice the Past Self
- Timeline reset (New Game+)
- Hidden reconciliation ending

---

## 🎨 UI/UX Design Guidelines

### Visual Style (Eldrum-Inspired)

The design references in `design_references/` show the target aesthetic from *Eldrum: Untold*:

- **Full-screen atmospheric background images** behind narrative text
- **Scrollable narrative text** with typewriter-reveal animation
- **Choice buttons**: dark pill-shaped, stacked vertically at center
- **Bottom navigation bar**: Character / Inventory / Quest tabs
- **Top HUD**: stat indicators (Timeline Stability bar, Act/Perspective label)
- **Bottom-sheet overlays** for inventory and quest panels
- **Minimal animations** — no flashy transitions, subtle fades only

### Color Palette

```
Background:      #1A1A1A (deep dark)
Surface:         #232323 (card/panel dark)
Surface Highlight:#2E2E2E (elevated surface)
Text Primary:    #E0E0E0 (main narrative text)
Text Secondary:  #AAAAAA (system/faded text)
Text System:     #888888 (meta/UI text)
Accent:          #6A99A2 (subdued teal — links, special items)
Error:           #B04A4A (timeline decay warnings)
```

Perspective-specific accents:
- **Past Self**: warm amber tones
- **Future Self**: cold teal tones

### Typography

- **Narrative prose**: Serif font, 18sp, generous line height (28sp)
- **System text**: Sans-serif, 14sp, light weight
- **Titles**: Serif, bold, 24sp
- **Choices/buttons**: Sans-serif, medium, 14sp, 1sp letter spacing

### Mobile-First Design Rules

- Large tap targets (minimum 48dp)
- Designed for short play sessions
- Auto-save after every major decision
- Scroll-friendly text layouts
- Interrupt-safe (can quit mid-scene and resume)

---

## 📝 Coding Conventions

### Kotlin Style

- Follow official Kotlin coding conventions
- Use data classes for all models
- Use sealed classes/interfaces for state representations
- Use enums for fixed categories (Perspective, AnomalyType, ToolType)
- Prefer immutable state (`val`, `copy()`) over mutable state

### Compose Style

- One composable per file for screens
- Components in `ui/components/`, screens in `ui/screens/`
- Use `MaterialTheme.colorScheme` and `MaterialTheme.typography` — never hardcode colors/fonts
- Use `Modifier` as first optional parameter
- Preview annotations on all screen composables

### Narrative Content Authoring

- All narrative content is authored in **Kotlin data structures** (not JSON files)
- Content lives in `data/` package, organized per-act
- Scenes reference choices by ID, choices reference next scenes by ID
- Conditional text uses lambdas that receive `GameState` and return text variants

### Testing

- Engine logic (ChoiceResolver, StateTracker, EndingResolver) must have unit tests
- Serialization round-trip tests for save system
- UI is tested manually on Android emulator

---

## 🚫 Guardrails — What NOT To Do

1. **DO NOT add combat mechanics** — this is a text-based narrative RPG, not an action game
2. **DO NOT add numeric stats/levels** — tools and armor are philosophical modifiers, not stat boosts
3. **DO NOT create a "correct" path** — every playthrough must feel valid
4. **DO NOT break iOS compilation** — all shared code must remain platform-agnostic
5. **DO NOT add light theme** — dark mode only, always
6. **DO NOT use Android-specific APIs in commonMain** — use expect/actual pattern
7. **DO NOT hardcode narrative text in UI composables** — all narrative flows through the engine
8. **DO NOT remove or modify the design reference images** — they are the UI north star
9. **DO NOT change the package structure** — `org.example.project` is the established namespace
10. **DO NOT add external game engines** (Unity, LibGDX, etc.) — this is a pure Compose Multiplatform project

---

## 📚 Reference Documents

Always consult these before making major design decisions:

| Document | Purpose |
|----------|---------|
| `README.md` | Full game overview, systems, mechanics |
| `game_design.md` | Design pillars, philosophy, endings |
| `technical_readme.md` | Architecture, state management, systems |
| `player_readme.md` | Player-facing description and experience goals |
| `folder.md` | Recommended project structure |

---

## 🔄 Development Workflow

1. **Primary development target**: Android (emulator or device)
2. **Build command**: `./gradlew :androidApp:assembleDebug`
3. **Test command**: `./gradlew :shared:allTests`
4. **Always verify iOS compiles**: `./gradlew :shared:compileKotlinIosArm64` (on macOS)
5. **Branch**: `Production` (main branch)
6. **Commit style**: Conventional commits (`feat:`, `fix:`, `chore:`, `refactor:`)

---

## 🎯 Current Priority

Build a **playable Act I prototype** that demonstrates:
- Title screen → narrative screen flow
- Typewriter text with atmospheric backgrounds
- Branching choices that alter Tyrant Index, Timeline Stability, Self-Trust
- HUD showing state variables
- Save/load between sessions
- Bottom navigation for Character/Inventory/Quest panels

Act I is played as the **Future Self**, dealing with consequences of unseen past decisions.
