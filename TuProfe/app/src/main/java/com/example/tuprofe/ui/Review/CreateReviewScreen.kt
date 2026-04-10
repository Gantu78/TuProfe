package com.example.tuprofe.ui.Review

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tuprofe.ui.utils.AppButton
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.TextFieldApp

@Composable
fun CreateReviewScreen(
    viewModel: CreateReviewViewModel = viewModel(),
    onSuccess: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.success) {
        if (state.success) {
            onSuccess()
            viewModel.resetSuccess()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        BackgroundImage()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            TextFieldApp(
                texto = "ID del Profesor",
                value = state.professorId,
                onValueChange = { viewModel.onProfessorIdChange(it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldApp(
                texto = "Calificación (1-5)",
                value = if (state.rating == 0) "" else state.rating.toString(),
                onValueChange = { 
                    val rating = it.toIntOrNull() ?: 0
                    viewModel.onRatingChange(rating)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldApp(
                texto = "Escribe tu review aquí",
                value = state.reviewText,
                onValueChange = { viewModel.onReviewTextChange(it) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                AppButton(
                    textoBoton = "Publicar Review",
                    onClick = { viewModel.createReview() },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            state.error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = Color.Red)
            }
        }
    }
}
