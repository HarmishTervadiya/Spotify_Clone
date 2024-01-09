package com.example.spotify_clone.screens

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spotify_clone.components.NowPlayingBar
import com.example.spotify_clone.musicPlayer.Player
import com.example.spotify_clone.ui.theme.Background

@Composable
fun SearchScreen(context:Context,player: Player) {

//    val player = remember {
//        Player(context)
//    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Background
    ) {

        var showBottomSheet = remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        fun bottomSheet() {
            showBottomSheet.value = true
        }

        Scaffold(
            containerColor = Background,
            bottomBar = {
                NowPlayingBar(context = context, player = player, onCLick = { bottomSheet() })
            },
            contentColor = Background
            // Place BottomNav within the Scaffold's bottomBar
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())

                )
                {

                }

            }


        }
    }

}
