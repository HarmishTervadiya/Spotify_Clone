package com.example.spotify_clone.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

sealed class UIEvent{
    data class NameChanged(val fullName:String):UIEvent()
    data class EmailChanged(val email:String):UIEvent()
    data class PasswordChanged(val password:String):UIEvent()

    data object RegisterButtonClicked:UIEvent()
    data object LoginButtonClicked:UIEvent()
    data object  GoogleSignIn:UIEvent()
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

class LoginRegisterViewModel : ViewModel() {

    val loginUIState= mutableStateOf(LoginUIState())
    val registrationUIState= mutableStateOf(RegistrationUIState())
    val loginValidation= mutableStateOf(false)
    val registerValidation= mutableStateOf(false)

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
                    passwordError =  event.password.length<=6
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
                logIn()
            }

            is UIEvent.RegisterButtonClicked ->{
                signUp(registrationUIState.value.name,registrationUIState.value.email,registrationUIState.value.password)
            }

            is UIEvent.GoogleSignIn ->{

            }


        }
    }

    private fun signUp(name: String, email: String, password: String) {

    }

    private fun loginValidationPassed(){
        loginValidation.value= (loginUIState.value.emailError==false) && (loginUIState.value.passwordError==false)
    }

    private fun registrationValidationPassed(){
        registerValidation.value=( (!registrationUIState.value.emailError) && (!registrationUIState.value.passwordError) && (!registrationUIState.value.nameError) )
    }
    private fun logIn() {

    }
}