plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}
rootProject.name = "ksp-processors"

include("object-update", "object-update-api")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin", "2.0.21")
            version("coroutines", "1.9.0")
            version("ksp", "2.0.21-1.0.25")
            version("kotlinpoet", "2.0.0")
            version("freefair", "8.10.2")

            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("kotlin-ksp", "com.google.devtools.ksp").versionRef("ksp")
            plugin("nexus-publish", "io.github.gradle-nexus.publish-plugin").version("2.0.0")
            plugin("validate-poms", "io.freefair.maven-central.validate-poms").versionRef("freefair")
            plugin("git-version", "io.freefair.git-version").versionRef("freefair")
            plugin("dependency-submission", "io.freefair.github.dependency-submission").versionRef("freefair")
            plugin("dokka", "org.jetbrains.dokka").version("1.9.20")

            library("ksp", "com.google.devtools.ksp", "symbol-processing-api").versionRef("ksp")

            library("kotlinpoet", "com.squareup", "kotlinpoet").versionRef("kotlinpoet")
            library("kotlinpoet-ksp", "com.squareup", "kotlinpoet-ksp").versionRef("kotlinpoet")
        }
    }
}