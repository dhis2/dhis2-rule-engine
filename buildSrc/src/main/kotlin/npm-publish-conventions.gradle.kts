import dev.petuska.npm.publish.extension.domain.NpmAccess

plugins {
    id("dev.petuska.npm.publish")
}

val npmjsToken: String? = System.getenv("NPMJS_TOKEN")

project.afterEvaluate {
    npmPublish {
        access.set(NpmAccess.PUBLIC)
        packages {
            named("js") {
                scope.set("dhis2")
                readme.set(File("./README.md"))
                packageJson {
                    "module" by "${project.name}.mjs"
                    "main" by ""
                    "exports" by {
                        "import" by "./${project.name}.mjs"
                    }
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
                }
            }
        }
        registries {
            npmjs {
                authToken.set(npmjsToken)
            }
        }
    }

    tasks.named("assembleJsPackage") {
        doLast {
            val file = file("${layout.buildDirectory.get()}/packages/js/package.json")
            val mainRegex = "\n    \"main\": \"\","
            val removedMain = file.readText().replace(mainRegex, "")
            file.writeText(removedMain)
        }
    }
}
