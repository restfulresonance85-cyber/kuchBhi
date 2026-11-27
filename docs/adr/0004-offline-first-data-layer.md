# ADR-0004: Offline-First Data Layer with Room, DataStore, and Retrofit

- **Status:** Accepted
- **Date:** 2026-11-28

## Context
Enterprise mobile apps must remain responsive on unreliable networks, support offline reading, and maintain strict separation between remote API schemas and internal domain models.

## Decision
Implement an **Offline-First Single Source of Truth (SSOT)** architecture:
- **Structured data:** Stored in **Room** (`:core:database`) with schema export enabled for automated migration testing.
- **Key-value user settings:** Stored in **Preferences DataStore** (`:core:datastore`).
- **Remote networking:** Handled via **Retrofit + OkHttp + Kotlinx Serialization** (`:core:network`).
- **Repositories (`:core:data`):** UI observes reactive `Flow` streams directly from local storage; remote sync writes into local storage while reconciling local-only state (e.g., bookmarks).

## Consequences
- **Positive:** Instant UI rendering, resilient offline experience, clean DTO/Entity/Domain decoupling, and easy unit testing via fakes.
- **Trade-off:** Requires explicit mapping functions between DTOs, Room entities, and domain models.
