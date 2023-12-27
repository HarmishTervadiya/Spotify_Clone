package com.example.spotify_clone.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.spotify_clone.navigation.Router
import com.example.spotify_clone.navigation.Screen
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest

sealed class UIEvent{
    data class NameChanged(val fullName:String):UIEvent()
    data class EmailChanged(val email:String):UIEvent()
    data class PasswordChanged(val password:String):UIEvent()

    data object RegisterButtonClicked:UIEvent()
    data object LoginButtonClicked:UIEvent()
//    data object  GoogleSignIn:UIEvent()
}


data class RegistrationUIState(
    var name:String="",
    var email:String="",
    var password: String="",

    var nameError:Boolean=false,
    var emailError:Boolean=false,
    var passwordError:Boolean=false,

    )

data class LoginUIState(

    var email:String="",
    var password: String="",

    var emailError:Boolean=false,
    var passwordError:Boolean=false,

    )

    val auth= Firebase.auth

//
//class GoogleAuth(private val context: Context,
//    private val oneTapClient:SignInClient){
//
//    private val auth =FirebaseAuth.getInstance()
//
//    suspend fun signIn():IntentSender? {
//     val result=try {
//         oneTapClient.beginSignIn(
//             buildSignInRequest()
//         ).await()
//     }catch (e:Exception){
//         Log.d("Google Error",e.message.toString())
//     }
//
//        return result.pe
//    }


//    private fun buildSignInRequest():BeginSignInRequest{
//        return  BeginSignInRequest.builder()
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    .setServerClientId(context.getString(R.string.webclient_id))
//                    .setFilterByAuthorizedAccounts(false)
//
//                    .build()
//            )
//            .setAutoSelectEnabled(true)
//            .build()
//    }
//
//}

class LoginRegisterViewModel : ViewModel() {

    val error= mutableStateOf("")


    val loginUIState= mutableStateOf(LoginUIState())
    val registrationUIState= mutableStateOf(RegistrationUIState())
    val loginValidation= mutableStateOf(false)
    val registerValidation= mutableStateOf(false)

    var progress= mutableStateOf(false)
    var errorMessage= mutableStateOf(false)


    fun onEvent(event:UIEvent){
        loginValidationPassed()
        registrationValidationPassed()
        when(event){
            is UIEvent.EmailChanged ->{
                loginUIState.value=loginUIState.value.copy(
                    email = event.email,
                    emailError = !event.email.contains("@gmail.com")
                )

                registrationUIState.value=registrationUIState.value.copy(
                    email = event.email,
                    emailError = !event.email.contains("@gmail.com")
                )
            }


            is UIEvent.PasswordChanged ->{
                loginUIState.value=loginUIState.value.copy(
                    password = event.password,
                    passwordError =  event.password.length<5
                )

                registrationUIState.value=registrationUIState.value.copy(
                    password = event.password,
                    passwordError =  event.password.length<=6
                )

            }

            is UIEvent.NameChanged ->{

                registrationUIState.value=registrationUIState.value.copy(
                    name = event.fullName,
                    nameError =  event.fullName.length<=4
                )

            }

            is UIEvent.LoginButtonClicked ->{
                progress.value=true
                Log.d("Progress",progress.value.toString())
                logIn(loginUIState.value.email,loginUIState.value.password)
            }


            is UIEvent.RegisterButtonClicked ->{
                progress.value=true
                signUp(registrationUIState.value.name,registrationUIState.value.email,registrationUIState.value.password)
            }



        }
    }

    fun googleSignIn(credential: AuthCredential) {

        progress.value=true
        auth.signInWithCredential(credential).addOnSuccessListener {
            Router.navigateTo(Screen.HomeScreen)
            progress.value=false
        }
            .addOnFailureListener {
                errorMessage.value=true
                progress.value=false
            }
    }

    private fun signUp(name: String, email: String, password: String) {

        try {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                run {
                    if (task.isSuccessful) {

                        val profileUpdates= userProfileChangeRequest {
                            displayName=name

                        }
                        auth.currentUser!!.updateProfile(profileUpdates).addOnCompleteListener {
                            if (it.isSuccessful){
                                progress.value=false
                                Router.navigateTo(Screen.HomeScreen)
                            }else{
                                errorMessage.value=false
                            }
                        }.addOnFailureListener {
                            errorMessage.value=false
                        }


                    }
                    else{
                        errorMessage.value=true
                    }
                }


            }.addOnFailureListener {
                errorMessage.value=true
            }

        }catch (e:Exception){
            progress.value=true
            error.value=errorMessage(e.message.toString())

        }
            }

    private fun loginValidationPassed(){
        loginValidation.value= (loginUIState.value.emailError==false) && (loginUIState.value.passwordError==false)
    }

    private fun registrationValidationPassed(){
        registerValidation.value=( (!registrationUIState.value.emailError) && (!registrationUIState.value.passwordError) && (!registrationUIState.value.nameError) )
    }
    private fun logIn(email: String, password: String) {

        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    progress.value=false
                    Router.navigateTo(Screen.HomeScreen)
                }else{
                    progress.value=false
                    errorMessage.value=true
                    Log.d("Complete","Complete $it")
                }
            }
            .addOnFailureListener {
                progress.value=false
                Log.d("Failure","Failure $it")
                errorMessage.value=true
            }
    }

    fun logout(){
        auth.signOut()
        Router.navigateTo(Screen.RegisterScreen)
    }

    fun errorMessage(message:String):String{
        errorMessage.value=true
        return  message
    }

}