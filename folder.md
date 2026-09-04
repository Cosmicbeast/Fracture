# 📁 Paradox Log – Java Project Folder Structure

This document defines the recommended **Java-based project structure** for *Paradox Log*, a mobile-first, text-based RPG built around a state-driven narrative engine.

The structure follows common Java conventions while remaining flexible for Android or cross-platform development.

---

## 🧱 High-Level Structure

```
paradox-log/
├── app/
├── core/
├── data/
├── engine/
├── ui/
├── save/
├── config/
├── util/
├── assets/
├── tests/
└── docs/
```

---

## 📱 app/

```
paradox-log/
└── app/
    ├── MainApplication.java
    ├── GameLauncher.java
    └── platform/
        ├── android/
        └── ios/
```

---

## 🧠 core/

```
paradox-log/
└── core/
    ├── GameState.java
    ├── Act.java
    ├── Scene.java
    ├── Choice.java
    ├── Ending.java
    └── enums/
        ├── Perspective.java
        ├── AnomalyType.java
        └── ToolType.java
```

---

## 📚 data/

```
paradox-log/
└── data/
    ├── acts/
    │   ├── act01_future/
    │   ├── act02_past/
    │   ├── act03_future/
    │   ├── act04_past/
    │   ├── act05_future/
    │   └── act06_past/
    │
    ├── dialogue/
    ├── choices/
    ├── anomalies/
    ├── tools/
    └── endings/
```

---

## ⚙ engine/

```
paradox-log/
└── engine/
    ├── ActManager.java
    ├── DialogueEngine.java
    ├── ChoiceResolver.java
    ├── TimelineStateTracker.java
    ├── EndingResolver.java
    └── anomaly/
        ├── AnomalyProcessor.java
        └── AnomalyRules.java
```

---

## 🖥 ui/

```
paradox-log/
└── ui/
    ├── TextRenderer.java
    ├── ChoiceView.java
    ├── InputHandler.java
    ├── ScrollController.java
    └── theme/
        ├── Colors.java
        └── Fonts.java
```

---

## 💾 save/

```
paradox-log/
└── save/
    ├── SaveManager.java
    ├── SaveSlot.java
    ├── SaveSerializer.java
    └── SaveValidator.java
```

---

## 🧪 config/

```
paradox-log/
└── config/
    ├── GameConstants.java
    ├── BalanceRules.java
    └── FeatureFlags.java
```

---

## 🛠 util/

```
paradox-log/
└── util/
    ├── Logger.java
    ├── JsonLoader.java
    ├── TimeUtils.java
    └── ValidationUtils.java
```

---

## 🧪 tests/

```
paradox-log/
└── tests/
    ├── engine/
    ├── save/
    ├── state/
    └── ui/
```

---

## 🎨 assets/

```
paradox-log/
└── assets/
    ├── fonts/
    ├── icons/
    └── sounds/
```

---

## 📄 docs/

```
paradox-log/
└── docs/
    ├── README.md
    ├── GAME_DESIGN.md
    ├── TECHNICAL_OVERVIEW.md
    └── JAVA_PROJECT_STRUCTURE.md
```
