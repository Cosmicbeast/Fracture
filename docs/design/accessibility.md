# Design: Accessibility Guidelines & Specifications

---

## 1. Accessibility Philosophy

As a text-centric narrative RPG, accessibility is not an auxiliary feature in *Paradox Log*—it is fundamental to the core user experience. Every player, regardless of visual, motor, or cognitive needs, must have unhindered access to reading prose, evaluating moral choices, and navigating the timeline.

---

## 2. Core Accessibility Requirements

| Requirement Category | Specification | Implementation Stance | Status |
| :--- | :--- | :--- | :---: |
| **Typography & Scaling** | Support Android dynamic system font scaling (up to 200%) without text truncation or layout breakage. | Design Requirement | `[PLANNED]` |
| **Color Contrast** | Minimum 4.5:1 contrast ratio for body text; 7:1 preferred against dark canvas (`#0B0C0E`). | Design Requirement | `[PLANNED]` |
| **Touch Targets** | All interactive choice buttons, menu rows, and controls must have a minimum touch target of `48dp x 48dp`. | Design Requirement | `[PLANNED]` |
| **Screen Readers** | Full TalkBack support with descriptive content descriptions for timeline nodes, state indicators, and choice locks. | Design Requirement | `[PLANNED]` |
| **Non-Color Dependence** | Critical metrics (e.g., low Timeline Stability) must be communicated via text, icons, and haptics, never color alone. | Design Requirement | `[PLANNED]` |
| **Reduced Motion** | Option to disable all screen transitions, text fades, and temporal flicker animations in Settings. | Design Requirement | `[PLANNED]` |
| **Haptic Feedback** | Optional subtle tactile feedback on choice confirmation and high-impact consequence triggers. | Design Requirement | `[PLANNED]` |
| **Choice Differentiation** | High visual and spatial distinction between choices to prevent accidental taps. | Design Requirement | `[PLANNED]` |

---

## 3. TalkBack & Screen Reader Guidelines

When implementing Composables during Phase 4:
- **Narrative Prose:** Ensure long narrative text blocks are exposed as continuous semantic units rather than fragmented word fragments.
- **Choice Options:** Choices must explicitly state their availability:
  *Example:* `"Choice 1 of 3: Overload the core. Condition locked: requires security flag."`
- **Telemetry Indicators:** Metrics must expose clear spoken values:
  *Example:* `"Timeline Stability: 85 percent. Status: Stable."`

---

## 4. Current Implementation Status vs. Design Target

| Feature | Target Specification | Current Status |
| :--- | :--- | :---: |
| **Dynamic Type Support** | Fully scalable Jetpack Compose Text with `sp` units | `[PLANNED]` |
| **High Contrast Mode** | Dedicated high-contrast monochrome color scheme | `[PLANNED]` |
| **Reduced Motion Mode** | Instantaneous alpha cuts bypassing animated transitions | `[PLANNED]` |
| **Semantic Tree Auditing** | Compose automated accessibility unit tests | `[PLANNED]` |

*Note: Accessibility features are scheduled for active implementation alongside the Jetpack Compose UI in Phase 4.*
