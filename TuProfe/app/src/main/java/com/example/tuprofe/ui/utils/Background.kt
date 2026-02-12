package com.example.tuprofe.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tuprofe.R

@Composable
fun BackgroundImage(
    modifier: Modifier = Modifier
){
    Image(
        painter = painterResource(R.drawable.fondo),
        contentDescription = stringResource(R.string.fondo_tuprofe),
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable
@Preview
fun BackgroundPrev(){
    BackgroundImage()
}