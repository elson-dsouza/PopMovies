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
    implementation("androidx.annotation:annotation:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("com.google.firebase:firebase-appindexing:20.0.0")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.fragment:fragment-ktx:1.5.4")
    implementation("androidx.security:security-crypto:1.1.0-alpha04")
    implementation("com.github.bumptech.glide:glide:$glideVersion")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("com.google.dagger:dagger:$daggerVersion")
    implementation("com.google.dagger:dagger-android:$daggerVersion")
    implementation("com.google.dagger:dagger-android-support:$daggerVersion")
    kapt("com.google.dagger:dagger-compiler:$daggerVersion")
    kapt("com.google.dagger:dagger-android-processor:$daggerVersion")

    debugImplementation("com.facebook.stetho:stetho:1.6.0")
    debugImplementation("com.facebook.stetho:stetho-okhttp3:1.6.0")
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.8.1")

    implementation(RealmDeps.realmDependency)
    implementation(KotlinDeps.kotlinStdLibDependency)
    implementation(PagingDeps.pagingDependency)
}

repositories {
    mavenCentral()
}
