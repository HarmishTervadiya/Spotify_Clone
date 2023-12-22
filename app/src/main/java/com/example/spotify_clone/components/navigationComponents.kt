package com.example.spotify_clone.components


import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.spotify_clone.navigation.Router
import com.example.spotify_clone.navigation.Screen
import com.example.spotify_clone.ui.theme.Background
import com.example.spotify_clone.ui.theme.Secondary

@Composable
fun BottomNav() {

    var selectedItem = remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Search", "Playlists")
    val icons= listOf(
        Icons.Filled.AccountCircle,
        Icons.Filled.Search,
        Icons.Filled.List
    )

    val screens= listOf(
        Screen.HomeScreen,
        Screen.SearchScreen,
        Screen.LibraryScreen

    )
    NavigationBar(modifier = Modifier.background(Background)
    , containerColor = Color.Transparent,
        contentColor = Color.White,
        tonalElevation = 5.dp,
        ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item) },
                selected = Router.currentScreen.value==screens[index],
                onClick = { Router.navigateTo(screens[index]) },
                modifier = Modifier.background(Color.Transparent),
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Secondary,
                    selectedTextColor = Secondary,
                    indicatorColor = Background,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White,


                )
                )
        }
    }
}