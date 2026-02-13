package com.example.tuprofe.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.LogoApp
import com.example.tuprofe.ui.utils.LogoLoading

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
    ){
        BackgroundImage()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            LogoApp()
            Spacer(modifier = Modifier.padding(15.dp))
            LogoLoading()
        }

    }
}

@Composable
@Preview (showBackground = true)
fun LoadingScreenPreview(){
    LoadingScreen()
}