package com.example.tuprofe.ui.review.create

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReviewScreen(
    viewModel: CreateReviewViewModel = hiltViewModel(),
    onSuccess: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

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
        if (state.includeLocation) {
            locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(state.success) {
        if (state.success) {
            onSuccess()
            viewModel.resetSuccess()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = 8.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ── Title ────────────────────────────────────────────────────────
            Text(
                text = stringResource(R.string.nueva_resena),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp)
            )

            // ── Professor section ─────────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.profesor),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                if (state.selectedProfessor != null) {
                    // Selected professor chip
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .clip(CircleShape)
                            .background(colorResource(R.color.pastel))
                            .border(1.5.dp, colorResource(R.color.verdetp), CircleShape)
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = state.selectedProfessor!!.imageprofeUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = state.selectedProfessor!!.nombreProfe,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { viewModel.onClearProfessor() },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                } else {
                    // Professor picker capsule
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CircleShape)
                                .background(colorResource(R.color.pastel))
                                .border(
                                    1.5.dp,
                                    colorResource(R.color.BordeTuProfe).copy(alpha = 0.6f),
                                    CircleShape
                                )
                                .clickable { viewModel.toggleDropdown() }
                                .padding(horizontal = 16.dp, vertical = 14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.seleccionar_profesor),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 15.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = if (state.isDropdownExpanded)
                                        Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = colorResource(R.color.verdetp)
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = state.isDropdownExpanded,
                            onDismissRequest = { viewModel.onDismissDropdown() },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            state.filteredProfessors.forEach { professor ->
                                DropdownMenuItem(
                                    text = { Text(professor.nombreProfe) },
                                    onClick = { viewModel.onProfessorSelected(professor) }
                                )
                            }
                        }
                    }
                }
            }

            // ── Materia section (only after professor selected) ───────────────
            if (state.selectedProfessor != null) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = stringResource(R.string.materia),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CircleShape)
                                .background(colorResource(R.color.pastel))
                                .border(
                                    1.5.dp,
                                    colorResource(R.color.BordeTuProfe).copy(alpha = 0.6f),
                                    CircleShape
                                )
                                .clickable { viewModel.toggleMateriaDropdown() }
                                .padding(horizontal = 16.dp, vertical = 14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (state.selectedMateria.isEmpty())
                                        stringResource(R.string.seleccionar_materia)
                                    else
                                        state.selectedMateria,
                                    color = if (state.selectedMateria.isEmpty())
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    else
                                        MaterialTheme.colorScheme.onSurface,
                                    fontSize = 15.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = if (state.isMateriaDropdownExpanded)
                                        Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = colorResource(R.color.verdetp)
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = state.isMateriaDropdownExpanded,
                            onDismissRequest = { viewModel.onDismissMateriaDropdown() },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            state.selectedProfessor!!.materias.forEach { materia ->
                                DropdownMenuItem(
                                    text = { Text(materia) },
                                    onClick = { viewModel.onMateriaSelected(materia) }
                                )
                            }
                        }
                    }
                }
            }

            // ── Rating section ────────────────────────────────────────────────
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.calificaci_n),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
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

            // ── Review text section ───────────────────────────────────────────
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        enabled = state.selectedImageUris.size < 4
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = stringResource(R.string.adjuntar_imagen),
                            tint = if (state.selectedImageUris.size < 4)
                                colorResource(R.color.verdetp) else Color.Gray
                        )
                    }
                    if (state.selectedImageUris.isNotEmpty()) {
                        Text(
                            text = "${state.selectedImageUris.size}/4",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Selected image thumbnails
                if (state.selectedImageUris.isNotEmpty()) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(state.selectedImageUris) { index, uri ->
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
                                    onClick = { viewModel.onRemoveImage(index) },
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

            // ── Location toggle ───────────────────────────────────────────────
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

            // ── Error ─────────────────────────────────────────────────────────
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

            // ── Submit button ─────────────────────────────────────────────────
            if (state.isLoading) {
                CircularProgressIndicator(
                    color = colorResource(R.color.verdetp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                AppButton(
                    textoBoton = stringResource(R.string.publicar_rese_a),
                    onClick = { viewModel.createReview() },
                    enabled = state.canSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .pressScaleEffect()
                )
            }
        }
    }
}