package com.example.tuprofe.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
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
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.theme.TuProfeTheme
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.RatingStars
import com.example.tuprofe.ui.utils.ResenaCard
import kotlin.math.roundToInt

@Composable
fun ProfeScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val allReviews = LocalReview.Reviews
    val professorName = LocalReview.Reviews[4].name
    val professorSubjects = LocalReview.Reviews[4].materia
    val professorRating = allReviews.map { it.rating }.average().roundToInt()
    val professorImage = LocalReview.Reviews[4].imageId

    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()
        Column(Modifier.fillMaxSize()) {
            ProfeScreenHeader(
                onBackClick = onBackClick
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 80.dp) // Add padding for the bottom bar
            ) {
                item {
                    ProfessorInfoCard(
                        professorName = professorName,
                        professorSubjects = professorSubjects,
                        generalRating = professorRating,
                        professorImageRes = professorImage
                    )
                }

                items(allReviews) { review ->
                    ResenaCard(reviewInfo = review)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        ProfeScreenBottomBar(
            onAddCommentClick = { /* TODO */ },
            onRateClick = { /* TODO */ },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ProfeScreenHeader(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.tuprofe),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.verdetp)
        )
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.atras),
                tint = colorResource(id = R.color.verdetp),
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
private fun ProfessorInfoCard(
    professorName: String,
    professorSubjects: String,
    generalRating: Int,
    @DrawableRes professorImageRes: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        border = BorderStroke(
            width = 2.5.dp,
            color = colorResource(R.color.BordeTuProfe)
        ),
        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF5E6).copy(alpha = 0.8f)) // Beige background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ProfessorImage(imageRes = professorImageRes)
                ProfessorNameAndSubject(name = professorName, subjects = professorSubjects)
            }
            Spacer(modifier = Modifier.height(16.dp))
            GeneralRating(rating = generalRating)
        }
    }
}

@Composable
private fun ProfessorImage(
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = stringResource(R.string.foto_de_perfil_del_profesor),
        modifier = modifier
            .size(100.dp)
            .clip(CircleShape)
    )
}

@Composable
private fun ProfessorNameAndSubject(
    name: String,
    subjects: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = subjects, fontSize = 16.sp, color = Color.Gray)
    }
}

@Composable
private fun GeneralRating(
    rating: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.calificaci_n_general),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        RatingStars(rating = rating)
    }
}



@Composable
fun ProfeScreenBottomBar(
    onAddCommentClick: () -> Unit,
    onRateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.verdetp))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onAddCommentClick,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.a_adir_comentario), color = Color.White, fontSize = 16.sp)
            }
            VerticalDivider(
                modifier = Modifier.height(30.dp),
                thickness = 1.dp,
                color = Color.White
            )
            TextButton(
                onClick = onRateClick,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.calificar), color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfeScreenPreview() {
    TuProfeTheme {
        ProfeScreen()
    }
}
