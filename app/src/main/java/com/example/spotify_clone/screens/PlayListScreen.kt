package com.example.spotify_clone.screens

import android.annotation.SuppressLint
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.spotify_clone.components.HeadingText
import com.example.spotify_clone.components.NowPlayingBar
import com.example.spotify_clone.data.PlayListScreenViewModel
import com.example.spotify_clone.musicPlayer.Player
import com.example.spotify_clone.ui.theme.Background


@SuppressLint("UnrememberedMutableState")
@Composable
fun PlayListScreen(context:Context){
//
    val listViewModel= remember{ PlayListScreenViewModel() }
    val listData by remember{
        mutableStateOf(listViewModel.getData())
    }

    val player = remember {
        Player(context)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Background
    ) {
        Scaffold(
            containerColor = Background,
            bottomBar = {
                NowPlayingBar(context = context, player = player, onCLick = {  })
            },
            contentColor = Background

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
//                    val listData= remember{ mutableStateOf(listViewModel.getData()) }
                    HeadingText(value =listData.listId, textColor = Color.White)
                    AsyncImage(model = listData.listImage, contentDescription = "Cover Image", modifier = Modifier.fillMaxSize())
                }

            }
        }
    }

}


