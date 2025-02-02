package com.deepseek.android.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.deepseek.android.ui.screens.ChatScreen
import com.deepseek.android.ui.screens.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeepSeekApp() {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentScreen = DeepSeekScreen.fromRoute(currentDestination?.route)

    Scaffold(
        bottomBar = {
            NavigationBar {
                DeepSeekScreen.values().forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentScreen == screen,
                        onClick = {
                            navController.navigate(screen.name) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DeepSeekScreen.Chat.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(DeepSeekScreen.Chat.name) {
                ChatScreen()
            }
            composable(DeepSeekScreen.Settings.name) {
                SettingsScreen()
            }
        }
    }
}
