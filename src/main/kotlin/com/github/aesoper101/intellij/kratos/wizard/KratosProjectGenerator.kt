package com.github.aesoper101.intellij.kratos.wizard

import com.github.aesoper101.intellij.kratos.KratosBundle
import com.github.aesoper101.intellij.kratos.KratosIcons
import com.github.aesoper101.intellij.kratos.core.impl.ProjectGeneratorImpl
import com.github.aesoper101.intellij.kratos.project.KratosNewProjectSettings
import com.goide.project.GoModuleBuilderBase
import com.goide.project.GoProjectLibrariesService
import com.goide.wizard.GoProjectGenerator
import com.intellij.facet.ui.ValidationResult
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.ProjectGeneratorPeer
import javax.swing.Icon


private fun getProjectPresentableName() = KratosBundle.message("new.project.name")
private fun getProjectDescription(): String = KratosBundle.message("new.project.description")
private fun getProjectIcon(): Icon = KratosIcons.KratosNewProjectIcon

class KratosProjectGenerator : GoProjectGenerator<KratosNewProjectSettings>() {
    override fun getDescription(): String = getProjectDescription()

    override fun getName(): String = getProjectPresentableName()

    override fun getLogo(): Icon = getProjectIcon()

    override fun validate(baseDirPath: String): ValidationResult = ValidationResult.OK

    override fun doGenerateProject(p0: Project, p1: VirtualFile, settings: KratosNewProjectSettings, module: Module) {
        ProjectGeneratorImpl(module, settings).doGenerator( true, p1.path)
    }

    override fun createPeer(): ProjectGeneratorPeer<KratosNewProjectSettings> = KratosProjectGeneratorPeer()
}


class KratosModuleBuilder : GoModuleBuilderBase<KratosNewProjectSettings> {

    constructor() : super(KratosProjectGeneratorPeer())

    constructor(peer: KratosProjectGeneratorPeer, isCreatingNewProject: Boolean) : super(peer, isCreatingNewProject)


    override fun getNodeIcon(): Icon = getProjectIcon()

    override fun getDescription(): String = getProjectDescription()

    override fun getPresentableName(): String = getProjectPresentableName()

    override fun moduleCreated(module: Module, isCreatingNewProject: Boolean) {
        if (isCreatingNewProject) {
            GoProjectLibrariesService.getInstance(module.project).isIndexEntireGopath = false
        }
        ProjectGeneratorImpl(module, settings).doGenerator(isCreatingNewProject, this.contentEntryPath)
    }
}



