package com.example.spotify_clone.data.Firebase

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

val listOfCurrentArtist= mutableStateOf("")
class ArtistViewModel : ViewModel() {
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

    fun getListOfArtist(value:String) {
        listOfCurrentArtist.value=""
        val list=if (value.contains(",")){
            value.split(",")
        }else{
            listOf(value)
        }
        val artists= mutableStateOf("")

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    it?.let {
                        if (list.contains(it.key.toString())){
                            if (artists.value == ""){
                                artists.value=it.child("Artist_Name").value.toString()
                            }else{
                                artists.value=artists.value+", ${it.child("Artist_Name").value.toString()}"
                            }

                            if (listOfCurrentArtist.value == ""){
                                listOfCurrentArtist.value=it.child("Artist_Name").value.toString()
                            }else{
                                listOfCurrentArtist.value= listOfCurrentArtist.value+", ${it.child("Artist_Name").value.toString()}"
                            }
                        }
                        Log.d("List Values",list.toString())
                        Log.d("Artist Values",artists.value)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }


}