package com.example.tuprofe.ui.resetPassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuprofe.R
import com.example.tuprofe.ui.theme.BebasNeue
import com.example.tuprofe.ui.utils.AppButton
import com.example.tuprofe.ui.utils.AppTextButton
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.LogoApp
import com.example.tuprofe.ui.utils.TextFieldApp

@Composable
fun ResetPasswordScreen(
    resetPasswordViewModel: ResetPasswordViewModel,
    modifier: Modifier = Modifier,
    onVolverClick: () -> Unit,
    )
    {
        val state by resetPasswordViewModel.uiState.collectAsState()
        Box(
            modifier = modifier
        ){
            BackgroundImage()
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ){
                LogoApp()
                Spacer(modifier = Modifier.padding(15.dp))
                TextosPassword()
                Spacer(modifier = Modifier.padding(10.dp))
                TextFieldApp(
                    stringResource(R.string.email),
                    value = state.email,
                    onValueChange = { resetPasswordViewModel.setEmail(it) },
                    singleLine = true
                )
                Spacer(modifier = Modifier.padding(10.dp))
                if (state.mostrarMensaje) {
                    Text("Si el correo está registrado, recibirás un enlace", color = Color(0xFF1AC06A), fontSize = 16.sp)
                } else if (state.mostrarError) {
                    Text(state.errorMessage, color = Color.Red, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.padding(10.dp))
                AppButton(stringResource(R.string.enviar_enlace),
                    onClick = { resetPasswordViewModel.resetPassword() }
                )
                Spacer(modifier = Modifier.padding(10.dp))
                AppTextButton(
                    modifier = Modifier,
                    stringResource(R.string.volver),
                    onClick = onVolverClick)

            }
        }

}

@Composable
fun TextosPassword(
    modifier: Modifier = Modifier
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(stringResource(R.string.recuperar_contrase_a),
            color = colorResource(R.color.verdetp2),
            fontFamily = BebasNeue,
            fontSize = 22.sp,

        )
        Spacer(modifier = Modifier.padding(15.dp))
        Text(stringResource(R.string.textorespass),
            textAlign = TextAlign.Center,
            fontSize = 17.sp,
            color = colorResource(R.color.black),
            modifier = Modifier.padding(horizontal = 50.dp, vertical = 0.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun TextosPasswordPreview(){
    TextosPassword()
}


@Composable
@Preview (showBackground = true)
fun RPasswordScreenPreview(){
    ResetPasswordScreen(
        onVolverClick = {},
        resetPasswordViewModel = viewModel()
    )
}

