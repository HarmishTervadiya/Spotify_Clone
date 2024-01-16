@file:OptIn(ExperimentalLayoutApi::class, ExperimentalLayoutApi::class,
    ExperimentalLayoutApi::class
)

package com.example.spotify_clone.screens

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotify_clone.components.CreatePlaylistDialog
import com.example.spotify_clone.components.NowPlayingBar
import com.example.spotify_clone.components.PlaylistRow
import com.example.spotify_clone.data.LibraryScreenViewModel
import com.example.spotify_clone.musicPlayer.Player
import com.example.spotify_clone.musicPlayer.PlayerEvent
import com.example.spotify_clone.navigation.Router
import com.example.spotify_clone.navigation.Screen
import com.example.spotify_clone.ui.theme.Background
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LibraryScreen(context: Context,player: Player) {

    val viewModel= remember{ LibraryScreenViewModel() }
    val openDialog= remember {
        mutableStateOf(false)
    }
    viewModel.refreshList()


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Background
    ) {


        var showBottomSheet = remember { mutableStateOf(true) }
        val scope = rememberCoroutineScope()

        fun bottomSheet() {
            showBottomSheet.value = true
        }

        Scaffold(
            containerColor = Background,
            bottomBar = {
                NowPlayingBar(player = player) { bottomSheet() }
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
//                    modifier = Modifier
//                        .verticalScroll(rememberScrollState())
                )
                {

                    viewModel.refreshList()

                    FlowRow(modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp), verticalArrangement = Arrangement.Center, horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text ="Hello, ${Firebase.auth.currentUser?.displayName}",
                            color = Color.White,
                            textAlign = TextAlign.Start,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Create Playlist", tint = Color.White, modifier = Modifier
                            .height(40.dp)
                            .width(35.dp)
                            .align(Alignment.CenterVertically)
                            .clickable { openDialog.value = true })
                    }

                    Text(text ="Your Library",
                        color = Color.White,
                        textAlign = TextAlign.Start,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 7.dp)
                    )
                    val playlists= remember {
                        viewModel.listOfPlaylists
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyColumn{
                        items(playlists.value){list->
                            PlaylistRow(image = list.child("cover_image").value.toString(), title = list.child("listName").value.toString(),songs =list.child("listOfSongId").children.count().toString(),id=list.key.toString(),onDelete={
                                scope.launch{
                                    viewModel.deletePlaylist(it)
                                    viewModel.refreshList()
                                }
                            } ) {
                                scope.launch {
                                    Router.navigateTo(
                                        Screen.PlayListScreen,listOf(
                                            list.key.toString(),
                                            list.ref.parent?.key.toString()
                                        )
                                    ) { player.onEvent(PlayerEvent.SongPaused(false)) }
                                }
                            }
                        }
                    }

                }

                when{
                    openDialog.value ->{
                        CreatePlaylistDialog(onDismissRequest = { openDialog.value=false }, onConfirm = { viewModel.createPlaylist(it)
                            viewModel.refreshList()
                        openDialog.value=false })
                    }
                }

            }
        }
    }

    BackHandler {
        Router.navigateTo(Screen.HomeScreen)
    }
}

