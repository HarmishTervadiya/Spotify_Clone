package com.example.spotify_clone.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.spotify_clone.isServerMessage
import com.example.spotify_clone.navigation.Router
import com.example.spotify_clone.navigation.Screen
import com.example.spotify_clone.serverMessage
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
            isServerMessage.value=true
            serverMessage.value="Sign In Successful"
        }
            .addOnFailureListener {
                errorMessage.value=true
                progress.value=false
                isServerMessage.value=true
                serverMessage.value=it.message.toString()
            }
    }

    private fun signUp(name: String, email: String, password: String) {
        if (name!="" && email !="" && password !="" ) {
            try {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    run {
                        if (task.isSuccessful) {

                            val profileUpdates = userProfileChangeRequest {
                                displayName = name

                            }
                            auth.currentUser!!.updateProfile(profileUpdates).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    progress.value = false
                                    isServerMessage.value=true
                                    serverMessage.value="Account Created Successfully"
                                    Router.navigateTo(Screen.HomeScreen)

                                } else {
                                    errorMessage.value = false
                                }
                            }.addOnFailureListener {
                                errorMessage.value = false

                            }


                        } else {
                            errorMessage.value = true
                        }
                    }


                }.addOnFailureListener {
                    errorMessage.value = true
                    isServerMessage.value = true
                    serverMessage.value = it.message.toString()
                }

            } catch (e: Exception) {
                progress.value = true
                error.value = errorMessage(e.message.toString())

            }
        }else{
            isServerMessage.value=true
            serverMessage.value="Enter Details and Try again"
        }
    }

    private fun loginValidationPassed(){
        loginValidation.value= (loginUIState.value.emailError==false) && (loginUIState.value.passwordError==false)
    }

    private fun registrationValidationPassed(){
        registerValidation.value=( (!registrationUIState.value.emailError) && (!registrationUIState.value.passwordError) && (!registrationUIState.value.nameError) )
    }
    private fun logIn(email: String, password: String) {
        if (email.isNullOrBlank() || password.isNullOrBlank()){
            isServerMessage.value=true
            serverMessage.value="Enter Email or Password again"
        }else {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        progress.value = false
                        isServerMessage.value = true
                        serverMessage.value = "Login Successful"
                        Router.navigateTo(Screen.HomeScreen)
                    } else {
                        progress.value = false
                        errorMessage.value = true
                        Log.d("Complete", "Complete $it")
                        isServerMessage.value = true
                        serverMessage.value = "Invalid Email or Password"
                    }
                }
                .addOnFailureListener {
                    progress.value = false
                    isServerMessage.value = true
                    serverMessage.value = "Invalid Email or Password"
                    Log.d("Failure", "Failure $it")
                    errorMessage.value = true
                }
        }
    }

    fun logout(){
        auth.signOut()
        isServerMessage.value=true
        serverMessage.value="Logout Successful"
        Router.navigateTo(Screen.RegisterScreen)
    }

    fun errorMessage(message:String):String{
        errorMessage.value=true
        return  message
    }

}