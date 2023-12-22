package com.example.spotify_clone.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.spotify_clone.navigation.Router
import com.example.spotify_clone.navigation.Screen
import com.example.spotify_clone.ui.theme.Background

@Composable
fun RegisterScreen(){
    Surface(modifier = Modifier.fillMaxSize(),
        color = Background
    ) {

        Button(onClick = { Router.navigateTo(Screen.HomeScreen) }
        ) {
            Text(text = "Go to Next screen")
        }
    }
}


@Preview
@Composable
fun DefaultPreviewRegisterScreen(){

    RegisterScreen()
}