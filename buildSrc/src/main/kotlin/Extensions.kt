@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.VariantDimension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.api.ApkVariantOutputImpl
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import java.io.File
import java.util.Locale
import java.util.Properties
import kotlin.math.pow
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.PluginAware
import org.gradle.kotlin.dsl.ScriptHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

const val cdnPrefix = "https://raw.githubusercontent.com/"
val apiHosts = mapOf(
  Flavor.Daily.name to "https://api.github.com/",
  Flavor.Online.name to "https://api.github.com/"
)

// app
const val appPackageName = "io.goooler.demoapp"
const val appName = "Demo"
const val extraScriptPath = "gradle/extra.gradle.kts"
val javaVersion = JavaVersion.VERSION_11
const val appVersionName = "1.5.0"

val appVersionCode: Int
  get() = appVersionName
    .takeWhile { it.isDigit() || it == '.' }
    .split('.')
    .map { it.toInt() }
    .reversed()
    .sumByIndexed { index, unit ->
      // 1.2.3 -> 102030
      (unit * 10.0.pow(2 * index + 1)).toInt()
    }

fun ScriptHandlerScope.classpaths(vararg names: Any): Array<Dependency?> =
  dependencies.config("classpath", *names)

fun DependencyHandler.apis(vararg names: Any): Array<Dependency?> = config("api", *names)

fun DependencyHandler.implementations(vararg names: Any): Array<Dependency?> =
  config("implementation", *names)

fun DependencyHandler.debugImplementations(vararg names: Any): Array<Dependency?> =
  config("debugImplementation", *names)

fun DependencyHandler.releaseImplementations(vararg names: Any): Array<Dependency?> =
  config("releaseImplementation", *names)

fun DependencyHandler.kapts(vararg names: Any): Array<Dependency?> = config("kapt", *names)

fun DependencyHandler.androidTestImplementations(vararg names: Any): Array<Dependency?> =
  config("androidTestImplementation", *names)

fun DependencyHandler.testImplementations(vararg names: Any): Array<Dependency?> =
  config("testImplementation", *names)

inline val Module.moduleName: String get() = ":${tag}"

fun PluginAware.applyPlugins(vararg names: String) {
  apply {
    names.forEach(::plugin)
  }
}

fun VariantDimension.putBuildConfigStringField(name: String, value: String?) {
  buildConfigField("String", name, "\"$value\"")
}

fun VariantDimension.putBuildConfigIntField(name: String, value: Int) {
  buildConfigField("Integer", name, value.toString())
}

fun BaseExtension.kotlinOptions(block: KotlinJvmOptions.() -> Unit) {
  (this as ExtensionAware).extensions.configure("kotlinOptions", block)
}

fun Project.kapt(block: KaptExtension.() -> Unit) = configure(block)

inline fun <reified T : BaseExtension> Project.setupBase(
  module: Module? = null,
  crossinline block: T.() -> Unit = {}
) {
  when (T::class) {
    LibraryExtension::class -> Plugins.androidLibrary
    BaseAppModuleExtension::class -> Plugins.androidApplication
    else -> null
  }?.let { applyPlugins(it) }

  applyPlugins(Plugins.kotlinAndroid, Plugins.kotlinKapt)
  extensions.configure<BaseExtension>("android") {
    compileSdkVersion(32)
    defaultConfig {
      minSdk = 21
      vectorDrawables.useSupportLibrary = true
      ndk.abiFilters += setOf("arm64-v8a")
      module?.let {
        resourcePrefix = "${it.tag}_"
        versionNameSuffix = "_${it.tag}"
      }
    }
    sourceSets.configureEach {
      java.srcDirs("src/$name/kotlin")
    }
    buildFeatures.buildConfig = false
    compileOptions.setDefaultJavaVersion(javaVersion)
    kotlinOptions {
      jvmTarget = javaVersion.toString()
      freeCompilerArgs = listOf(
        // https://kotlinlang.org/docs/compiler-reference.html#progressive
        "-progressive",
        "-Xopt-in=kotlin.RequiresOptIn",
        // Generate native Java 8 default interface methods.
        "-Xjvm-default=all",
        // Generate smaller bytecode by not generating runtime not-null assertions.
        "-Xno-call-assertions",
        "-Xno-param-assertions",
        "-Xno-receiver-assertions"
      )
    }
    packagingOptions.resources.excludes += setOf(
      "**/*.proto",
      "**/*.bin",
      "**/*.java",
      "**/*.properties",
      "**/*.version",
      "**/*.*_module",
      "*.txt",
      "META-INF/services/**",
      "META-INF/com/**",
      "META-INF/licenses/**",
      "META-INF/AL2.0",
      "META-INF/LGPL2.1",
      "com/**",
      "kotlin/**",
      "kotlinx/**",
      "okhttp3/**",
      "google/**"
    )
    (this as CommonExtension<*, *, *, *>).lint {
      abortOnError = true
    }
    dependencies {
      implementations(fileTree(mapOf("dir" to "libs", "include" to arrayOf("*.jar", "*.aar"))))
      testImplementations(*Libs.tests)
      androidTestImplementations(*Libs.androidTests)
    }
    (this as T).block()
  }
}

fun Project.setupLib(
  module: Module? = null,
  block: LibraryExtension.() -> Unit = {}
) = setupCommon(module, block)

fun Project.setupApp(
  appPackageName: String,
  appName: String,
  block: BaseAppModuleExtension.() -> Unit = {}
) = setupCommon<BaseAppModuleExtension> {
  defaultConfig {
    applicationId = appPackageName
    targetSdk = 32
    versionCode = appVersionCode
    versionName = appVersionName
    manifestPlaceholders += mapOf("appName" to appName)
    resourceConfigurations += setOf("en", "zh-rCN", "xxhdpi")
  }
  signingConfigs.create("release") {
    keyAlias = getSignProperty("keyAlias")
    keyPassword = getSignProperty("keyPassword")
    storeFile = File(rootDir, getSignProperty("storeFile"))
    storePassword = getSignProperty("storePassword")
    enableV3Signing = true
    enableV4Signing = true
  }
  buildTypes {
    release {
      resValue("string", "app_name", appName)
      signingConfig = signingConfigs["release"]
      isMinifyEnabled = true
      proguardFiles("$rootDir/gradle/proguard-rules.pro")
    }
    debug {
      resValue("string", "app_name", "${appName}.debug")
      signingConfig = signingConfigs["release"]
      applicationIdSuffix = ".debug"
      versionNameSuffix = ".debug"
      isJniDebuggable = true
      isRenderscriptDebuggable = true
      isCrunchPngs = false
    }
  }
  dependenciesInfo.includeInApk = false
  applicationVariants.all {
    outputs.all {
      (this as? ApkVariantOutputImpl)?.outputFileName =
        "../../../../${appName}_${versionName}_${versionCode}_" +
          "${flavorName.toLowerCase(Locale.ROOT)}_${buildType.name}.apk"
    }
  }
  block()
}

private inline fun <reified T : BaseExtension> Project.setupCommon(
  module: Module? = null,
  crossinline block: T.() -> Unit = {}
) = setupBase<T>(module) {
  flavorDimensions("channel")
  productFlavors {
    create(Flavor.Daily.name)
    create(Flavor.Online.name)
  }
  kapt {
    arguments {
      arg("AROUTER_MODULE_NAME", project.name)
      arg("room.incremental", "true")
    }
  }
  dependencies {
    if (module != Module.Common) {
      implementations(project(Module.Common.moduleName))
    }
    implementations(
      project(Module.Base.moduleName),
      project(Module.Adapter.moduleName),

      // router
      Libs.arouter,

      // UI
      Libs.constraintLayout,
      Libs.cardView,
      Libs.material,
      *Libs.smartRefreshLayout,
      Libs.photoView,

      // utils
      *Libs.hilt,
      *Libs.room,
      *Libs.rx,
      *Libs.moshi,
      Libs.collection,
      Libs.utils
    )
    kapts(Libs.arouterCompiler, Libs.moshiCompiler, Libs.roomCompiler, Libs.hiltCompiler)
  }
  applyPlugins(Plugins.kotlinParcelize, Plugins.arouter, Plugins.hilt)
  block()
}

private fun DependencyHandler.config(operation: String, vararg names: Any): Array<Dependency?> =
  names.map { add(operation, it) }.toTypedArray()

private fun Project.getSignProperty(
  key: String,
  path: String = "gradle/keystore.properties"
): String = Properties().apply {
  rootProject.file(path).inputStream().use(::load)
}.getProperty(key)

private inline fun <T> List<T>.sumByIndexed(selector: (Int, T) -> Int): Int {
  var index = 0
  var sum = 0
  for (element in this) {
    sum += selector(index++, element)
  }
  return sum
}
