package com.example.tuprofe.ui.utils

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
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
fun ProfileHeaderCard(
    username: String,
    email: String,
    carrera: String,
    imageRes: Int,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    showStar: Boolean = true
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        onClick = onProfileClick,
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(2.5.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(84.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = username,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = colorResource(R.color.verdetp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = email, color = Color.Gray, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = carrera, color = Color.Gray, fontSize = 14.sp)
            }

            if (showStar) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = colorResource(R.color.verdetp),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun Resena(
    reviewInfo: ReviewInfo,
    modifier: Modifier = Modifier,
    onCommentsClick: () -> Unit = {},
    onProfileClick: () -> Unit

) {

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        TuProfeCardHeader(
            profeName = reviewInfo.profeName,
            userName = reviewInfo.userName,
            carrera = reviewInfo.materia,
            imagen = reviewInfo.imageId,
            onProfileClick = onProfileClick
        )

        RatingStars(
            rating = reviewInfo.rating,
            starColor = colorResource(R.color.verdetp)
        )

        TuProfeCardBody(
            content = reviewInfo.content,
            date = reviewInfo.time
        )

        ResenaCardActions(
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
        onCommentsClick = {},
        onProfileClick = {}
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
        modifier = modifier.fillMaxWidth()
        .padding(start = 2.dp),
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
    profeName: String,
    userName: String,
    carrera: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    imagen: Int = R.drawable.avatar,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(imagen),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onProfileClick)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Text(
                    text = profeName,
                    fontSize = 16.sp,
                    lineHeight = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = carrera,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.Top
        ) {
            Text(
                stringResource(R.string.por),
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                stringResource(R.string.arroba) + userName,
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
        profeName = review.profeName,
        userName = review.userName,
        carrera = review.materia,
        imagen = review.imageId,
        onProfileClick = {}
    )
}


@Composable
fun TuProfeCardBody(
    content: String,
    date: String,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.padding(start = 2.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(content)

        Text(
            text = date,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            color = Color.Gray
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