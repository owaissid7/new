pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

        // Allow legacy and GitHub plugins
        jcenter() // Required for older Android + Firebase Gradle plugins
        maven("https://jitpack.io") // Required for GitHub libraries
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()

        // Add jcenter and jitpack to support older and GitHub-based dependencies
        jcenter() // Deprecated but still needed for older libs
        maven("https://jitpack.io")
    }
}

rootProject.name = "Sticker App"
include(":app")



//pluginManagement {
//    repositories {
//        google {
//            content {
//                includeGroupByRegex("com\\.android.*")
//                includeGroupByRegex("com\\.google.*")
//                includeGroupByRegex("androidx.*")
//            }
//        }
//        mavenCentral()
//        gradlePluginPortal()
//    }
//}
//dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
//    repositories {
//        google()
//        mavenCentral()
//    }
//}
//
//rootProject.name = "Sticker App"
//include(":app")
