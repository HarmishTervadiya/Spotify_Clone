@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.spotify_clone.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spotify_clone.components.ListItem
import com.example.spotify_clone.components.NowPlayingBar
import com.example.spotify_clone.components.SearchBarInput
import com.example.spotify_clone.data.SearchScreenViewModel
import com.example.spotify_clone.musicPlayer.Player
import com.example.spotify_clone.musicPlayer.PlayerEvent
import com.example.spotify_clone.navigation.Router
import com.example.spotify_clone.navigation.Screen
import com.example.spotify_clone.ui.theme.Background
import kotlinx.coroutines.launch

//val viewModel=SearchScreenViewModel()
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SearchScreen(context:Context,player: Player) {
    val viewModel= remember{ SearchScreenViewModel() }

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
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                )
                {

                    SearchBarInput(onChange = {
                        scope.launch {
                            viewModel.updateInput(it)
                            viewModel.getSearchResult()
                        }
                    })

                    viewModel.searchList.value.map {item->
                        ListItem(
                            image = item.child("cover_image").value.toString(),
                            title = item.child("Song_Name").value.toString(),
                            rank = "",
                            likes = item.child("Likes").value.toString(),
                            onOptionClick = { }
                        ) {
                            scope.launch {
                                player.onEvent(PlayerEvent.PlaySong(item))
                            }
                        }

                    }
                }

            }


        }
    }

    BackHandler {
        Router.navigateTo(Screen.HomeScreen)
    }
}
