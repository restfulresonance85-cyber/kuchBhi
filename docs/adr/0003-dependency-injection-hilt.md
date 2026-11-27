# ADR-0003: Hilt for Multi-Module Dependency Injection

- **Status:** Accepted
- **Date:** 2026-11-28

## Context
With 13+ Gradle modules and strict Dependency Inversion between `:feature:*` and `:core:data`, compile-time validated dependency injection is required to avoid runtime wiring failures.

## Decision
Use **Dagger Hilt** with **KSP** (via the `kuchbhi.android.hilt` convention plugin):
- `:core:domain` remains pure JVM using standard `javax.inject.Inject`.
- `:core:data` binds repository implementations to domain interfaces using `@Binds` in `SingletonComponent`.
- `:app` injects environment-specific values (`@ApiBaseUrl`, `@IsDebugBuild`) into `:core:network` via qualifiers so library modules stay flavor-free.

## Consequences
- **Positive:** Compile-time graph verification, first-class ViewModel/Compose Navigation integration, and zero flavor proliferation.
- **Trade-off:** KSP code generation adds modest build overhead compared to runtime service locators, mitigated by Gradle build caching and modularization.
