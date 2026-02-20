package com.example.tuprofe.ui.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.example.tuprofe.R
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.ui.TuProfeCardBody
import com.example.tuprofe.ui.TuProfeCardFooter
import com.example.tuprofe.ui.TuProfeCardHeader
import com.example.tuprofe.ui.theme.BebasNeue

@Composable
fun LogoApp(
    modifier: Modifier = Modifier
){
    Image(
        painter = painterResource(R.drawable.img),
        contentDescription = stringResource(R.string.logo_tuprofe)
    )
}

@Composable
fun LogoLoading(
    modifier: Modifier = Modifier
){
    Image(
        painter = painterResource(R.drawable.loading),
        contentDescription = (stringResource(R.string.loading))
    )
}


@Composable
fun AppButton(
    textoBoton: String,
    onClick: () -> Unit = {},
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
    textoBoton: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
){
    TextButton(
        onClick = onClick, // Usamos el parámetro
        modifier = modifier
    ) {
        Text(
            text = textoBoton,
            fontSize = 20.sp,
            textDecoration = TextDecoration.Underline, // Subraya el texto
            color = colorResource(R.color.verdetp2),
            fontFamily = BebasNeue
        )
    }
}


@Composable
fun TextFieldApp(
    texto: String,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    modifier: Modifier = Modifier
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
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
    texto: String,
    mostrarPassword: Boolean,
    click: () -> Unit,
    value: String = "",
    onValueChange: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    icono: Int
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
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
        modifier = Modifier.padding(vertical = 2.dp)
    )
}
@Composable
fun ConfigItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(2.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.pastel)
        )
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
fun BottomBar(
    modifier: Modifier = Modifier,
    boton1: String,
    boton2: String ="",
    boton3: String = "",

    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.verdetp))
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text("$boton1", color = Color.White)
        Text("$boton2", color = Color.Black, fontWeight = FontWeight.Bold)
        Text("$boton3", color = Color.White)
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .clip(RoundedCornerShape(50))
            .background(colorResource(R.color.verdetp))
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Busca a TuProfe",
            color = Color.White,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = Color.White
        )
    }
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
fun HeaderSection(
    title: String,
    showSearchBar: Boolean = true,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (onBackClick != null) {
            BackButtonHeader(onBackClick = onBackClick)
        }

        if (showSearchBar) {
            SearchBar()
            Spacer(modifier = Modifier.height(20.dp))
        }

        TitleHeader(title = title)

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ResenaCardActions(
    likes: Int,
    comments: Int,
    onCommentsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TuProfeCardFooter(
            likes = likes,
            comments = comments
        )

        IconButton(
            onClick = onCommentsClick,
            modifier = Modifier.size(30.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MailOutline,
                contentDescription = "Comments",
                tint = colorResource(R.color.verdetp)
            )
        }
    }
}

@Composable
fun ResenaCard(
    reviewInfo: ReviewInfo,
    modifier: Modifier = Modifier,
    onCommentsClick: () -> Unit = {}

) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5 .dp, horizontal = 40.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 2.5.dp,
            color = colorResource(R.color.BordeTuProfe)
        ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.pastel)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(6.dp)
        ) {

            TuProfeCardHeader(
                name = reviewInfo.name,
                username = reviewInfo.materia

            )

            Spacer(modifier = Modifier.height(8.dp))

            RatingStars(
                rating = reviewInfo.rating,
                starColor = colorResource(R.color.verdetp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            TuProfeCardBody(
                content = reviewInfo.content
            )

            Spacer(modifier = Modifier.height(16.dp))

            ResenaCardActions(
                likes = reviewInfo.likes,
                comments = reviewInfo.comments,
                onCommentsClick = onCommentsClick
            )
        }
    }
}
@Composable
fun RatingStars(
    rating: Int,
    starColor: Color = Color(0xFF1DB954) // verde por defecto
) {
    Row {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (index < rating) starColor else Color.LightGray
            )
        }
    }
}
