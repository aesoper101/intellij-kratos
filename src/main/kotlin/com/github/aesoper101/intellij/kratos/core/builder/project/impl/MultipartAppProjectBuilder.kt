package com.github.aesoper101.intellij.kratos.core.builder.project.impl

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

        val props = mapOf(
            Pair("ModuleName", settings.moduleName),
            Pair("AppName", settings.appName),
            Pair("CamelAppName", settings.appName),
            Pair("IsMultipart", true)
        )

        // third_party
        assets.add(GeneratorResource("third_party", javaClass.getResource("/other/third_party")!!))

        // app/${settings.appName}/internal/conf
        assets.add(
            GeneratorTemplateFile(
                "app/${settings.appName}/internal/conf/conf.proto",
                "conf.conf.proto",
                evt = ConfProtoGenerateEvent()
            )
        )

        // app/${settings.appName}/internal/data
        assets.add(
            GeneratorTemplateFile(
                "app/${settings.appName}/internal/data/README.md", "README.md", mapOf(Pair("TITLE", "Data"))
            )
        )
        assets.add(
            GeneratorTemplateFile(
                "app/${settings.appName}/internal/data/data.go",
                "data.go",
                props
            )
        )
        assets.add(
            GeneratorTemplateFile(
                "app/${settings.appName}/internal/data/greeter.go",
                "data.greeter.go",
                props
            )
        )

        // app/${settings.appName}/internal/biz
        assets.add(
            GeneratorTemplateFile(
                "app/${settings.appName}/internal/biz/README.md", "README.md", mapOf(Pair("TITLE", "Biz"))
            )
        )
        assets.add(
            GeneratorTemplateFile(
                "app/${settings.appName}/internal/biz/biz.go",
                "biz.go",
                props
            )
        )
        assets.add(
            GeneratorTemplateFile(
                "app/${settings.appName}/internal/biz/greeter.go",
                "biz.greeter.go",
                props
            )
        )

        // app/${settings.appName}/internal/service
        assets.add(
            GeneratorTemplateFile(
                "app/${settings.appName}/internal/service/README.md", "README.md", mapOf(Pair("TITLE", "Service"))
            )
        )
        assets.add(
            GeneratorTemplateFile(
                "app/${settings.appName}/internal/service/service.go",
                "service.go",
                props
            )
        )
        assets.add(
            GeneratorTemplateFile(
                "app/${settings.appName}/internal/service/greeter.go",
                "service.greeter.go",
                props
            )
        )

        // app/${settings.appName}/internal/server
        assets.add(
            GeneratorTemplateFile(
                "app/${settings.appName}/internal/server/README.md", "README.md", mapOf(Pair("TITLE", "Server"))
            )
        )

        assets.add(
            GeneratorTemplateFile(
                "app/${settings.appName}/internal/server/server.go",
                "server.go",
                props
            )
        )

        assets.add(
            GeneratorTemplateFile(
                "app/${settings.appName}/internal/server/http.go",
                "server.http.go",
                props
            )
        )

        assets.add(
            GeneratorTemplateFile(
                "app/${settings.appName}/internal/server/grpc.go",
                "server.grpc.go",
                props
            )
        )


        // api

        assets.add(
            GeneratorTemplateFile(
                "api/${settings.appName}/v1/greeter.proto",
                "api.greeter.proto",
                props,
                evt = ApiProtoGenerateEvent()
            )
        )

        assets.add(
            GeneratorTemplateFile(
                "api/${settings.appName}/v1/error_reason.proto",
                "api.error_reason.proto",
                props,
                evt = ApiProtoGenerateEvent()
            )
        )


        // app/${settings.appName}/configs/conf.yaml
        assets.add(GeneratorTemplateFile("app/${settings.appName}/configs/conf.yaml", "configs.config.yaml"))

        // .gitignore
        assets.add(GeneratorTemplateFile(".gitignore", ".gitignore"))

        // go.mod
        assets.add(getGoModGeneratorAsset())
        // Dockerfile
        assets.add(GeneratorTemplateFile("app/${settings.appName}/Dockerfile", ".dockerfile", props))
        // Makefile
        assets.add(GeneratorTemplateFile("app/${settings.appName}/Makefile", ".makefile",props))
        // app/${settings.appName}/cmd
        assets.add(
            GeneratorTemplateFile(
                "app/${settings.appName}/cmd/server/main.go",
                "main.go",
                props
            )
        )
        assets.add(
            GeneratorTemplateFile(
                "app/${settings.appName}/cmd/server/wire.go",
                "wire.go",
                props,
            )
        )

        assets.add(
            GeneratorTemplateFile(
                "app/${settings.appName}/cmd/server/wire_gen.go",
                "wire_gen.go",
                props,
            )
        )

        return assets
    }


}