import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.timemanager.pomodorofocus"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.timemanager.pomodorofocus"
        minSdk = 24
        targetSdk = 36
        versionCode = 2
        versionName = project.findProperty("versionName") as? String ?: "v0.1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val fileName =
                    "PomodoroFocus-${variant.flavorName}-${variant.buildType.name}-${variant.versionName}-code${variant.versionCode}.apk"
                output.outputFileName = fileName
            }
    }

    signingConfigs {
        create("release_config") {
            // 1. Try Environment Variables (CI/CD)
            var storeFilePath = System.getenv("RELEASE_STORE_FILE")
            var storePasswordVal = System.getenv("RELEASE_STORE_PASSWORD")
            var keyAliasVal = System.getenv("RELEASE_KEY_ALIAS")
            var keyPasswordVal = System.getenv("RELEASE_KEY_PASSWORD")

            // 2. If Env vars missing, try local.properties (Local Build)
            if (storeFilePath == null || storePasswordVal == null) {
                val keystorePropertiesFile = rootProject.file("local.properties")
                if (keystorePropertiesFile.exists()) {
                    val properties = Properties()
                    properties.load(keystorePropertiesFile.inputStream())

                    storeFilePath = properties.getProperty("RELEASE_STORE_FILE")
                    storePasswordVal = properties.getProperty("RELEASE_STORE_PASSWORD")
                    keyAliasVal = properties.getProperty("RELEASE_KEY_ALIAS")
                    keyPasswordVal = properties.getProperty("RELEASE_KEY_PASSWORD")
                }
            }

            if (!storeFilePath.isNullOrEmpty() && !storePasswordVal.isNullOrEmpty()) {
                storeFile = file(storeFilePath)
                storePassword = storePasswordVal
                keyAlias = keyAliasVal
                keyPassword = keyPasswordVal
                enableV1Signing = true
                enableV2Signing = true
            }
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }

        release {
            val config = signingConfigs.getByName("release_config")
            if (config.storeFile != null) {
                signingConfig = config
            }
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        create("profileable") {
            isProfileable = true
            isDebuggable = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    flavorDimensions += "performance"

    productFlavors {
        create("performance") {
            dimension = "performance"
            applicationIdSuffix = ".perfTest"
            versionNameSuffix = "-perfTest"
            buildConfigField("boolean", "IS_PERFORMANCE_TEST_ENABLED", "true")
            buildConfigField("Long", "THREAD_SLEEP_MS", "3000L")
        }
        create("_default") {
            dimension = "performance"
            buildConfigField("boolean", "IS_PERFORMANCE_TEST_ENABLED", "false")
            buildConfigField("Long", "THREAD_SLEEP_MS", "null")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {

    // Core & Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.constraintlayout)

    // Compose Tools
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Navigation
    implementation(libs.androidx.navigation)
    implementation(libs.androidx.hilt.navigation)

    // Hilt - Dependency Injection
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
    kapt(libs.kotlin.metadata.jvm) // Kapt Metadata (Fix for Hilt/Kotlin version mismatch)

    // Room - Database
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.paging.room)
    ksp(libs.androidx.room.compiler)

    // Paging
    implementation(libs.androidx.paging.common.android)
    implementation(libs.androidx.paging.compose)

    // DataStore
    implementation(libs.androidx.datastore.core.android)
    implementation(libs.androidx.datastore.preferacndes)

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)

    // Instrumented Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
}
