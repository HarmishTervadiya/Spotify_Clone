package com.example.spotify_clone.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spotify_clone.components.IndicatorText
import com.example.spotify_clone.components.ListItem
import com.example.spotify_clone.components.NowPlayingBar
import com.example.spotify_clone.data.PlayListScreenViewModel
import com.example.spotify_clone.musicPlayer.Player
import com.example.spotify_clone.musicPlayer.PlayerEvent
import com.example.spotify_clone.navigation.Router
import com.example.spotify_clone.ui.theme.Background

val listViewModel= PlayListScreenViewModel()
val songList= listViewModel.getSongList(Router.listId.value[1],Router.listId.value[0])

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun PlayListScreen(context:Context,player: Player){


    val listRef=if (Router.listId.value[1] == "Artists") {
        listViewModel.getArtist(Router.listId.value[0])

    } else if (Router.listId.value[1] == "Albums") {
        listViewModel.getAlbums(Router.listId.value[0])

    } else if (Router.listId.value[1] == "Songs") {
        listViewModel.getPlaylist(Router.listId.value[0])

    } else {
        listViewModel.getArtist(Router.listId.value[0])
    }
//
//    val player = remember {
//        Player(context)
//    }

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
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Red,
                        modifier = Modifier
                            .padding(5.dp)
                            .height(30.dp)
                            .width(50.dp)
                    )
                    ElevatedCard(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    ) {
                        AsyncImage(
                            model = listRef.value.listImage,
                            contentDescription = "Cover Image",
                            contentScale = ContentScale.Crop,
                            alignment = Alignment.Center,
                            modifier = Modifier
                                .width(160.dp)
                                .height(160.dp)
                        )
                    }

                    IndicatorText(
                        value = listRef.value.listName,
                        textColor = Color.White,
                        align = TextAlign.Center
                    )

                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = listRef.value.listName,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        ElevatedButton(
                            onClick = { },
                            shape = CircleShape,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(60.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "Play All",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(10.dp))
                    var num = 0

                    songList.map {
                        num++
                        ListItem(
                            image = it.child("cover_image").value.toString(),
                            title = it.child("Song_Name").value.toString(),
                            rank = num.toString(),
                            likes = it.child("Likes").value.toString()
                        ){
                            player.onEvent(PlayerEvent.SongPaused(true))
                            player.onEvent(PlayerEvent.PlaylistPlay(currentSong = it,list= songList))
                        }

                    }
                }



            }
        }
    }

}



