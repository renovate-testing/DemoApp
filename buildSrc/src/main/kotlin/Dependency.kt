@file:Suppress("unused", "SpellCheckingInspection")

private const val coreVersion = "1.3.2"
private const val appCompatVersion = "1.2.0"
private const val supportVersion = "1.0.0"
private const val constraintLayoutVersion = "2.0.2"
private const val fragmentVersion = "1.2.5"
private const val materialVersion = "1.2.1"
private const val flexBoxVersion = "2.0.1"
private const val cardViewVersion = "1.0.0"
private const val recyclerViewVersion = "1.1.0"
private const val annotationVersion = "1.1.0"
private const val lifecycleVersion = "2.2.0"
private const val navigationVersion = "2.3.0"
private const val workVersion = "2.3.4"
private const val preferenceVersion = "1.1.1"
private const val multiDexVersion = "2.0.1"
private const val okHttpVersion = "4.9.0"
private const val retrofitVersion = "2.9.0"
private const val fastjsonVersion = "1.1.72.android"
private const val arouterVersion = "1.5.0"
private const val arouterCompilerVersion = "1.2.2"
private const val gsonVersion = "2.8.6"
private const val moshiVersion = "1.11.0"
private const val eventBusVersion = "3.2.0"
private const val liveDataBusVersion = "1.6.1"
private const val rxAndroidVersion = "3.0.0"
private const val rxJavaVersion = "3.0.7"
private const val rxPermissionVersion = "0.12"
private const val mpChartVersion = "v3.1.0"
private const val lottieVersion = "3.0.1"
private const val smartRefreshLayoutVersion = "2.0.1"
private const val glideVersion = "4.11.0"
private const val glideTransformVersion = "4.3.0"
private const val leakCanaryVersion = "2.5"
private const val lubanVersion = "1.1.8"
private const val mmkvVersion = "1.2.2"
private const val coroutinesVersion = "1.4.0"
private const val desugarVersion = "1.0.10"
private const val objectBoxVersion = "2.7.1"
private const val protocVersion = "3.13.0"
private const val tbsVersion = "43938"
private const val baseRvHelperVersion = "3.0.4"
private const val flycoTabLayoutVersion = "2.1.4.2"
private const val coilVersion = "0.13.0"
private const val utilsVersion = "1.30.1"
private const val pagingVersion = "3.0.0-alpha07"
private const val viewPager2Version = "1.0.0"
private const val protobufVersion = "0.8.13"
private const val photoViewVersion = "2.3.0"

object Plugins {
    const val kotlinAndroid = "kotlin-android"
    const val kotlinKapt = "kotlin-kapt"
    const val androidLibrary = "com.android.library"
    const val androidApplication = "com.android.application"
    const val protobuf = "com.google.protobuf"
    const val protobufPlugin = "com.google.protobuf:protobuf-gradle-plugin:$protobufVersion"
}

object Libs {
    const val core = "androidx.core:core-ktx:$coreVersion"
    const val desugar = "com.android.tools:desugar_jdk_libs:$desugarVersion"
    const val appCompat = "androidx.appcompat:appcompat:$appCompatVersion"
    const val annotation = "androidx.annotation:annotation:$annotationVersion"
    const val multiDex = "androidx.multidex:multidex:$multiDexVersion"
    const val fragment = "androidx.fragment:fragment-ktx:$fragmentVersion"
    const val legacySupport = "androidx.legacy:legacy-support-v4:$supportVersion"
    const val preference = "androidx.preference:preference:$preferenceVersion"
    const val palette = "androidx.palette:palette-ktx:$cardViewVersion"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"
    const val material = "com.google.android.material:material:$materialVersion"
    const val recyclerView = "androidx.recyclerview:recyclerview:$recyclerViewVersion"
    const val workManager = "androidx.work:work-runtime-ktx:$workVersion"
    const val flexBox = "com.google.android:flexbox:$flexBoxVersion"
    const val cardView = "androidx.cardview:cardview:$cardViewVersion"
    const val mpChart = "com.github.PhilJay:MPAndroidChart:$mpChartVersion"
    const val lottie = "com.airbnb.android:lottie:$lottieVersion"
    const val eventBus = "org.greenrobot:eventbus:$eventBusVersion"
    const val liveDataBus = "com.jeremyliao:live-event-bus-x:$liveDataBusVersion"
    const val fastjson = "com.alibaba:fastjson:$fastjsonVersion"
    const val arouter = "com.alibaba:arouter-api:$arouterVersion"
    const val luban = "top.zibin:Luban:$lubanVersion"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:$leakCanaryVersion"
    const val mmkv = "com.tencent:mmkv-static:$mmkvVersion"
    const val protoc = "com.google.protobuf:protoc:$protocVersion"
    const val protobufLite = "com.google.protobuf:protobuf-javalite:$protocVersion"
    const val tbs = "com.tencent.tbs.tbssdk:sdk:$tbsVersion"
    const val baseRvHelper = "com.github.CymChad:BaseRecyclerViewAdapterHelper:$baseRvHelperVersion"
    const val flycoTabLayout = "com.github.ernestchang:flycotablayout:$flycoTabLayoutVersion"
    const val coil = "io.coil-kt:coil:$coilVersion"
    const val utils = "com.blankj:utilcodex:$utilsVersion"
    const val rxPermission = "com.github.tbruyelle:rxpermissions:$rxPermissionVersion"
    const val retrofit = "com.squareup.retrofit2:retrofit:$retrofitVersion"
    const val paging = "androidx.paging:paging-runtime:$pagingVersion"
    const val viewPager2 = "androidx.viewpager2:viewpager2:$viewPager2Version"
    const val photoView = "com.github.chrisbanes:PhotoView:$photoViewVersion"

    const val glideKapt = "com.github.bumptech.glide:compiler:$glideVersion"
    const val objectBoxKapt = "io.objectbox:objectbox-processor:$objectBoxVersion"
    const val eventBusKapt = "org.greenrobot:eventbus-annotation-processor:$eventBusVersion"
    const val arouterKapt = "com.alibaba:arouter-compiler:$arouterCompilerVersion"
    const val moshiKapt = "com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion"

    val coroutines = arrayOf(
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion",
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
    )
    val lifecycle = arrayOf(
        "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion",
        "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion",
        "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion",
        "androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycleVersion",
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion",
        "androidx.lifecycle:lifecycle-service:$lifecycleVersion",
        "androidx.lifecycle:lifecycle-process:$lifecycleVersion",
        "androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion"
    )
    val navigation = arrayOf(
        "androidx.navigation:navigation-ui-ktx:$navigationVersion",
        "androidx.navigation:navigation-fragment-ktx:$navigationVersion",
        "androidx.navigation:navigation-common-ktx:$navigationVersion",
        "androidx.navigation:navigation-runtime-ktx:$navigationVersion"
    )
    val smartRefreshLayout = arrayOf(
        "com.scwang.smart:refresh-layout-kernel:$smartRefreshLayoutVersion",
        "com.scwang.smart:refresh-header-classics:$smartRefreshLayoutVersion",
        "com.scwang.smart:refresh-header-material:$smartRefreshLayoutVersion",
        "com.scwang.smart:refresh-footer-classics:$smartRefreshLayoutVersion"
    )
    val okHttp = arrayOf(
        "com.squareup.okhttp3:okhttp:$okHttpVersion",
        "com.squareup.okhttp3:logging-interceptor:$okHttpVersion"
    )
    val gson = arrayOf(
        "com.google.code.gson:gson:$gsonVersion",
        "com.squareup.retrofit2:converter-gson:$retrofitVersion"
    )
    val moshi = arrayOf(
        "com.squareup.moshi:moshi:$moshiVersion",
        "com.squareup.retrofit2:converter-moshi:$retrofitVersion"
    )
    val glide = arrayOf(
        "com.github.bumptech.glide:glide:$glideVersion",
        "com.github.bumptech.glide:okhttp3-integration:$glideVersion",
        "jp.wasabeef:glide-transformations:$glideTransformVersion"
    )
    val objectBox = arrayOf(
        "io.objectbox:objectbox-android:$objectBoxVersion",
        "io.objectbox:objectbox-kotlin:$objectBoxVersion"
    )
    val rx = arrayOf(
        "io.reactivex.rxjava3:rxjava:$rxJavaVersion",
        "io.reactivex.rxjava3:rxandroid:$rxAndroidVersion",
        "com.squareup.retrofit2:adapter-rxjava3:$retrofitVersion"
    )
}