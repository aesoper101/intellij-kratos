package com.github.aesoper101.intellij.kratos.core.builder.project

import com.github.aesoper101.intellij.kratos.core.GeneratorAsset
import com.github.aesoper101.intellij.kratos.core.GeneratorAssetContext
import com.github.aesoper101.intellij.kratos.core.GeneratorProcessor
import com.github.aesoper101.intellij.kratos.notification.NotificationManager
import com.github.aesoper101.intellij.kratos.project.KratosNewProjectSettings
import com.goide.actions.tool.GoFmtProjectAction
import com.goide.sdk.GoSdkUtil
import com.goide.util.GoExecutor
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset


abstract class AbstractProjectGenerator(
    var module: Module, var settings: KratosNewProjectSettings, var contentRootDirectory: String
) {

    fun doGenerate(isInGoWorkSpace: Boolean = false) {

        when (isInGoWorkSpace) {
            true -> {
                generate()
            }
            false -> {
                ApplicationManager.getApplication().invokeLater {
                    WriteAction.run<RuntimeException> {
                        generate()
                    }
                }
            }
        }
    }

    private fun generate() {
        try {
            val directory = VfsUtil.createDirectoryIfMissing(contentRootDirectory)
            if (directory != null) {
                val assets = getGeneratorAssets()

                doGenerate(directory, assets)

                afterDoGenerate(directory)
            }
        } catch (e: IOException) {
            NotificationManager.getInstance().createNotification().error(module.project, e.message!!)
        }
    }

    private fun doGenerate(directory: VirtualFile, assets: List<GeneratorAsset>) {
        GeneratorProcessor(module).execute(GeneratorAssetContext(assets, directory))
    }

    protected abstract fun getGeneratorAssets(): List<GeneratorAsset>


    open fun afterDoGenerate(projectDirectory: VirtualFile) {

    }


    @Throws(IOException::class)
    protected fun copyToString(inputStream: InputStream): String {
        val out = StringBuilder()
        val reader = InputStreamReader(inputStream, Charset.forName("utf8"))
        val buffer = CharArray(4096)
        var bytesRead: Int
        while (reader.read(buffer).also { bytesRead = it } != -1) {
            out.append(buffer, 0, bytesRead)
        }
        return out.toString()
    }
}
