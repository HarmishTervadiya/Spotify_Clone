package com.example.spotify_clone.data.Firebase

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

val userUid=Firebase.auth.currentUser?.uid.toString()
class PlayListViewModel {
    private val db= Firebase.database.reference.child("Playlists").ref
    val id= mutableIntStateOf(0)



    fun addPlaylist(name:String): Boolean {
        val res= mutableStateOf(false)
        var index= mutableStateOf("Pl${(1 until 99999).random()}")
        val idList= mutableStateListOf<String>()
        db.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    it?.let {
                        idList.add(it.key.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        when(idList.contains(index.value)){
            true->{
                index.value="Pl${(1 until 99999).random()}"
            }

            false->{
                db.child(index.value).setValue(PlayList(
                    userId = userUid,
                    listName = name,
                    listOfSongId = listOf<String>("SO00001"),
                    cover_image = "https://firebasestorage.googleapis.com/v0/b/spotify-clone-red-ronin.appspot.com/o/images.png?alt=media&token=61e690f2-922f-4e53-8b2a-f1abdda312c9"
                ))
                    .addOnSuccessListener {
                        Log.d("Success","Success")
                        res.value=true
                    }
                    .addOnFailureListener {
                        Log.d("Failed",it.message.toString())
                    }
            }
        }

        return res.value

    }

    fun getPlaylist(): SnapshotStateList<DataSnapshot> {
        val list= mutableStateListOf<DataSnapshot>()
        db.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    it?.let {
                        list.add(it)
                    }
                }
                Log.d("Failed",list.toList().toString())

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        Log.d("Failed",list.toList().toString())
        return list
    }
}

data class PlayList(
    val userId:String,
    val listOfSongId:List<String>,
    val listName:String,
    val cover_image:String
)