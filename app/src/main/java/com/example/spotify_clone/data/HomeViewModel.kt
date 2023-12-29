package com.example.spotify_clone.data

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class HomeViewModel(context:Context) : ViewModel() {

    private val db = Firebase.database.reference

    val dataError = mutableStateOf(false)

    fun getAllAlbums(): SnapshotStateList<DataSnapshot> {

        val db=Firebase.database.reference
        val value= mutableStateListOf<DataSnapshot>()
//        val scope=CoroutineScope(context = this.viewModelScope.coroutineContext)

        db.child("Albums").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    it?.let {
                        value.add(it)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        return value
        }

    }
