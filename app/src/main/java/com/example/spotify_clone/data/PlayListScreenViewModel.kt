package com.example.spotify_clone.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.spotify_clone.navigation.Router
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.database

data class ListRef(
    var listId:String="Something",
    var listName:String="",
    var listImage:String="",
    var playedCount:String=""
)

class PlayListScreenViewModel : ViewModel() {
    private val listRef = mutableStateOf(ListRef())

    private val query = getQuery()

    private fun getQuery(): Task<DataSnapshot> {
        return if (Router.listId.value[1] == "Artists") {
            Firebase.database.reference.child("Artists").child(Router.listId.value[0]).get()
        } else if (Router.listId.value[1] == "Albums") {
            Firebase.database.reference.child("Artists").child(Router.listId.value[0]).get()
        } else if (Router.listId.value[1] == "Songs") {
            Firebase.database.reference.child("Songs").child(Router.listId.value[0]).get()
        } else {
            Firebase.database.reference.child("Artists").child(Router.listId.value[0]).get()
        }


    }

    fun getData(): ListRef {
        query.addOnSuccessListener { data ->
            listRef.value = listRef.value.copy(
                listId = data.key.toString(),
                listName = data.child("Artist_Name").value.toString(),
                listImage = data.child("profile").value.toString(),
                playedCount = data.child("played").value.toString()
            )

//        listRef.value=listRef.value.copy(
//            listId=data.key.toString(),
//            listName=data.child("Artist_Name").value.toString(),
//            listImage=data.child("profile").value.toString(),
//            playedCount=data.child("played").value.toString()
//        )

        }
        Log.d("Data Updated", listRef.value.toString())
        return listRef.value
    }
//
//    fun getData(): ListRef {
//        Log.d("Data Updated",listRef.value.toString())
//        return listRef.value
//    }


}