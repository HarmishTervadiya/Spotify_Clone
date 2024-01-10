package com.example.spotify_clone.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.spotify_clone.data.Firebase.PlayListViewModel
import com.example.spotify_clone.data.Firebase.userUid
import com.google.firebase.database.DataSnapshot

class LibraryScreenViewModel : ViewModel() {
    private val playListViewModel=PlayListViewModel()
    val newPlaylistName= mutableStateOf("")
    private val allPlaylists= mutableStateOf(playListViewModel.getPlaylist())
    val listOfPlaylists=mutableStateOf(playListViewModel.getPlaylist())

    fun createPlaylist(value:String){
        newPlaylistName.value=value
//        Log.d("Playlist Created",newPlaylistName.value)
        val res=PlayListViewModel().addPlaylist(newPlaylistName.value)
        Log.d("Playlist Created",res.toString())

    }

    fun refreshList(){
        val list= SnapshotStateList<DataSnapshot>()
        allPlaylists.value.forEach {
            it?.let {
                if (it.child("userId").value.toString().contains(userUid,ignoreCase = true)){
                    list.add(it)
//                    Log.d("Playlists",it.value.toString())
                }

            }
        }
//        Log.d("Playlist Result",list.toList().toString())
        listOfPlaylists.value.clear()
        listOfPlaylists.value=list

    }
}