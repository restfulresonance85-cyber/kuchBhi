# KuchBhi — AI Agent Architecture & Engineering Contract

> **Mandatory Instructions for All AI Coding Agents** (Antigravity, Claude Code, Codex, Copilot, Cursor, etc.).  
> Read and follow every rule in this document before designing, creating, or modifying any code in **KuchBhi**. Do not deviate from these architectural boundaries or quality gates.

---

## 1. Project Identity & Toolchain

| Property | Value |
| --- | --- |
| **Project Name** | `KuchBhi` |
| **Base Package / Application ID** | `com.kuchBhi.com` (preserve exact casing) |
| **SDK Levels** | `minSdk = 35`, `targetSdk = 35`, `compileSdk = 35` (Android 15+) |
| **Toolchain** | Kotlin `2.0.21`, Java `17` (Temurin), AGP `8.7.2`, Gradle `8.10.2`, KSP |
| **Architecture** | Now in Android Multi-Module (`:app`, `:core:*`, `:feature:*`, `:build-logic`) |
| **UI & Presentation** | Jetpack Compose (Material 3) + MVI (Unidirectional Data Flow) |
| **Core Stack** | Hilt, Retrofit/OkHttp, Kotlinx Serialization, Room, Preferences DataStore, Coroutines/Flow |

If the development machine does not have JDK 17, Gradle `8.10.2`, or Android SDK API 35 installed, follow **`README.md` (Steps 0–8)** to bootstrap the environment first.

---

## 2. Non-Negotiable Module Dependency Boundaries

Respect the dependency graph strictly. Never introduce shortcuts or circular dependencies.

| Module | Module Type | Allowed Internal Dependencies | Strictly Forbidden |
| --- | --- | --- | --- |
| `:core:model` | Pure Kotlin JVM | None | Any `android.*` / `androidx.*` API, any other module |
| `:core:domain` | Pure Kotlin JVM | `:core:model` | Any `android.*` / `androidx.*` API, `:core:data`, `:core:network`, `:core:database`, `:core:datastore` |
| `:core:common` | Android Library + Hilt | None | `:core:data`, `:feature:*` |
| `:core:network` | Android Library + Hilt | `:core:common`, `:core:model` | `:core:database`, `:core:data`, `:feature:*`, product flavors |
| `:core:database` | Android Library + Hilt + Room | `:core:common`, `:core:model` | `:core:network`, `:core:data`, `:feature:*` |
| `:core:datastore` | Android Library + Hilt | `:core:common`, `:core:model` | `:core:network`, `:core:database`, `:core:data`, `:feature:*` |
| `:core:data` | Android Library + Hilt | `:core:common`, `:core:model`, `:core:domain`, `:core:network`, `:core:database`, `:core:datastore` | `:feature:*`, `:core:ui`, `:core:designsystem` |
| `:core:designsystem` | Android Library + Compose | None | `:core:domain`, `:core:data`, `:feature:*` |
| `:core:ui` | Android Library + Compose | `:core:designsystem`, `:core:common`, `:core:model` | `:core:data`, `:feature:*` |
| `:core:testing` | Android Library | `:core:model`, `:core:domain` | Production app/feature dependencies |
| `:feature:*` | Feature Library (`kuchbhi.android.feature`) | `:core:common`, `:core:model`, `:core:domain`, `:core:designsystem`, `:core:ui` (`:core:testing` for tests) | **NEVER** depend on `:core:data`, `:core:network`, `:core:database`, `:core:datastore`, or another `:feature:*` |
| `:app` | Android Application | `:feature:*`, `:core:*` | Business logic, direct DAO/API calls, screen UI implementations |

---

## 3. Build System & Gradle Rules

1. **Centralized Version Catalog:** Declare **all** library and plugin versions in `gradle/libs.versions.toml`. Never hardcode version numbers inside module `build.gradle.kts` files.
2. **Use `build-logic` Convention Plugins:**
   - New feature modules MUST apply `alias(libs.plugins.kuchbhi.android.feature)` and only declare `android { namespace = "com.kuchBhi.com.feature.<name>" }` plus any feature-specific dependencies.
   - Pure Kotlin modules MUST use `alias(libs.plugins.kuchbhi.jvm.library)`.
   - Android libraries MUST use `kuchbhi.android.library` (+ `kuchbhi.android.library.compose`, `kuchbhi.android.hilt`, `kuchbhi.android.room` as needed).
3. **Type-Safe Project Accessors:** Always reference internal modules via `projects.core.domain`, `projects.feature.home`, etc.
4. **No Flavor Proliferation:** Product flavors (`dev`, `staging`, `prod`) belong **only** in `:app`. Pass flavor values into `:core:*` via Hilt qualifiers defined in `:core:common` (`@ApiBaseUrl`, `@IsDebugBuild`) and bound in `app/.../di/AppConfigModule.kt`.

---

## 4. Presentation Layer Rules (Jetpack Compose + MVI)

Every screen in `:feature:<name>` MUST follow this exact 4-file blueprint:

1. **`<Feature>Contract.kt`**:
   - `<Feature>UiState : UiState` — single immutable `data class` holding all UI state.
   - `<Feature>Intent : UiIntent` — `sealed interface` covering every user action.
   - `<Feature>Effect : UiEffect` — `sealed interface` covering one-shot side effects (navigation, snackbars).
2. **`<Feature>ViewModel.kt`**:
   - Annotated with `@HiltViewModel`, extending `BaseMviViewModel<S, I, E>` from `:core:ui`.
   - Injects Domain UseCases only (never Repositories directly if a UseCase exists, and never DAOs/APIs).
   - Mutates state exclusively via `updateState { copy(...) }`.
   - Emits one-off events exclusively via `sendEffect(...)`.
3. **`<Feature>Screen.kt`**:
   - Public `<Feature>Route(...)` composable: obtains `hiltViewModel()`, collects `uiState` via `collectAsStateWithLifecycle()`, collects `effects` via `ObserveAsEvents(viewModel.effects) { ... }`, and delegates rendering to `<Feature>Screen`.
   - `internal` `<Feature>Screen(...)` composable: 100% stateless, accepts `(uiState, onIntent, modifier)`, has zero ViewModel/Hilt references, and includes a `@Preview` wrapped in `KuchBhiTheme(dynamicColor = false)`.
4. **`navigation/<Feature>Navigation.kt`**:
   - Type-safe `@Serializable` route destination (`data object` or `data class` for arguments).
   - `NavController.navigateTo<Feature>(...)` and `NavGraphBuilder.<feature>Screen(...)` extension functions.
   - Wired into `app/src/main/kotlin/com/kuchBhi/com/navigation/KuchBhiNavHost.kt`.

**Design System Discipline:**
- Always use `MaterialTheme.colorScheme`, `MaterialTheme.typography`, `MaterialTheme.shapes`, and shared components from `:core:designsystem` and `:core:ui`.
- Never hardcode raw `Color(0xFF...)` literals inside `:feature:*`.
- Always provide stable `key = { it.id }` in `LazyColumn` / `LazyRow`.

---

## 5. Domain, Data & Concurrency Rules

1. **Offline-First Single Source of Truth (SSOT):**
   - UI observes local storage (Room / DataStore) via `Flow`.
   - Network fetches write into local storage via `Upsert` while reconciling local-only fields (e.g., bookmarks).
   - Never expose network DTOs (`*Dto`) or Room entities (`*Entity`) outside `:core:network` / `:core:database` / `:core:data`; always map them to `:core:model` domain types.
2. **Error Handling:**
   - Wrap fallible `suspend` operations with `runAppCatching { ... }` returning `AppResult<T>`.
   - **Never** catch and swallow `CancellationException`.
3. **Structured Concurrency & Dispatchers:**
   - **Never** use `GlobalScope` or `runBlocking` in production code.
   - **Never** hardcode `Dispatchers.IO`, `Dispatchers.Default`, or `Dispatchers.Main` inside repositories/use cases; inject `@Dispatcher(KuchBhiDispatchers.IO)` (or `Default`/`Main`).
4. **Room Migrations:**
   - Keep `exportSchema = true` and commit generated schemas under `core/database/schemas/`.
   - Write explicit `Migration` objects for schema version increments; never use `fallbackToDestructiveMigration()` in production builds.

---

## 6. Security & Release Integrity Rules

- **Zero Hardcoded Secrets:** Never commit API keys, tokens, signing keystores (`*.jks`, `*.keystore`), `google-services.json`, or `local.properties`.
- **HTTPS Only in Production:** Never enable `cleartextTrafficPermitted="true"` in `app/src/main/res/xml/network_security_config.xml`.
- **Backup & Obfuscation:** Keep `android:allowBackup="false"` in `AndroidManifest.xml`, keep `isMinifyEnabled = true` and `isShrinkResources = true` for `release`, and update `app/proguard-rules.pro` whenever adding reflection-dependent libraries.

---

## 7. Mandatory Testing & Verification Gates

Every code change MUST satisfy these requirements before completion:

1. **Unit Tests Required:**
   - ViewModel changes -> unit tests in `feature/<name>/src/test/...` using `MainDispatcherRule`, `Turbine`, `Truth`, and fakes from `:core:testing`.
   - Repository / UseCase changes -> unit tests in `core/data/src/test/...` or `core/domain/src/test/...`.
2. **Run All Verification Commands (Must Exit 0):**
   ```bash
   ./gradlew ktlintCheck detekt
   ./gradlew testDevDebugUnitTest :core:model:test :core:domain:test
   ./gradlew :app:lintDevDebug
   ./gradlew :app:assembleDevDebug
   ```
   *(If formatting fails, run `./gradlew ktlintFormat` and re-verify. On Windows PowerShell, use `.\gradlew.bat`.)*
3. **Static Analysis Guardrails:**
   - Never suppress `ktlint`, `Detekt`, or Android Lint warnings blindly (`@Suppress`) unless justified with an inline comment.
   - Keep functions focused (Detekt `LongMethod <= 80`, `MaxLineLength <= 120`, `maxIssues: 0`).

---

## 8. Adding a New Feature Module (Checklist)

1. Create `feature/<name>/build.gradle.kts` applying `alias(libs.plugins.kuchbhi.android.feature)`.
2. Register `include(":feature:<name>")` in `settings.gradle.kts`.
3. Add `implementation(projects.feature.<name>)` to `app/build.gradle.kts`.
4. Implement Contract, ViewModel, Route/Screen + Preview, Navigation, and ViewModel unit tests.
5. Wire the route inside `KuchBhiNavHost.kt`.
6. Update `.github/CODEOWNERS` if a dedicated owner applies.
7. Run the full verification suite in Section 7.

---

## 9. Commits & Architectural Decisions

- Follow **Conventional Commits**: `feat(<scope>): ...`, `fix(<scope>): ...`, `refactor(<scope>): ...`, `test(<scope>): ...`, `docs(<scope>): ...`, `chore(<scope>): ...`.
- For any structural or library-level decision (e.g., adding a new persistence engine, navigation pattern, or DI framework), add an ADR under `docs/adr/NNNN-<title>.md` and index it in `docs/adr/README.md`.
- Keep `README.md`, `docs/architecture.md`, `docs/modularization.md`, and this `AGENTS.md` synchronized whenever modules, SDK levels, or toolchain versions change.
