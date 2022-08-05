package com.github.aesoper101.intellij.kratos.core.builder.application

import com.github.aesoper101.intellij.kratos.KratosConstants
import com.github.aesoper101.intellij.kratos.core.GeneratorAsset
import com.github.aesoper101.intellij.kratos.core.GeneratorAssetContext
import com.github.aesoper101.intellij.kratos.core.GeneratorProcessor
import com.github.aesoper101.intellij.kratos.core.GeneratorResource
import com.github.aesoper101.intellij.kratos.utils.FileUtil
import com.goide.util.GoUtil

import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project

import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile

import java.io.File

class ApplicationGenerator(
    private val module: Module,
    private val projectDirectory: VirtualFile,
    private val appName: String
) {
    fun doGenerate() {
        val assets = mutableListOf<GeneratorAsset>()
        projectDirectory
        assets.add(
            GeneratorResource(
                "api/$appName/v1",
                javaClass.getResource("/other/kratos-monorepo-layout/api/helloworld/v1")!!
            )
        )
        assets.add(
            GeneratorResource(
                "app/$appName",
                javaClass.getResource("/other/kratos-monorepo-layout/app/helloworld")!!
            )
        )

        GeneratorProcessor(module).execute(GeneratorAssetContext(assets, projectDirectory))


        val goModFile = projectDirectory.findChild("go.mod")!!
        val goModuleName = GoUtil.getModuleName(goModFile)


        VfsUtil.collectChildrenRecursively(projectDirectory).filter {
            it.path.startsWith(projectDirectory.path + "/app/$appName") || it.path.startsWith(projectDirectory.path + "/api/$appName")
        }.forEach {
            val content = FileUtil.read(it)
                ?.replace("api.helloworld.v1", "api.$appName.v1".lowercase())
                ?.replace(
                    "${KratosConstants.KRATOS_MONOREPO_LAYOUT_URL}/api/helloworld",
                    "$goModuleName/api/$appName"
                )
                ?.replace(
                    "${KratosConstants.KRATOS_MONOREPO_LAYOUT_URL}/app/helloworld",
                    "$goModuleName/app/$appName"
                )

            if (content != null) {
                it.setBinaryContent(content.toByteArray())
                it.refresh(true, false)
            }
        }

    }
}