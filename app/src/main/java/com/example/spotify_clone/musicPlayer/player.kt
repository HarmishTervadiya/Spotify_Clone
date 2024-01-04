package com.example.spotify_clone.musicPlayer

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.spotify_clone.data.Firebase.SongsViewModel
import com.google.firebase.database.DataSnapshot
import kotlin.math.abs

sealed class PlayerEvent{
    data class SongPaused(val isPlaying: Boolean):PlayerEvent()
    data class PlaylistPlay(val isPlaying: Boolean):PlayerEvent()
    data class SongResumed(val isPlaying: Boolean):PlayerEvent()
    data class PlaySong(val currentSong:DataSnapshot):PlayerEvent()

}

data class SongTrack(
    var title:String="",
    var image:String="https://firebasestorage.googleapis.com/v0/b/spotify-clone-red-ronin.appspot.com/o/song_cover%2Fdownload%20(3).jpg?alt=media&token=d1d636e8-5c16-44fb-86a6-8d0a9bc6de0c",
    var isPlaying:Boolean=false,
    var artist:String="",
    var duration: String="",
    var songUri:String="",
    var mediaItemId:String=""

)
class Player(context: Context) : ViewModel() {

    private val player = ExoPlayer.Builder(context).build()
    val currentSongTrack = mutableStateOf(SongTrack())
    private val thread = Thread()
    private val songViewModel=SongsViewModel(context)


    private fun playSong() {

        val songList=songViewModel.Songs
        val list= mutableListOf<String>()
        songList.forEach {
            it.let{
                list.add(it.child("Song_Name").value.toString())
            }
        }

        player.clearMediaItems()
        thread.run {

            val currentItem=MediaItem.Builder().setMediaId(currentSongTrack.value.mediaItemId)
                .setUri(currentSongTrack.value.songUri).build()

            player.addMediaItem(currentItem)
            var randomIndex=(0 until abs(songList.lastIndex).inc()).random()
            var nextItem = songList[randomIndex]

            var nextItemToPlay=MediaItem.Builder().setMediaId(nextItem.key.toString())
                            .setUri(nextItem.child("SongUri").value.toString()).build()

            player.addMediaItem(nextItemToPlay)

            player.addListener(object : Player.Listener{
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                        Log.d("Call back successful","Call back Successful")
                    super.onIsPlayingChanged(isPlaying)
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {

                    Log.d("Call back successful","Call back Successful ${mediaItem?.mediaId}")
                    if (mediaItem?.mediaId!=currentSongTrack.value.mediaItemId) {
                        currentSongTrack.value = currentSongTrack.value.copy(
                            title = nextItem.child("Song_Name").value.toString(),
                            image = nextItem.child("cover_image").value.toString(),
                            songUri = nextItem.child("SongUri").value.toString(),
                            mediaItemId = nextItem.key.toString()
                        )

                        randomIndex = (0 until abs(songList.lastIndex).inc()).random()
                        nextItem = songList[randomIndex]
                        nextItemToPlay = MediaItem.Builder().setMediaId(nextItem.key.toString())
                            .setUri(nextItem.child("SongUri").value.toString()).build()
                        player.addMediaItem(nextItemToPlay)

                    }

                }
            })

                player.prepare()
                player.play()


        }

    }

     private fun pause(){
        player.pause()
    }

     private fun resume(){
        player.play()
    }



    fun onEvent(event: PlayerEvent) = when(event){
        is  PlayerEvent.SongPaused-> {
            pause()
            currentSongTrack.value = currentSongTrack.value.copy(
                isPlaying = event.isPlaying
            )
        }

        is  PlayerEvent.SongResumed-> {
            resume()
            currentSongTrack.value = currentSongTrack.value.copy(
                isPlaying = event.isPlaying
            )
        }

        is PlayerEvent.PlaySong->{
            if (currentSongTrack.value.mediaItemId != event.currentSong.key.toString()) {
                player.stop()
                player.clearMediaItems()

                currentSongTrack.value = currentSongTrack.value.copy(
                    isPlaying = true,
                    title = event.currentSong.child("Song_Name").value.toString(),
                    image = event.currentSong.child("cover_image").value.toString(),
                    songUri = event.currentSong.child("SongUri").value.toString(),
                    mediaItemId = event.currentSong.key.toString()
                )
            }
            playSong()

        }

        else ->{
            player.stop()
        }



    }




}