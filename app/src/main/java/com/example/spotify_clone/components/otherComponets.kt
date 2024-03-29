@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalLayoutApi::class
)

package com.example.spotify_clone.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.spotify_clone.R
import com.example.spotify_clone.data.Firebase.listOfCurrentArtist
import com.example.spotify_clone.data.LibraryScreenViewModel
import com.example.spotify_clone.musicPlayer.Player
import com.example.spotify_clone.musicPlayer.PlayerEvent
import com.example.spotify_clone.musicPlayer.artistViewModel
import com.example.spotify_clone.musicPlayer.currentSongTrack
import com.example.spotify_clone.ui.theme.Background
import com.example.spotify_clone.ui.theme.Secondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumCard(title:String, image: String, onClick:()->Unit){

    Card( colors = CardDefaults.cardColors(
        containerColor = Color.Transparent,
        contentColor = Color.LightGray,
        disabledContainerColor = Color.Transparent

    ), onClick = { onClick.invoke() }
        , modifier = Modifier
            .requiredWidth(160.dp)
            .height(190.dp)) {

        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {
                AsyncImage(model = image,
                    contentDescription = title,
                    placeholder = painterResource(id = R.drawable.logo),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    contentScale = ContentScale.Crop,
                    )


                Text(text = title, modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp, 2.dp, 0.dp, 0.dp), fontSize = 12.sp, textAlign = TextAlign.Center)
            }

        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun NowPlayingBar(player: Player, onCLick: () -> Unit) {


    val track=mutableStateOf(currentSongTrack.value)
    val artists= remember {
    mutableStateOf(artistViewModel.getListOfArtist(track.value.artist))
    }

    val showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    val currentValue = remember{ mutableFloatStateOf(player.player.currentPosition.toFloat()) }

    val libraryScreenViewModel=LibraryScreenViewModel()

    val showContextMenu= remember {
        mutableStateOf(false)
    }

    LaunchedEffect(player.player) {
        while (true) {
            currentValue.floatValue = player.player.currentPosition.toFloat()
            delay(1000) // Adjust the delay based on your update frequency
        }
    }

    if (track.value.title!="") {
        NavigationBar(
            modifier = Modifier
                .background(Background)
                .height(70.dp)
                .clickable {
                    onCLick.invoke()
                    showBottomSheet.value = true
                },
            containerColor = Color(0x3AD8D8D8),
            contentColor = Color.White,
            tonalElevation = 8.dp,

            ) {

            AsyncImage(
                model = track.value.image,
                contentDescription = "",
                placeholder = painterResource(id = R.drawable.logo),
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxHeight()
                    .width(70.dp),
                contentScale = ContentScale.Fit
            )



            Text(
                text = track.value.title, color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(12.dp),
                textAlign = TextAlign.Left
            )


            if (track.value.isPlaying) {
                Icon(
                    painter = painterResource(id = R.drawable.pauseicon),
                    contentDescription = " Pause",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .align(Alignment.CenterVertically)
                        .weight(0.3f)
                        .clickable {
//                            onClick.value
                            player.onEvent(PlayerEvent.SongPaused(false))
                        })

            } else {
                Icon(
                    imageVector = Icons.Filled.PlayArrow, contentDescription = "Play",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .align(Alignment.CenterVertically)
                        .weight(0.3f)
                        .clickable {
//                            onClick.value
                            player.onEvent(PlayerEvent.SongResumed(true))

                        })
            }

        }

//    Expanded State

        if (showBottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet.value = false
                },
                sheetState = sheetState,
                containerColor = Color.DarkGray,
                modifier = Modifier.fillMaxSize()
            ) {

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(imageVector = Icons.Filled.KeyboardArrowDown,
                        tint = Color.White,
                        contentDescription = "Collapse",
                        modifier = Modifier
                            .clickable {
                                scope.launch {
                                    sheetState.hide()
                                    showBottomSheet.value = false
                                }
                            }
                            .height(30.dp)
                            .width(30.dp)
                            .align(Alignment.Top))

                    Text(
                        text = "Current Playing Song",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                            .padding(2.dp)
                    )



                }


                AsyncImage(
                    model = track.value.image, contentDescription = currentSongTrack.value.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .padding(8.dp), contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = track.value.title,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                )

                Text(
                    text = listOfCurrentArtist.value,
                    color = Color.LightGray,
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                )



                Slider(
                    valueRange = 0f..abs(player.player.contentDuration.toFloat()),
                    value = currentValue.floatValue,
                    onValueChange = {
                        scope.launch {
                            currentValue.floatValue = it
                            track.value.duration = it
                            player.onEvent(PlayerEvent.SkipSong(it))
                        }
                    },
                    steps = 0,
                    modifier = Modifier.padding(horizontal = 30.dp),
                    colors = SliderDefaults.colors(
                        activeTrackColor = Color.Red,
                        activeTickColor = Color.Red,
                        thumbColor = Color.Red,
                    )
                )


                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 30.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    val liked = remember {
                        mutableStateOf(false)
                    }

                    val color = remember {
                        mutableStateOf(Color.White)
                    }
                    Icon(imageVector = if (liked.value) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Rounded.FavoriteBorder
                    }, contentDescription = "Like", tint = color.value, modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                            liked.value = true
                            color.value = Color.Red
                            libraryScreenViewModel.addLike(track.value.mediaItemId)
                        })

                    IconButton(
                        onClick = {  player.onEvent(PlayerEvent.PreviousSong(0)) },
                        colors = IconButtonDefaults.iconButtonColors(
                            disabledContentColor = Color.Gray,
                            disabledContainerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                      enabled = track.value.hasPrevious,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(painterResource(id = R.drawable.previous),
                            contentDescription = "Previous",
                            modifier = Modifier
                                .width(45.dp)
                                .align(Alignment.CenterVertically))
                    }

                    if (track.value.isPlaying) {
                        IconButton(
                            onClick = { player.onEvent(PlayerEvent.SongPaused(false)) },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.White,

                            ),
                            modifier = Modifier
                                .width(70.dp)
                                .height(70.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.pauseicon),
                                contentDescription = "Pause",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp)
                                    .align(Alignment.CenterVertically)
                            )
                        }
                    } else {

                        IconButton(
                            onClick = { player.onEvent(PlayerEvent.SongResumed(true)) },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.White
                            ),
                            modifier = Modifier
                                .width(70.dp)
                                .height(70.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = "Pause",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp)
                                    .align(Alignment.CenterVertically)
                            )
                        }

                    }

                    IconButton(
                        onClick = {  player.onEvent(PlayerEvent.NextSong(0)) },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.White
                        ),
                        enabled = track.value.hasNext,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.nextbutton),
                            contentDescription = "Next",
                            modifier = Modifier
                                .width(45.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }



                    Icon(imageVector = Icons.Outlined.AddCircle,
                        contentDescription = "Add to Playlist",
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .clickable { showContextMenu.value=true})


                }

            }
        }
    }


    val currentItem= remember {
        mutableListOf(track.value.mediaItemId)
    }

    if (showContextMenu.value){
        SongOptionMenu(data = currentItem) {
            showContextMenu.value=false
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListItem(image:String,title:String,rank:String,likes:String,onOptionClick:()->Unit,onClick: () -> Unit){

    FlowRow(modifier = Modifier
        .fillMaxWidth()
        .height(70.dp)
        .padding(4.dp)
        .clickable { onClick.invoke() }
    , verticalArrangement = Arrangement.Center, horizontalArrangement = Arrangement.Start
        ) {

        if (rank!="") {
            Text(
                modifier = Modifier
                    .width(50.dp)
                    .padding(4.dp)
                    .align(Alignment.CenterVertically),
                text = rank,
                color = Color.White,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
        }

        AsyncImage(model = image, contentDescription = title, placeholder = painterResource(id = R.drawable.logo),
            modifier = Modifier
                .width(50.dp)
                .padding(5.dp), contentScale = ContentScale.Crop)

        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .weight(1f)
        ) {
            Text(modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
                .weight(1f)
                ,text = title, color = Color.White, fontSize = 15.sp, textAlign =TextAlign.Start, maxLines = 3, softWrap = true )

            Text(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp)
                .weight(1f)
                ,text = "Likes : $likes", color = Color.White, fontSize = 12.sp, textAlign =TextAlign.Start, maxLines = 3, softWrap = true )
        }

        Icon(imageVector = Icons.Filled.List, contentDescription = "", modifier = Modifier
            .width(30.dp)
            .padding(4.dp)
            .weight(.1f)
            .clickable { onOptionClick.invoke() }
            .align(Alignment.CenterVertically), tint = Color.White)

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlaylistRow(image:String,title:String,songs:String,onDelete:(String)->Unit,id:String,onClick: () -> Unit){
    FlowRow(modifier = Modifier
        .fillMaxWidth()
        .height(70.dp)
        .padding(4.dp)
        .clickable { onClick.invoke() }
        , verticalArrangement = Arrangement.Center, horizontalArrangement = Arrangement.Start
    ) {

        AsyncImage(model = image, contentDescription = title, placeholder = painterResource(id = R.drawable.logo),
            modifier = Modifier
                .width(70.dp)
                .padding(5.dp), contentScale = ContentScale.Crop)

        Column(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .weight(1f)
        ) {
            Text(modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
                .weight(1f)
                ,text = title, color = Color.White, fontSize = 15.sp, textAlign =TextAlign.Start, maxLines = 3, softWrap = true )

            Text(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp)
                .weight(1f)
                ,text = "Songs : $songs", color = Color.White, fontSize = 12.sp, textAlign =TextAlign.Start, maxLines = 3, softWrap = true )
        }

        Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete", modifier = Modifier
            .width(30.dp)
            .clickable {
                onDelete.invoke(id)
            }
            .padding(4.dp)
            .weight(.1f)
            .align(Alignment.CenterVertically), tint = Color.White)
    }
}


@Composable
fun SearchBarInput(onChange:(String)->Unit){
    IndicatorText(value = "Search", textColor = Color.White, align = TextAlign.Start)
    val search= remember { mutableStateOf("") }
    val localFocus= LocalFocusManager.current
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            cursorColor = Secondary,
            focusedBorderColor = Color.LightGray,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        maxLines = 1,
        keyboardActions = KeyboardActions {
            localFocus.clearFocus()
        },
        value = search.value,
        onValueChange = {
            search.value = it
            onChange.invoke(it)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
        })
}



@Composable
fun CreatePlaylistDialog(onDismissRequest:()->Unit,onConfirm:(String)->Unit){
    val value= remember {
        mutableStateOf("")
    }
    Dialog(onDismissRequest = { onDismissRequest.invoke() }, properties = DialogProperties(dismissOnBackPress = true,dismissOnClickOutside = true)) {
        ElevatedCard(modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
            shape = CardDefaults.elevatedShape,
            colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray
            )) {

            Spacer(modifier = Modifier.height(10.dp))
            IndicatorText(value = "Give your playlist a name", textColor = Color.White, align = TextAlign.Center )

            TextField(value = value.value, onValueChange = {
                value.value=it
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(17.dp)
                .background(Color.Transparent),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.LightGray,
                    focusedTextColor = Color.Black
                ))

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = LibraryScreenViewModel().newPlaylistName.value)

            FlowRow(modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp), verticalArrangement = Arrangement.Center, horizontalArrangement = Arrangement.SpaceAround) {

                ElevatedButton(onClick = { onDismissRequest.invoke() }) {
                    Text(text = "Cancel")
                }

                ElevatedButton(onClick = { onConfirm.invoke(value.value) }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                )) {
                    Text(text = "Create")
                }
            }
        }

    }
}


@Composable
fun MessageDialog(message: String,onDismissRequest: () -> Unit){
//    val value= remember {
//        mutableStateOf("")
//    }
    Dialog(onDismissRequest = { onDismissRequest.invoke() }, properties = DialogProperties(dismissOnBackPress = true,dismissOnClickOutside = true)) {
        ElevatedCard(modifier = Modifier
            .fillMaxWidth()
            ,
            shape = CardDefaults.elevatedShape,
            colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray
            )) {

            TextField(value = message, onValueChange = {
            }, modifier = Modifier
                .fillMaxWidth()
//                .padding(17.dp)
                .background(Color.Transparent),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black
                ), readOnly = true)


        }

    }
}


@Composable
fun SongOptionMenu(data: MutableList<String>, onDismissRequest: () -> Unit){
    val viewModel= remember {
        LibraryScreenViewModel()
    }

    val playlists= remember {
        viewModel.listOfPlaylists
    }

    viewModel.refreshList()
    ModalBottomSheet(onDismissRequest = { onDismissRequest.invoke() },
        sheetState = rememberModalBottomSheetState(),
        containerColor = Background) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())) {
            FlowRow(modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.addLike(data.first()) }
                .padding(horizontal = 15.dp), verticalArrangement = Arrangement.Center, horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Like", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(20.dp))
                Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Like", tint = Color.Red, modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterVertically))
            }

            Text(text = "Add to Playlist", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 35.dp))

            playlists.value.map { list->
                Divider(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 10.dp))
                FlowRow(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        viewModel.addToPlaylist(listId = list.key.toString(), songId = data)
                        viewModel.refreshList()
                        onDismissRequest.invoke()
                    }
                    .padding(horizontal = 15.dp), verticalArrangement = Arrangement.Center, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text =list.child("listName").value.toString() , color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 20.dp))
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "Like", tint = Color.Red, modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .align(Alignment.CenterVertically))
                }
            }

            Spacer(modifier = Modifier.height(30.dp))


        }
    }
}


