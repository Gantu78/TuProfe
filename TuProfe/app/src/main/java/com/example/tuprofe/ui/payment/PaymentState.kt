package com.example.tuprofe.ui.payment

sealed class PaymentState {
    object Idle : PaymentState()
    object Loading : PaymentState()
    data class ReadyToPay(val clientSecret: String) : PaymentState()
    object Success : PaymentState()
    data class Error(val message: String) : PaymentState()
}