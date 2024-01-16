package com.example.spotify_clone.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spotify_clone.R
import com.example.spotify_clone.components.HeadingText
import com.example.spotify_clone.components.MessageDialog
import com.example.spotify_clone.components.RegisterBottomSheet
import com.example.spotify_clone.components.SignUPButton
import com.example.spotify_clone.data.LoginRegisterViewModel
import com.example.spotify_clone.isServerMessage
import com.example.spotify_clone.navigation.Router
import com.example.spotify_clone.navigation.Screen
import com.example.spotify_clone.serverMessage
import com.example.spotify_clone.ui.theme.Background
import com.example.spotify_clone.ui.theme.Primary
import com.example.spotify_clone.ui.theme.Secondary
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(context: Context){

    val registerViewModel= LoginRegisterViewModel()

    val launcher= rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()){

        val account=GoogleSignIn.getSignedInAccountFromIntent(it.data)

        try {
            val result=account.result
            val credentials=GoogleAuthProvider.getCredential(result.idToken,null)

            registerViewModel.googleSignIn(credentials)
        }catch (e:Exception){

            print(e.message.toString())
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background),
        contentAlignment = Alignment.Center
    ){

        Surface(modifier = Modifier.fillMaxSize(),
            color = Background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f))
                Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                    , alignment = Alignment.Center)

                HeadingText(value = "Millions of Free Songs on Red Ronin", textColor = Color.White)

                val scope= rememberCoroutineScope()
                val sheetState= rememberStandardBottomSheetState(
                    initialValue = SheetValue.Hidden,
                    skipHiddenState = false
                )

                SignUPButton(value = "Sign Up for free", bgColor = Secondary, textColor = Color.White, icon = Icons.Filled.AccountCircle, paddingValues = 15.dp
                ){
                    scope.launch {
                        if(sheetState.isVisible){
                            sheetState.hide()
                        }else{
                            sheetState.show()
                            sheetState.expand()

                        }

                    }
                }

                SignUPButton(value = "Continue with Google", bgColor = Color.White, textColor =Color.Black , icon = ImageVector.vectorResource(
                    id = R.drawable.icons8_google
                ), paddingValues =15.dp ) {



                    val signInRequest= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestProfile()
                        .requestIdToken("470684949469-keb3mp2ljdum9p3nsek65p07683m3509.apps.googleusercontent.com")
                        .build()

                    val googleSignInClient=GoogleSignIn.getClient(context,signInRequest)

                        launcher.launch(googleSignInClient.signInIntent)

                }


                SignUPButton(value = "Login with your account", bgColor = Primary, textColor = Color.White, icon = Icons.Filled.Clear, paddingValues = 15.dp
                ){
                    Router.navigateTo(Screen.LoginScreen)
                }

                RegisterBottomSheet(sheetState =sheetState)

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

    }


}



@Preview
@Composable
fun DefaultPreviewRegisterScreen(){
//    RegisterScreen()
}