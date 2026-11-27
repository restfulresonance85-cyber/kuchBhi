# KuchBhi — Enterprise Architecture Guide

This document defines the architectural layers, data flow patterns, dependency rules, and testing standards for **KuchBhi**.

---

## 1. Architectural Overview

KuchBhi follows Google's official Android Architecture Guidelines combined with **Clean Architecture** and **Now in Android** multi-module practices:

1. **UI Layer (`:feature:*`, `:core:ui`, `:core:designsystem`, `:app`)**
   - Declarative UI built with **Jetpack Compose** and **Material 3**.
   - Presentation logic implemented with **MVI (Model-View-Intent)** enforcing strict Unidirectional Data Flow (UDF).
   - Navigation uses **Type-Safe Navigation Compose** (`@Serializable` route objects).
2. **Domain Layer (`:core:domain`, `:core:model`)**
   - **Pure Kotlin JVM modules** with zero Android framework dependencies.
   - Defines repository interfaces (Dependency Inversion) and reusable single-responsibility UseCases.
3. **Data Layer (`:core:data`, `:core:network`, `:core:database`, `:core:datastore`)**
   - Implements domain repository interfaces.
   - Enforces **Offline-First Single Source of Truth (SSOT)** using Room and DataStore.
   - Maps external DTOs (`:core:network`) and persistence Entities (`:core:database`) into clean Domain Models (`:core:model`).

---

## 2. MVI Pattern Implementation

Every feature screen implements a three-part contract extending `:core:ui`:

- **`UiState`**: Single immutable `data class` representing everything the screen needs to render. Exposed via `StateFlow<S>` and collected with `collectAsStateWithLifecycle()`.
- **`UiIntent`**: Sealed interface representing all user and lifecycle actions dispatched via `viewModel.onIntent(intent)`.
- **`UiEffect`**: Sealed interface representing one-shot side effects (navigation, snackbars) sent through a buffered `Channel<E>` and collected via `ObserveAsEvents(...)`.

### Composable Split Standard

Every feature exposes:
- `<Feature>Route`: Stateful entry point wired into Navigation Compose that obtains the `@HiltViewModel`, collects `uiState`, handles `effects`, and passes state + intent lambda down.
- `<Feature>Screen`: Stateless, pure UI composable with `@Preview` support and zero ViewModel/Hilt references.

---

## 3. Dependency Injection (Hilt)

- `@HiltAndroidApp` initializes the root graph in `KuchBhiApplication`.
- **Qualifiers prevent flavor leakage:** `:core:common` defines `@ApiBaseUrl` and `@IsDebugBuild`; `:app` provides their values from `BuildConfig`, and `:core:network` consumes them without declaring product flavors.
- **Injectable Dispatchers:** Never hardcode `Dispatchers.IO` or `Dispatchers.Default` inside repositories or use cases. Inject `@Dispatcher(KuchBhiDispatchers.IO)` for deterministic coroutine testing.

---

## 4. Offline-First Data Flow

1. UI observes `ObserveFeedItemsUseCase()` -> `FeedRepository.observeFeedItems()` -> `FeedItemDao.observeAll()` (`Flow<List<FeedItemEntity>>`).
2. User triggers `HomeIntent.Refresh` -> `RefreshFeedUseCase()` -> `OfflineFirstFeedRepository.refreshFeed()`.
3. Repository fetches DTOs from `NetworkDataSource`, reconciles local state (preserving user bookmarks), and upserts into `FeedItemDao`.
4. Room automatically emits the updated list to active `Flow` collectors, updating `HomeUiState` reactively.

---

## 5. Testing Strategy

| Layer | Test Type | Tools |
| --- | --- | --- |
| `:core:model` & `:core:domain` | Fast pure JVM unit tests | JUnit 4, Truth, Coroutines Test |
| `:core:data` | Repository & mapper unit tests | JUnit 4, MockK, Truth, Coroutines Test |
| `:feature:*` | ViewModel MVI state & effect tests | JUnit 4, Turbine, Truth, `:core:testing` fakes |
| `:core:database` | DAO & migration tests | Room Testing (exported schemas in `core/database/schemas/`) |
| `:app` & UI | Compose UI & end-to-end tests | Compose UI Test JUnit4, Hilt Android Testing |
