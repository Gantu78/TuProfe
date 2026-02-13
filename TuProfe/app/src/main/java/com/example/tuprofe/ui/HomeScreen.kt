package com.example.tuprofe.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tuprofe.R
import com.example.tuprofe.ui.utils.AppButton
import com.example.tuprofe.ui.utils.AppButtonRow
import com.example.tuprofe.ui.utils.AppTextButton
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.TextFieldApp
import com.example.tuprofe.ui.utils.TextFieldContraApp
import com.example.tuprofe.ui.utils.LogoApp

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
    ) {
        BackgroundImage()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            LogoApp()
            Spacer(modifier = Modifier.padding(30.dp))
            FormularioInicio()
            Spacer(modifier = Modifier.padding(15.dp))
            AppButton(stringResource(R.string.iniciar_sesion))
            Spacer(modifier = Modifier.padding(30.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {

                AppButtonRow(stringResource(R.string.olvide_la_contrase_a))
                Spacer(modifier = Modifier.width(30.dp))
                AppButtonRow(stringResource(R.string.crear_cuenta))
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}

@Composable
@Preview
fun HomeScreenPreview(){
    HomeScreen()
}

@Composable
fun FormularioInicio(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    ) {
        TextFieldApp(stringResource(R.string.usuario))
        TextFieldContraApp(stringResource(R.string.contrase_a))
    }

}