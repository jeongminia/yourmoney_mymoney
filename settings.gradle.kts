pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
//        google() // 주석 처리된 부분은 필요에 따라 활성화할 수 있습니다.
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // 필요 시 추가된 리포지토리
    }
}

rootProject.name = "nisonnaeson" // 프로젝트 이름 선택
include(":app")
