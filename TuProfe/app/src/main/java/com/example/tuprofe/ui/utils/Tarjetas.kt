package com.example.tuprofe.ui.utils

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tuprofe.R
import com.example.tuprofe.data.ReviewInfo

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
    onProfileClick: () -> Unit, // Navigate to Professor Profile
    onUserClick: () -> Unit // Navigate to User Profile
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TuProfeCardHeader(
            profeName = reviewInfo.profesor.nombreProfe,
            userName = reviewInfo.usuario.nombreUsu,
            carrera = reviewInfo.materia.nombreMateria,
            imageUrl = reviewInfo.profesor.imageprofeUrl,
            onProfessorClick = onProfileClick,
            onUserClick = onUserClick
        )

        RatingStars(
            rating = reviewInfo.rating.toFloat(),
            starColor = colorResource(R.color.verdetp)
        )

        TuProfeCardBody(
            content = reviewInfo.content,
            date = reviewInfo.time
        )

        ResenaCardActions(
            likes = reviewInfo.likes,
            comments = reviewInfo.commentsCount,
            onCommentsClick = onCommentsClick
        )
    }
}

@Composable
fun TuProfeCardHeader(
    modifier: Modifier = Modifier,
    profeName: String,
    userName: String,
    carrera: String,
    imageUrl: String?,
    onProfessorClick: () -> Unit,
    onUserClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
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
                .shadow(elevation = 4.dp, shape = CircleShape, clip = false)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = CircleShape
                )
                .clickable(onClick = onProfessorClick)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
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
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onProfessorClick)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Por: @$userName",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.clickable(onClick = onUserClick)
                )
            }

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

private val LeftHalfShape = object : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density) =
        Outline.Rectangle(Rect(0f, 0f, size.width / 2f, size.height))
}

@Composable
fun RatingStars(
    modifier: Modifier = Modifier,
    rating: Float,
    starSize: androidx.compose.ui.unit.Dp = 24.dp,
    starColor: Color = Color(0xFF1DB954)
) {
    Row(modifier = modifier) {
        repeat(5) { index ->
            val threshold = index + 1f
            when {
                rating >= threshold -> Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = starColor,
                    modifier = Modifier.size(starSize)
                )
                rating >= threshold - 0.5f -> Box(modifier = Modifier.size(starSize)) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(starSize)
                    )
                    Box(modifier = Modifier.size(starSize).clip(LeftHalfShape)) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = starColor,
                            modifier = Modifier.size(starSize)
                        )
                    }
                }
                else -> Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(starSize)
                )
            }
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
        modifier = modifier
            .fillMaxWidth()
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
) {
    Row(modifier = modifier) {
        TuProfeCardFooterItem(likes, R.string.likes)
        TuProfeCardFooterItem(comments, label = R.string.comentarios)
    }
}

@Composable
fun TuProfeCardBody(
    content: String,
    date: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(start = 2.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(content)
        Text(
            text = date,
            fontSize = 12.sp,
            lineHeight = 12.sp,
            color = colorResource(R.color.gris)
        )
    }
}

@Composable
fun TuProfeCardFooterItem(
    cantidad: Int,
    @StringRes label: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(end = 8.dp)) {
        Text(
            cantidad.toString(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 4.dp)
        )
        Text(stringResource(label))
    }
}
