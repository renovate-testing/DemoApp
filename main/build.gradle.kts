setupLib(LibModule.Main) {
  sourceSets["main"].res.srcDirs(
    "src/main/res/core",
    "src/main/res/other"
  )
}

dependencies {
  implementations(
    Libs.coordinatorLayout,
    Libs.flycoTabLayout
  )
}
