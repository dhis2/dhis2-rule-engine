object Props {
    const val DESCRIPTION = "Rule Engine"

    const val REPOSITORY_TYPE = "git"
    const val REPOSITORY_SYSTEM = "GitHub"
    const val REPOSITORY_URL = "https://github.com/dhis2/dhis2-rule-engine.git"

    const val ORGANIZATION_NAME = "UiO"
    const val ORGANIZATION_URL = "https://dhis2.org"

    const val LICENSE_NAME = "BSD-3-Clause"
    const val LICENSE_URL = "https://opensource.org/license/bsd-3-clause/"

    val DEVELOPERS = listOf(
        Developer(
            name = "Enrico Colasante",
            email = "enrico@dhis2.org",
            organization = "UiO",
            organizationUrl = "https://www.uio.no/"
        ),
        Developer(
            name = "Zubair Asghar",
            email = "zubair@dhis2.org",
            organization = "UiO",
            organizationUrl = "https://www.uio.no/"
        ),
    )
}

data class Developer(
    val name: String,
    val email: String,
    val organization: String,
    val organizationUrl: String,
)