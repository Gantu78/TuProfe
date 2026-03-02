package com.example.tuprofe.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.colorResource
import com.example.tuprofe.R
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.Resena


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onResenaClick: (Int) -> Unit,
    onProfileClick: () -> Unit
) {

    val allReviews = LocalReview.Reviews

    Box(
        modifier = modifier
            .fillMaxSize()

    ) {

        BackgroundImage()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                top = 10.dp,
                bottom = 0.dp
            )
        ) {

            items(allReviews) { review ->
                ResenaCard(
                    reviewInfo = review,
                    onCommentsClick = onResenaClick,
                    onProfileClick = onProfileClick
                )
            }
        }
    }

}

@Composable
fun ResenaCard(
    reviewInfo: ReviewInfo,
    modifier: Modifier = Modifier,
    onCommentsClick: (Int) -> Unit,
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
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
            pressedElevation = 30.dp,
            hoveredElevation = 10.dp,
            focusedElevation = 10.dp,
            disabledElevation = 10.dp
        ),
        onClick = {onCommentsClick(reviewInfo.reviewId)}
    ) {
        Resena(
            reviewInfo = reviewInfo,
            onProfileClick = onProfileClick
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        onResenaClick = {},
        onProfileClick = {}
    )
}











