package com.example.spotify_clone.screens.adminScreens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.spotify_clone.components.HeadingText
import com.google.firebase.auth.FirebaseAuth

@Composable
fun demoScreen(){
    Surface(
        Modifier.fillMaxSize()

    ) {
        HeadingText(value = FirebaseAuth.getInstance().currentUser?.displayName.toString(), textColor = Color.Black)
    }
}
