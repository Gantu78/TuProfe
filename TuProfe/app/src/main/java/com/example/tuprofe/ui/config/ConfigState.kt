package com.example.tuprofe.ui.config

data class ConfigState(
    val username: String = "",
    val email: String = "",
    val carrera: String = "",
    val isLoading: Boolean = false,
    val profileImageUrl: String? = null,
    val errorMessage: String? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val showFollowersSheet: Boolean = false,
    val showFollowingSheet: Boolean = false,
    val followersList: List<com.example.tuprofe.data.Usuario> = emptyList(),
    val followingList: List<com.example.tuprofe.data.Usuario> = emptyList(),
    val isLoadingList: Boolean = false
)
