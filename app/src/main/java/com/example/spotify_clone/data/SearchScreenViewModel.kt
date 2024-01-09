package com.example.spotify_clone.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.spotify_clone.data.Firebase.SongsViewModel
import com.google.firebase.database.DataSnapshot

val allSongs=SongsViewModel().Songs
class SearchScreenViewModel() : ViewModel() {

    private val searchText = mutableStateOf("")
    val searchList= mutableStateOf(SongsViewModel().getTopSongs(10))
    fun updateInput(value: String){
        searchText.value=value
    }

    fun getSearchResult(){
        val list=SnapshotStateList<DataSnapshot>()
        allSongs.forEach {
            it?.let {
                if (it.child("Song_Name").value.toString().contains(searchText.value,ignoreCase = true)){
                    list.add(it)
                    Log.d("Search",it.value.toString())
                }

                if (it.child("Genre").value.toString().contains(searchText.value,ignoreCase = true)){
                    list.add(it)
                }
            }
        }
        Log.d("Search Result",list.toList().toString())
        searchList.value.clear()
        searchList.value=list
        if (searchText.value==""){
//            searchList.value.removeAll(searchList.value)
            searchList.value=SongsViewModel().getTopSongs(10)
        }
    }


}

