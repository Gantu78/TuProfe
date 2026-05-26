package com.example.tuprofe.ui.ajustes

data class AjustesState(

    val perfilAnonimo: Boolean = false,
    val perfilPublico: Boolean = true,
    val resenasEnPerfil: Boolean = true,

    val selectedLanguage: String = "",

    val languageChanged: Boolean = false
)