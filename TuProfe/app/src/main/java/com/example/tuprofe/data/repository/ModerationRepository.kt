package com.example.tuprofe.data.repository

import com.example.tuprofe.data.ReviewInfo
import com.example.tuprofe.data.ReviewMapMarker
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.tasks.await
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

data class BlockedUser(val userId: String, val username: String, val fotoPerfil: String? = null)

// ── In-memory moderation filter cache (singleton) ────────────────────────────

object ModerationCache {
    var blockedUserIds: Set<String> = emptySet()
        private set
    var blockedByUserIds: Set<String> = emptySet()
        private set
    var mutedReviewIds: Set<String> = emptySet()
        private set
    var mutedCommentIds: Set<String> = emptySet()
        private set

    private val _updateEvent = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val updateEvent: SharedFlow<Unit> = _updateEvent.asSharedFlow()

    fun blockUser(userId: String) {
        blockedUserIds = blockedUserIds + userId
        _updateEvent.tryEmit(Unit)
    }

    fun unblockUser(userId: String) {
        blockedUserIds = blockedUserIds - userId
        _updateEvent.tryEmit(Unit)
    }

    fun muteReview(reviewId: String) {
        mutedReviewIds = mutedReviewIds + reviewId
        _updateEvent.tryEmit(Unit)
    }

    fun muteComment(commentId: String) {
        mutedCommentIds = mutedCommentIds + commentId
        _updateEvent.tryEmit(Unit)
    }

    fun load(
        blocked: Set<String>,
        blockedBy: Set<String>,
        mutedReviews: Set<String>,
        mutedComments: Set<String>
    ) {
        blockedUserIds = blocked
        blockedByUserIds = blockedBy
        mutedReviewIds = mutedReviews
        mutedCommentIds = mutedComments
        _updateEvent.tryEmit(Unit)
    }
}

@JvmName("applyModerationFilterReviews")
fun List<ReviewInfo>.applyModerationFilter() = filter {
    it.usuario.usuarioId !in ModerationCache.blockedUserIds &&
    it.usuario.usuarioId !in ModerationCache.blockedByUserIds &&
    it.reviewId !in ModerationCache.mutedReviewIds
}

@JvmName("applyModerationFilterMarkers")
fun List<ReviewMapMarker>.applyModerationFilter() = filter {
    it.authorUserId !in ModerationCache.blockedUserIds &&
    it.authorUserId !in ModerationCache.blockedByUserIds &&
    it.reviewId !in ModerationCache.mutedReviewIds
}

// ── Repository ───────────────────────────────────────────────────────────────

@Singleton
class ModerationRepository @Inject constructor(
    private val db: FirebaseFirestore
) {
    suspend fun loadCacheForUser(userId: String) {
        try {
            val blocksDeferred = db.collection("blocks")
                .whereEqualTo("blockerId", userId)
                .get()
            val blockedByDeferred = db.collection("blocks")
                .whereEqualTo("blockedId", userId)
                .get()
            val mutesDeferred = db.collection("mutes")
                .whereEqualTo("userId", userId)
                .get()

            val blocks = blocksDeferred.await()
                .documents.mapNotNull { it.getString("blockedId") }.toSet()
            val blockedBy = blockedByDeferred.await()
                .documents.mapNotNull { it.getString("blockerId") }.toSet()
            val muteDocs = mutesDeferred.await().documents

            val mutedReviews = muteDocs
                .filter { it.getString("targetType") == "review" }
                .mapNotNull { it.getString("targetId") }.toSet()
            val mutedComments = muteDocs
                .filter { it.getString("targetType") == "comment" }
                .mapNotNull { it.getString("targetId") }.toSet()

            ModerationCache.load(blocks, blockedBy, mutedReviews, mutedComments)
        } catch (_: Exception) {}
    }

    suspend fun report(reporterId: String, targetId: String, targetType: String) {
        db.collection("reports").add(
            mapOf(
                "reporterId" to reporterId,
                "targetId" to targetId,
                "targetType" to targetType,
                "createdAt" to Instant.now().toString()
            )
        ).await()
        when (targetType) {
            "review"  -> ModerationCache.muteReview(targetId)
            "comment" -> ModerationCache.muteComment(targetId)
        }
    }

    suspend fun getBlockedUsers(blockerId: String): List<BlockedUser> {
        val blockedIds = db.collection("blocks")
            .whereEqualTo("blockerId", blockerId)
            .get().await()
            .documents.mapNotNull { it.getString("blockedId") }

        return blockedIds.mapNotNull { blockedId ->
            val userDoc = db.collection("users").document(blockedId).get().await()
            if (!userDoc.exists()) return@mapNotNull null
            BlockedUser(
                userId = blockedId,
                username = userDoc.getString("username") ?: blockedId,
                fotoPerfil = userDoc.getString("fotoPerfil")
            )
        }
    }

    suspend fun unblockUser(blockerId: String, blockedId: String) {
        db.collection("blocks").document("${blockerId}_${blockedId}").delete().await()
        ModerationCache.unblockUser(blockedId)
    }

    suspend fun blockUser(blockerId: String, blockedId: String) {
        db.collection("blocks").document("${blockerId}_${blockedId}").set(
            mapOf(
                "blockerId" to blockerId,
                "blockedId" to blockedId,
                "createdAt" to Instant.now().toString()
            )
        ).await()
        ModerationCache.blockUser(blockedId)
    }

    suspend fun mute(userId: String, targetId: String, targetType: String) {
        db.collection("mutes").add(
            mapOf(
                "userId" to userId,
                "targetId" to targetId,
                "targetType" to targetType,
                "createdAt" to Instant.now().toString()
            )
        ).await()
        when (targetType) {
            "review" -> ModerationCache.muteReview(targetId)
            "comment" -> ModerationCache.muteComment(targetId)
        }
    }
}
