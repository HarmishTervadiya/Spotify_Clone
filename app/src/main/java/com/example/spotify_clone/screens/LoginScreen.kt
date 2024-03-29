package com.example.spotify_clone.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spotify_clone.components.HeadingText
import com.example.spotify_clone.components.LoginInputTextBox
import com.example.spotify_clone.components.MessageDialog
import com.example.spotify_clone.components.SignUPButton
import com.example.spotify_clone.data.LoginRegisterViewModel
import com.example.spotify_clone.data.UIEvent
import com.example.spotify_clone.isServerMessage
import com.example.spotify_clone.navigation.Router
import com.example.spotify_clone.navigation.Screen
import com.example.spotify_clone.serverMessage
import com.example.spotify_clone.ui.theme.Background
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LoginScreen(){

    val loginViewModel=LoginRegisterViewModel()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Background
        ) {

            Box(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)){
                Column {
                    Spacer(modifier = Modifier.height(50.dp))
                    HeadingText(value = "Sign In", textColor = Color.White )

                    LoginInputTextBox(value = "Email", onChange = { loginViewModel.onEvent(UIEvent.EmailChanged(it)) }, state = loginViewModel.loginUIState.value.emailError)

                    LoginInputTextBox(value = "Password", onChange = {
                        loginViewModel.onEvent(UIEvent.PasswordChanged(it))
                    }, state = loginViewModel.loginUIState.value.passwordError)

//                    ClickText(value = "Forgot Password ?") {
//
//                    }
                    SignUPButton(value = "Login", bgColor = Color.Red, textColor = Color.White, icon = Icons.Filled.Clear, paddingValues = 38.dp, state = loginViewModel.loginValidation.value) {
                        loginViewModel.onEvent(UIEvent.LoginButtonClicked)

                    }
                }
            }
        }



        }


        val scope= rememberCoroutineScope()
        when{
            isServerMessage.value ->{
                MessageDialog(message = serverMessage.value){
                    isServerMessage.value=false
                }
                scope.launch {
                    delay(3000)
                    isServerMessage.value=false
                }
            }
        }

    BackHandler {
        Router.navigateTo(Screen.RegisterScreen)
    }
}



@Preview
@Composable
fun DefaultPreviewLoginScreen(){
    LoginScreen()
}