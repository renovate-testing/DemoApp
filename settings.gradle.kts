pluginManagement {
  repositories {
    google()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

plugins {
  id("com.gradle.enterprise") version "3.11.1"
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
    publishAlways()
  }
}

include(
  // App
  ":app",
  // Common
  ":base",
  ":common",
  ":adapter",
  // Biz
  ":biz:login",
  ":biz:main",
  ":biz:detail",
  ":biz:web",
  ":biz:obsolete",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
