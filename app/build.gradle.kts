plugins {
    id ("com.android.application")
    id("kotlin-android")
    kotlin("android")
    kotlin("kapt")
    id ("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = ConfigData.compileSdkVersion

    defaultConfig {
        applicationId = "com.example.carefertask"
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        versionCode = ConfigData.versionCode
        versionName = ConfigData.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    javaVersions()
}

fun com.android.build.gradle.internal.dsl.BaseAppModuleExtension.javaVersions() {
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")

    // android ui
    implementation(AndroidUI.appCompact)
    implementation(AndroidUI.materialDesign)
    implementation(AndroidUI.constraintLayout)
    implementation(AndroidUI.recycleView)
    implementation(Utils.timber)

    // Retrofit
    implementation (Retrofit.retrofit)
    implementation (Retrofit.retrofitConverter)
    implementation (Retrofit.logInterceptor)

    // Coroutines
    implementation(Coroutines.coroutines)

    // Hilt
    implementation(Hilt.hiltAndroid)
    kapt(Hilt.hiltCompiler)

    //Navigation
    implementation(NavigationComponent.navigationFragmentKTX)
    implementation(NavigationComponent.navigationUIKTX)

    //Lifecycle
    implementation(Lifecycle.coreLifeCycle)
    implementation(Lifecycle.fragmentLifeCycle)
    implementation(Lifecycle.activityLifeCycle)
    implementation(Lifecycle.runtimeLifeCycle)
    implementation(Lifecycle.livedataLifeCycle)
    implementation(Lifecycle.viewModelLifeCycle)

}