package com.example.tuprofe.ui.profe

import com.example.tuprofe.R
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.client.OpenAIHost
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.example.tuprofe.BuildConfig
import com.example.tuprofe.data.repository.ProfessorRepository
import com.example.tuprofe.data.repository.ReviewRepository
import com.example.tuprofe.data.repository.applyModerationFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfeViewModel @Inject constructor(
    private val professorRepository: ProfessorRepository,
    private val reviewRepository: ReviewRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val profeId: String = checkNotNull(savedStateHandle["profeId"])
    private val _uiState = MutableStateFlow(ProfeState())
    val uiState: StateFlow<ProfeState> = _uiState.asStateFlow()

    // Configuración GROQ (GROQ ES LA IA PARA EL RESUMEN)
    private val groqClient = OpenAI(
        config = OpenAIConfig(
            token = BuildConfig.IAAPIKEY,
            host = OpenAIHost("https://api.groq.com/openai/v1/")
        )
    )

    init {
        cargarDatos(profeId)
    }

    private fun cargarDatos(profeId: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val professorResult = professorRepository.getProfessorById(profeId)
            val reviewsResult = reviewRepository.getReviews()

            if (professorResult.isSuccess && reviewsResult.isSuccess) {
                val professor = professorResult.getOrNull()
                val allReviews = reviewsResult.getOrNull() ?: emptyList()
                
                // Filtrar reseñas para el profesor + moderación
                val filteredReviews = allReviews
                    .filter { it.profesor.profeId == profeId }
                    .applyModerationFilter()

                val average = if (filteredReviews.isNotEmpty()) {
                    val raw = filteredReviews.map { it.rating }.average()
                    (Math.round(raw * 10) / 10.0).toFloat()
                } else 0f

                _uiState.update {
                    it.copy(
                        profesor = professor,
                        professorReviews = filteredReviews,
                        averageRating = average,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { 
                    it.copy(
                        isLoading = false
                    ) 
                }
            }
        }
    }

    fun generarResumenIA() {
        val reseñasText = uiState.value.professorReviews
            .joinToString("\n") { "- ${it.content}" }

        if (reseñasText.isBlank()) {
            _uiState.update { it.copy(errorIA = R.string.no_hay_resenas_suficientes) }
            return
        }

        _uiState.update { it.copy(isLoadingIA = true, errorIA = null) }

        viewModelScope.launch {
            try {
                val chatCompletionRequest = ChatCompletionRequest(
                    model = ModelId("llama-3.3-70b-versatile"),
                    messages = listOf(
                        ChatMessage(
                            role = ChatRole.System,
                            content = "Eres un asistente experto en analizar reseñas de profesores. Resume los puntos clave (lo bueno y lo malo) de forma muy concisa y objetiva en español. Usa un tono profesional."
                        ),
                        ChatMessage(
                            role = ChatRole.User,
                            content = "Basado en estas reseñas de alumnos, genera un resumen corto:\n\n$reseñasText"
                        )
                    )
                )
                
                val completion = groqClient.chatCompletion(chatCompletionRequest)
                val resumen = completion.choices.firstOrNull()?.message?.content

                _uiState.update { 
                    it.copy(
                        resumenIA = resumen,
                        isLoadingIA = false 
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoadingIA = false, 
                        errorIA = R.string.error_al_generar_resumen
                    ) 
                }
            }
        }
    }
}
