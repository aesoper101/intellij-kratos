package com.github.aesoper101.intellij.kratos.core


import com.github.aesoper101.intellij.kratos.utils.ExecUtils
import com.github.aesoper101.intellij.kratos.utils.ProcessEntity
import com.github.aesoper101.intellij.kratos.utils.backgroundTask
import com.github.aesoper101.intellij.kratos.utils.runProcessWithProgressSynchronously
import com.intellij.execution.process.ProcessOutput
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile

interface GenerateEvent {
    fun postGenerate(project: Project, projectDirectory: VirtualFile, file: VirtualFile)
}

class ApiProtoGenerateEvent : GenerateEvent {
    override fun postGenerate(project: Project, projectDirectory: VirtualFile, file: VirtualFile) {
        val apiPath = VfsUtil.getRelativePath(file.parent, projectDirectory)
        val filePath = VfsUtil.getRelativePath(file, projectDirectory)

        val params = listOf(
            "--proto_path=./${apiPath}",
            "--proto_path=./third_party",
            "--go_out=paths=source_relative:./${apiPath}",
            "--go-http_out=paths=source_relative:./${apiPath}",
            "--go-grpc_out=paths=source_relative:./${apiPath}",
            "--openapi_out=fq_schema_naming=true,default_response=false:.",
            "./${filePath}"
        )

        project.backgroundTask("Convert api ${file.path} to go file", callback = {
            ExecUtils.runCmd(object : ProcessEntity(project, "protoc", params, projectDirectory.path) {
                override fun afterRun(output: ProcessOutput) {
                    println("Convert api ${file.path} to go file success")
                    file.parent?.refresh(true, true)
                }
            })
        })

    }
}

class ConfProtoGenerateEvent : GenerateEvent {
    override fun postGenerate(project: Project, projectDirectory: VirtualFile, file: VirtualFile) {
        val confPath = VfsUtil.getRelativePath(file.parent, projectDirectory)
        val filePath = VfsUtil.getRelativePath(file, projectDirectory)

        val params = listOf(
            "--proto_path=./${confPath}",
            "--proto_path=./third_party",
            "--go_out=paths=source_relative:./${confPath}",
            "./${filePath}"
        )

        project.backgroundTask("Convert conf ${file.path} to go file", callback = {
            ExecUtils.runCmd(object : ProcessEntity(project, "protoc", params, projectDirectory.path) {
                override fun afterRun(output: ProcessOutput) {
                    println("Convert conf ${file.path} to go file success")
                    file.parent?.refresh(true, true)
                }
            })
        })

    }
}

class WireGenerateEvent : GenerateEvent {
    override fun postGenerate(project: Project, projectDirectory: VirtualFile, file: VirtualFile) {
        val wirePath = file.parent.path
        val params = emptyList<String>()

        project.backgroundTask("Create wire_gen", callback = {
            ExecUtils.runCmd(object : ProcessEntity(project, "wire", params, wirePath) {
                override fun afterRun(output: ProcessOutput) {
                    println("Create wire_gen success")
                    file.parent?.refresh(true, true)
                }
            })
        })

    }
}




