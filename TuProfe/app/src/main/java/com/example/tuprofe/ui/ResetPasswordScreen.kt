package com.example.tuprofe.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuprofe.R
import com.example.tuprofe.ui.theme.BebasNeue
import com.example.tuprofe.ui.utils.AppButton
import com.example.tuprofe.ui.utils.AppTextButton
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.LogoApp
import com.example.tuprofe.ui.utils.TextFieldApp

@Composable
fun ResetPasswordScreen(
    modifier: Modifier = Modifier
    )
    {
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
                TextFieldApp(stringResource(R.string.email))
                Spacer(modifier = Modifier.padding(20.dp))
                AppButton(stringResource(R.string.enviar_enlace))
                Spacer(modifier = Modifier.padding(10.dp))
                AppTextButton(stringResource(R.string.volver))

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
    ) {
        Text(stringResource(R.string.recuperar_contrase_a),
            color = colorResource(R.color.verdetp),
            fontFamily = BebasNeue,
            fontSize = 22.sp
        )
        Spacer(modifier = Modifier.padding(15.dp))
        Text(stringResource(R.string.textorespass),
            textAlign = TextAlign.Center,
            fontSize = 17.sp,
            modifier = Modifier.padding(horizontal = 50.dp, vertical = 0.dp)
        )
    }
}

@Composable
@Preview (showBackground = true)
fun RPasswordScreenPreview(){
    ResetPasswordScreen()
}

