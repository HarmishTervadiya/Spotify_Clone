package com.example.spotify_clone.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spotify_clone.ui.theme.Primary

@Composable
fun HeadingText(value:String,textColor:Color){

    Text(text = value, modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        fontSize = 30.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        color = textColor,
        textAlign = TextAlign.Center)
}

@Composable
fun InputTextBox(value: String,onChange:(String)->Unit,state:Boolean){

    val textValue= remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        value=textValue.value,
        isError = state,
        label = { Text(text = value)},
        onValueChange ={
            textValue.value=it
                onChange(it)
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            focusedSupportingTextColor = Color.Black,

            focusedIndicatorColor = Color.Black,
            cursorColor = Primary,
            focusedTextColor = Color.Black,
            disabledTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = Primary,
            errorContainerColor = Color.Transparent,

        ),
        keyboardOptions = KeyboardOptions(imeAction= ImeAction.Next),
        singleLine = true,
        maxLines = 1,

    )
}


@Composable
fun LoginInputTextBox(value: String,onChange:(String)->Unit,state:Boolean){

    val textValue= remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        value=textValue.value,
        isError = state,
        label = { Text(text = value)},
        onValueChange ={
            textValue.value=it
            onChange(it)
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            focusedSupportingTextColor = Color.White,
            focusedIndicatorColor = Color.White,
            cursorColor = Color.LightGray,
            focusedTextColor = Color.White,
            disabledTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = Color.LightGray,
            errorContainerColor = Color.Transparent,
            unfocusedLabelColor = Color.White,
            unfocusedSupportingTextColor = Color.White,
            errorTextColor = Color.White

            ),
        keyboardOptions = KeyboardOptions(imeAction= ImeAction.Next),
        singleLine = true,
        maxLines = 1,

        )
}


//@Composable
//fun SingleHighlightedText(normalText:String,highLightedText:String,onClick:(String)->Unit) {
//
//    val annotatedString = buildAnnotatedString {
//
//        append(normalText)
//        withStyle(style = SpanStyle(color = Color.LightGray)) {
//            pushStringAnnotation(tag = highLightedText, annotation = highLightedText)
//            append(highLightedText)
//        }
//    }
//
//    ClickableText(text = annotatedString, modifier = Modifier.fillMaxWidth(),
//
//        style = TextStyle(textAlign = TextAlign.Center,
//            color = Color.White),
//        onClick = { offset ->
//
//            annotatedString.getStringAnnotations(offset, offset)
//                .firstOrNull()?.also { span ->
//                    if (span.item == highLightedText) {
//                        onClick(span.item)
//                    }
//                }
//        })
//}

@Composable
fun ClickText(value: String,onClick:()->Unit){

    val annotatedText= buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.LightGray)){
            pushStringAnnotation(tag = value, annotation = value)
                append(value)

        }
    }
    ClickableText(
        modifier = Modifier.padding(8.dp)
            .fillMaxWidth(),
                text =annotatedText , onClick ={onClick.invoke()}, style = TextStyle(textAlign = TextAlign.Right) )
}

@Preview
@Composable
fun DefaultPreviewTextComponent(){

    InputTextBox(value = "Enter your Email", onChange = {null}, state =false )

}
