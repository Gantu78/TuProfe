package com.example.tuprofe.data.repository

import com.example.tuprofe.data.remote.PaymentService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository @Inject constructor(
    private val paymentService: PaymentService
) {
    suspend fun createPaymentIntent(): Result<String> =
        paymentService.createPaymentIntent()
}