package com.github.aesoper101.intellij.kratos.core



import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

interface GenerateEvent {
    fun postGenerate(project: Project, projectDirectory: VirtualFile, file: VirtualFile)
}


