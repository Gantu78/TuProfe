package com.example.tuprofe.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tuprofe.R
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.utils.Resena

@Composable
fun DetalleScreen (

    onLike: () -> Unit,
    onShare: () -> Unit,
    onComment: () -> Unit,
    ReviewInfo: ReviewInfo,
    responseReviews: List<ReviewInfo>,
    modifier: Modifier = Modifier

){

    LazyColumn(
        modifier = modifier
    ) {

        item {
            Resena(
                ReviewInfo
            )
            HorizontalDivider(thickness = 1.dp)
            ReviewActionBar(
                onLike = {/*TODO¨*/},
                onComment = {/*TODO¨*/},
                onShare = {/*TODO¨*/}
            )
            HorizontalDivider(thickness = 1.dp)
            Text(
                "Comentarios mas relevantes",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
            )
        }
        items(responseReviews.size){

            Resena(
                reviewInfo = responseReviews[it],
                modifier = Modifier.padding(top = 6.dp, bottom = 6.dp)
            )
            HorizontalDivider(thickness = 1.dp)

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