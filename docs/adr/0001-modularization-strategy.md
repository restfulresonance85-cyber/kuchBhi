# ADR-0001: Feature + Core Multi-Module Architecture

- **Status:** Accepted
- **Date:** 2026-11-28

## Context
Enterprise Android codebases quickly suffer from slow incremental builds, tight coupling between features, and unclear ownership when organized as a monolith or purely horizontal layers.

## Decision
Adopt the **Now in Android** Feature + Core modularization topology:
- `:app` acts strictly as the composition root (navigation graph, DI wiring, build flavors).
- `:feature:*` modules encapsulate vertical user journeys and depend only on `:core:domain`, `:core:model`, `:core:common`, `:core:ui`, and `:core:designsystem`.
- `:core:*` modules provide focused, reusable infrastructure (`:core:network`, `:core:database`, `:core:datastore`, `:core:data`, etc.).
- `build-logic` convention plugins eliminate duplicated Gradle configuration across modules.

## Consequences
- **Positive:** Parallel Gradle execution, strict compile-time boundary enforcement, independent feature teams, and pure JVM domain testing.
- **Trade-off:** Slight initial boilerplate when adding a new module, mitigated by `kuchbhi.android.feature` convention plugin.
