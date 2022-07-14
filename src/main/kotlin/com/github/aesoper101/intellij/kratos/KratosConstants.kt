package com.github.aesoper101.intellij.kratos

data class FileTemplateEntity(
    val generatePath: String,
    val generateName: String,
    val fileTemplateName: String
)

object KratosConstants {
    @JvmField
    val KRATOS_THIRD_PARTY_TEMPLATES = listOf(
        FileTemplateEntity("third_party/errors", "errors.proto", "kratos.errors.proto"),
        FileTemplateEntity("third_party/google/api", "annotations.proto", "google.api.annotations.proto"),
        FileTemplateEntity("third_party/google/api", "client.proto", "google.api.client.proto"),
        FileTemplateEntity("third_party/google/api", "field_behavior.proto", "google.api.field_behavior.proto"),
        FileTemplateEntity("third_party/google/api", "http.proto", "google.api.http.proto"),
        FileTemplateEntity("third_party/google/api", "httpbody.proto", "google.api.httpbody.proto"),
        FileTemplateEntity("third_party/protobuf", "descriptor.proto", "protobuf.descriptor.proto"),
    )

    @JvmField
    val KRATOS_CONF_PROTO_TEMPLATES = listOf(
        FileTemplateEntity("internal/conf", "conf.proto", "conf.conf.proto")
    )


    @JvmField
    val KRATOS_CONFIGS_YAML_TEMPLATES = listOf(
        FileTemplateEntity("configs", "config.yaml", "configs.config.yaml")
    )

    const val KRATOS_BIZ_LOGIC_TEMPLATE_NAME = "internal.biz.go"

    @JvmField
    val KRATOS_BIZ_TEMPLATES = listOf(
        FileTemplateEntity("internal/biz", "provider.go", "internal.provider.go"),
        FileTemplateEntity("internal/biz", "README.md", "internal.README.md"),
        FileTemplateEntity("internal/biz", "biz.go", KRATOS_BIZ_LOGIC_TEMPLATE_NAME)
    )
}

