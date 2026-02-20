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
    modifier: Modifier = Modifier,
    onResenaClick: (Int) -> Unit = { println("Clicked on review $it") },
    onProfileClick: () -> Unit = { println("Clicked on profile") },
    onTeachersClick: () -> Unit = { println("Clicked on teachers") },
    onHomeClick: () -> Unit = { println("Clicked on home") }
) {

    val allReviews = LocalReview.Reviews

    Scaffold (
        bottomBar = {
            BottomBar(
                on1Click = onHomeClick,
                on2Click = onTeachersClick,
                on3Click = onProfileClick
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
                        title = stringResource(R.string.tuprofe),
                        showSearchBar = true
                    )
                }

                items(allReviews) { review ->
                    ResenaCard(
                        reviewInfo = review,
                        onCommentsClick = { onResenaClick(review.imageId) })
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
    carrera: String,
    modifier: Modifier = Modifier,
    imagen: Int = R.drawable.avatar
){
    Row(
        modifier = modifier
    ) {
        Image(
            painter = painterResource(imagen),
            contentDescription = stringResource(R.string.foto_de_perfil),
            modifier = modifier
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
                carrera,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TuProfeCardHeaderPreview(){
    val review = LocalReview.Reviews[0]
    TuProfeCardHeader(
        name = review.name,
        carrera = review.materia
    )
}


@Composable
fun TuProfeCardBody(
    content: String,
    date: String,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
    ) {
        Text(
            content
        )
        Text(
            date,
            color= Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TuProfeCardBodyPreview() {
    val review = LocalReview.Reviews[0]
    TuProfeCardBody(review.content, review.time)
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
    val review = LocalReview.Reviews[0]
    TuProfeCardFooter(
        likes = review.likes,
        comments = review.comments
    )
}
