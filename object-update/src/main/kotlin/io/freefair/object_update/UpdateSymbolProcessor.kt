package io.freefair.object_update

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.writeTo
import io.freefair.object_update.generation.UpdaterGeneration

class UpdateSymbolProcessor(
    private val codeGenerator: CodeGenerator,
    options: Map<String, String>
) : SymbolProcessor {
    private val updaterGeneration = UpdaterGeneration(options)

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val classDeclarations = resolver.getSymbolsWithAnnotation("io.freefair.updater.annotations.Updater", true).filterIsInstance<KSClassDeclaration>()
        classDeclarations.forEach {
            updaterGeneration.generateUpdater(it).writeTo(codeGenerator, Dependencies(true, it.containingFile!!))
        }
        return emptyList()
    }
}