plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "examples"

include("object-update-test")

includeBuild("../") {
    dependencySubstitution {
        substitute(module("io.freefair.ksp-processors:object-update")).using(project(":object-update"))
        substitute(module("io.freefair.ksp-processors:object-update-api")).using(project(":object-update-api"))
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin", "2.0.21")
            version("coroutines", "1.9.0")
            version("ksp", "2.0.21-1.0.25")
            version("kotlinpoet", "2.0.0")

            plugin("kotlin-jvm", "org.jetbrains.kotlin.jvm").versionRef("kotlin")
            plugin("kotlin-ksp", "com.google.devtools.ksp").versionRef("ksp")

            library("ksp", "com.google.devtools.ksp", "symbol-processing-api").versionRef("ksp")

            library("kotlinpoet", "com.squareup", "kotlinpoet").versionRef("kotlinpoet")
            library("kotlinpoet-ksp", "com.squareup", "kotlinpoet-ksp").versionRef("kotlinpoet")

            library("object-update", "io.freefair.ksp-processors", "object-update").withoutVersion()
            library("object-update-api", "io.freefair.ksp-processors", "object-update-api").withoutVersion()
        }
    }
}