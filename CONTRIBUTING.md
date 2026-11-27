# Contributing to KuchBhi

Thank you for contributing to **KuchBhi**. This repository follows enterprise Android engineering standards to ensure scalability, maintainability, and fast build times.

## Branching Strategy

We use **Trunk-Based Development** with short-lived feature branches:

- `main`: Always releasable, protected by required CI checks and code review.
- `feat/<ticket-id>-short-description`: New features.
- `fix/<ticket-id>-short-description`: Bug fixes.
- `chore/<short-description>`: Build, dependency, or tooling updates.

## Commit Convention

We follow [Conventional Commits](https://www.conventionalcommits.org/):

```text
<type>(<scope>): <short summary>

feat(home): add pull-to-refresh intent handling
fix(network): preserve bookmark status during feed sync
refactor(build-logic): extract flavor configuration plugin
docs(adr): record offline-first repository strategy
```

## Architectural Guardrails

1. **Respect Module Boundaries:**
   - `:feature:*` modules must **never** depend on `:core:data`, `:core:network`, `:core:database`, or `:core:datastore`.
   - `:core:model` and `:core:domain` must stay **pure Kotlin JVM** (zero Android framework imports).
   - Features must **never** depend on other features directly; cross-feature navigation is wired in `:app`.
2. **Follow MVI Unidirectional Data Flow:**
   - Every screen defines a `<Feature>Contract.kt` (`UiState`, `UiIntent`, `UiEffect`).
   - ViewModels extend `BaseMviViewModel`.
   - UI composables are split into a stateful `<Feature>Route` (injects ViewModel) and a stateless `<Feature>Screen` (pure state + `onIntent` callback, previewable).
3. **Offline-First Data Access:**
   - UI reads from local storage (Room / DataStore) via `Flow` exposed through Domain UseCases.
4. **Testing Requirements:**
   - Every ViewModel and Repository change must include unit tests using `:core:testing` fakes and `MainDispatcherRule`.

## Local Pre-Push Verification

Run the same checks enforced by CI before opening a PR:

```bash
./gradlew ktlintCheck detekt testDevDebugUnitTest :core:model:test :core:domain:test :app:lintDevDebug
```
