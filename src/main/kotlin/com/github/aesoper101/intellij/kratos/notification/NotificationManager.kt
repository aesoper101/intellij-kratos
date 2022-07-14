package com.github.aesoper101.intellij.kratos.notification

class NotificationManager() {
    private val notificationGroupId: String = "Kratos notification group"

    companion object {
        fun getInstance(): NotificationManager {
            return NotificationManager()
        }
    }

    fun createNotification(): Notification = Notification(notificationGroupId)
}
