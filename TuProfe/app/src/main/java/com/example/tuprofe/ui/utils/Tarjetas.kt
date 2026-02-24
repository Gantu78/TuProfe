package com.example.tuprofe.ui.utils

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuprofe.R
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.local.LocalReview


@Composable
fun Resena(
    reviewInfo: ReviewInfo,
    modifier: Modifier = Modifier,
    onCommentsClick: () -> Unit = {}

) {

        Column(
            modifier = Modifier.padding(6.dp)
        ) {

            TuProfeCardHeader(
                modifier = modifier.
                padding(horizontal = 10.dp),
                name = reviewInfo.name,
                carrera = reviewInfo.materia,
                imagen = reviewInfo.imageId
            )

            Spacer(modifier = Modifier.height(8.dp))

            RatingStars(
                rating = reviewInfo.rating,
                starColor = colorResource(R.color.verdetp),
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            TuProfeCardBody(
                content = reviewInfo.content,
                date = reviewInfo.time,
                modifier = Modifier.padding(horizontal = 10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ResenaCardActions(
                modifier = Modifier.padding(horizontal = 10.dp),
                likes = reviewInfo.likes,
                comments = reviewInfo.comments,
                onCommentsClick = onCommentsClick
            )
        }
    }


@Preview(showBackground = true)
@Composable
fun ResenaPreview() {
    val example = LocalReview.Reviews[0]
    Resena(
        reviewInfo = example,
        onCommentsClick = {}
    )
}

@Composable
fun RatingStars(
    rating: Int,
    starColor: Color = Color(0xFF1DB954), // verde por defecto
    modifier: Modifier = Modifier
) {
    Row (modifier =  modifier){

        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (index < rating) starColor else Color.LightGray
            )
        }
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

    }
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
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(top = 5.dp, )
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