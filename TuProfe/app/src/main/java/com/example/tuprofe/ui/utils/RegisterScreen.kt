package com.example.tuprofe.ui.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
    ){
        BackgroundImage()
    }

}

@Composable
@Preview
fun RegisterScreenPreview(){
    RegisterScreen()
}