plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    buildToolsVersion = libs.versions.buildTools.get()
    namespace = libs.versions.appId.get()
    compileSdk = libs.versions.androidApiTarget.get().toInt()

    defaultConfig {
        applicationId = libs.versions.appId.get()
        minSdk = libs.versions.androidApiMin.get().toInt()
        targetSdk = libs.versions.androidApiTarget.get().toInt()
        versionCode = 4
        versionName = "1.1.2"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility(libs.versions.java.get())
        targetCompatibility(libs.versions.java.get())
    }

    kotlinOptions.jvmTarget = libs.versions.java.get()

    buildFeatures.compose = true

    composeOptions.kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()

    packaging.resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
}

dependencies {
    implementation(projects.core)

    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.activityCompose)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.animation)
    implementation(libs.compose.foundation)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.materialIcons)

    implementation(libs.accompanist.systemUiController)

    implementation(libs.navigation.compose)

    implementation(libs.lifecycle.runtimeKtx)
    implementation(libs.lifecycle.runtimeCompose)
    implementation(libs.lifecycle.viewmodelCompose)

    implementation(libs.hilt.android)
    kapt(libs.hilt.androidCompiler)

    implementation(libs.hilt.extCompose)
    kapt(libs.hilt.extCompiler)

    implementation(libs.coroutines.android)

    implementation(libs.datastore.preferences)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    testImplementation(libs.junit4)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.mockito)
    testImplementation(libs.turbine)
}