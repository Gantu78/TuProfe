package com.example.tuprofe.data.repository

import com.example.tuprofe.data.AppNotification
import com.example.tuprofe.data.datasource.NotificationFirestoreDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val dataSource: NotificationFirestoreDataSource
) {
    fun listenNotifications(userId: String): Flow<List<AppNotification>> =
        dataSource.listenNotifications(userId)

    fun listenUnreadCount(userId: String): Flow<Int> =
        dataSource.listenUnreadCount(userId)

    suspend fun saveNotification(userId: String, notification: AppNotification) =
        dataSource.saveNotification(userId, notification)

    suspend fun markRead(userId: String, notifId: String) =
        dataSource.markRead(userId, notifId)

    suspend fun markAllRead(userId: String) =
        dataSource.markAllRead(userId)
}