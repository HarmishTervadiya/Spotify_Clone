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
    var MediaItemId:String=""

)
class Player(context: Context) : ViewModel() {

    private val player = ExoPlayer.Builder(context).build()
    val currentSongTrack = mutableStateOf(SongTrack())
    private val thread = Thread()
    val songViewModel=SongsViewModel(context)


    private fun playSong(value: String) {


        Log.d("Song", value)
        val songList=songViewModel.Songs
        var list= mutableListOf<String>()
        songList.forEach {
            it?.let{
            list.add(it.child("Song_Name").value.toString())
            }
        }

        player.clearMediaItems()
        thread.run {
            Log.d("MediaItemID",currentSongTrack.value.MediaItemId)
            var currentItem=MediaItem.Builder().setMediaId(currentSongTrack.value.MediaItemId)
                .setUri(currentSongTrack.value.songUri).build()

            player.addMediaItem(currentItem)
            var nextItemToPlay=currentItem
//                if (!player.isPlaying) {
//                    if (!player.hasNextMediaItem()) {
                       var randomIndex=(0 until abs(songList.lastIndex).inc()).random()
                        val nextItem = songList[randomIndex]
//                        Log.d("Next Item",nextItem.toString())
//                        if(!player.isCurrentMediaItemLive){
//                            currentSongTrack.value = currentSongTrack.value.copy(
//                                title = nextItem.child("Song_Name").value.toString(),
//                                image = nextItem.child("cover_image").value.toString(),
//                                )
//                        }
                        nextItemToPlay=MediaItem.Builder().setMediaId(nextItem.value.toString())
                            .setUri(nextItem.child("SongUri").value.toString()).build()

//                        player.addMediaItem(MediaItem.fromUri(nextItem.child("SongUri").value.toString()))
//                    }
//                }


            player.addListener(object : Player.Listener{
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    if (mediaItem?.mediaId==nextItemToPlay.mediaId){
                        currentSongTrack.value=currentSongTrack.value.copy(
                            title = nextItem.child("Song_Name").value.toString(),
                            image = nextItem.child("cover_image").value.toString(),
                            songUri = nextItem.child("cover_image").value.toString(),
                        )
                    }
                    player.addMediaItem(nextItemToPlay)
                    super.onMediaItemTransition(mediaItem, reason)
                }
            })

                player.prepare()
                player.play()


//            if (!player.isPlaying) {
//                if (!player.hasNextMediaItem()) {
//                    var randomIndex=(0 until abs(songList.lastIndex).inc()).random()
//                    val nextItem = songList[randomIndex]
////                        Log.d("Next Item",nextItem.toString())
//                    currentSongTrack.value = currentSongTrack.value.copy(
//                        title = nextItem.child("Song_Name").value.toString(),
//                        image = nextItem.child("cover_image").value.toString(),
//                        MediaItemId = nextItem.value.toString()
//                        )
//                    player.addMediaItem(MediaItem.fromUri(nextItem.child("SongUri").value.toString()))
//                }
//            }




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
            if (!currentSongTrack.value.title.equals(event.currentSong.child("Song_Name").value.toString()))
            {
                player.stop()
                player.removeMediaItem(0)
            }

            currentSongTrack.value=currentSongTrack.value.copy(
                isPlaying = true,
                title = event.currentSong.child("Song_Name").value.toString(),
                image = event.currentSong.child("cover_image").value.toString(),
                songUri = event.currentSong.child("SongUri").value.toString()
            )
            Log.d("check",currentSongTrack.value.toString())
            playSong(event.currentSong.child("SongUri").value.toString())

        }

        else ->{
            player.stop()
        }



    }




}