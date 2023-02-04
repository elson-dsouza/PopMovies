object JetpackDeps {
    private const val composeBomVersion = "2023.01.00"
    const val kotlinCompilerExtensionVersion = "1.4.0"

    private const val pagingVersion = "3.1.1"
    private const val pagingJetpackComposeVersion = "1.0.0-alpha17"

    private const val constraintLayoutVersion = "2.1.4"
    private const val constraintLayoutComposeVersion = "1.0.1"

    private const val composeLiveDataVersion = "1.3.3"
    private const val lifecycleVersion = "2.5.1"

    val composeBomDependency by lazy { "androidx.compose:compose-bom:$composeBomVersion" }
    val material3Dependency by lazy { "androidx.compose.material3:material3" }

    val pagingDependency by lazy { "androidx.paging:paging-runtime:$pagingVersion" }
    val pagingComposeDependency by lazy { "androidx.paging:paging-compose:$pagingJetpackComposeVersion" }

    val constraintLayoutDependency by lazy { "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion" }
    val constraintLayoutComposeDependency by lazy { "androidx.constraintlayout:constraintlayout-compose:$constraintLayoutComposeVersion" }

    val composeLiveDataDependency by lazy { "androidx.compose.runtime:runtime-livedata:$composeLiveDataVersion" }
    val liveDataDependency by lazy { "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion" }
    val viewModelDependency by lazy { "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion" }
}