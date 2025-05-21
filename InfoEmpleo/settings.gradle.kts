pluginManagement {
    repositories {
        // 1) Plugin Portal **primero**
        gradlePluginPortal()
        // 2) Después los repositorios de Android
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        // 3) Por último Maven Central
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

rootProject.name = "JotpackComposelnstagram"
include(":app")
 