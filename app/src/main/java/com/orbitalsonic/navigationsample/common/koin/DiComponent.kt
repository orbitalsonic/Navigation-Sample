package com.orbitalsonic.navigationsample.common.koin

import com.orbitalsonic.navigationsample.common.firebase.RemoteConfiguration
import com.orbitalsonic.navigationsample.common.network.InternetManager
import com.orbitalsonic.navigationsample.storage.preferences.SharedPrefManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DiComponent : KoinComponent {
    val internetManager by inject<InternetManager>()
    val sharedPrefManager by inject<SharedPrefManager>()

    // Remote Configuration
    val remoteConfiguration by inject<RemoteConfiguration>()
}