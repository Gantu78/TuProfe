package com.example.tuprofe.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tuprofe.R

@Composable
fun BackgroundImage(
    modifier: Modifier = Modifier
){
    Image(
        painter = painterResource(R.drawable.fondo),
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alignment = Alignment.TopCenter
    )
}

@Composable
@Preview
fun BackgroundPrev(){
    BackgroundImage()
}