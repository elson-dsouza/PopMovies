plugins {
    id(RealmDeps.realmPlugin) version RealmDeps.realmVersion apply false
    id(KotlinDeps.kotlinJvmPlugin) version KotlinDeps.kotlinVersion apply false
    id(KotlinDeps.kotlinParcelizePlugin) version KotlinDeps.kotlinVersion apply false
}

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath(GradleDeps.androidGradlePlugin)
        classpath(KotlinDeps.kotlinGradlePlugin)
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
