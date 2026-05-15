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
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import com.google.android.gms.maps.GoogleMapOptions
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tuprofe.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlin.math.abs
import kotlin.math.log2
import kotlin.math.pow
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.unit.sp

private const val MAP_ID_LIGHT = "bf3da9be2a1ab1b22986850b"

private data class MarkerGroup(
    val markers: List<ReviewMapMarker>,
    val centerLat: Double,
    val centerLng: Double
) {
    val count = markers.size
    val representative = markers.first()
}

private fun zoomToDissolve(group: MarkerGroup): Float {
    var maxDist = 0.0
    val markers = group.markers
    for (i in markers.indices) {
        for (j in i + 1 until markers.size) {
            val dist = maxOf(
                abs(markers[i].latitude - markers[j].latitude),
                abs(markers[i].longitude - markers[j].longitude)
            )
            if (dist > maxDist) maxDist = dist
        }
    }
    return if (maxDist <= 0.0) 21f
    else (log2(40.0 / maxDist).toFloat() + 0.5f).coerceAtMost(21f)
}

private fun groupMarkers(markers: List<ReviewMapMarker>, zoom: Float): List<MarkerGroup> {
    val threshold = 40.0 / 2.0.pow(zoom.toDouble())
    val assigned = BooleanArray(markers.size)
    val groups = mutableListOf<MarkerGroup>()
    for (i in markers.indices) {
        if (assigned[i]) continue
        val group = mutableListOf(markers[i])
        assigned[i] = true
        for (j in i + 1 until markers.size) {
            if (assigned[j]) continue
            if (abs(markers[i].latitude - markers[j].latitude) < threshold &&
                abs(markers[i].longitude - markers[j].longitude) < threshold) {
                group.add(markers[j])
                assigned[j] = true
            }
        }
        groups.add(MarkerGroup(
            markers  = group,
            centerLat = group.sumOf { it.latitude } / group.size,
            centerLng = group.sumOf { it.longitude } / group.size
        ))
    }
    return groups
}

@Composable
fun MapaScreen(
    viewModel: MapaViewModel = hiltViewModel(),
    onReviewClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDarkTheme = isSystemInDarkTheme()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var locationGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> locationGranted = granted }

    val colombiaLatLng = LatLng(4.6097, -74.0817)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(colombiaLatLng, 12f)
    }

    LaunchedEffect(Unit) {
        if (!locationGranted) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    var userLocation by remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(locationGranted) {
        if (locationGranted) {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            fusedClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    userLocation = latLng
                    cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }
        }
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

        key(isDarkTheme) {
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isDarkTheme) Color(0xFF242f3e) else Color(0xFFE8EFE9)),
                cameraPositionState = cameraPositionState,
                googleMapOptionsFactory = {
                    if (!isDarkTheme) GoogleMapOptions().mapId(MAP_ID_LIGHT)
                    else GoogleMapOptions()
                },
                properties = if (isDarkTheme) MapProperties(
                    mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_dark),
                    isMyLocationEnabled = locationGranted
                ) else MapProperties(
                    isMyLocationEnabled = locationGranted
                ),
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
                val groups by remember(uiState.markers) {
                    derivedStateOf { groupMarkers(uiState.markers, cameraPositionState.position.zoom) }
                }
                groups.forEach { group ->
                    key(group.centerLat, group.centerLng) {
                        if (group.count == 1) {
                            MarkerComposable(
                                state = MarkerState(position = LatLng(group.representative.latitude, group.representative.longitude)),
                                onClick = {
                                    viewModel.onMarkerSelected(group.representative)
                                    scope.launch {
                                        cameraPositionState.animate(
                                            CameraUpdateFactory.newLatLngZoom(
                                                LatLng(group.representative.latitude, group.representative.longitude),
                                                cameraPositionState.position.zoom.coerceAtLeast(17f)
                                            ),
                                            durationMs = 700
                                        )
                                    }
                                    true
                                }
                            ) {
                                GroupMarkerBadge(count = 0)
                            }
                        } else {
                            val center = LatLng(group.centerLat, group.centerLng)
                            MarkerComposable(
                                state = MarkerState(position = center),
                                onClick = {
                                    viewModel.onGroupSelected(group.markers)
                                    scope.launch {
                                        cameraPositionState.animate(
                                            CameraUpdateFactory.newLatLngZoom(center, zoomToDissolve(group)),
                                            durationMs = 700
                                        )
                                    }
                                    true
                                }
                            ) {
                                GroupMarkerBadge(count = group.count)
                            }
                        }
                    }
                }
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
                        userLocation = userLocation,
                        onItemClick = { marker -> viewModel.onReviewListItemClick(marker) },
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
        }

        // Speed dial FAB
        MapControlsFab(
            cameraPositionState = cameraPositionState,
            locationGranted = locationGranted,
            context = context,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 16.dp, end = 16.dp)
        )

        // Tarjeta al tocar marcador individual
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

        // Carousel al tocar grupo
        AnimatedVisibility(
            visible = uiState.selectedGroup != null,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(tween(300)) { it } + fadeIn(tween(250)),
            exit = slideOutVertically(tween(250)) { it } + fadeOut(tween(200))
        ) {
            uiState.selectedGroup?.let { group ->
                MarkerCarouselCard(
                    markers = group,
                    onDismiss = { viewModel.onDismissMarker() },
                    onReviewClick = onReviewClick
                )
            }
        }
    }
}

@Composable
private fun MapControlsFab(
    cameraPositionState: CameraPositionState,
    locationGranted: Boolean,
    context: Context,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(tween(150)) + expandVertically(tween(200), expandFrom = Alignment.Bottom),
            exit = fadeOut(tween(100)) + shrinkVertically(tween(150), shrinkTowards = Alignment.Bottom)
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (locationGranted) {
                    MapFabItem(
                        icon = Icons.Default.MyLocation,
                        label = "Mi ubicación",
                        onClick = {
                            expanded = false
                            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
                            fusedClient.lastLocation.addOnSuccessListener { location ->
                                location?.let {
                                    scope.launch {
                                        cameraPositionState.animate(
                                            CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 15f),
                                            durationMs = 800
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
                MapFabItem(
                    icon = Icons.Default.Add,
                    label = "Acercar",
                    onClick = { scope.launch { cameraPositionState.animate(CameraUpdateFactory.zoomIn(), 300) } }
                )
                MapFabItem(
                    icon = Icons.Default.Remove,
                    label = "Alejar",
                    onClick = { scope.launch { cameraPositionState.animate(CameraUpdateFactory.zoomOut(), 300) } }
                )
            }
        }

        FloatingActionButton(
            onClick = { expanded = !expanded },
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(6.dp)
        ) {
            AnimatedContent(
                targetState = expanded,
                transitionSpec = {
                    fadeIn(tween(150)) togetherWith fadeOut(tween(150))
                },
                label = "fabIcon"
            ) { isExpanded ->
                Icon(
                    imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Tune,
                    contentDescription = "Controles del mapa"
                )
            }
        }
    }
}

@Composable
private fun MapFabItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp
        ) {
            Text(
                text = label,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        SmallFloatingActionButton(
            onClick = onClick,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(4.dp)
        ) {
            Icon(imageVector = icon, contentDescription = label, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun GroupMarkerBadge(count: Int) {
    Box(modifier = Modifier.size(if (count > 0) 48.dp else 44.dp)) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = Color(0xFFE53935),
            modifier = Modifier
                .size(44.dp)
                .align(Alignment.BottomCenter)
        )
        if (count > 0) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.TopEnd)
                    .background(Color(0xFF43A047), CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (count > 9) "9+" else "$count",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    lineHeight = 9.sp
                )
            }
        }
    }
}

private fun Modifier.scrollbar(
    state: LazyListState,
    thumbColor: Color,
    trackColor: Color
): Modifier = drawWithContent {
    drawContent()
    val info = state.layoutInfo
    val totalItems = info.totalItemsCount
    val visible = info.visibleItemsInfo
    if (totalItems == 0 || visible.isEmpty()) return@drawWithContent
    val viewportH = (info.viewportEndOffset - info.viewportStartOffset).toFloat()
    val avgH = visible.sumOf { it.size } / visible.size.toFloat()
    val totalH = totalItems * avgH
    if (totalH <= viewportH) return@drawWithContent
    val w = 3.dp.toPx()
    val thumbH = (viewportH * viewportH / totalH).coerceAtLeast(24.dp.toPx())
    val scrolled = state.firstVisibleItemIndex * avgH + state.firstVisibleItemScrollOffset
    val thumbTop = (scrolled / (totalH - viewportH)) * (viewportH - thumbH)
    drawRoundRect(
        color = trackColor,
        topLeft = Offset(size.width - w, 0f),
        size = Size(w, viewportH),
        cornerRadius = CornerRadius(w / 2)
    )
    drawRoundRect(
        color = thumbColor,
        topLeft = Offset(size.width - w, thumbTop),
        size = Size(w, thumbH),
        cornerRadius = CornerRadius(w / 2)
    )
}

private fun formatDistance(meters: Float): String =
    if (meters < 1000f) "${meters.toInt()} m" else "${"%.1f".format(meters / 1000f)} km"

@Composable
private fun MarkerCarouselCard(
    markers: List<ReviewMapMarker>,
    onDismiss: () -> Unit,
    onReviewClick: (String) -> Unit
) {
    val pagerState = rememberPagerState { markers.size }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 90.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {

            // Handle + indicador X/Y
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .align(Alignment.Center)
                )
                Text(
                    text = "${pagerState.currentPage + 1}/${markers.size}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                val marker = markers[page]
                Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        AsyncImage(
                            model = marker.profesorFotoUrl.takeUnless { it.isNullOrBlank() },
                            contentDescription = marker.profesorNombre,
                            modifier = Modifier.size(52.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(R.drawable.avatar),
                            error = painterResource(R.drawable.avatar)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = marker.profesorNombre,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (marker.materia.isNotBlank()) {
                                Text(
                                    text = marker.materia,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(5) { index ->
                                    Icon(
                                        imageVector = if (index < marker.rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                        contentDescription = null,
                                        tint = if (index < marker.rating) Color(0xFFFFB300) else MaterialTheme.colorScheme.outlineVariant,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "${marker.rating}.0",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { onDismiss() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onReviewClick(marker.reviewId) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Ver reseña completa", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }

            // Dots
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(markers.size) { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(if (index == pagerState.currentPage) 8.dp else 5.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == pagerState.currentPage)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.outlineVariant
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun ReviewListDropdown(
    markers: List<ReviewMapMarker>,
    userLocation: LatLng?,
    onItemClick: (ReviewMapMarker) -> Unit,
    modifier: Modifier = Modifier
) {
    val sortedMarkers = remember(markers, userLocation) {
        if (userLocation == null) markers
        else markers.sortedBy { marker ->
            val result = FloatArray(1)
            android.location.Location.distanceBetween(
                userLocation.latitude, userLocation.longitude,
                marker.latitude, marker.longitude,
                result
            )
            result[0]
        }
    }

    val listState = rememberLazyListState()
    val thumbColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
    val trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)

    Card(
        modifier = modifier
            .width(240.dp)
            .heightIn(max = 280.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.scrollbar(listState, thumbColor, trackColor)
        ) {
            itemsIndexed(sortedMarkers) { index, marker ->
                val distanceText = userLocation?.let {
                    val result = FloatArray(1)
                    android.location.Location.distanceBetween(
                        it.latitude, it.longitude,
                        marker.latitude, marker.longitude,
                        result
                    )
                    formatDistance(result[0])
                }
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
                            if (distanceText != null) {
                                Text(
                                    text = distanceText,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    maxLines = 1
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
                    if (index < sortedMarkers.lastIndex) {
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
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 90.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {

            // Handle
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outlineVariant)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Avatar
                AsyncImage(
                    model = marker.profesorFotoUrl.takeUnless { it.isNullOrBlank() },
                    contentDescription = marker.profesorNombre,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(R.drawable.avatar),
                    error = painterResource(R.drawable.avatar)
                )

                // Nombre, materia y estrellas
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = marker.profesorNombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (marker.materia.isNotBlank()) {
                        Text(
                            text = marker.materia,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < marker.rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                contentDescription = null,
                                tint = if (index < marker.rating) Color(0xFFFFB300) else MaterialTheme.colorScheme.outlineVariant,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "${marker.rating}.0",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Botón cerrar
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Ver reseña completa",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
