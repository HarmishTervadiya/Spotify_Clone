@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.spotify_clone.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotify_clone.data.LoginRegisterViewModel
import com.example.spotify_clone.data.UIEvent
import kotlinx.coroutines.launch

@Composable
fun SignUPButton(
    value:String, bgColor: Color,
    textColor: Color,
    icon:ImageVector,
    paddingValues: Dp,
    state:Boolean=true,
    onClick:()->Unit){

    ElevatedButton(
        enabled = state,
        onClick = { onClick.invoke() },
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .height(48.dp)
            , colors = ButtonDefaults.buttonColors(
                containerColor = bgColor,
                contentColor = textColor
            ),

        ) {

        if (icon!=Icons.Filled.Clear) {
            Icon(imageVector = icon, contentDescription = value)
        }
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxSize(),
            textAlign = TextAlign.Center)
    }
    
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterBottomSheet(sheetState: SheetState){

    val registrationViewModel=LoginRegisterViewModel()
    val scaffoldState= rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    val scope= rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState=scaffoldState,
        containerColor = Color.Transparent,
        contentColor =Color.Transparent ,
        sheetContainerColor = Color.LightGray,
        sheetContentColor = Color.LightGray,
        sheetPeekHeight = 450.dp,
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxSize(),
        sheetContent = {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
            .padding(5.dp)){

            Column(modifier=Modifier.fillMaxSize()){

                HeadingText(value = "Sign Up",Color.Black)
                InputTextBox(value = "Enter your Name", onChange = { registrationViewModel.onEvent(
                    UIEvent.NameChanged(it)) }, state = registrationViewModel.registrationUIState.value.nameError)

                InputTextBox(value = "Enter your Email", onChange = { registrationViewModel.onEvent(UIEvent.EmailChanged(it)) }, state =registrationViewModel.registrationUIState.value.emailError )
                InputTextBox(value = "Enter your Password", onChange = { registrationViewModel.onEvent(UIEvent.PasswordChanged(it)) }, state =registrationViewModel.registrationUIState.value.passwordError )

                SignUPButton(value = "Register", bgColor = Color.Red, textColor =Color.White , icon =Icons.Filled.Clear, paddingValues = 28.dp,state=registrationViewModel.registerValidation.value) {
                    registrationViewModel.onEvent(UIEvent.RegisterButtonClicked)
                }

                RoundButton(onClick = {
                    scope.launch {
                        sheetState.hide()
                    }
                },modifier= Modifier.align(Alignment.CenterHorizontally))


            }

        }

    }) {

    }

}


@Composable
fun RoundButton(onClick: () -> Unit, modifier: Modifier){
    SmallFloatingActionButton(onClick = { onClick.invoke() },modifier=modifier ) {
        Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DefaultPreviewRegisterScreen(){

    RegisterBottomSheet(sheetState = SheetState(skipPartiallyExpanded = false, initialValue = SheetValue.Hidden))
}
