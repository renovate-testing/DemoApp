plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(appTargetSdk)
    buildToolsVersion(appBuildTool)
    defaultConfig {
        minSdkVersion(appMinSdk)
        targetSdkVersion(appTargetSdk)
        versionCode = buildTime
        versionName = appVersionName
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
        versionNameSuffix = VersionNameSuffix.base
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
    kotlinOptions {
        jvmTarget = javaVersion.toString()
    }
}

dependencies {
    // local
    api(fileTree(localLibs))

    // architecture
    api(Libs.kotlin)
    api(Libs.core)
    api(Libs.annotation)
    api(Libs.multiDex)
    api(Libs.lifecycle)

    // UI
    api(Libs.appCompat)
    api(Libs.fragment)
    api(Libs.material)
    api(Libs.constraintLayout)
    api(Libs.recyclerView)
    api(Libs.cardView)
    api(Libs.smartRefreshLayout)
    api(Libs.baseRvHelper)

    // network
    api(Libs.okHttp)
    api(Libs.retrofit)
    api(Libs.tbs)
    api(Libs.glide)
    kapt(Libs.glideKapt)

    // storage
    api(Libs.objectBox)
    api(Libs.mmkv)

    // serializable
    api(Libs.fastjson)
    api(Libs.gson)

    // async
    api(Libs.rx)
    api(Libs.eventBus)
    kapt(Libs.eventBusKapt)

    // analyze
    debugApi(Libs.leakCanary)
}