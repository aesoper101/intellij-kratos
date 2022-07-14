package com.github.aesoper101.intellij.kratos.core

import com.github.aesoper101.intellij.kratos.notification.NotificationManager
import com.github.aesoper101.intellij.kratos.project.KratosNewProjectSettings
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.module.Module

abstract class AbstractApplicationGenerator(var module: Module, var settings: KratosNewProjectSettings) {
    fun doGenerator(
        projectDirectory: VirtualFile,
        appName: String
    ) {
        val applicationContainerDirectory = VfsUtil.createDirectoryIfMissing(projectDirectory, "application")

        var applicationDirectory = applicationContainerDirectory.findChild(appName)
        if (applicationDirectory != null) {
            NotificationManager.getInstance().createNotification().warning(module.project, "Exists")
            return
        }

        applicationDirectory = applicationContainerDirectory.createChildDirectory(object {}, appName)

        generateCmd(applicationDirectory)

        generateConfigs(applicationDirectory)

        generateInternal(applicationDirectory)
    }

    protected abstract fun generateCmd(applicationDirectory: VirtualFile)

    protected abstract fun generateInternal(applicationDirectory: VirtualFile)

    protected abstract fun generateConfigs(applicationDirectory: VirtualFile)
}