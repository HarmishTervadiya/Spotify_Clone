package com.example.spotify_clone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotify_clone.components.BottomNav
import com.example.spotify_clone.navigation.Router
import com.example.spotify_clone.navigation.Screen
import com.example.spotify_clone.screens.ContainerScreen
import com.example.spotify_clone.screens.LoginScreen
import com.example.spotify_clone.screens.RegisterScreen
import com.example.spotify_clone.screens.adminScreens.demoScreen
import com.example.spotify_clone.ui.theme.Background
import com.example.spotify_clone.ui.theme.Spotify_CloneTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val auth=FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(auth.currentUser?.email.toString() == "harmishtervadiya@gmail.com"){
            Router.navigateTo(Screen.AdminScreen)
        }
        else if (auth.currentUser!=null){
            Router.navigateTo(Screen.HomeScreen)
        }


        val splash=SplashScreenModel()
//        installSplashScreen().apply {
//            this.setKeepOnScreenCondition{splash.isLoading.value}
//        }

        setContent {
            Spotify_CloneTheme {
                // A surface container using the 'background' color from the theme

                Crossfade(targetState = Router.currentScreen.value, animationSpec = tween(easing = LinearEasing))
                { screen ->
                    when(screen){
                        is Screen.RegisterScreen ->{
                            RegisterScreen(this)
                        }

                        is Screen.LoginScreen ->{
                            LoginScreen()
                        }

                        is Screen.AdminScreen ->{
                            demoScreen()
                        }

                        else ->{
                            ContainerScreen(this)
                        }
                    }

                    
                }
            }
        }
    }

}




@Composable
fun MainScreen() {
    Scaffold(
        containerColor = Background,
        bottomBar = { BottomNav() },
        contentColor = Background
        // Place BottomNav within the Scaffold's bottomBar
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(Background),
            // Apply Scaffold's inner padding
        ) {

            // Screens to be displayed
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Background
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text(text = "Hello Everyone")
                }
            }




        }
    }
}
@Preview
@Composable
fun DefaultPreviewMainScreen(){
    MainScreen()
}

class SplashScreenModel : ViewModel(){
    private val _isLoading= MutableStateFlow(true)
    val isLoading=_isLoading.asStateFlow()
    init {
        viewModelScope.launch {
            delay(2000)
            _isLoading.value=false
        }
    }
}