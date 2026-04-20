package com.example.tuprofe.ui.perfilAjeno

import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.Usuario

data class UserProfileState(
    val user: Usuario = Usuario("", "", "", "", "", 0, 0, false),
    val userReviews: List<ReviewInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val currentUserId: String = "",
    val showFollowersSheet: Boolean = false,
    val showFollowingSheet: Boolean = false,
    val followersList: List<Usuario> = emptyList(),
    val followingList: List<Usuario> = emptyList(),
    val isLoadingList: Boolean = false
)
