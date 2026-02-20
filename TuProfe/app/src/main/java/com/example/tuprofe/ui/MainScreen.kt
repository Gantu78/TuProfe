package com.example.tuprofe.ui


import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import com.example.tuprofe.R
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.BottomBar
import com.example.tuprofe.ui.utils.HeaderSection
import com.example.tuprofe.ui.utils.ResenaCard


@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) {

    val allReviews = LocalReview.Reviews

    Scaffold (
        bottomBar = {
            BottomBar(
                boton1 = ("Inicio"),
                boton2 = ("Profesores"),
                boton3 = ("perfil")
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            BackgroundImage()

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 5.dp,
                    bottom = 20.dp
                )
            ) {

                item {
                    HeaderSection(
                        title = "TuProfe",
                        showSearchBar = true,
                        onBackClick = null
                    )
                }

                items(allReviews) { review ->
                    ResenaCard(reviewInfo = review)
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}




@Preview(showBackground = true)
@Composable
fun ResenaCardPreview() {
    val example = LocalReview.Reviews[0]
    ResenaCard(
        reviewInfo = example,
        onCommentsClick = {}
    )
}

@Composable
fun TuProfeCardHeader(
    name: String,
    username: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(R.drawable.avatar),
            contentDescription = null,
            Modifier
                .padding(end = 8.dp)
                .height(40.dp)
                .width(40.dp)
                .clip(CircleShape)
        )
        Column() {
            Text(
                name,
                fontWeight = FontWeight.Bold
            )
            Text(
                username,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TuProfeCardHeaderPreview(){
    TuProfeCardHeader(
        name = "Juan Perez",
        username = "@juanputo"
    )
}


@Composable
fun TuProfeCardBody(
    content: String,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    ) {
        Text(
            content
        )
        Text(
            "8:26 PM * Dec 1 2022",
            color= Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TuProfeCardBodyPreview() {

    TuProfeCardBody("Lorem Ipsum Dolor Sit Amet")
}

@Composable
fun TuProfeCardFooterItem(
    cantidad: Int,
    @StringRes label: Int,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.padding(end = 8.dp)
    ) {
        Text(
            cantidad.toString(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(stringResource(label))
    }
}

@Preview(showBackground = true)
@Composable
fun TuProfeCardFooterItemPreview(){
    TuProfeCardFooterItem(
        cantidad = 1000,
        label = R.string.likes
    )
}

@Composable
fun TuProfeCardFooter(
    likes: Int,
    comments: Int,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
    ) {
        TuProfeCardFooterItem(
            likes,
            R.string.likes
        )
        TuProfeCardFooterItem(
            comments,
            label = R.string.comentarios
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TuProfeCardFooterPreview(){
    TuProfeCardFooter(

        likes = 500,
        comments = 100
    )
}
