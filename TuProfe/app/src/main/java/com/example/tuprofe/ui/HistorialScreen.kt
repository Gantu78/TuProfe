package com.example.tuprofe.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuprofe.R
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.utils.AppButton
import com.example.tuprofe.ui.utils.AppButtonRow
import com.example.tuprofe.ui.utils.BackgroundImage

@Composable
fun HistorialScreen(
    modifier: Modifier = Modifier,
    onFilterClick: () -> Unit,
    onVerCalificacionClick: (ReviewInfo) -> Unit
) {
    val reviews = LocalReview.Reviews

    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp)
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            HistorialHeader(
                onFilterClick = onFilterClick
            )

            // Reviews List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(reviews) { review ->
                    HistorialCard(
                        review = review,
                        onVerCalificacionClick = { onVerCalificacionClick(review) }
                    )
                }
            }
        }



    }
}

@Composable
fun HistorialHeader(
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
){

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {

        AppButton(
            textoBoton = stringResource(R.string.filtrar),
            onClick = onFilterClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(56.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.texto_calificaciones),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 22.sp,
            color = Color.Gray
        )
    }
}

@Composable
@Preview
fun HistorialHeaderPreview() {
    HistorialHeader(onFilterClick = {})
}


@Composable
fun HistorialCard(
    review: ReviewInfo,
    onVerCalificacionClick: (ReviewInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        border = BorderStroke(1.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.surface
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(review.imageId),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            HistorialCardBody(
                review = review,
                onVerCalificacionClick = { onVerCalificacionClick(review) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun HistorialCardBody(
    review: ReviewInfo,
    onVerCalificacionClick: () -> Unit,
    modifier: Modifier = Modifier
){

    Column(modifier = modifier) {
        Text(
            text = review.name,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            text = review.materia,
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        AppButtonRow(
            textoBoton = stringResource(R.string.ver_calificaci_n),
            onClick = onVerCalificacionClick,
            modifier = Modifier.height(36.dp)
        )
    }
}

@Composable
@Preview (showBackground = true)
fun HistorialCardBodyPreview() {
    HistorialCardBody(
        review = LocalReview.Reviews[0],
        onVerCalificacionClick = {}
    )
}

@Composable
@Preview (showBackground = true)
fun HistorialCardPreview() {
    HistorialCard(
        review = LocalReview.Reviews[0],
        onVerCalificacionClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun HistorialScreenPreview() {
    HistorialScreen(
        onFilterClick = {},
        onVerCalificacionClick = {}
    )
}
