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
import kotlinx.coroutines.delay

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReviewScreen(
    viewModel: CreateReviewViewModel = hiltViewModel(),
    onSuccess: () -> Unit = {},
    modifier: Modifier = Modifier
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

    LaunchedEffect(Unit) {
        locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    LaunchedEffect(state.success) {
        if (state.success) {
            onSuccess()
            viewModel.resetSuccess()
        }
    }

    // Staggered entrance for the whole form
    var showTitle   by remember { mutableStateOf(false) }
    var showForm    by remember { mutableStateOf(false) }
    var showActions by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        showTitle = true; delay(120)
        showForm = true;  delay(120)
        showActions = true
    }

    Box(modifier = modifier.fillMaxSize()) {
        BackgroundImage()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedScreen(delayMs = 0) {
                Text(
                    text = stringResource(R.string.crear_rese_a),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            }

            AnimatedScreen(delayMs = 100) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // ── Professor selector ────────────────────────────────────
                    ExposedDropdownMenuBox(
                        expanded = state.isDropdownExpanded,
                        onExpandedChange = { viewModel.toggleDropdown() }
                    ) {
                        TextFieldApp(
                            texto = stringResource(R.string.buscar_profesor),
                            value = state.professorQuery,
                            onValueChange = { viewModel.onProfessorQueryChange(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            trailingIcon = {
                                Icon(
                                    imageVector = if (state.isDropdownExpanded)
                                        Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = state.isDropdownExpanded,
                            onDismissRequest = { viewModel.onDismissDropdown() }
                        ) {
                            state.filteredProfessors.forEach { professor ->
                                DropdownMenuItem(
                                    text = { Text(professor.nombreProfe) },
                                    onClick = { viewModel.onProfessorSelected(professor) }
                                )
                            }
                        }
                    }

                    // ── Materia selector (visible only after professor selected) ──
                    if (state.selectedProfessor != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        ExposedDropdownMenuBox(
                            expanded = state.isMateriaDropdownExpanded,
                            onExpandedChange = { viewModel.toggleMateriaDropdown() }
                        ) {
                            TextFieldApp(
                                texto = stringResource(R.string.seleccionar_materia),
                                value = state.selectedMateria,
                                onValueChange = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                trailingIcon = {
                                    Icon(
                                        imageVector = if (state.isMateriaDropdownExpanded)
                                            Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                        contentDescription = null
                                    )
                                }
                            )
                            ExposedDropdownMenu(
                                expanded = state.isMateriaDropdownExpanded,
                                onDismissRequest = { viewModel.onDismissMateriaDropdown() }
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

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Star rating with bounce animation ─────────────────────
                    Text(
                        text = stringResource(R.string.calificaci_n),
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(modifier = Modifier.padding(vertical = 8.dp)) {
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
                                    .clickable {
                                        viewModel.onRatingChange(ratingValue)
                                    }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    TextFieldApp(
                        texto = stringResource(R.string.tu_opini_n_sobre_el_profesor),
                        value = state.reviewText,
                        onValueChange = { viewModel.onReviewTextChange(it) },
                        singleLine = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Image picker toolbar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            enabled = state.selectedImageUris.size < 4
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "Agregar imágenes",
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
                                            .border(1.dp, colorResource(R.color.BordeTuProfe), RoundedCornerShape(8.dp))
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
                                            contentDescription = "Eliminar imagen",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedScreen(delayMs = 200) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (state.isLoading) {
                        CircularProgressIndicator(color = colorResource(R.color.verdetp))
                    } else {
                        AppButton(
                            textoBoton = stringResource(R.string.publicar_rese_a),
                            onClick = { viewModel.createReview() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .pressScaleEffect()
                        )
                    }
                    state.error?.let { errRes ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = stringResource(errRes), color = Color.Red, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
