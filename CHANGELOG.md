# Changelog

All notable changes to **KuchBhi** will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added
- Enterprise multi-module Android architecture (`:app`, `:core:*`, `:feature:*`, `:build-logic`) targeting Android 15 (API 35+).
- Centralized Gradle Version Catalog (`gradle/libs.versions.toml`) and reusable convention plugins (`build-logic`).
- Jetpack Compose Material 3 design system (`:core:designsystem`) and MVI UDF framework (`:core:ui`).
- Offline-first data pipeline with Hilt, Retrofit/OkHttp, Room, DataStore, Coroutines/Flow, and Kotlinx Serialization.
- Reference feature modules (`:feature:home`, `:feature:settings`) with type-safe Compose Navigation and unit tests.
- Multi-environment build flavors (`dev`, `staging`, `prod`) with R8/ProGuard rules and network security configs.
- Static analysis (`ktlint`, `Detekt`, Android Lint), GitHub Actions CI/CD pipelines, and Architecture Decision Records (ADRs).
