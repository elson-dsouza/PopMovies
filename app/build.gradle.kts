plugins {
    id(AndroidDeps.androidApplicationPlugin)
    id(KotlinDeps.kotlinAndroidPlugin)
    id(KotlinDeps.kotlinKaptPlugin)
    id(RealmDeps.realmPlugin)
    id(KotlinDeps.kotlinParcelizePlugin)
}

val daggerVersion = "2.44.2"
val glideVersion = "4.11.0"

android {
    compileSdk = 33

    buildFeatures {
        dataBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = JetpackDeps.kotlinCompilerExtensionVersion
    }

    defaultConfig {
        applicationId = "com.example.elson.popmovies"
        minSdk = 23
        targetSdk = 33
        versionCode = 2
        versionName = "2.0"

        buildConfigField("String", "TMDB_V3_API_TOKEN", "\"${AppConfigurations.tmdbV3ApiToken}\"")
        buildConfigField("String", "TMDB_V4_API_TOKEN", "\"${AppConfigurations.tmdbV4ApiToken}\"")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles.add(getDefaultProguardFile("proguard-android.txt"))
            proguardFiles.add(file("proguard-rules.pro"))
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    namespace = "com.example.elson.popmovies"
}

dependencies {
    implementation("com.google.android.material:material:1.8.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.google.firebase:firebase-appindexing:20.0.0")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.security:security-crypto:1.1.0-alpha04")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("com.google.dagger:dagger:$daggerVersion")
    implementation("com.google.dagger:dagger-android:$daggerVersion")
    implementation("com.google.dagger:dagger-android-support:$daggerVersion")
    implementation("androidx.window:window:1.0.0")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
    kapt("com.google.dagger:dagger-android-processor:$daggerVersion")

    debugImplementation("com.facebook.stetho:stetho:1.6.0")
    debugImplementation("com.facebook.stetho:stetho-okhttp3:1.6.0")
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")

    implementation("com.github.bumptech.glide:glide:$glideVersion")
    implementation("com.github.bumptech.glide:compose:1.0.0-alpha.1")

    implementation(RealmDeps.realmDependency)

    implementation(KotlinDeps.kotlinStdLibDependency)

    implementation("androidx.annotation:annotation:1.5.0")
    implementation("androidx.appcompat:appcompat:1.6.0")
    implementation("androidx.fragment:fragment-ktx:1.5.5")
    implementation("androidx.activity:activity-compose:1.6.1")

    implementation(platform(JetpackDeps.composeBomDependency))
    implementation(JetpackDeps.material3Dependency)
    implementation(JetpackDeps.pagingDependency)
    implementation(JetpackDeps.pagingComposeDependency)
    implementation(JetpackDeps.constraintLayoutDependency)
    implementation(JetpackDeps.constraintLayoutComposeDependency)
    implementation(JetpackDeps.composeLiveDataDependency)
    implementation(JetpackDeps.liveDataDependency)
    implementation(JetpackDeps.viewModelDependency)

    implementation("com.google.accompanist:accompanist-webview:0.28.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("androidx.compose.material3:material3-window-size-class")
}

repositories {
    mavenCentral()
}
