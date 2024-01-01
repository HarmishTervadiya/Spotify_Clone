package com.example.spotify_clone.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.spotify_clone.R

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




