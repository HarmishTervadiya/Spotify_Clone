package com.example.spotify_clone.screens

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotify_clone.R
import com.example.spotify_clone.components.AlbumCard
import com.example.spotify_clone.components.HeadingText
import com.example.spotify_clone.data.HomeViewModel
import com.example.spotify_clone.data.LoginRegisterViewModel
import com.example.spotify_clone.musicPlayer.Player
import com.example.spotify_clone.ui.theme.Background
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.LocalTime
import java.time.ZoneId

@SuppressLint("CoroutineCreationDuringComposition", "NewApi")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(context: Context){
    val res= remember {
        mutableStateOf(false)
    }
    Surface(modifier = Modifier.fillMaxSize(),
        color = Background) {

        val database = Firebase.database.reference
        val scope= rememberCoroutineScope()
        val homeViewModel=HomeViewModel(context)
//        var data= remember {
//            mutableListOf(homeViewModel.list)
//        }

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)){
            Column(modifier = Modifier.scrollable(
                ScrollableState { 1F }, orientation = Orientation.Vertical
                ,overscrollEffect = null,enabled = true,reverseDirection = true,flingBehavior = null
            ))
            {


                val currentTime= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalTime.now(Clock.tickMinutes(ZoneId.systemDefault()))
                } else {
                    TODO("VERSION.SDK_INT < O")
                }

                val greeting:String = when (currentTime) {
                    LocalTime.NOON -> {
                        "Good AfterNoon"
                    }

                    LocalTime.of(5,30) -> {
                        "Good Evening"
                    }

                    else -> {
                        "Good Morning"
                    }
                }

                Row (horizontalArrangement = Arrangement.Start, modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp))


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
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Setting",
                        tint = Color.White,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(12.dp)
                            .align(Alignment.Bottom)
                            .clickable {
                                data class Album(
                                    var title: String,
                                    var imageUri: String,
                                    var albumId: Int? = null
                                )

                                val user = Album(
                                    "Top 5 Songs",
                                    imageUri = "https://firebasestorage.googleapis.com/v0/b/spotify-clone-red-ronin.appspot.com/o/album%20cover%2Fdownload.jpg?alt=media&token=78ec283e-4510-49b5-b6ef-36c6b627847b",
                                )
                                scope.launch(Dispatchers.IO) {
                                    database
                                        .child("Albums")
                                        .child("123")
                                        .setValue(user)
                                        .addOnFailureListener {
                                            Toast
                                                .makeText(context, "Failed", Toast.LENGTH_SHORT)
                                                .show()
                                            Log.d("Adding", it.message.toString())
                                        }
                                        .addOnSuccessListener {
                                            if (Player(context).player.isPlaying) {
                                                Player(context).onPause()
                                            } else {
                                                Player(context).playSong("https://firebasestorage.googleapis.com/v0/b/spotify-clone-red-ronin.appspot.com/o/Ganpati%20Bappa.mp3?alt=media&token=6a75275a-9fa4-47a5-a16a-66627cabfda2")
                                            }
                                        }
                                }
                            }

                    )

                }



                HeadingText(value = "Top Picks", textColor = Color.White)
                Spacer(modifier = Modifier.height(10.dp))

//
//                val data=homeViewModel.getAllAlbums()
//                    data.map {
//                        HeadingText(value = it.value.toString(), textColor =Color.White )
//                    }
//
//                var result=mutableListOf<DataSnapshot>()
//                object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        result=snapshot.children.toMutableList()
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        TODO("Not yet implemented")
//                    }
//
//                }

//                val items= remember {
//                    mutableStateListOf<Item>()
//                }
//                var album=""
//                LaunchedEffect(database){
//                    database.child("Albums").addValueEventListener(object : ValueEventListener{
//                        @SuppressLint("NewApi")
//                        override fun onDataChange(snapshot: DataSnapshot) {
//                            items.clear()
//                            album=snapshot.child("123").child("title").value.toString()
//                            snapshot.children.forEach {child ->
//                                val item=child.getValue(Item::class.java)
//                                item?.let {
//                                    items.add(it)
//                                }
//
//                            }
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                            Log.d("Failure",error.message)
//                        }
//                    })
//
//                }


                val data=homeViewModel.getAllAlbums()


                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            userScrollEnabled = true,
                        ){

//                            items(items.toList()){
//                                HeadingText(value =it.selectionBehavior.toString(), textColor =Color.White)
//                            }

                            this.items(data){
                                AlbumCard(title =it.value.toString() , image = painterResource(
                                    id = R.drawable.logo
                                )) {}


                            }

                        }




            }
        }

    }
}


@Composable
fun getData(): SnapshotStateList<DataSnapshot> {
    val scope= rememberCoroutineScope()
    val db=Firebase.database.reference
    val value= remember{ mutableStateListOf<DataSnapshot>() }

    LaunchedEffect(db){

        db.child("Albums").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    it?.let {
                        value.add(it)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    return value

}


@Preview
@Composable
fun DefaultPreviewHomeScreen(){

//    HomeScreen()
}
