object KotlinDeps {
    const val kotlinVersion = "1.7.21"

    val kotlinJvmPlugin by lazy { "org.jetbrains.kotlin.jvm" }
    val kotlinAndroidPlugin by lazy { "kotlin-android" }
    val kotlinKaptPlugin by lazy { "kotlin-kapt" }
    val kotlinParcelizePlugin by lazy { "org.jetbrains.kotlin.plugin.parcelize" }
    val kotlinGradlePlugin by lazy { "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion" }
}