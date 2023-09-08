package ru.mleykhner.shkedapp.android.ui.screens

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.icerock.moko.resources.StringResource
import ru.mleykhner.shared_resources.SharedRes
import ru.mleykhner.shkedapp.android.R
import ru.mleykhner.shkedapp.android.ui.theme.AppTheme
import ru.mleykhner.shkedapp.utils.Strings

sealed class Screen(val route: String, val nameId: StringResource, @DrawableRes val icon: Int) {
    object News : Screen("news", SharedRes.strings.news, R.drawable.newspaper)
    object Schedule : Screen("schedule", SharedRes.strings.schedule, R.drawable.calendar_view_day)
    object Tasks : Screen("tasks", SharedRes.strings.tasks, R.drawable.book)
}

val items = listOf(
    Screen.News,
    Screen.Schedule,
    Screen.Tasks
)


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(painterResource(id = screen.icon), contentDescription = null) },
                        label = { Text(Strings(LocalContext.current).get(screen.nameId, emptyList())) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            Log.e("Navigation Bar", "Page changed to: ${screen.route}")
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
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
    ) { scaffoldPadding ->
        NavHost(navController, startDestination = Screen.Schedule.route, modifier = Modifier.padding(scaffoldPadding)) {
            composable(Screen.News.route) { Text(Screen.News.route) }
            composable(Screen.Schedule.route) { AuthScreen(
                isPresented = true,
                onDismiss = { /*TODO*/ }) }
            composable(Screen.Tasks.route) { Text(Screen.Tasks.route) }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 360, heightDp = 740,
    locale = "en"
)
@Composable
fun DefaultPreview() {
    AppTheme {
        MainScreen()
    }
}