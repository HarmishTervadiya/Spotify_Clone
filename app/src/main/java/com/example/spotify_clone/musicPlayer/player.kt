package com.example.spotify_clone.musicPlayer

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerControlView

class Player(context: Context){

    private val player=ExoPlayer.Builder(context).build()
    @SuppressLint("UnsafeOptInUsageError")
    val view=PlayerControlView(context)

    @SuppressLint("UnsafeOptInUsageError")
    fun playSong(value: String){

        Log.d("Song",value)
        val mediaItem= MediaItem.fromUri(value)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }


    fun onPause(){
        player.pause()
    }

    fun onResume(){
        player.play()
    }




}