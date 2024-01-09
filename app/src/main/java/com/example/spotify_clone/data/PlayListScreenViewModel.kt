package com.example.spotify_clone.data

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

data class ListRef(
    var listId:String="Something",
    var listName:String="",
    var listImage:String="",
    var playedCount:String=""
)

class PlayListScreenViewModel : ViewModel() {

    fun getArtist(Id: String): MutableState<ListRef> {
        val value = mutableStateOf(ListRef())
        val query = Firebase.database.reference.child("Artists").child(Id)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                value.value = value.value.copy(
                    listName = snapshot.child("Artist_Name").value.toString(),
                    listId = snapshot.key.toString(),
                    playedCount = snapshot.child("played").value.toString(),
                    listImage = snapshot.child("profile").value.toString()
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event if needed
            }
        })

        return value
    }

    fun getAlbums(Id: String): MutableState<ListRef> {
        val value = mutableStateOf(ListRef())
        val query = Firebase.database.reference.child("Albums").child(Id)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                value.value = value.value.copy(
                    listName = snapshot.child("title").value.toString(),
                    listId = snapshot.key.toString(),
                    playedCount = snapshot.child("played").value.toString(),
                    listImage = snapshot.child("image").value.toString()
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event if needed
            }
        })

        return value
    }

    fun getPlaylist(Id: String): MutableState<ListRef> {
        val value = mutableStateOf(ListRef())
        val query = Firebase.database.reference.child("Artists").child(Id)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                value.value = value.value.copy(
                    listName = snapshot.child("Artist_Name").value.toString(),
                    listId = snapshot.key.toString(),
                    playedCount = snapshot.child("played").value.toString(),
                    listImage = snapshot.child("profile").value.toString()
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event if needed
            }
        })

        return value
    }

    fun getSongList(ref:String,id:String): SnapshotStateList<DataSnapshot>
    {
        val arg= when (ref) {
            "Artists" -> {
                "ArtistId"
            }
            "Albums" -> {
                "AlbumId"
            }
            else -> (
                    "Genre"
                    )
        }
        val value= mutableStateListOf<DataSnapshot>()
        val query=Firebase.database.reference.child("Songs").orderByChild(arg).equalTo(id)

        query.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                value.clear()
                snapshot.children.forEach {
                    it?.let{
                        value.add(it)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Songs Reading Failed",error.message)
            }
        })

        Log.d("Song List",value.toList().toString())
        return value
    }

}