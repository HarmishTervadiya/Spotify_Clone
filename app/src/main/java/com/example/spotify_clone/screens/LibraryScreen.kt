package com.example.spotify_clone.screens

import android.content.Context
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.spotify_clone.musicPlayer.Player
import com.example.spotify_clone.navigation.Router
import com.example.spotify_clone.navigation.Screen

@Composable
fun LibraryScreen(context: Context,player: Player) {

    Button(onClick = { Router.navigateTo(Screen.HomeScreen) }
    ) {
        Text(text = "Playlist screen")
    }
}
