package com.example.tuprofe.ui.review.edit

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.tuprofe.R
import com.example.tuprofe.ui.utils.*
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
@Composable
fun EditReviewScreen(
    modifier: Modifier = Modifier,
    viewModel: EditReviewViewModel = hiltViewModel(),
    onSuccess: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) viewModel.onImagesSelected(uris)
    }

    val locationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            LocationServices.getFusedLocationProviderClient(context)
                .lastLocation
                .addOnSuccessListener { location ->
                    viewModel.onLocationReceived(location?.latitude, location?.longitude)
                }
        }
    }

    LaunchedEffect(state.includeLocation) {
        if (state.includeLocation && state.latitude == null) {
            locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(state.success) {
        if (state.success) onSuccess()
    }

    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        when {
            state.isInitialLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = colorResource(R.color.verdetp)
                )
            }
            else -> {
                AnimatedScreen(delayMs = 0) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(top = 8.dp, bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // ── Title ───────────────────────────────────────────
                        Text(
                            text = stringResource(R.string.editar_rese_a),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .padding(top = 16.dp)
                        )

                        // ── Professor name ───────────────────────────────────
                        if (state.professorName.isNotEmpty()) {
                            Text(
                                text = stringResource(R.string.profesor_label, state.professorName),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        }

                        // ── Rating ────────────────────────────────────────────
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = stringResource(R.string.calificaci_n),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                repeat(5) { index ->
                                    val ratingValue = index + 1
                                    val isSelected = state.rating >= ratingValue
                                    val scale = rememberStarBounceScale(selected = isSelected)
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (isSelected) colorResource(R.color.verdetp) else Color.LightGray,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .graphicsLayer { scaleX = scale; scaleY = scale }
                                            .pressScaleEffect()
                                            .clickable { viewModel.onRatingChange(ratingValue) }
                                    )
                                }
                            }
                        }

                        // ── Review text ───────────────────────────────────────
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = stringResource(R.string.tu_resena),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                            TextFieldApp(
                                texto = stringResource(R.string.describe_tu_experiencia),
                                value = state.reviewText,
                                onValueChange = { viewModel.onReviewTextChange(it) },
                                singleLine = false,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .padding(horizontal = 16.dp)
                            )

                            // Image picker toolbar
                            val totalImages = state.existingImageUrls.size + state.newImageUris.size
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { imagePickerLauncher.launch("image/*") },
                                    enabled = totalImages < 4
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Image,
                                        contentDescription = stringResource(R.string.adjuntar_imagen),
                                        tint = if (totalImages < 4)
                                            colorResource(R.color.verdetp) else Color.Gray
                                    )
                                }
                                if (totalImages > 0) {
                                    Text(
                                        text = "$totalImages/4",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Thumbnails: existing (network) + new (local URIs)
                            if (totalImages > 0) {
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 24.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    itemsIndexed(state.existingImageUrls) { _, url ->
                                        Box(modifier = Modifier.size(80.dp)) {
                                            AsyncImage(
                                                model = url,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .border(
                                                        1.dp,
                                                        colorResource(R.color.BordeTuProfe),
                                                        RoundedCornerShape(8.dp)
                                                    )
                                            )
                                            IconButton(
                                                onClick = { viewModel.onRemoveExistingImage(url) },
                                                modifier = Modifier
                                                    .size(22.dp)
                                                    .align(Alignment.TopEnd)
                                                    .clip(CircleShape)
                                                    .background(Color.Black.copy(alpha = 0.6f))
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                    }
                                    itemsIndexed(state.newImageUris) { index, uri ->
                                        Box(modifier = Modifier.size(80.dp)) {
                                            AsyncImage(
                                                model = uri,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .border(
                                                        1.dp,
                                                        colorResource(R.color.BordeTuProfe),
                                                        RoundedCornerShape(8.dp)
                                                    )
                                            )
                                            IconButton(
                                                onClick = { viewModel.onRemoveNewImage(index) },
                                                modifier = Modifier
                                                    .size(22.dp)
                                                    .align(Alignment.TopEnd)
                                                    .clip(CircleShape)
                                                    .background(Color.Black.copy(alpha = 0.6f))
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // ── Location toggle ───────────────────────────────────
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.incluir_ubicacion),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = stringResource(R.string.aparecera_en_mapa),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = state.includeLocation,
                                onCheckedChange = { viewModel.onToggleIncludeLocation(it) },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = colorResource(R.color.verdetp)
                                )
                            )
                        }

                        // ── Error ─────────────────────────────────────────────
                        state.error?.let { errRes ->
                            Text(
                                text = stringResource(errRes),
                                color = Color.Red,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                            )
                        }

                        // ── Save button ───────────────────────────────────────
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                color = colorResource(R.color.verdetp),
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        } else {
                            AppButton(
                                textoBoton = stringResource(R.string.guardar_cambios),
                                onClick = { viewModel.updateReview() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .pressScaleEffect()
                            )
                        }
                    }
                }
            }
        }
    }
}
