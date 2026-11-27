# ADR-0002: Jetpack Compose with MVI Unidirectional Data Flow

- **Status:** Accepted
- **Date:** 2026-11-28

## Context
Complex enterprise screens frequently encounter inconsistent UI states, race conditions from multiple mutable streams, and lost one-shot events (navigation or snackbars) across configuration changes.

## Decision
Standardize all UI features on **Jetpack Compose (Material 3)** paired with **MVI (Model-View-Intent)**:
- Each screen defines a single immutable `UiState`, a sealed `UiIntent` hierarchy, and a sealed `UiEffect` hierarchy.
- ViewModels extend `BaseMviViewModel` in `:core:ui`, exposing `StateFlow<UiState>` and a buffered `Channel<UiEffect>` collected lifecycle-safely via `ObserveAsEvents`.
- Screens separate stateful `<Feature>Route` composables from stateless, previewable `<Feature>Screen` composables.

## Consequences
- **Positive:** Deterministic state transitions, trivial unit testing with Turbine, zero state desynchronization, and clean Compose previews.
- **Trade-off:** Requires defining explicit contract classes per screen.
