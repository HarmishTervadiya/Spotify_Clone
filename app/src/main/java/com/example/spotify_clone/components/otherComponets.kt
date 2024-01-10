@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.example.spotify_clone.components

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.spotify_clone.data.LibraryScreenViewModel
import com.example.spotify_clone.musicPlayer.Player
import com.example.spotify_clone.musicPlayer.PlayerEvent
import com.example.spotify_clone.musicPlayer.currentSongTrack
import com.example.spotify_clone.ui.theme.Background
import com.example.spotify_clone.ui.theme.Secondary
import kotlinx.coroutines.launch

@Composable
fun HeadingTopBar(value:String,icon1:ImageVector,icon2:ImageVector,onHeartCLick:()->Unit,onSettingClick:()->Unit){

    Row (horizontalArrangement = Arrangement.Start, modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)) {
        Text(
            text = value,
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
            imageVector = icon1,
            contentDescription = "Favourite",
            tint = Color.Red,
            modifier = Modifier
                .fillMaxHeight()
                .padding(15.dp)
                .align(Alignment.Bottom)
                .clickable {
                    onHeartCLick.invoke()
                }
        )

        Icon(
            imageVector = icon2,
            contentDescription = "Favourite",
            tint = Color.White,
            modifier = Modifier
                .fillMaxHeight()
                .padding(12.dp)
                .align(Alignment.Bottom)
                .clickable {
                    onSettingClick.invoke()
                }
        )

    }
}



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
fun NowPlayingBar(context: Context,player:Player,onCLick:()->Unit) {



    val track=mutableStateOf(currentSongTrack.value)
    var showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    NavigationBar(modifier = Modifier
        .background(Background)
        .height(70.dp)
        .clickable {
            onCLick.invoke()
            showBottomSheet.value = true
            scope.launch {
            }
        }
        , containerColor = Color(0x3AD8D8D8),
        contentColor = Color.White,
        tonalElevation = 8.dp,

        ) {

            AsyncImage(model = track.value.image, contentDescription = "", placeholder = painterResource(id = R.drawable.logo),
            modifier = Modifier
                .padding(5.dp)
                .fillMaxHeight()
                .width(70.dp), contentScale = ContentScale.Fit
                )



            Text(text = track.value.title, color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(12.dp),
                textAlign = TextAlign.Left)


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

            }else {
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

    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet.value = false
            },
            sheetState = sheetState,
            modifier = Modifier.fillMaxSize()
        ) {
            // Sheet content
            Button(onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet.value =false
                    }
                }
            }) {
                Text("Hide bottom sheet")
            }
        }
    }

    }


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ListItem(image:String,title:String,rank:String,likes:String,onClick: () -> Unit){

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
            .align(Alignment.CenterVertically), tint = Color.White)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PlaylistRow(image:String,title:String,songs:String,onClick: () -> Unit){
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
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.LightGray,
            cursorColor = Secondary,
            containerColor = Color.White
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