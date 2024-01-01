package com.example.spotify_clone.data.Firebase

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ArtistViewModel(context: Context) : ViewModel() {
    val database=Firebase.database.reference.child("Artists").ref
    val value= mutableStateListOf<DataSnapshot>()

    fun getAllArtists(): SnapshotStateList<DataSnapshot>
    {
        val value= mutableStateListOf<DataSnapshot>()
        database.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    it?.let{
                        value.add(it)
                }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Artists Reading Failed",error.message)
            }

        })

        return value
    }



}