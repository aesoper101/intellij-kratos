package com.github.aesoper101.intellij.kratos.core.builder.project

import com.github.aesoper101.intellij.kratos.core.GeneratorAsset
import com.github.aesoper101.intellij.kratos.core.GeneratorAssetContext
import com.github.aesoper101.intellij.kratos.core.GeneratorProcessor
import com.github.aesoper101.intellij.kratos.notification.NotificationManager
import com.github.aesoper101.intellij.kratos.project.KratosNewProjectSettings
import com.github.aesoper101.intellij.kratos.utils.ExecUtils
import com.github.aesoper101.intellij.kratos.utils.ProcessEntity
import com.github.aesoper101.intellij.kratos.utils.backgroundTask
import com.goide.actions.tool.GoFmtProjectAction
import com.goide.sdk.GoSdkUtil
import com.goide.util.GoExecutor
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.Consumer
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.*


abstract class AbstractProjectGenerator(
    var module: Module, var settings: KratosNewProjectSettings, var contentRootDirectory: String
) {

    fun doGenerate() {
        ApplicationManager.getApplication().invokeLater {
            WriteAction.run<RuntimeException> {
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
        }

    }

    private fun doGenerate(directory: VirtualFile, assets: List<GeneratorAsset>) {
        GeneratorProcessor(module).execute(GeneratorAssetContext(assets, directory))
    }

    protected abstract fun getGeneratorAssets(): List<GeneratorAsset>


    open fun afterDoGenerate(projectDirectory: VirtualFile) {
        goFmtProject()

        goModTidy(projectDirectory)
    }

    @Suppress("DialogTitleCapitalization")
    private fun goModTidy(projectDirectory: VirtualFile) {
        val presentationName = "go mod tidy"
        GoExecutor.`in`(module.project, module).disablePty().withPresentableName(presentationName)
            .withWorkDirectory(projectDirectory.path).withParameters("mod", "tidy").executeWithProgress()
    }

    private fun goFmtProject() {
        FileDocumentManager.getInstance().saveAllDocuments()
        val var3: Iterator<*> = GoSdkUtil.getGoModules(module.project).iterator()

        while (var3.hasNext()) {
            val module = var3.next() as Module
            val var5 = ModuleRootManager.getInstance(module).contentRoots
            val var6 = var5.size
            for (var7 in 0 until var6) {
                val file = var5[var7]
                val presentation = "go fmt " + file.path


                GoExecutor.`in`(module.project, module).disablePty().withPresentableName(presentation)
                    .withWorkDirectory(file.path).withParameters("fmt", "./...").executeWithProgress(
                        true, true
                    ) {
                        VfsUtil.markDirtyAndRefresh(
                            true, true, true, file
                        )
                    }
            }
        }
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
