/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.gradle.generator.apple

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import dev.icerock.gradle.generator.FilesGenerator
import dev.icerock.gradle.generator.ObjectBodyExtendable
import org.gradle.api.file.FileTree
import java.io.File

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
class AppleFilesGenerator(
    inputFileTree: FileTree
) : FilesGenerator(
    inputFileTree = inputFileTree
), ObjectBodyExtendable by AppleGeneratorHelper() {

    override fun getClassModifiers(): Array<KModifier> = arrayOf(KModifier.ACTUAL)

    override fun getPropertyModifiers(): Array<KModifier> = arrayOf(KModifier.ACTUAL)

    override fun getPropertyInitializer(fileSpec: FileSpec): CodeBlock? {
        return CodeBlock.of(
            "FileResource(fileName = %S, extension = %S,bundle = ${AppleMRGenerator.BUNDLE_PROPERTY_NAME})",
            fileSpec.file.nameWithoutExtension,
            fileSpec.file.extension
        )
    }

    override fun generateResources(
        resourcesGenerationDir: File,
        files: List<FileSpec>
    ) {
        val targetDir = File(resourcesGenerationDir, "files")
        targetDir.mkdirs()

        files.forEach { (_, file) ->
            file.copyTo(File(targetDir, file.name))
        }
    }
}
