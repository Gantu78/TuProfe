package com.example.tuprofe.ui.utils

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tuprofe.R
import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.local.LocalReview



@Composable
fun ProfileHeaderCard(
    username: String,
    email: String,
    carrera: String,
    imageUrl: String?,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    showStar: Boolean = true
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        onClick = onProfileClick,
        shape = RoundedCornerShape(32.dp),
        border = BorderStroke(
            2.dp,
            colorResource(R.color.BordeTuProfe)
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = stringResource(R.string.foto_de_perfil),
                placeholder = painterResource(R.drawable.loading_img),
                error = painterResource(R.drawable.avatar),
                modifier = Modifier
                    .size(88.dp)
                    .shadow(6.dp, CircleShape, clip = false)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                        CircleShape
                    ),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(18.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = username,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    if (showStar) {
                        Spacer(modifier = Modifier.width(6.dp))

                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = colorResource(R.color.verdetp),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Text(
                    text = email,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = carrera,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
            profeName = reviewInfo.profesor.nombreProfe,
            userName = reviewInfo.username,
            carrera = reviewInfo.materia.nombreMateria,
            imageUrl = reviewInfo.profesor.imageprofeUrl,
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
    starColor: Color = Color(0xFF1DB954),
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
    imageUrl: String?,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth()
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {


        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(R.string.foto_de_perfil),
            placeholder = painterResource(R.drawable.loading_img),
            error = painterResource(R.drawable.avatar),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    clip = false
                )
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = CircleShape
                )
                .clickable(onClick = onProfileClick)

        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {

            // 🔹 Fila superior
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = profeName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Por: @$userName",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // 🔹 Materia
            Text(
                text = carrera,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TuProfeCardHeaderPreview(){
    val review = LocalReview.Reviews[0]
    TuProfeCardHeader(
        profeName = review.profesor.nombreProfe,
        userName = review.username,
        carrera = review.materia.nombreMateria,
        imageUrl = "https://co.linkedin.com/in/gerardo-tole-galvis-ms-c-94239452",
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