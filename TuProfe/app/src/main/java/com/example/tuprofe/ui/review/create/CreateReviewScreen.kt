package com.example.tuprofe.ui.review.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tuprofe.R
import com.example.tuprofe.ui.utils.AppButton
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.TextFieldApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReviewScreen(
    viewModel: CreateReviewViewModel = hiltViewModel(),
    onSuccess: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.crear_rese_a),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 24.dp)
            )

            // Selector de Profesor con Autocompletado
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
                            imageVector = if (state.isDropdownExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
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

            Spacer(modifier = Modifier.height(20.dp))

            // Selector de Calificación (Estrellas)
            Text(text = stringResource(R.string.calificaci_n), fontWeight = FontWeight.Medium)
            Row(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                repeat(5) { index ->
                    val ratingValue = index + 1
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (state.rating >= ratingValue) colorResource(R.color.verdetp) else Color.LightGray,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { viewModel.onRatingChange(ratingValue) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            TextFieldApp(
                texto = stringResource(R.string.tu_opini_n_sobre_el_profesor),
                value = state.reviewText,
                onValueChange = { viewModel.onReviewTextChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (state.isLoading) {
                CircularProgressIndicator(color = colorResource(R.color.verdetp))
            } else {
                AppButton(
                    textoBoton = stringResource(R.string.publicar_rese_a),
                    onClick = { viewModel.createReview() },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            state.error?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = it, color = Color.Red, fontSize = 14.sp)
            }
        }
    }
}
