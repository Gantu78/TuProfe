package com.example.tuprofe.ui.mapa

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

private const val MAP_ID_LIGHT = "bf3da9be2a1ab1b22986850b"

@Composable
fun MapaScreen(
    viewModel: MapaViewModel = hiltViewModel(),
    onReviewClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    val colombiaLatLng = LatLng(4.6097, -74.0817)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(colombiaLatLng, 12f)
    }

    val navigateToMarker = uiState.navigateToMarker
    LaunchedEffect(navigateToMarker) {
        navigateToMarker?.let { marker ->
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(
                    LatLng(marker.latitude, marker.longitude),
                    15f
                ),
                durationMs = 800
            )
            viewModel.onNavigationConsumed()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            googleMapOptionsFactory = {
                GoogleMapOptions().mapId(MAP_ID_LIGHT)
            },
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                myLocationButtonEnabled = false,
                compassEnabled = false,
                mapToolbarEnabled = false
            ),
            onMapClick = {
                viewModel.onDismissMarker()
                if (uiState.showReviewList) viewModel.toggleReviewList()
            }
        ) {
            uiState.markers.forEach { marker ->
                Marker(
                    state = MarkerState(position = LatLng(marker.latitude, marker.longitude)),
                    title = marker.profesorNombre,
                    snippet = "Rating: ${marker.rating}/5",
                    onClick = {
                        viewModel.onMarkerSelected(marker)
                        true
                    }
                )
            }
        }

        // Loading
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        // Error
        uiState.error?.let {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp, start = 16.dp, end = 16.dp),
                action = {
                    TextButton(onClick = { viewModel.loadMarkers() }) { Text("Reintentar") }
                }
            ) { Text(it) }
        }

        // Botón refresh
        FloatingActionButton(
            onClick = { viewModel.loadMarkers() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(6.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Actualizar")
        }

        // Chip contador + lista desplegable
        if (!uiState.isLoading && uiState.markers.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 16.dp, start = 16.dp)
            ) {
                Surface(
                    modifier = Modifier.clickable { viewModel.toggleReviewList() },
                    shape = RoundedCornerShape(20.dp),
                    color = if (uiState.showReviewList)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surface,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${uiState.markers.size} reseñas hoy",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (uiState.showReviewList)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else
                                MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = if (uiState.showReviewList) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = if (uiState.showReviewList)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else
                                MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                AnimatedVisibility(
                    visible = uiState.showReviewList,
                    enter = fadeIn(tween(200)) + expandVertically(tween(250), expandFrom = Alignment.Top),
                    exit = fadeOut(tween(150)) + shrinkVertically(tween(200), shrinkTowards = Alignment.Top)
                ) {
                    ReviewListDropdown(
                        markers = uiState.markers,
                        onItemClick = { marker -> viewModel.onReviewListItemClick(marker) },
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
        }

        // Tarjeta al tocar marcador
        AnimatedVisibility(
            visible = uiState.selectedMarker != null,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(tween(300)) { it } + fadeIn(tween(250)),
            exit = slideOutVertically(tween(250)) { it } + fadeOut(tween(200))
        ) {
            uiState.selectedMarker?.let { marker ->
                MarkerInfoCard(
                    marker = marker,
                    onDismiss = { viewModel.onDismissMarker() },
                    onClick = { onReviewClick(marker.reviewId) }
                )
            }
        }
    }
}

@Composable
private fun ReviewListDropdown(
    markers: List<ReviewMapMarker>,
    onItemClick: (ReviewMapMarker) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(240.dp)
            .heightIn(max = 280.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        LazyColumn {
            itemsIndexed(markers) { index, marker ->
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(marker) }
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = marker.profesorNombre,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (marker.materia.isNotBlank()) {
                                Text(
                                    text = marker.materia,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFB300),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${marker.rating}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    if (index < markers.lastIndex) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                            thickness = 0.5.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MarkerInfoCard(
    marker: ReviewMapMarker,
    onDismiss: () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 90.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 12.dp,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = marker.profesorNombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                TextButton(
                    onClick = onDismiss,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("✕", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 16.sp)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(5) { index ->
                    Icon(
                        imageVector = if (index < marker.rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = null,
                        tint = if (index < marker.rating) Color(0xFFFFB300) else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${marker.rating}/5",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
            ) {
                Text(
                    text = "📍 Reseña reciente",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}
