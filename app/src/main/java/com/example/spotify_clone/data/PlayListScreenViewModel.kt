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
    var playedCount:String="",
    var listOfSongId: MutableList<String> = mutableListOf("")
)

val idList = mutableStateListOf<String>()
class PlayListScreenViewModel : ViewModel() {

    fun getArtist(id: String): MutableState<ListRef> {
        val value = mutableStateOf(ListRef())
        val query = Firebase.database.reference.child("Artists").child(id)

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

    fun getAlbums(id: String): MutableState<ListRef> {
        val value = mutableStateOf(ListRef())
        val query = Firebase.database.reference.child("Albums").child(id)

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

    fun getUserPlaylistRef(id: String): MutableState<ListRef> {
        val value = mutableStateOf(ListRef())
        val query = Firebase.database.reference.child("Playlists").child(id)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.child("listOfSongId").children.forEach {
                    it?.let{
                       idList.add(it.value.toString())
//                        Log.d("List of songs",it.value.toString())
                    }
                }


                value.value = value.value.copy(
                    listName = snapshot.child("listName").value.toString(),
                    listId = snapshot.key.toString(),
                    listImage = snapshot.child("cover_image").value.toString(),
                    listOfSongId =idList
                )
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event if needed
            }
        })

        return value
    }

    fun getPlaylist(id: String): MutableState<ListRef> {
        val value = mutableStateOf(ListRef())
        val query = Firebase.database.reference.child("Artists").child(id)

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


    fun getUserPlayList(listOfSongId: List<String>): SnapshotStateList<DataSnapshot>
    {
        val value= mutableStateListOf<DataSnapshot>()
        allSongs.forEach {
            it.let {
                if (listOfSongId.contains(it.key.toString())){
                    value.add(it)
                }
            }
        }

        Log.d("Song List", "Hello${value.toList()}")
        return value
    }

}