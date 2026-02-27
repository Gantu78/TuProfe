package com.example.tuprofe.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tuprofe.R
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.Resena

@Composable
fun DetalleScreen (

    onLike: () -> Unit,
    onShare: () -> Unit,
    onComment: () -> Unit,
    ReviewInfo: ReviewInfo,
    responseReviews: List<ReviewInfo>,
    modifier: Modifier = Modifier

) {
    Box() {
        BackgroundImage()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp),
            contentPadding = PaddingValues(
                top = 10.dp,
                bottom = 0.dp
            )
        ) {

            item {
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 0.dp, horizontal = 0.dp),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(
                        width = 2.5.dp,
                        color = colorResource(R.color.BordeTuProfe),
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
                    onClick = {},

                ) {
                    Resena(
                        reviewInfo = ReviewInfo
                    )
                }
                HorizontalDivider(thickness = 1.dp)
                ReviewActionBar(
                    onLike = {/*TODO¨*/ },
                    onComment = {/*TODO¨*/ },
                    onShare = {/*TODO¨*/ }
                )
                HorizontalDivider(thickness = 1.dp)
                Text(
                    "Comentarios mas relevantes",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 6.dp, bottom = 6.dp),
                    color = colorResource(R.color.verdetp)
                )
            }
            items(responseReviews.size) {
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
                        MaterialTheme.colorScheme.surface
                    ),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp,
                        pressedElevation = 30.dp,
                        hoveredElevation = 10.dp,
                        focusedElevation = 10.dp,
                        disabledElevation = 10.dp
                    ),
                    onClick = {}
                ) {
                    Resena(
                        reviewInfo = responseReviews[it],
                        modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
                    )
                }
                HorizontalDivider(thickness = 1.dp)

            }

        }

    }

}

@Preview(showBackground = true)
@Composable
fun DetalleScreenPreview(){
    val review = LocalReview.Reviews[0]
    val respuestas = LocalReview.Reviews

    DetalleScreen(
        onLike = {},
        onComment = {},
        onShare = {},
        ReviewInfo = review,
        responseReviews = respuestas

    )
}

@Composable
fun ReviewActionBar(
    onLike: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        IconButton(onClick = onLike) {
            Icon(imageVector = Icons.Outlined.ThumbUp, contentDescription = "Like", tint = colorResource(R.color.verdetp))
        }
        IconButton(onClick = onComment) {
            Icon(imageVector = Icons.Outlined.Comment, contentDescription = "Comment", tint = colorResource(R.color.verdetp))
        }
        IconButton(onClick = onShare) {

            Icon(imageVector = Icons.Outlined.Share, contentDescription = "Share", tint = colorResource(R.color.verdetp))
        }

    }

}

@Preview(showBackground = true)
@Composable
fun ReviewActionBarPreview(){
    ReviewActionBar(
        onLike = {},
        onComment = {},
        onShare = {}
    )
}