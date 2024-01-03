package com.example.spotify_clone.data.Firebase

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class SongsViewModel(context:Context) :ViewModel() {

    val database= Firebase.database.reference.child("Songs").ref
    val value= mutableStateListOf<DataSnapshot>()
    val Songs= getAllSongs()

    fun getAllSongs(): SnapshotStateList<DataSnapshot>
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

    fun getNextSong() {

        val value= mutableStateListOf<DataSnapshot>()

        val query=database.orderByChild("Likes")
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
        var temp= mutableStateOf("")
        value.forEach {
            temp.value=it.child("Song_Name").value.toString()
        }
        Log.d("data",
           "Hello ${temp.value}"
        )
//        var randomIndex=(0 until abs(value.lastIndex).inc()).random()
//        Log.d("Sizze",randomIndex.toString())
//        Log.d("Random Number",value[randomIndex].value.toString())

    }



}