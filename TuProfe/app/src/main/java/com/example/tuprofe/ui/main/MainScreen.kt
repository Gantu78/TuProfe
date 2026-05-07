package com.example.tuprofe.ui.main

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuprofe.R
import com.example.tuprofe.data.Profesor
import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.local.LocalReview
import com.example.tuprofe.ui.utils.AnimatedListItem
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.Resena
import com.example.tuprofe.ui.utils.ReviewListSkeleton
import com.example.tuprofe.ui.utils.pressScaleEffect

private val tabs = listOf("Para ti", "Siguiendo")

@Composable
fun MainScreen(
    modifier: Modifier = Modifier.testTag("mainScreen"),
    onResenaClick: (String) -> Unit,
    onProfileClick: (Profesor) -> Unit,
    onUserClick: (String) -> Unit,
    mainViewModel: MainViewModel
) {
    val uiState by mainViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.fetchReviews()
    }

    LaunchedEffect(uiState.selectedTab) {
        if (uiState.selectedTab == 1) mainViewModel.refreshFollowingReviews()
    }

    when {
        uiState.isLoading -> {
            // Shimmer skeletons instead of a spinner
            Box(modifier = modifier.fillMaxSize()) {
                BackgroundImage()
                ReviewListSkeleton(count = 5)
            }
        }

        uiState.errorMessage != null -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = uiState.errorMessage ?: "Error desconocido")
            }
        }

        else -> {
            Box(modifier = modifier.fillMaxSize()) {
                BackgroundImage()

                Column(modifier = Modifier.fillMaxSize()) {
                    FeedTabBar(
                        selectedTab = uiState.selectedTab,
                        onTabSelected = { mainViewModel.selectTab(it) }
                    )

                    // AnimatedContent for smooth tab switching
                    AnimatedContent(
                        targetState = uiState.selectedTab,
                        transitionSpec = {
                            val dir = if (targetState > initialState) 1 else -1
                            (slideInHorizontally(tween(280)) { dir * it / 4 } +
                                    fadeIn(tween(260))) togetherWith
                                    (slideOutHorizontally(tween(220)) { -dir * it / 5 } +
                                            fadeOut(tween(200)))
                        },
                        label = "tabContent"
                    ) { selectedTab ->
                        val currentList =
                            if (selectedTab == 0) uiState.reviews else uiState.followingReviews

                        if (selectedTab == 1 && currentList.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Aún no sigues a nadie\no las personas que sigues no han publicado reseñas",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(32.dp)
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(top = 6.dp, bottom = 100.dp)
                            ) {
                                itemsIndexed(
                                    currentList,
                                    key = { _, review -> review.reviewId }
                                ) { index, review ->
                                    AnimatedListItem(index = index) {
                                        ResenaCard(
                                            reviewInfo = review,
                                            onCommentsClick = { onResenaClick(review.reviewId) },
                                            onProfileClick = { onProfileClick(review.profesor) },
                                            onUserClick = { onUserClick(review.usuario.usuarioId) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeedTabBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    TabRow(
        selectedTabIndex = selectedTab,
        modifier = modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        contentColor = colorResource(R.color.verdetp),
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                height = 2.dp,
                color = colorResource(R.color.verdetp)
            )
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, label ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                modifier = Modifier
                    .height(48.dp)
                    .testTag(if (index == 1) "tab_siguiendo" else "tab_para_ti")
            ) {
                // Animate font weight change on selection
                val weight by animateFloatAsState(
                    targetValue = if (selectedTab == index) 700f else 400f,
                    animationSpec = tween(200),
                    label = "tabWeight"
                )
                Text(
                    text = label,
                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 15.sp,
                    color = if (selectedTab == index)
                        colorResource(R.color.verdetp)
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ResenaCard(
    reviewInfo: ReviewInfo,
    modifier: Modifier = Modifier,
    onCommentsClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onUserClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 18.dp)
            .pressScaleEffect()
            .testTag("review_card"),
        shape = RoundedCornerShape(30.dp),
        border = BorderStroke(
            width = 2.5.dp,
            color = colorResource(R.color.BordeTuProfe)
        ),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        onClick = { onCommentsClick(reviewInfo.reviewId) }
    ) {
        Resena(
            reviewInfo = reviewInfo,
            onProfileClick = onProfileClick,
            onUserClick = onUserClick
        )
    }
}

@Preview
@Composable
fun ResenaCardPreview() {
    ResenaCard(
        reviewInfo = LocalReview.Reviews[0],
        onCommentsClick = {},
        onProfileClick = {},
        onUserClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        onResenaClick = {},
        onProfileClick = {},
        onUserClick = {},
        mainViewModel = viewModel()
    )
}
