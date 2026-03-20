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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.tuprofe.R
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.Historia.HistorialViewModel
import com.example.tuprofe.ui.utils.AppButton
import com.example.tuprofe.ui.utils.AppButtonRow
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.RatingStars

@Composable
fun HistorialScreen(
    historialViewModel: HistorialViewModel,
    onVerCalificacionClick: (ReviewInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state by historialViewModel.uiState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                top = 24.dp,
                bottom = 80.dp
            )
        ) {

            item {
                HistorialHeader(
                    onFilterClick = { historialViewModel.onFilterClick("")} //mientras establecemos que filtros
                )
            }

            items(state.userReviews) { review ->
                HistorialCard(
                    review = review,
                    onVerCalificacionClick = onVerCalificacionClick
                )
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
                .height(56.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.texto_calificaciones),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = colorResource(R.color.BordeTuProfe),
            modifier = Modifier.fillMaxWidth()
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
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(1.dp, colorResource(R.color.BordeTuProfe)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfesorAvatar(imageUrl = review.profesor.imageprofeUrl)

            Spacer(modifier = Modifier.width(14.dp))

            HistorialCardBody(
                review = review,
                onVerCalificacionClick = { onVerCalificacionClick(review) }
            )
        }
    }
}


@Composable
private fun ProfesorAvatar(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = stringResource(R.string.foto_de_perfil),
        placeholder = painterResource(R.drawable.loading_img),
        error = painterResource(R.drawable.avatar),
        modifier = modifier.size(72.dp).clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfesorAvatarPreview() {
    ProfesorAvatar(imageUrl = " https://img.lalr.co/cms/2017/06/16184524/1280x1440_CARLOS-PARRA.jpg?r=6_5&ns=1&w=372&d=2.625")
}


@Composable
private fun HistorialCardBody(
    review: ReviewInfo,
    onVerCalificacionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = review.profesor.nombreProfe,
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            RatingStars(rating = review.rating, modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = review.materia.nombreMateria,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        AppButtonRow(
            textoBoton = stringResource(R.string.ver_rese_a),
            onClick = onVerCalificacionClick,
            modifier = Modifier.height(34.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HistorialCardBodyPreview() {
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
        historialViewModel = viewModel(),
        onVerCalificacionClick = {}
    )
}
