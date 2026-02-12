package com.example.tuprofe.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.tuprofe.R

@Composable
fun logoApp(
    modifier: Modifier = Modifier
){
    Image(
        painter = painterResource(R.drawable.logo),
        contentDescription = stringResource(R.string.logo_tuprofe)
    )
}