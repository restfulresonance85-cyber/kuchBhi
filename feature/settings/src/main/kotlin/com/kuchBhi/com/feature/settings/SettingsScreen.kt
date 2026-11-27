package com.kuchBhi.com.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuchBhi.com.core.designsystem.component.KuchBhiLoadingIndicator
import com.kuchBhi.com.core.designsystem.component.KuchBhiTopAppBar
import com.kuchBhi.com.core.designsystem.theme.KuchBhiTheme
import com.kuchBhi.com.core.model.ThemeConfig
import com.kuchBhi.com.core.ui.mvi.ObserveAsEvents

@Composable
fun SettingsRoute(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.effects) { effect ->
        when (effect) {
            SettingsEffect.NavigateBack -> onNavigateBack()
        }
    }

    SettingsScreen(
        uiState = uiState,
        onIntent = viewModel::onIntent,
        modifier = modifier,
    )
}

@Composable
internal fun SettingsScreen(
    uiState: SettingsUiState,
    onIntent: (SettingsIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            KuchBhiTopAppBar(
                title = "Settings",
                navigationIcon = {
                    IconButton(onClick = { onIntent(SettingsIntent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        if (uiState.isLoading) {
            KuchBhiLoadingIndicator(modifier = Modifier.padding(innerPadding))
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Appearance",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            ThemeConfig.entries.forEach { config ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onIntent(SettingsIntent.SelectTheme(config)) }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(
                        selected = uiState.themeConfig == config,
                        onClick = { onIntent(SettingsIntent.SelectTheme(config)) },
                    )
                    Text(
                        text = config.displayLabel(),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }

            HorizontalDivider()
            Spacer(modifier = Modifier.height(4.dp))

            SettingsSwitchRow(
                title = "Dynamic Color",
                subtitle = "Match app colors with your Android 15 system wallpaper",
                checked = uiState.useDynamicColor,
                onCheckedChange = { onIntent(SettingsIntent.ToggleDynamicColor(it)) },
            )

            SettingsSwitchRow(
                title = "Usage Analytics",
                subtitle = "Share anonymous crash & performance diagnostics",
                checked = uiState.analyticsEnabled,
                onCheckedChange = { onIntent(SettingsIntent.ToggleAnalytics(it)) },
            )
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

private fun ThemeConfig.displayLabel(): String = when (this) {
    ThemeConfig.FOLLOW_SYSTEM -> "Follow System"
    ThemeConfig.LIGHT -> "Light"
    ThemeConfig.DARK -> "Dark"
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    KuchBhiTheme(dynamicColor = false) {
        SettingsScreen(
            uiState = SettingsUiState(isLoading = false),
            onIntent = {},
        )
    }
}
