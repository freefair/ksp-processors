package io.freefair.object_update.generation

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSPropertyDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import io.freefair.updater.annotations.NotUpdatable

class UpdaterGeneration(options: Map<String, String>) {
    private val generateType = options["generate-update-class"]?.toBoolean() ?: false
    private val generateExtension = options["generate-update-extensions"]?.toBoolean() ?: false

    fun generateUpdater(classDeclaration: KSClassDeclaration): FileSpec {
        val className = classDeclaration.toClassName()
        val builder = FileSpec.builder(className.packageName, "${className.simpleName}Updater")
        if(generateExtension) {
            val updatableProperties = getFilteredProperties(classDeclaration)
            builder
                .addFunction(generateExtensionUpdateFunction(updatableProperties, classDeclaration))
                .addFunction(generateExtensionPatchFunction(updatableProperties, classDeclaration))
        }
        if(generateType) {
            builder.addType(generateType(classDeclaration))
        }
        return builder.build()
    }

    private fun generateType(classDeclaration: KSClassDeclaration): TypeSpec {
        val className = classDeclaration.toClassName()
        val classBuilder = TypeSpec.classBuilder("${className.simpleName}Updater")
        val updatableProperties = getFilteredProperties(classDeclaration)

        classBuilder.addFunction(generateUpdateFunction(updatableProperties, classDeclaration))
        classBuilder.addFunction(generatePatchFunction(updatableProperties, classDeclaration))

        return classBuilder.build()
    }

    @OptIn(KspExperimental::class)
    private fun getFilteredProperties(classDeclaration: KSClassDeclaration) =
        classDeclaration.getAllProperties().filter { property ->
            !property.isAnnotationPresent(NotUpdatable::class) && property.isMutable
        }

    private fun generateUpdateFunction(
        updatableProperties: Sequence<KSPropertyDeclaration>,
        classDeclaration: KSClassDeclaration
    ): FunSpec {
        val func = FunSpec.builder("update")
        func.addParameter("entity", classDeclaration.toClassName())
        func.addParameter("update", classDeclaration.toClassName())
        func.returns(classDeclaration.toClassName())

        updatableProperties.forEach { property ->
            func.addStatement("entity.%L = update.%L", property.simpleName.asString(), property.simpleName.asString())
        }

        func.addStatement("return entity")

        return func.build()
    }

    private fun generatePatchFunction(
        updatableProperties: Sequence<KSPropertyDeclaration>,
        classDeclaration: KSClassDeclaration
    ): FunSpec {
        val func = FunSpec.builder("patch")
        func.addParameter("entity", classDeclaration.toClassName())
        func.addParameter("update", classDeclaration.toClassName())
        func.returns(classDeclaration.toClassName())

        updatableProperties.forEach { property ->
            func.addStatement("if (update.%L != null) entity.%L = update.%L", property.simpleName.asString(), property.simpleName.asString(), property.simpleName.asString())
        }

        func.addStatement("return entity")
        return func.build()
    }

    private fun generateExtensionUpdateFunction(
        updatableProperties: Sequence<KSPropertyDeclaration>,
        classDeclaration: KSClassDeclaration
    ): FunSpec {
        val func = FunSpec.builder("update")
        func.receiver(classDeclaration.toClassName())
        func.addParameter("update", classDeclaration.toClassName())
        func.returns(classDeclaration.toClassName())

        updatableProperties.forEach { property ->
            func.addStatement("this.%L = update.%L", property.simpleName.asString(), property.simpleName.asString())
        }

        func.addStatement("return this")

        return func.build()
    }

    private fun generateExtensionPatchFunction(
        updatableProperties: Sequence<KSPropertyDeclaration>,
        classDeclaration: KSClassDeclaration
    ): FunSpec {
        val func = FunSpec.builder("patch")
        func.receiver(classDeclaration.toClassName())
        func.addParameter("update", classDeclaration.toClassName())
        func.returns(classDeclaration.toClassName())

        updatableProperties.forEach { property ->
            func.addStatement("if (update.%L != null) this.%L = update.%L", property.simpleName.asString(), property.simpleName.asString(), property.simpleName.asString())
        }

        func.addStatement("return this")
        return func.build()
    }
}