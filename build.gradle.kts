import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jmailen.gradle.kotlinter.KotlinterExtension
import org.jmailen.gradle.kotlinter.support.ReporterType

buildscript {
  apply(extraScriptPath)

  repositories {
    google()
    gradlePluginPortal()
  }
  classpaths(
    rootProject.getExtra("androidGradlePlugin"),
    rootProject.getExtra("kotlinPlugin"),
    Libs.hiltPlugin,
    Libs.arouterPlugin,
    Libs.protobufPlugin,
    Libs.kotlinterPlugin,
    Libs.dependencyUpdatesPlugin,
    Libs.bintrayPublishPlugin
  )
}

allprojects {
  apply {
    from("${rootDir.path}/$extraScriptPath")
    plugin(Plugins.dependencyUpdate)
    plugin(Plugins.kotlinter)
  }
  configure<KotlinterExtension> {
    indentSize = 2
    reporters = arrayOf(ReporterType.html.name)
  }
}

tasks {
  named(GradleTask.DependencyUpdate.task, DependencyUpdatesTask::class) {
    rejectVersionIf {
      candidate.version.isStableVersion().not()
    }
  }
  create(GradleTask.Clean.task, Delete::class) {
    delete(rootProject.buildDir)
  }
}
