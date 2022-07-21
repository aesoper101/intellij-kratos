package com.github.aesoper101.intellij.kratos.core.builder.project

import com.github.aesoper101.intellij.kratos.core.GeneratorAsset
import com.github.aesoper101.intellij.kratos.core.GeneratorAssetContext
import com.github.aesoper101.intellij.kratos.core.GeneratorProcessor
import com.github.aesoper101.intellij.kratos.core.GeneratorTemplateFile
import com.github.aesoper101.intellij.kratos.notification.NotificationManager
import com.github.aesoper101.intellij.kratos.project.KratosNewProjectSettings
import com.github.aesoper101.intellij.kratos.utils.ExecUtils
import com.github.aesoper101.intellij.kratos.utils.FileReload
import com.github.aesoper101.intellij.kratos.utils.ProcessEntity
import com.github.aesoper101.intellij.kratos.utils.backgroundTask
import com.goide.psi.impl.GoPackage
import com.goide.sdk.GoSdkService
import com.goide.vendor.GoVendoringUtil
import com.intellij.execution.process.ProcessOutput
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.module.Module
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.file.impl.FileManager
import java.io.IOException
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


    open fun getGoModGeneratorAsset(): GeneratorAsset {
        val props = mapOf(
            Pair("GoVersion", settings.sdk.version),
            Pair("ModuleName", settings.moduleName)
        )

        return GeneratorTemplateFile("go.mod", "go.mod", props)
    }

    open fun afterDoGenerate(projectDirectory: VirtualFile) {
        module.project.backgroundTask("go mod tidy", callback = {
            ExecUtils.runCmd(object : ProcessEntity(module.project, "go", listOf("mod", "tidy"), projectDirectory.path) {
                override fun afterRun(output: ProcessOutput) {
                    println("go mod tidy success")
                    projectDirectory.refresh(true, true)
                }
            })
        })
    }
}