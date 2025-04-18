plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
//    alias(libs.plugins.room)
}

android {
    namespace = "com.devrachit.ken"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.devrachit.ken"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    flavorDimensions += "version"
    productFlavors {
        create("dev") {
            dimension = "version"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"

        }
        create("staging") {
            dimension = "version"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"

        }
        create("prod") {
            dimension = "version"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
//    room {
////        schemaDirectory("${projectDir.absolutePath.replace("\\", "/")}/schemas")
//        schemaDirectory("${projectDir.absolutePath.replace("\\", "/").replace(" ", "\\ ")}/schemas")
//    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.compose.navigation)
    implementation(libs.androidx.hilt.compose.navigation)

    implementation(libs.hilt)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    ksp(libs.hilt.compiler)

    implementation(libs.ktor.client.core)

    implementation(libs.datastore)
    implementation(libs.datastore.preferences)
    implementation(libs.protobuf.javalite)

    implementation(libs.coil)
    implementation(libs.timber)
    implementation(libs.googlePlayServiceAuth)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.sdp.android)

    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.gson)
    implementation(libs.scalar)

    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(libs.accompanist.pager.indicators)
    implementation(libs.accompanist.pager)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.shimmer)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.datastore)
    implementation(libs.datastore.preferences)

    // Glance for AppWidgets
    implementation(libs.androidx.glance)
    implementation(libs.androidx.glance.appwidget)
    implementation(libs.androidx.glance.material3)

    // For WorkManager and Hilt integration
//    implementation(libs.androidx.work.runtime.ktx)
//    implementation(libs.androidx.hilt.work)

    // TODO: Remove these chucker dependencies  
    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0") // For Debug
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:4.0.0") // No-op in release
}