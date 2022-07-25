package com.github.aesoper101.intellij.kratos.core


import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import java.io.IOException

class GeneratorProcessor(private val module: Module) {
    private val log = logger<GeneratorProcessor>()

    internal fun execute(ctx: GeneratorAssetContext) {
        val outputDir = VfsUtil.createDirectoryIfMissing(ctx.outputDirectory.fileSystem, ctx.outputDirectory.path)
            ?: throw IllegalStateException("Unable to create directory ${ctx.outputDirectory.path}")
        generateSources(outputDir, ctx.assets)
    }

    private fun generateSources(
        outputDirectory: VirtualFile, assets: List<GeneratorAsset>
    ): List<VirtualFile> {
        assets.forEach { asset ->
            when (asset) {
                is GeneratorEmptyDirectory -> generateEmptyDirectory(outputDirectory, asset)
                is GeneratorResource -> generateFromResource(outputDirectory, asset)
                is GeneratorTemplateFile -> generateFromTemplateFile(outputDirectory, asset)
            }
        }

        return VfsUtil.collectChildrenRecursively(outputDirectory)
    }


    private fun generateFromTemplateFile(
        outputDirectory: VirtualFile,
        asset: GeneratorTemplateFile,
    ) {

        val sourceCode = try {
            val instance = FileTemplateManager.getInstance(module.project)
            when (asset.templateCategory) {
                TemplateCategory.J2EE -> {
                    instance.getJ2eeTemplate(asset.templateName).getText(asset.properties)
                }
                TemplateCategory.Code -> {
                    instance.getCodeTemplate(asset.templateName).getText(asset.properties)
                }
                TemplateCategory.Default -> {
                    instance.getDefaultTemplate(asset.templateName).getText(asset.properties)
                }
                TemplateCategory.Internal -> {
                    instance.getInternalTemplate(asset.templateName).getText(asset.properties)
                }
                else -> {
                    instance.getTemplate(asset.templateName).getText(asset.properties)
                }
            }

        } catch (e: Exception) {
            println("===================================================")
            println(e.message)
            println("===================================================")
            throw TemplateProcessingException(e)
        }
        val file = createFile(outputDirectory, asset.targetFileOrDirName)
        VfsUtil.saveText(file, sourceCode)

        asset.evt?.postGenerate(module.project, outputDirectory, file )
    }

    private fun generateEmptyDirectory(
        outputDirectory: VirtualFile,
        asset: GeneratorEmptyDirectory,
    ) {
        log.info("Creating empty directory ${asset.targetFileOrDirName} in ${outputDirectory.path}")
        VfsUtil.createDirectoryIfMissing(outputDirectory, asset.targetFileOrDirName)
    }

    private fun generateFromResource(
        outputDirectory: VirtualFile,
        asset: GeneratorResource,
    ) {
        val resourceFile = VfsUtil.findFileByURL(asset.resource)
        when {
            resourceFile == null -> {
                return
            }
            resourceFile.isDirectory -> {
                VfsUtil.copyDirectory(
                    null,
                    resourceFile,
                    VfsUtil.createDirectoryIfMissing(outputDirectory, asset.targetFileOrDirName),
                    null
                )
            }
            !resourceFile.isDirectory -> {
                val file = createFile(outputDirectory, asset.targetFileOrDirName)
                asset.resource.openStream().use {
                    file.setBinaryContent(it.readBytes())
                }
            }
        }
    }

    private fun createFile(outputDirectory: VirtualFile, relativePath: String): VirtualFile {
        val subPath = if (relativePath.contains("/")) "/" + relativePath.substringBeforeLast("/")
        else ""

        val fileDirectory = if (subPath.isEmpty()) {
            outputDirectory
        } else {
            VfsUtil.createDirectoryIfMissing(outputDirectory, subPath)
                ?: throw IllegalStateException("Unable to create directory $subPath in ${outputDirectory.path}")
        }

        val fileName = relativePath.substringAfterLast("/")
        log.info("Creating file $fileName in ${fileDirectory.path}")
        return fileDirectory.findOrCreateChildData(this, fileName)
    }

    private class TemplateProcessingException(t: Throwable) : IOException("Unable to process template", t)
}