package com.github.aesoper101.intellij.kratos.core.builder.project.impl

import com.github.aesoper101.intellij.kratos.KratosConstants
import com.github.aesoper101.intellij.kratos.core.*
import com.github.aesoper101.intellij.kratos.core.builder.project.AbstractProjectGenerator
import com.github.aesoper101.intellij.kratos.project.KratosNewProjectSettings
import com.intellij.openapi.module.Module
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile


class MultipartAppProjectBuilder(module: Module, settings: KratosNewProjectSettings, contentRootDirectory: String) :
        AbstractProjectGenerator(module, settings, contentRootDirectory) {

    override fun getGeneratorAssets(): List<GeneratorAsset> {
        val assets = mutableListOf<GeneratorAsset>()

        assets.add(GeneratorResource(".", javaClass.getResource("/other/kratos-monorepo-layout")!!))

        return assets
    }

    override fun afterDoGenerate(projectDirectory: VirtualFile) {
        VfsUtil.collectChildrenRecursively(projectDirectory).forEach {
            if (!it.isDirectory) {
                it.setBinaryContent(
                        copyToString(it.inputStream)
                                .replace(
                                        KratosConstants.KRATOS_MONOREPO_LAYOUT_URL,
                                        projectDirectory.name,
                                        false
                                )
                                .replace(KratosConstants.KRATOS_MONOREPO_LAYOUT_NAME, projectDirectory.name, false).toByteArray()
                )
            }
        }

        super.afterDoGenerate(projectDirectory)
    }


}