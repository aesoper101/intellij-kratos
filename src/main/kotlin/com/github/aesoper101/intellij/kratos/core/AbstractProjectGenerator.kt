package com.github.aesoper101.intellij.kratos.core


import com.github.aesoper101.intellij.kratos.notification.NotificationManager
import com.github.aesoper101.intellij.kratos.project.KratosNewProjectSettings
import com.goide.vgo.configuration.VgoProjectSettings
import com.goide.vgo.configuration.VgoSettings
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import java.io.IOException
import java.io.StringWriter


abstract class AbstractProjectGenerator(val module: Module, val settings: KratosNewProjectSettings) {
    private val freemarker = FreemarkerConfiguration()

    fun doGenerator(
        isCreatingNewProject: Boolean,
        contentRoot: String?
    ) {
        if (!StringUtil.isEmpty(contentRoot)) {
            ApplicationManager.getApplication().invokeLater {
                WriteAction.run<RuntimeException> {
                    try {
                        val directory =
                            VfsUtil.createDirectoryIfMissing(contentRoot!!)
                        if (directory != null) {
                            generateApplication(directory, settings.appName)

                            generateProjectThirdParty(directory)

                            generatePkg()

                            generateDeploy()
                        }
                    } catch (_: IOException) {

                    }
                }
            }
        }
        if (isCreatingNewProject || !VgoProjectSettings.getInstance(module.project).isIntegrationEnabled) {
            val vgoProjectSettings = VgoProjectSettings.getInstance(module.project)
            vgoProjectSettings.isIntegrationEnabled = true
            vgoProjectSettings.isAutoVendoringMode = settings.vendoringMode
            vgoProjectSettings.environment = settings.environment
            VgoSettings.getInstance().addEnvironmentVars(settings.environment)
        }

        settings.kratosSettings.saveState(module.project)
    }

    protected abstract fun generateProjectThirdParty(projectDirectory: VirtualFile)

    protected abstract fun generateApplication(projectDirectory: VirtualFile, appName: String)

    protected abstract fun generateDeploy()

    protected abstract fun generatePkg()


    fun writeFile(
        project: Project,
        parentDirectory: VirtualFile,
        entryPath: String?,
        filename: String,
        ftl: String,
        requestor: Any?
    ) {
        try {
            val stringWriter = StringWriter()
            val template = freemarker.getTemplate(ftl)
            template.process(requestor, stringWriter)

            stringWriter.close()

            var outputDirectory = parentDirectory
            if (entryPath != null) {
                outputDirectory = VfsUtil.createDirectoryIfMissing(parentDirectory, entryPath)
            }

            if (outputDirectory.findChild(filename) != null) {
                NotificationManager.getInstance().createNotification().error(project, "Ex")
                return
            }

            outputDirectory.createChildData(null, filename).setBinaryContent(stringWriter.toString().toByteArray())
        } catch (e: Exception) {
            NotificationManager.getInstance().createNotification().error(project, e.printStackTrace().toString())
        }
    }
}


