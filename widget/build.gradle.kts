plugins {
    id(Plugins.androidLibrary)
}

setupBase().run {
    resourcePrefix = getResourcePrefix(Module.Widget)
    defaultConfig.versionNameSuffix = getVersionNameSuffix(Module.Widget)
}

dependencies {
    implementation(Libs.core)
}