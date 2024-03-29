package com.example.spotify_clone.screens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.util.UnstableApi
import com.example.spotify_clone.components.AlbumCard
import com.example.spotify_clone.components.IndicatorText
import com.example.spotify_clone.components.MessageDialog
import com.example.spotify_clone.components.NowPlayingBar
import com.example.spotify_clone.data.Firebase.ArtistViewModel
import com.example.spotify_clone.data.Firebase.SongsViewModel
import com.example.spotify_clone.data.HomeViewModel
import com.example.spotify_clone.data.LoginRegisterViewModel
import com.example.spotify_clone.isServerMessage
import com.example.spotify_clone.musicPlayer.Player
import com.example.spotify_clone.musicPlayer.PlayerEvent
import com.example.spotify_clone.navigation.Router
import com.example.spotify_clone.navigation.Screen
import com.example.spotify_clone.serverMessage
import com.example.spotify_clone.ui.theme.Background
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(UnstableApi::class) @SuppressLint("CoroutineCreationDuringComposition", "NewApi")
@Composable
fun HomeScreen(context: Context,player:Player){



    Surface(modifier = Modifier.fillMaxSize(),
        color = Background) {

        var showBottomSheet by remember { mutableStateOf(true) }
        val scope= rememberCoroutineScope()
        val homeViewModel=HomeViewModel(context)

        fun bottomSheet(){
            showBottomSheet=true
        }
        Scaffold(
            containerColor = Background,
            bottomBar = { NowPlayingBar(player = player) { bottomSheet() }
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


                    val currentTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalTime.now(Clock.tickMinutes(ZoneId.systemDefault()))
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }

                    val greeting: String =
                        if (currentTime.format(DateTimeFormatter.ISO_TIME) > LocalTime.of(20, 0, 0)
                                .format(
                                    DateTimeFormatter.ISO_TIME
                                )
                        ) {
                            "Good Night"
                        } else if (currentTime.format(DateTimeFormatter.ISO_TIME) > LocalTime.of(
                                16,
                                0,
                                0
                            ).format(
                                DateTimeFormatter.ISO_TIME
                            )
                        ) {
                            "Good Evening"
                        } else if (currentTime.format(DateTimeFormatter.ISO_TIME) > LocalTime.NOON.format(
                                DateTimeFormatter.ISO_TIME
                            )
                        ) {
                            "Good Afternoon"
                        } else {
                            "Good Morning"
                        }

                    Row(
                        horizontalArrangement = Arrangement.Start, modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    )


                    {
                        Text(
                            text = greeting,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(10.dp)
                                .weight(.1f)
                                .fillMaxHeight()
                        )
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "Favourite",
                            tint = Color.Red,
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(15.dp)
                                .align(Alignment.Bottom)
                                .clickable {
                                    LoginRegisterViewModel().logout()
                                }
                        )
//                        Icon(
//                            imageVector = Icons.Filled.Settings,
//                            contentDescription = "Setting",
//                            tint = Color.White,
//                            modifier = Modifier
//                                .fillMaxHeight()
//                                .padding(12.dp)
//                                .align(Alignment.Bottom)
//                                .clickable {
//
//                                }
//
//                        )

                    }


                    // Top Picks
                    IndicatorText(
                        value = "Top Picks",
                        textColor = Color.White,
                        align = TextAlign.Left
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val topPicks = SongsViewModel().getTopSongs()
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        userScrollEnabled = true,
                    ) {

                        this.items(topPicks) {
                            AlbumCard(
                                title = it.child("Song_Name").value.toString(),
                                image = it.child("cover_image").value.toString(),
                                onClick = {
                                    scope.launch {
                                        player.onEvent(PlayerEvent.PlaySong(it))
                                    }
                                     })
                        }

                    }


                    // Top Albums
                    IndicatorText(
                        value = "Top Albums",
                        textColor = Color.White,
                        align = TextAlign.Left
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val data = homeViewModel.getAllAlbums()
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        userScrollEnabled = true,
                    ) {

                        this.items(data) {
                            AlbumCard(
                                title = it.child("title").value.toString(),
                                image = it.child("image").value.toString()
                            ) {
                                scope.launch {
                                    Router.navigateTo(
                                        Screen.PlayListScreen,listOf(
                                            it.key.toString(),
                                            it.ref.parent?.key.toString()
                                        )
                                    ) { player.onEvent(PlayerEvent.SongPaused(false)) }
                                }
                            }

                        }

                    }


                    // Top Artists
                    IndicatorText(
                        value = "Top Artists",
                        textColor = Color.White,
                        align = TextAlign.Left
                    )
                    Spacer(modifier = Modifier.height(8.dp))


                    val topArtists = ArtistViewModel().getAllArtists()
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        userScrollEnabled = true,

                        ) {

                        this.items(topArtists) {
                            AlbumCard(

                                title = it.child("Artist_Name").value.toString(),
                                image = it.child("profile").value.toString()
                            ) {
                                scope.launch {
                                    Router.navigateTo(
                                        Screen.PlayListScreen,listOf(
                                            it.key.toString(),
                                            it.ref.parent?.key.toString()
                                        )
                                    ) { player.onEvent(PlayerEvent.SongPaused(false)) }
                                }
                            }

                        }

                    }
                }
//                MusicPlayer(showBottomSheet)
                when{
                    isServerMessage.value ->{
                        MessageDialog(message = serverMessage.value){
                            isServerMessage.value=false
                        }
                        scope.launch {
                            delay(2000)
                            isServerMessage.value=false
                        }
                    }
                }
            }

        }


    }
}


@Preview
@Composable
fun DefaultPreviewHomeScreen(){

//    HomeScreen(context= LocalContext.current.applicationContext)
}
