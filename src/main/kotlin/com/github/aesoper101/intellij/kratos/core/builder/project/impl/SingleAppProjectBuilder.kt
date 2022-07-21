package com.github.aesoper101.intellij.kratos.core.builder.project.impl

import com.github.aesoper101.intellij.kratos.core.*
import com.github.aesoper101.intellij.kratos.core.builder.project.AbstractProjectGenerator
import com.github.aesoper101.intellij.kratos.project.KratosNewProjectSettings
import com.github.aesoper101.intellij.kratos.utils.toCamelCase
import com.goide.psi.impl.GoPackage
import com.goide.sdk.GoSdkService
import com.goide.vendor.GoVendoringUtil
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.jetbrains.rd.framework.base.deepClonePolymorphic
import java.util.*


class SingleAppProjectBuilder(module: Module, settings: KratosNewProjectSettings, contentRootDirectory: String) :
    AbstractProjectGenerator(module, settings, contentRootDirectory) {


    override fun getGeneratorAssets(): List<GeneratorAsset> {
        val assets = mutableListOf<GeneratorAsset>()

        // third_party
        assets.add(GeneratorResource("/third_party", javaClass.getResource("/other/third_party")!!))

        return assets
    }


}

