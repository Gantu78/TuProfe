package com.example.tuprofe.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.PaymentRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    private val _state = MutableStateFlow<PaymentState>(PaymentState.Idle)
    val state: StateFlow<PaymentState> = _state.asStateFlow()

    fun startPayment() {
        viewModelScope.launch {
            _state.value = PaymentState.Loading
            paymentRepository.createPaymentIntent()
                .onSuccess { clientSecret ->
                    _state.value = PaymentState.ReadyToPay(clientSecret)
                }
                .onFailure { error ->
                    _state.value = PaymentState.Error(error.message ?: "Error al iniciar pago")
                }
        }
    }

    fun onPaymentSuccess() {
        viewModelScope.launch {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch

                // Fecha de vencimiento = hoy + 30 días
                val endDate = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_MONTH, 30)
                }.time

                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(userId)
                    .update(
                        mapOf(
                            "subscriptionActive" to true,
                            "subscriptionEnd" to endDate
                        )
                    ).await()

            } catch (e: Exception) {
                // Si falla el guardado igual mostramos éxito
            }
            _state.value = PaymentState.Success
        }
    }

    fun onPaymentError(message: String) { _state.value = PaymentState.Error(message) }
    fun resetState() { _state.value = PaymentState.Idle }
}