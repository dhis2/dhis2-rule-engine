import dev.petuska.npm.publish.extension.domain.NpmAccess
import java.nio.file.Files

plugins {
    id("dev.petuska.npm.publish")
}

project.afterEvaluate {
    npmPublish {
        access.set(NpmAccess.PUBLIC)
        packages {
            named("js") {
                scope.set("dhis2")
                packageName.set("rule-engine")
                readme.set(File("./README.md"))
                packageJson {
                    "module" by "mjs/${project.name}.mjs"
                    "main" by "cjs/${project.name}.js"
                    "exports" by {
                        "." by {
                            "import" by "./mjs/${project.name}.mjs"
                            "require" by "./cjs/${project.name}.js"
                        }
                    }
                    "types" by "./mjs/${project.name}.d.ts"
                    "contributors" by Props.DEVELOPERS.map { developer ->
                        "${developer.name} <${developer.email}>"
                    }
                    "description" by Props.DESCRIPTION
                    "license" by Props.LICENSE_NAME
                    "repository" by {
                        "type" by Props.REPOSITORY_TYPE
                        "url" by Props.REPOSITORY_URL
                    }
                    "publishConfig" by {
                        "access" by "public"
                    }
                    "private" by false
                }
            }
        }
    }

    tasks.named("packJsPackage") {
        doLast {
            val buildFolder = "./build/packages/js"

            if (project.hasProperty("useCommonJs")) {
                moveFilesToSubFolder(buildFolder, subFolder = "cjs", packageType = "commonjs")
            } else {
                moveFilesToSubFolder(buildFolder, subFolder = "mjs", packageType = "module")
            }
        }
    }
}

fun moveFilesToSubFolder(buildFolder: String, subFolder: String, packageType: String) {
    val excludedFiles = listOf("README.md", "package.json")
    Files.createDirectory(File("$buildFolder/$subFolder").toPath())

    println("\nMoving to final destination folder '$subFolder', type '$packageType'")

    File(buildFolder).listFiles()?.forEach { file ->
        if (file.isFile && !excludedFiles.contains(file.name)) {
            val dstPath = "${file.parent}/$subFolder/${file.name}"
            file.renameTo(File(dstPath))
        }
    }

    // Add a package.json file in each subfolder containing the package type
    File("$buildFolder/$subFolder/package.json")
        .writeText("""
            {
              "type" : "$packageType"
            }
        """.trimIndent())
}
