pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/WSCSports/blaze-android-release")
            credentials {
                /** you can add to gradle.properties the following keys
                 ** gprUsr=GITHUB_USER_ID & gprKey=PERSONAL_ACCESS_TOKEN
                 * GITHUB_USER_ID is your github user
                 * PERSONAL_ACCESS_TOKEN is your github personal token
                 **/

                username = extra["gprUsr"] as String? ?: ""
                password = extra["gprKey"] as String? ?: ""
            }
        }
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "blaze-sample-android-v2"
include(":app")
include(":core:ui")
include(":samples:widgets")
include(":samples:globalsettings")
include(":samples:momentscontainer")
include(":samples:entrypoint")
include(":samples:playerstyle")
include(":samples:ads")
include(":samples:compose")
