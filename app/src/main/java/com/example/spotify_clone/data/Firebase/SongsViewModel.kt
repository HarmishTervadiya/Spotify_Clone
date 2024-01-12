package com.example.spotify_clone.data.Firebase

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class SongsViewModel :ViewModel() {

    private val database= Firebase.database.reference.child("Songs").ref
    val value= mutableStateListOf<DataSnapshot>()
    val Songs= getAllSongs()

    private fun getAllSongs(): SnapshotStateList<DataSnapshot>
    {
        val value= mutableStateListOf<DataSnapshot>()
        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
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

        return value
    }


    fun getTopSongs(limit:Int=6): SnapshotStateList<DataSnapshot>
    {
        val value= mutableStateListOf<DataSnapshot>()

        val query=database.orderByChild("Likes").limitToFirst(limit)
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

        return value
    }


    fun searchSong(query:String): SnapshotStateList<DataSnapshot> {
        val value= mutableStateListOf<DataSnapshot>()
        val dbQuery=Firebase.database.reference.child("Songs").orderByChild("Song_Name").startAt(query).endAt(query.lowercase()+"\uf8ff")
        dbQuery.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
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

        return value
    }

    fun songLike(id: String) {
        database.child(id).child("Likes").setValue(ServerValue.increment(1))
    }

}