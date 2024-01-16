package com.example.spotify_clone.musicPlayer

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.spotify_clone.data.Firebase.ArtistViewModel
import com.example.spotify_clone.data.Firebase.SongsViewModel
import com.google.firebase.database.DataSnapshot
import kotlin.math.abs

sealed class PlayerEvent{
    data class SongPaused(val isPlaying: Boolean):PlayerEvent()
    data class PlaylistPlay(val currentSong:DataSnapshot,val list:SnapshotStateList<DataSnapshot>):PlayerEvent()
    data class SongResumed(val isPlaying: Boolean):PlayerEvent()
    data class PlaySong(val currentSong:DataSnapshot):PlayerEvent()
    data class SkipSong(val duration:Float):PlayerEvent()

    data class NextSong(val index:Int):PlayerEvent()

    data class PreviousSong(val index:Int):PlayerEvent()

}

data class SongTrack(
    var title:String="",
    var image:String="https://firebasestorage.googleapis.com/v0/b/spotify-clone-red-ronin.appspot.com/o/song_cover%2Fdownload%20(3).jpg?alt=media&token=d1d636e8-5c16-44fb-86a6-8d0a9bc6de0c",
    var isPlaying:Boolean=false,
    var artist:String="",
    var duration: Float=0f,
    var songUri:String="",
    var mediaItemId:String="",
    var hasNext:Boolean=false,
    var hasPrevious:Boolean=false

)

val currentSongTrack = mutableStateOf(SongTrack())
val artistViewModel=ArtistViewModel()
class Player(context: Context) : ViewModel() {
    val player = ExoPlayer.Builder(context).build()
    private val thread = Thread()
    private val songViewModel=SongsViewModel()
    val songList=songViewModel.Songs


    private fun playSong() {
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

                    Log.d("Call back successful","Call back Successful ${player.hasNextMediaItem()} ${player.hasPreviousMediaItem()}")
                    if (mediaItem?.mediaId!=currentSongTrack.value.mediaItemId) {
                        currentSongTrack.value = currentSongTrack.value.copy(
                            title = nextItem.child("Song_Name").value.toString(),
                            image = nextItem.child("cover_image").value.toString(),
                            songUri = nextItem.child("SongUri").value.toString(),
                            mediaItemId = nextItem.key.toString(),
                            artist =  nextItem.child("ArtistId").value.toString(),
                            hasPrevious = true,
                            hasNext =player.hasNextMediaItem()
                        )

                        randomIndex = (0 until abs(songList.lastIndex).inc()).random()
                        nextItem = songList[randomIndex]
                        nextItemToPlay = MediaItem.Builder().setMediaId(nextItem.key.toString())
                            .setUri(nextItem.child("SongUri").value.toString()).build()
                        player.addMediaItem(nextItemToPlay)

                    }
                }


            })

            if(!player.isPlaying) {
                player.prepare()
                player.play()
            }

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
                    mediaItemId = event.currentSong.key.toString(),
                    artist =  event.currentSong.child("ArtistId").value.toString(),
                    duration = event.currentSong.child("duration").value.toString().toFloat(),
                    hasPrevious = false,
                    hasNext =false
                )
            }
            playSong()

        }

        is PlayerEvent.PlaylistPlay->{
            if(currentSongTrack.value.mediaItemId != event.currentSong.key.toString()) {
                player.stop()
                player.clearMediaItems()

                currentSongTrack.value = currentSongTrack.value.copy(
                    isPlaying = true,
                    title = event.currentSong.child("Song_Name").value.toString(),
                    image = event.currentSong.child("cover_image").value.toString(),
                    songUri = event.currentSong.child("SongUri").value.toString(),
                    mediaItemId = event.currentSong.key.toString(),
                    artist =  event.currentSong.child("ArtistId").value.toString(),
                    hasPrevious = player.hasPreviousMediaItem(),
                    hasNext =player.hasNextMediaItem()
                )
            }
            playSongList(event.list)
        }

        is PlayerEvent.SkipSong->{
            player.seekTo(event.duration.toLong())
//            player.addListener(object : Player.Listener{
//                override fun onSeekForwardIncrementChanged(seekForwardIncrementMs: Long) {
//                    super.onSeekForwardIncrementChanged(seekForwardIncrementMs)
//                }
//            })
        }

        is PlayerEvent.NextSong -> {
            pause()
            player.seekToNextMediaItem()
            val nextSong= mutableStateOf(songList.first())
            songList.forEach { snapshot ->
                snapshot?.let {
                    if (it.key.toString()==player.currentMediaItem?.mediaId){
                        nextSong.value=it
                    }
                }
            }

            currentSongTrack.value= currentSongTrack.value.copy(
                isPlaying = true,
                title = nextSong.value.child("Song_Name").value.toString(),
                image = nextSong.value.child("cover_image").value.toString(),
                songUri = nextSong.value.child("SongUri").value.toString(),
                mediaItemId = nextSong.value.key.toString(),
                artist =  nextSong.value.child("ArtistId").value.toString(),
                hasPrevious = player.hasPreviousMediaItem(),
                hasNext =player.hasNextMediaItem()
            )
            resume()
        }
        is PlayerEvent.PreviousSong -> {
            pause()
            player.seekToPreviousMediaItem()
            val previousSong= mutableStateOf(songList.first())
            songList.forEach { snapshot ->
                snapshot?.let {
                    if (it.key.toString()==player.currentMediaItem?.mediaId){
                        previousSong.value=it
                    }
                }
            }

            currentSongTrack.value= currentSongTrack.value.copy(
                isPlaying = true,
                title = previousSong.value.child("Song_Name").value.toString(),
                image = previousSong.value.child("cover_image").value.toString(),
                songUri = previousSong.value.child("SongUri").value.toString(),
                mediaItemId = previousSong.value.key.toString(),
                artist =  previousSong.value.child("ArtistId").value.toString(),
                hasPrevious = player.hasPreviousMediaItem(),
                hasNext =player.hasNextMediaItem()
            )

            resume()
        }
    }

    private fun playSongList(list: SnapshotStateList<DataSnapshot>) {
        player.stop()
        player.clearMediaItems()

        thread.run {

            val currentItem = MediaItem.Builder().setMediaId(currentSongTrack.value.mediaItemId)
                .setUri(currentSongTrack.value.songUri).build()
            player.setMediaItem(currentItem)



            var randomIndex=1
            if (randomIndex > list.lastIndex){
                player.stop()
                return
            }
            var nextItem = list[randomIndex]
            var nextItemToPlay=MediaItem.Builder().setMediaId(nextItem.key.toString())
                .setUri(nextItem.child("SongUri").value.toString()).build()

            if (randomIndex > list.lastIndex){
                player.stop()
                return
            }else{
                player.addMediaItem(nextItemToPlay)
            }

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
                            mediaItemId = nextItem.key.toString(),
                            artist =  nextItem.child("ArtistId").value.toString(),
                            hasPrevious = true,
                            hasNext =player.hasNextMediaItem()
                        )

                        randomIndex++
                        if (randomIndex>list.lastIndex){
                            return
                        }else {
                            nextItem = list[randomIndex]
                            nextItemToPlay = MediaItem.Builder().setMediaId(nextItem.key.toString())
                                .setUri(nextItem.child("SongUri").value.toString()).build()
                            player.addMediaItem(nextItemToPlay)
                        }
                    }

                }
            })

            if(!player.isPlaying) {
                player.prepare()
                player.play()
            }
        }
    }


}