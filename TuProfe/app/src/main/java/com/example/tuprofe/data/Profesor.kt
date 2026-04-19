package com.example.tuprofe.data


data class Profesor(
    val profeId: String,
    val nombreProfe: String,
    val imageprofeUrl: String?,
    val departamento: String = ""
)
