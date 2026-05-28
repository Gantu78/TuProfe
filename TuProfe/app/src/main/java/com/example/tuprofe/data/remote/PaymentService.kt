package com.example.tuprofe.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentService @Inject constructor() {

    private val backendUrl = " https://scolding-safeguard-bleep.ngrok-free.dev/create-payment-intent"

    suspend fun createPaymentIntent(): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(backendUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.setRequestProperty("ngrok-skip-browser-warning", "69420")
                connection.setRequestProperty("User-Agent", "TuProfeApp/1.0")
                connection.doOutput = true
                connection.connectTimeout = 15000
                connection.readTimeout = 15000

                val writer = OutputStreamWriter(connection.outputStream)
                writer.write("{}")
                writer.flush()
                writer.close()

                val responseCode = connection.responseCode
                val response = if (responseCode == HttpURLConnection.HTTP_OK) {
                    connection.inputStream.bufferedReader().readText()
                } else {
                    connection.errorStream.bufferedReader().readText()
                }

                val json = JSONObject(response)
                Result.success(json.getString("clientSecret"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}