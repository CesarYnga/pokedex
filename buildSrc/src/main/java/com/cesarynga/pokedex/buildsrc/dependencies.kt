package com.cesarynga.pokedex.buildsrc

object Versions {
    const val ktlint = "0.45.2"
}

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.2.1"
    const val jdkDesugar = "com.android.tools:desugar_jdk_libs:1.1.5"

    const val junit = "junit:junit:4.13.2"

    const val material3 = "com.google.android.material:material:1.6.0-alpha03"
    const val truth = "com.google.truth:truth:1.1.3"

    object Kotlin {
        private const val version = "1.6.21"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.6.1"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.4.1"
        const val coreKtx = "androidx.core:core-ktx:1.7.0"
        const val palette = "androidx.palette:palette-ktx:1.0.0"

        object Activity {
            const val compose = "androidx.activity:activity-compose:1.4.0"
        }

        object Compose {
            const val snapshot = ""
            private const val version = "1.2.0-beta02"

            const val ui = "androidx.compose.ui:ui:$version"
            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"
            const val material = "androidx.compose.material:material:$version"
            const val materialIconsExtended = "androidx.compose.material:material-icons-extended:$version"
            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val runtimeLivedata = "androidx.compose.runtime:runtime-livedata:$version"
            const val uiTooling = "androidx.compose.ui:ui-tooling:$version"
            const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:$version"
            const val test = "androidx.compose.ui:ui-test:$version"
            const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"
            const val uiText = "androidx.compose.ui:ui-text-google-fonts:$version"
            const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$version"
            const val uiUtil = "androidx.compose.ui:ui-util:${version}"
            const val viewBinding = "androidx.compose.ui:ui-viewbinding:$version"

            object Material3 {
                const val snapshot = ""
                private const val version = "1.0.0-alpha12"

                const val material3 = "androidx.compose.material3:material3:$version"
            }
        }

        object Navigation {
            private const val version = "2.4.2"
            const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:$version"
            const val uiKtx = "androidx.navigation:navigation-ui-ktx:$version"
            const val compose = "androidx.navigation:navigation-compose:$version"
        }

        object Test {
            private const val version = "1.4.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"

            object Ext {
                private const val version = "1.1.3"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }

            const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
        }

        object Lifecycle {
            private const val version = "2.4.1"

            const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val viewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:$version"
        }
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val converterGson = "com.squareup.retrofit2:converter-gson:$version"
    }

    object OkHttp {
        private const val version = "4.9.3"
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:$version"
    }

    object Coil {
        private const val version = "2.1.0"
        const val compose = "io.coil-kt:coil-compose:$version"
    }

    object Koin {
        private const val version = "3.1.5"
        const val androidXCompose = "io.insert-koin:koin-androidx-compose:$version"
    }
}
