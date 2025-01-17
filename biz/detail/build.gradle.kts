plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.ksp)
}

android {
  buildFeatures.compose = true
  composeOptions.kotlinCompilerExtensionVersion = libs.versions.androidX.composeCompiler.get()
}

dependencies {
  implementation(projects.common)

  implementation(libs.androidX.activity.compose)
  implementation(libs.bundles.androidX.compose)
  implementation(libs.bundles.accompanist)

  implementation(libs.square.moshi)
  ksp(libs.square.moshi.compiler)
}
