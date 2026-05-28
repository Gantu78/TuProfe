package com.example.tuprofe.ui.payment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tuprofe.R
import com.example.tuprofe.ui.utils.BackgroundImage
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet

@Composable
fun PaymentScreen(
    onBackClick: () -> Unit,
    onSuccess: () -> Unit,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val paymentSheet = rememberPaymentSheet { result ->
        when (result) {
            is PaymentSheetResult.Completed -> viewModel.onPaymentSuccess()
            is PaymentSheetResult.Failed    -> viewModel.onPaymentError(result.error.message ?: "Error")
            is PaymentSheetResult.Canceled  -> viewModel.resetState()
        }
    }

    LaunchedEffect(state) {
        if (state is PaymentState.ReadyToPay) {
            val clientSecret = (state as PaymentState.ReadyToPay).clientSecret
            paymentSheet.presentWithPaymentIntent(
                clientSecret,
                PaymentSheet.Configuration(merchantDisplayName = "TuProfe")
            )
        }
        if (state is PaymentState.Success) onSuccess()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundImage()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = colorResource(R.color.verdetp),
                modifier = Modifier.size(72.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "TuProfe Premium",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Apoya el proyecto y accede\na todas las funciones",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(32.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Plan Mensual", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "$9.900 COP",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.verdetp)
                    )
                    Text("por mes", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(16.dp))
                    listOf(
                        "✓ Reseñas ilimitadas",
                        "✓ Sin anuncios",
                        "✓ Acceso prioritario"
                    ).forEach {
                        Text(it, modifier = Modifier.padding(vertical = 2.dp))
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            when (state) {
                is PaymentState.Loading -> {
                    CircularProgressIndicator(color = colorResource(R.color.verdetp))
                }
                is PaymentState.Error -> {
                    Text(
                        text = (state as PaymentState.Error).message,
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.startPayment() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.verdetp)
                        ),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text("Reintentar", color = Color.White, fontSize = 16.sp)
                    }
                }
                else -> {
                    Button(
                        onClick = { viewModel.startPayment() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.verdetp)
                        ),
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text("Suscribirme ahora", color = Color.White, fontSize = 16.sp)
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            TextButton(onClick = onBackClick) {
                Text("Ahora no", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}