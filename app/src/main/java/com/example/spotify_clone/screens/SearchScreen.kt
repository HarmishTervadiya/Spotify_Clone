package com.example.spotify_clone.screens

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.spotify_clone.navigation.Router
import com.example.spotify_clone.navigation.Screen

@Composable
fun SearchScreen(){

    Button(onClick = { Router.navigateTo(Screen.HomeScreen) }
    ) {
        Text(text = "Search screen")
    }
}
