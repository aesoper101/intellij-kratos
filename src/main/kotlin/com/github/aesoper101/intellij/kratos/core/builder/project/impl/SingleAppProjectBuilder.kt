package com.github.aesoper101.intellij.kratos.core.builder.project.impl

import com.github.aesoper101.intellij.kratos.core.*
import com.github.aesoper101.intellij.kratos.core.builder.project.AbstractProjectGenerator
import com.github.aesoper101.intellij.kratos.project.KratosNewProjectSettings
import com.github.aesoper101.intellij.kratos.utils.toCamelCase
import com.goide.psi.impl.GoPackage
import com.goide.refactor.move.GoMoveDeclarationDelegate
import com.goide.refactor.rename.GoRenamePsiElementProcessor
import com.goide.sdk.GoSdkService
import com.goide.vendor.GoVendoringUtil

import com.intellij.openapi.module.Module
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile


class SingleAppProjectBuilder(module: Module, settings: KratosNewProjectSettings, contentRootDirectory: String) :
    AbstractProjectGenerator(module, settings, contentRootDirectory) {


    override fun getGeneratorAssets(): List<GeneratorAsset> {
        val assets = mutableListOf<GeneratorAsset>()

        assets.add(GeneratorResource(".", javaClass.getResource("/other/kratos-layout")!!))

        return assets
    }


    override fun afterDoGenerate(projectDirectory: VirtualFile) {
        VfsUtil.collectChildrenRecursively(projectDirectory).forEach {
            if (!it.isDirectory) {
                val old = "github.com/aesoper101/kratos-layout"
                it.setBinaryContent(
                    copyToString(it.inputStream).replace(old, projectDirectory.name, false).toByteArray()
                )
            }
        }
        super.afterDoGenerate(projectDirectory)
    }

}

