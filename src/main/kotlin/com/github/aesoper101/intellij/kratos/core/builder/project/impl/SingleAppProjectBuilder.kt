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
        val apiDir = "helloworld"

        val props = mapOf(
            Pair("ModuleName", settings.moduleName),
            Pair("AppName", apiDir),
            Pair("CamelAppName", apiDir.toCamelCase()),
            Pair("IsMultipart", false)
        )


        // third_party
        assets.add(GeneratorResource("/third_party", javaClass.getResource("/other/third_party")!!))

        // internal/conf
        assets.add(
            GeneratorTemplateFile(
                "internal/conf/conf.proto",
                "conf.conf.proto",
                evt = ConfProtoGenerateEvent()
            )
        )

        // internal/data
        assets.add(
            GeneratorTemplateFile(
                "internal/data/README.md", "README.md", mapOf(Pair("TITLE", "Data"))
            )
        )
        assets.add(
            GeneratorTemplateFile(
                "internal/data/data.go",
                "data.go",
                props
            )
        )
        assets.add(
            GeneratorTemplateFile(
                "internal/data/greeter.go",
                "data.greeter.go",
                props
            )
        )

        // internal/biz
        assets.add(
            GeneratorTemplateFile(
                "internal/biz/README.md", "README.md", mapOf(Pair("TITLE", "Biz"))
            )
        )
        assets.add(
            GeneratorTemplateFile(
                "internal/biz/biz.go",
                "biz.go",
                props
            )
        )
        assets.add(
            GeneratorTemplateFile(
                "internal/biz/greeter.go",
                "biz.greeter.go",
                props
            )
        )

        // internal/service
        assets.add(
            GeneratorTemplateFile(
                "internal/service/README.md", "README.md", mapOf(Pair("TITLE", "Service"))
            )
        )
        assets.add(
            GeneratorTemplateFile(
                "internal/service/service.go",
                "service.go",
                props
            )
        )
        assets.add(
            GeneratorTemplateFile(
                "internal/service/greeter.go",
                "service.greeter.go",
                props
            )
        )

        // internal/server
        assets.add(
            GeneratorTemplateFile(
                "internal/server/README.md", "README.md", mapOf(Pair("TITLE", "Server"))
            )
        )

        assets.add(
            GeneratorTemplateFile(
                "internal/server/server.go",
                "server.go",
                props
            )
        )

        assets.add(
            GeneratorTemplateFile(
                "internal/server/http.go",
                "server.http.go",
                props
            )
        )

        assets.add(
            GeneratorTemplateFile(
                "internal/server/grpc.go",
                "server.grpc.go",
                props
            )
        )


        // api

        assets.add(
            GeneratorTemplateFile(
                "api/${apiDir}/v1/greeter.proto",
                "api.greeter.proto",
                props,
                evt = ApiProtoGenerateEvent()
            )
        )

        assets.add(
            GeneratorTemplateFile(
                "api/${apiDir}/v1/error_reason.proto",
                "api.error_reason.proto",
                props,
                evt = ApiProtoGenerateEvent()
            )
        )


        // configs/conf.yaml
        assets.add(GeneratorTemplateFile("configs/conf.yaml", "configs.config.yaml"))

        // .gitignore
        assets.add(GeneratorTemplateFile(".gitignore", ".gitignore"))

        // go.mod
        assets.add(getGoModGeneratorAsset())
        // Dockerfile
        assets.add(GeneratorTemplateFile("Dockerfile", ".dockerfile", props))
        // Makefile
        assets.add(GeneratorTemplateFile("Makefile", ".makefile", props))

        // cmd
        assets.add(
            GeneratorTemplateFile(
                "cmd/server/main.go",
                "main.go",
                props
            )
        )
        assets.add(
            GeneratorTemplateFile(
                "cmd/server/wire.go",
                "wire.go",
                props,
            )
        )

        assets.add(
            GeneratorTemplateFile(
                "cmd/server/wire_gen.go",
                "wire_gen.go",
                props,
            )
        )


        return assets
    }


}

