package com.example.spotify_clone.screens

import android.os.Build
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.spotify_clone.ui.theme.Background
import java.time.Clock
import java.time.LocalTime
import java.time.ZoneId

@Composable
fun HomeScreen(){

    Surface(modifier = Modifier.fillMaxSize(),
        color = Background) {

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)){
            Column {
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
                    .height(60.dp)) {
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
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Favourite",
                        tint = Color.Red,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(15.dp)
                            .align(Alignment.Bottom)
                    )

                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Favourite",
                        tint = Color.White,
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(12.dp)
                            .align(Alignment.Bottom)
                    )

                }

                val item=listOf("Arijit Singh dbdfdfgdfgd","Hariharan","sdvss","svdsdsvsdv","svsdsds")
                val item2=listOf(R.drawable.logo,R.drawable.icons8_google)

                HeadingText(value = "Top Picks", textColor = Color.White)
                Spacer(modifier = Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp), userScrollEnabled = true,){
                  items(item){ listValue, ->
                      AlbumCard(listValue, painterResource(id = R.drawable.logo),{})
                  }
                }
            }
        }

    }
}



@Preview
@Composable
fun DefaultPreviewHomeScreen(){

    HomeScreen()
}
