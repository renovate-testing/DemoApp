plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
}

dependencies {
  implementation(projects.common)

  implementation(libs.square.okHttp)
  implementation(libs.square.retrofit.gson)
  implementation(libs.square.okHttp.logInterceptor)
  implementation(libs.google.gson)
  implementation(libs.bundles.glide)
  implementation(libs.rxJava3.java)
  implementation(libs.rxJava3.android)
}
