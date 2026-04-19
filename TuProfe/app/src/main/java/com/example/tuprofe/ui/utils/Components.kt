package com.example.tuprofe.ui.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.example.tuprofe.HeaderSection
import com.example.tuprofe.R
import com.example.tuprofe.ui.theme.BebasNeue

@Composable
fun LogoApp(
    modifier: Modifier = Modifier
){
    Image(
        modifier = modifier,
        painter = painterResource(R.drawable.img),
        contentDescription = stringResource(R.string.logo_tuprofe)
    )
}



@Composable
fun AppButton(
    textoBoton: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Button(
        onClick =  onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.verdetp)
        ),

        modifier = modifier
    ) {
        Text(textoBoton, fontSize = 20.sp, fontFamily = BebasNeue)
    }
}


@Composable
fun AppButtonRow(
    textoBoton: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Button(
        onClick =  onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.verdetp)
        ),

        modifier = modifier
    ) {
        Text(textoBoton, fontSize = 15.sp, fontFamily = BebasNeue)
    }
}


@Composable
fun AppTextButton(
    modifier: Modifier = Modifier,
    textoBoton: String,
    onClick: () -> Unit = {}
){
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = textoBoton,
            fontSize = 20.sp,
            textDecoration = TextDecoration.Underline,
            color = colorResource(R.color.verdetp2),
            fontFamily = BebasNeue
        )
    }
}


@Composable
fun TextFieldApp(
    modifier: Modifier = Modifier,
    texto: String,
    value: String = "",
    onValueChange: (String) -> Unit = {}
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text(text = texto)},
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = colorResource(R.color.pastel),
            focusedContainerColor = colorResource(R.color.pastel),
            unfocusedLabelColor = colorResource(R.color.gris),
            focusedLabelColor = colorResource(R.color.gris),
            unfocusedIndicatorColor = colorResource(R.color.gris),
            focusedIndicatorColor = colorResource(R.color.gris)
        ),
        shape = RoundedCornerShape(30.dp),
        modifier = modifier
    )
}


@Composable
fun TextFieldContraApp(
    modifier: Modifier = Modifier,
    texto: String,
    mostrarPassword: Boolean,
    click: () -> Unit,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    icono: Int
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        label = { Text(text = texto)},
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = colorResource(R.color.pastel),
            focusedContainerColor = colorResource(R.color.pastel),
            unfocusedLabelColor = colorResource(R.color.gris),
            focusedLabelColor = colorResource(R.color.gris),
            unfocusedIndicatorColor = colorResource(R.color.gris),
            focusedIndicatorColor = colorResource(R.color.gris)
        ),
        shape = RoundedCornerShape(30.dp),
        visualTransformation = if(mostrarPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = click) {
                Icon(
                    painter = painterResource(icono),
                    contentDescription = stringResource(R.string.mostrar_password),
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
        },
        modifier = modifier.padding(vertical = 2.dp)
    )
}
@Composable
fun ConfigItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(2.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.surface
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorResource(R.color.verdetp),
                modifier = Modifier.size(30.dp)
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Text(title, fontWeight = FontWeight.Bold)
                Text(subtitle, color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}




@Composable
fun SearchBar(
query: String,
onQueryChange: (String) -> Unit,
modifier: Modifier = Modifier,
placeholder: String = "Busca a TuProfe"
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text(placeholder) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = colorResource(R.color.verdetp)
            )
        },
        shape = RoundedCornerShape(50.dp),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = colorResource(R.color.pastel),
            focusedContainerColor = colorResource(R.color.pastel),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = colorResource(R.color.verdetp)
        ),
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
fun TitleHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(R.color.verdetp),
        modifier = modifier
    )
}



@Composable
fun BackButtonHeader(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = colorResource(R.color.verdetp)
            )
        }
    }
}



@Composable
@Preview
fun HeaderSectionPreview(){
    HeaderSection(
        title = stringResource(R.string.tuprofe)
    )
}








