package com.example.spotify_clone.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

sealed class Screen {

    data object RegisterScreen:Screen()
    data object LoginScreen:Screen()
    data object HomeScreen:Screen()
    data object SearchScreen:Screen()
    data object LibraryScreen:Screen()

    data object PlayListScreen:Screen()
    data object AdminScreen:Screen()


}

object Router{
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.HomeScreen)
    var listId= mutableStateOf(listOf<String>())

    fun navigateTo(destination:Screen,dataSnapshot:List<String> = listOf("",""),onClick:()->Unit={}){
        onClick.invoke()
        currentScreen.value=destination
        listId.value=dataSnapshot
    }


}