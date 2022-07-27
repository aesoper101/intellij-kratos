package com.github.aesoper101.intellij.kratos.core


import com.github.aesoper101.intellij.kratos.utils.ExecUtils
import com.github.aesoper101.intellij.kratos.utils.ProcessEntity
import com.github.aesoper101.intellij.kratos.utils.backgroundTask
import com.github.aesoper101.intellij.kratos.utils.runProcessWithProgressSynchronously
import com.goide.util.GoExecutor
import com.intellij.execution.process.ProcessOutput
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile

interface GenerateEvent {
    fun postGenerate(project: Project, projectDirectory: VirtualFile, file: VirtualFile)
}

class WireGenerateEvent : GenerateEvent {
    override fun postGenerate(project: Project, projectDirectory: VirtualFile, file: VirtualFile) {
        val wirePath = file.parent.path
        val params = emptyList<String>()



    }
}




