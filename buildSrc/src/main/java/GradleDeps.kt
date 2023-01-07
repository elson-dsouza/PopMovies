object GradleDeps {
    private const val androidGradleVersion = "7.3.1"

    val androidGradlePlugin by lazy { "com.android.tools.build:gradle:$androidGradleVersion" }
}