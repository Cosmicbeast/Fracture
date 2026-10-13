# Design: Navigation Architecture

---

## 1. Navigation Overview

Navigation in *Paradox Log* is designed as a declarative, unidirectional routing graph connecting features (`feature/*`) within the single-activity architecture (`MainActivity`).

```text
               ┌────────────────┐
               │   SplashRoute  │
               └───────┬────────┘
                       │
                       ▼
               ┌────────────────┐
               │   MainMenuRoute│
               └───────┬────────┘
                       │
       ┌───────────────┼───────────────┐
       ▼               ▼               ▼
┌──────────────┐┌──────────────┐┌──────────────┐
│  StoryRoute  ││ SettingsRoute││  CodexRoute  │
└──────┬───────┘└──────────────┘└──────────────┘
       │
       ├───────────────────────────────┐
       ▼                               ▼
┌──────────────┐                ┌──────────────┐
│ActTransition │                │  EndingRoute │
└──────────────┘                └──────────────┘
```

---

## 2. Intended Route Structure

| Route Identifier | Destination Feature | Parameters | Status |
| :--- | :--- | :--- | :---: |
| `route_splash` | `feature/splash` | None | `[PLANNED]` |
| `route_main_menu`| `feature/mainmenu` | None | `[PLANNED]` |
| `route_onboarding`| `feature/onboarding` | None | `[PLANNED]` |
| `route_story` | `feature/story` | None (reads `GameState.currentSceneId`) | `[PLANNED]` |
| `route_act_transition`| `feature/acttransition`| `actId: String` | `[PLANNED]` |
| `route_ending` | `feature/ending` | `endingId: String` | `[PLANNED]` |
| `route_settings` | `feature/settings` | None | `[PLANNED]` |
| `route_codex` | `feature/paradoxlog` | None | `[PLANNED]` |

---

## 3. Strict Navigation Prohibitions

To preserve clean architecture and single-source-of-truth principles, the navigation graph must adhere to these prohibitions:

1. **NO Game Rules in Navigation:**
   Navigation callbacks must **never** check `tyrantIndex`, calculate branch availability, or determine which scene should play next. Scene sequencing is strictly determined by `GameState.currentSceneId` emitted by the Game Engine.
2. **NO Direct State Mutation:**
   Navigating between routes must never mutate `GameState` directly.
3. **NO Content Parsing in NavHost:**
   Routes must not accept raw JSON or parse narrative content.

---

## 4. Navigation Flow Execution

The intended runtime interaction flow between user, navigation, and game engine:

```text
1. Player clicks Choice in StoryScreen
2. StoryScreen emits OnChoiceSelected(choiceId) to StoryViewModel
3. StoryViewModel requests engine execution: gameEngine.selectChoice(choiceId)
4. Game Engine evaluates conditions, mutates GameState, updates currentSceneId
5. If currentSceneId indicates Act boundary or Ending, StoryViewModel triggers Navigation Event:
   navigateTo(route_act_transition) or navigateTo(route_ending)
6. Otherwise, StoryScreen remains active and recomposes with the new Scene content.
```

---

## 5. Current Implementation Status

- **Location:** Reserved under `app/src/main/java/com/paradoxlog/app/core/navigation/`
- **Status:** `[PLANNED]` (Navigation graph and Compose NavHost will be initialized during Phase 4 UI implementation).
