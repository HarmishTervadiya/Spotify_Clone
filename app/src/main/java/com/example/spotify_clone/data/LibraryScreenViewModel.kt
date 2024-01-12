package com.example.spotify_clone.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.spotify_clone.data.Firebase.PlayListViewModel
import com.example.spotify_clone.data.Firebase.SongsViewModel
import com.example.spotify_clone.data.Firebase.userUid
import com.google.firebase.database.DataSnapshot

val playListViewModel=PlayListViewModel()
class LibraryScreenViewModel : ViewModel() {
    val newPlaylistName= mutableStateOf("")
    private val allPlaylists= mutableStateOf(playListViewModel.getPlaylist())
    val listOfPlaylists=mutableStateOf(playListViewModel.getPlaylist())
    fun createPlaylist(value:String){
        newPlaylistName.value=value
//        Log.d("Playlist Created",newPlaylistName.value)
        val res=PlayListViewModel().addPlaylist(newPlaylistName.value.trim())
        Log.d("Playlist Created",res.toString())
    }

    fun refreshList(){
        val list= SnapshotStateList<DataSnapshot>()
        allPlaylists.value.forEach {
            it.let {
                if (it.child("userId").value.toString().contains(userUid,ignoreCase = true)){
                    if(!list.contains(it)){
                        list.add(it)
                    }
        //                    Log.d("Playlists",it.value.toString())
                }
            }
        }
//        Log.d("Playlist Result",list.toList().toString())
        listOfPlaylists.value.clear()
        listOfPlaylists.value=list

    }


    fun deletePlaylist(playlistId:String): Boolean {
        return playListViewModel.removePlaylist(playlistId)
    }

    fun addToPlaylist(listId: String, songId: MutableList<String>) {
        val list= mutableListOf<String>()
        refreshList()

        listOfPlaylists.value.forEach {
            it.child(listId).child("listOfSongsId").children.forEach{ song ->
                list.add(song.value.toString())
            }
        }

        if (!list.contains(songId.first())){
            playListViewModel.pushToPlaylist(listId,songId)
        }

    }

    fun addLike(id: String) {
        SongsViewModel().songLike(id)
    }
}