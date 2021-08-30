package dev.icerock.gradle.generator.jvm

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.KModifier
import dev.icerock.gradle.generator.AssetsGenerator
import dev.icerock.gradle.generator.ObjectBodyExtendable
import org.gradle.api.file.FileTree
import java.io.File

class JvmAssetsGenerator(
    inputFileTree: FileTree
) : AssetsGenerator(inputFileTree), ObjectBodyExtendable by ClassLoaderExtender() {

    override fun getClassModifiers(): Array<KModifier> = arrayOf(KModifier.ACTUAL)

    override fun getPropertyModifiers(): Array<KModifier> = arrayOf(KModifier.ACTUAL)

    override fun getPropertyInitializer(fileSpec: AssetSpec) = CodeBlock.of(
        "FileResource(resourcesClassLoader = resourcesClassLoader, filePath = %S)",
        File(FILES_DIR, fileSpec.pathRelativeToBase).path
    )

    override fun generateResources(
        assetsGenerationDir: File, resourcesGenerationDir: File, files: List<AssetSpec>
    ) {
        val fileResDir = File(resourcesGenerationDir, FILES_DIR).apply { mkdirs() }
        files.forEach {
            it.file.copyTo(File(fileResDir, it.pathRelativeToBase))
        }
    }

    companion object {
        private const val FILES_DIR = "files"
    }
}