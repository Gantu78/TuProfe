package com.example.tuprofe.ui


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableOpenTarget
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tuprofe.ui.utils.AppButton
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.logoApp
import com.example.tuprofe.R
import com.example.tuprofe.ui.utils.AppTextButton
import com.example.tuprofe.ui.utils.TextFieldApp
import com.example.tuprofe.ui.utils.TextFieldContraApp

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier.Companion
) {

    Box(
        modifier = modifier
    ) {
        BackgroundImage()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            logoApp()
            FormularioRegistro()
            Spacer(modifier = Modifier.padding(15.dp))
            AppButton(stringResource(R.string.registrarse))
            AppTextButton(stringResource(R.string.ya_tengo_una_cuenta))
            AppTextButton(stringResource(R.string.volver))
        }
    }

}

@Composable
@Preview
fun RegisterScreenPreview(){
    RegisterScreen()
}

 @Composable
 fun FormularioRegistro(
     modifier: Modifier = Modifier
 ){
     Column(
         modifier = modifier
     ) {
         TextFieldApp(stringResource(R.string.email))
         TextFieldApp(stringResource(R.string.email))
         TextFieldApp(stringResource(R.string.email))
         TextFieldContraApp(stringResource(R.string.contrase_a))
         TextFieldContraApp(stringResource(R.string.repetir_contrase_a) )
     }


 }