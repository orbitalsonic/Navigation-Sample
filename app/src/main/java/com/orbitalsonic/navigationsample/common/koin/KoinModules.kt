package com.orbitalsonic.navigationsample.common.koin

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.orbitalsonic.navigationsample.common.firebase.RemoteConfiguration
import com.orbitalsonic.navigationsample.common.network.InternetManager
import com.orbitalsonic.navigationsample.storage.preferences.SharedPrefManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.lazyModule

private val sharedPrefModule = lazyModule {
    single {
        SharedPrefManager(
            androidContext().getSharedPreferences(
                "app_preferences",
                Application.MODE_PRIVATE
            )
        )
    }
}

private val internetModules = lazyModule {
    single { InternetManager(androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) }
}

private val firebaseModule = lazyModule {
    single {
        RemoteConfiguration(
            get(),
            androidContext().getSharedPreferences("firebase_preferences", Application.MODE_PRIVATE)
        )
    }
}

val lazyModulesList = listOf(
    sharedPrefModule,
    internetModules,
    firebaseModule
)