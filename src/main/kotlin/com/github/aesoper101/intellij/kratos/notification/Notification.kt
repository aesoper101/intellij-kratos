package com.github.aesoper101.intellij.kratos.notification

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

class Notification(private val groupId: String) {
    fun info(project: Project, content: String) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup(groupId)
            .createNotification(content, NotificationType.INFORMATION)
            .notify(project);
    }

    fun warning(project: Project, content: String) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup(groupId)
            .createNotification(content, NotificationType.WARNING)
            .notify(project);
    }

    fun error(project: Project, content: String) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup(groupId)
            .createNotification(content, NotificationType.ERROR)
            .notify(project);
    }

    fun showUpdate(project: Project, content: String) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup(groupId)
            .createNotification(content, NotificationType.IDE_UPDATE)
            .notify(project);
    }
}


