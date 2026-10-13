# Design: Visual Design System Specification

---

## 1. Design Philosophy

The visual language of *Paradox Log* is built on four core pillars:

1. **Minimalist & Calm:** An uncluttered, distraction-free environment that prioritizes deep reading, psychological immersion, and deliberate decision-making.
2. **Typography-First:** Text is the primary aesthetic medium. The weight, rhythm, leading, and scale of text carry emotional and narrative atmosphere.
3. **iOS-Inspired Visual Restraint:** Clean negative space, delicate structural dividers, and harmonious proportioning, implemented natively on Android through restrained Material 3 components.
4. **Zero Decorative Noise:** No superfluous card borders, ornate frames, skeuomorphic gauges, or flashy arcade animations. Visual feedback is subtle, atmospheric, and purposeful.

---

## 2. Color System

*Paradox Log* utilizes a disciplined monochromatic baseline with precise accent shifts to reflect reality integrity:

| Token | Conceptual Role | Specification | Status |
| :--- | :--- | :--- | :---: |
| `colorBackground` | Deep black/near-black canvas | `#0B0C0E` (OLED-friendly dark foundation) | `[TBD]` |
| `colorSurface` | Subtle elevated panels / choice containers | `#14171A` (Minimal elevation distinction) | `[TBD]` |
| `colorTextPrimary` | High-contrast narrative prose and dialogue | `#E6EDF3` (Crisp off-white, low glare) | `[TBD]` |
| `colorTextSecondary` | Metadata, speaker headers, timestamps | `#8B949E` (Subdued slate gray) | `[TBD]` |
| `colorTimelineStability` | Reality cohesion status accent | Cyan / Cool Blue (shifts to Amber on critical drop) | `[TBD]` |
| `colorTyrantAccent` | Authoritarian indicator accent | Crimson / Deep Ochre (subtle, non-jarring) | `[TBD]` |
| `colorSelfTrustAccent` | Psychological resonance indicator | Violet / Silver | `[TBD]` |

---

## 3. Typography Hierarchy

Typography is the emotional voice of the game. When implemented in Compose, typography tokens must strictly adhere to this scale:

| Level | Role | Expected Style | Status |
| :--- | :--- | :--- | :---: |
| **Display / Act Title** | Act introductions, major milestones | Sans-Serif, Light / Medium, tracking wide | `[PLANNED]` |
| **Headline / Scene** | Scene titles, location markers | Sans-Serif, Semi-Bold, uppercase, subtle | `[PLANNED]` |
| **Body Large / Prose** | Primary narrative exposition | Serif / Crisp Sans, generous line-height (1.6) | `[PLANNED]` |
| **Body Medium / Dialogue**| Spoken dialogue text | Distinct font styling or subtle indent | `[PLANNED]` |
| **Label / Choice** | Interactive decision prompts | Clean Sans-Serif, Medium weight, distinct contrast | `[PLANNED]` |
| **Caption / Metadata** | Variable metrics, timestamps, logs | Monospace or condensed Sans, small scale | `[PLANNED]` |

---

## 4. Spacing & Spatial Philosophy

- **Generous Negative Space:** Ample padding (minimum 24dp screen padding) prevents visual fatigue and focuses the eye on decision prose.
- **Rhythmic Vertical Stacking:** Fixed 8dp spatial grid (`8dp`, `16dp`, `24dp`, `32dp`, `48dp`).
- **Low Visual Clutter:** Margin boundaries between narrative blocks provide breathing room between heavy narrative beats.

---

## 5. Component Guidelines

### 5.1 Dialogue Presentation
- Presented sequentially with graceful, subtle alpha fade-in.
- Speaker identities displayed as small, muted captions above dialogue prose.
- No bulky character portrait cards or comic dialogue balloons.

### 5.2 Choice Presentation
- Choices are presented as clean, minimal horizontal selectable rows or subtle bordered pill containers.
- Locked choices (due to unmet `ChoiceCondition`) display a subtle lock indicator or faint text treatment without cluttering the screen.
- Clear visual differentiation on touch/hover states using subtle background luminosity shifts rather than garish color changes.

### 5.3 State Indicators (HUD)
- A minimal status header displaying:
  - Current Act & Scene identifier.
  - Minimalistic horizontal indicators or numeric telemetry for `Timeline Stability`, `Tyrant Index`, and `Self-Trust`.
- Never intrudes on narrative reading; fades or remains subtle during long prose passages.

### 5.4 Material 3 Usage Policy
- Material 3 theme tokens (`MaterialTheme.colorScheme`, `MaterialTheme.typography`) are used for framework compatibility.
- Standard out-of-the-box Material 3 default shapes and heavy elevated cards are explicitly avoided in favor of flat, minimalist surfaces with subtle alpha separation.
