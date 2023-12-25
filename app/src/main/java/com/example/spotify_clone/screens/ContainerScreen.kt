package com.example.spotify_clone.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.spotify_clone.components.BottomNav
import com.example.spotify_clone.navigation.Router
import com.example.spotify_clone.navigation.Screen
import com.example.spotify_clone.ui.theme.Background


@Composable
fun ContainerScreen(){
    Scaffold(
        containerColor = Background,
        bottomBar = { BottomNav() },
        contentColor = Background
        // Place BottomNav within the Scaffold's bottomBar
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Background),
            // Apply Scaffold's inner padding
        ) {

            // Screens to be displayed

            Crossfade(targetState = Router.currentScreen.value, label = "",animationSpec = tween(easing =  LinearEasing)
            ) { screen ->
                when(screen){
                    is Screen.HomeScreen ->{
                        HomeScreen()
                    }

                    is Screen.SearchScreen ->{
                        SearchScreen()
                    }

                    is Screen.LibraryScreen ->{
                        LibraryScreen()
                    }
                    
                    else -> {
                        HomeScreen()
                    }
                }


            }



        }
    }
}

@Preview
@Composable
fun DefaultPreviewContainerScreen(){
    ContainerScreen()
}