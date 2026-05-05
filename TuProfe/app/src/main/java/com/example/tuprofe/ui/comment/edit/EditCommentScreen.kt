package com.example.tuprofe.ui.comment.edit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tuprofe.R
import com.example.tuprofe.ui.utils.AnimatedScreen
import com.example.tuprofe.ui.utils.AppButton
import com.example.tuprofe.ui.utils.BackgroundImage
import com.example.tuprofe.ui.utils.TextFieldApp
import com.example.tuprofe.ui.utils.pressScaleEffect

@Composable
fun EditCommentScreen(
    modifier: Modifier = Modifier,
    viewModel: EditCommentViewModel = hiltViewModel(),
    onSuccess: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

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
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Editar Comentario",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 24.dp)
                        )

                        TextFieldApp(
                            texto = "Tu comentario...",
                            value = state.commentText,
                            onValueChange = { viewModel.onCommentTextChange(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        if (state.isLoading) {
                            CircularProgressIndicator(color = colorResource(R.color.verdetp))
                        } else {
                            AppButton(
                                textoBoton = "GUARDAR CAMBIOS",
                                onClick = { viewModel.updateComment() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .pressScaleEffect()
                            )
                        }

                        state.error?.let {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = it, color = Color.Red, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}
