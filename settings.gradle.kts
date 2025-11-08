pluginManagement {
    repositories {
        gradlePluginPortal()
        google()          // Required for google-services and Android Gradle Plugin
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "CTEC3703_CafeApp"
include(":app")
 