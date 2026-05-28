package com.example.tuprofe.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tuprofe.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    fun onPaymentSuccess() { _state.value = PaymentState.Success }
    fun onPaymentError(message: String) { _state.value = PaymentState.Error(message) }
    fun resetState() { _state.value = PaymentState.Idle }
}