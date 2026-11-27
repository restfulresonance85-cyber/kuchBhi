# Modularization Guide

## Module Catalog

| Module | Type | Convention Plugins | Responsibility |
| --- | --- | --- | --- |
| `:app` | Android Application | `application`, `compose`, `flavors`, `hilt` | App entry point, top-level navigation graph, flavor config, R8 rules |
| `:core:model` | Pure Kotlin JVM | `kuchbhi.jvm.library` | Domain models and `AppResult` |
| `:core:domain` | Pure Kotlin JVM | `kuchbhi.jvm.library` | Repository interfaces and UseCases |
| `:core:common` | Android Library | `library`, `hilt` | Coroutine dispatchers, `ApplicationScope`, DI qualifiers |
| `:core:network` | Android Library | `library`, `hilt`, `serialization` | Retrofit APIs, OkHttp client, DTOs |
| `:core:database` | Android Library | `library`, `hilt`, `room` | Room DB, Entities, DAOs, schema exports |
| `:core:datastore` | Android Library | `library`, `hilt` | Preferences DataStore wrapper |
| `:core:data` | Android Library | `library`, `hilt` | Offline-first repository implementations & mappers |
| `:core:designsystem` | Android Library | `library`, `library.compose` | Material 3 theme, colors, typography, shapes, base components |
| `:core:ui` | Android Library | `library`, `library.compose` | `BaseMviViewModel`, `ObserveAsEvents`, shared state views |
| `:core:testing` | Android Library | `library` | `MainDispatcherRule`, shared test fakes |
| `:feature:home` | Feature Library | `kuchbhi.android.feature` | Home feed MVI screen, ViewModel, route, unit tests |
| `:feature:settings` | Feature Library | `kuchbhi.android.feature` | User preferences MVI screen, ViewModel, route, unit tests |

---

## How to Add a New Feature Module

1. Create `feature/<name>/build.gradle.kts`:
   ```kotlin
   plugins {
       alias(libs.plugins.kuchbhi.android.feature)
   }

   android {
       namespace = "com.kuchBhi.com.feature.<name>"
   }
   ```
2. Register `include(":feature:<name>")` in `settings.gradle.kts`.
3. Add `implementation(projects.feature.<name>)` in `app/build.gradle.kts`.
4. Implement the standard feature files under `feature/<name>/src/main/kotlin/com/kuchBhi/com/feature/<name>/`:
   - `<Name>Contract.kt` (`UiState`, `UiIntent`, `UiEffect`)
   - `<Name>ViewModel.kt` (`@HiltViewModel` extending `BaseMviViewModel`)
   - `<Name>Screen.kt` (`<Name>Route` + stateless `<Name>Screen` + `@Preview`)
   - `navigation/<Name>Navigation.kt` (`@Serializable` route + `NavGraphBuilder.<name>Screen(...)`)
5. Wire the new destination inside `KuchBhiNavHost.kt`.
6. Add ViewModel unit tests under `feature/<name>/src/test/kotlin/...`.
7. Add module ownership entry in `.github/CODEOWNERS`.
