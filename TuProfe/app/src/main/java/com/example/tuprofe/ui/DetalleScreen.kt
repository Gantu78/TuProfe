package com.example.tuprofe.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tuprofe.R
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.utils.Resena

@Composable
fun DetalleScreen(
    onLike: () -> Unit,
    onShare: () -> Unit,
    onComment: () -> Unit,
    ReviewInfo: ReviewInfo,
    responseReviews: List<ReviewInfo>,
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(
            top = 16.dp,
            bottom = 120.dp
        )
    ) {

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(
                    1.5.dp,
                    colorResource(R.color.BordeTuProfe)
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {

                Column {

                    Resena(
                        reviewInfo = ReviewInfo,
                        onProfileClick = onProfileClick
                    )

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    ReviewActionBar(
                        onLike = onLike,
                        onComment = onComment,
                        onShare = onShare
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 12.dp)
            ) {

                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(22.dp)
                        .background(colorResource(R.color.verdetp))
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = stringResource(R.string.comentarios_m_s_relevantes),
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        items(responseReviews) { review ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(
                    1.dp,
                    colorResource(R.color.BordeTuProfe)
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {

                Resena(
                    reviewInfo = review,
                    modifier = Modifier.padding(vertical = 8.dp),
                    onProfileClick = onProfileClick
                )
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
        responseReviews = respuestas,
        onProfileClick = {}

    )
}

@Composable
fun ReviewActionBar(
    onLike: () -> Unit,
    onComment: () -> Unit,
    onShare: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        IconButton(onClick = onLike) {
            Icon(
                imageVector = Icons.Outlined.ThumbUp,
                contentDescription = "Like",
                tint = colorResource(R.color.verdetp)
            )
        }

        IconButton(onClick = onComment) {
            Icon(
                imageVector = Icons.Outlined.Comment,
                contentDescription = "Comment",
                tint = colorResource(R.color.verdetp)
            )
        }

        IconButton(onClick = onShare) {
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = "Share",
                tint = colorResource(R.color.verdetp)
            )
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