package com.example.tuprofe.ui


import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.ui.res.colorResource
import com.example.tuprofe.R
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.Resena


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onResenaClick: (Int) -> Unit = { println("Clicked on review $it") },
    onProfileClick: () -> Unit = { println("Clicked on profile") },
    onTeachersClick: () -> Unit = { println("Clicked on teachers") },
    onHomeClick: () -> Unit = { println("Clicked on home") }
) {

    val allReviews = LocalReview.Reviews

    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {

        BackgroundImage()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 5.dp,
                bottom = 20.dp
            )
        ) {

            items(allReviews) { review ->
                ResenaCard(
                    reviewInfo = review,
                    onCommentsClick = { onResenaClick(review.imageId) })
            }
        }
    }

}

@Composable
fun ResenaCard(
    reviewInfo: ReviewInfo,
    modifier: Modifier = Modifier,
    onCommentsClick: () -> Unit = {}

) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 20.dp),
        shape = RoundedCornerShape(30.dp),
        border = BorderStroke(
            width = 2.5.dp,
            color = colorResource(R.color.BordeTuProfe)
        ),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.pastel)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
            pressedElevation = 30.dp,
            hoveredElevation = 10.dp,
            focusedElevation = 10.dp,
            disabledElevation = 10.dp
        )
    ) {
        Resena(
            reviewInfo = reviewInfo,
            onCommentsClick = onCommentsClick
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}











