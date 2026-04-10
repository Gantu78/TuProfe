package com.example.tuprofe.ui.Main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuprofe.R
import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.Resena

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onResenaClick: (String) -> Unit,
    onProfileClick: (Profesor) -> Unit,
    mainViewModel: MainViewModel
) {
    val uiState by mainViewModel.uiState.collectAsState()
    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 10.dp, bottom = 0.dp)
            ) {
                items(uiState.reviews) { review ->
                    ResenaCard(
                        reviewInfo = review,
                        onCommentsClick = { onResenaClick(review.reviewId) },
                        onProfileClick = { onProfileClick(review.profesor) }
                    )
                }
            }
        }
    }
}

@Composable
fun ResenaCard(
    reviewInfo: ReviewInfo,
    modifier: Modifier = Modifier,
    onCommentsClick: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 18.dp),
        shape = RoundedCornerShape(30.dp),
        border = BorderStroke(
            width = 2.5.dp,
            color = colorResource(R.color.BordeTuProfe)
        ),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        onClick = { onCommentsClick(reviewInfo.reviewId) }
    ) {
        Resena(
            reviewInfo = reviewInfo,
            onProfileClick = onProfileClick
        )
    }
}

@Preview
@Composable
fun ResenaCardPreview() {
    ResenaCard(
        reviewInfo = LocalReview.Reviews[0],
        onCommentsClick = {},
        onProfileClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        onResenaClick = {},
        onProfileClick = {},
        mainViewModel = viewModel()
    )
}
