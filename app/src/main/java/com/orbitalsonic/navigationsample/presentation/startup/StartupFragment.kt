package com.orbitalsonic.navigationsample.presentation.startup

import com.orbitalsonic.navigationsample.R
import com.orbitalsonic.navigationsample.common.koin.DiComponent
import com.orbitalsonic.navigationsample.databinding.FragmentStartupBinding
import com.orbitalsonic.navigationsample.helpers.handlers.withDelay
import com.orbitalsonic.navigationsample.helpers.lifecycle.launchWhenResumed
import com.orbitalsonic.navigationsample.helpers.navigation.navigateTo
import com.orbitalsonic.navigationsample.presentation.base.fragments.BaseFragment
import org.koin.android.ext.android.getKoin
import org.koin.core.runOnKoinStarted

class StartupFragment :
    BaseFragment<FragmentStartupBinding>(FragmentStartupBinding::inflate) {

    private val diComponent by lazy { DiComponent() }

    override fun onViewCreated() {
        getKoin().runOnKoinStarted {
            getRemoteConfigValues()
            withDelay(2000) {
                moveDecision()
            }
        }

    }

    private fun getRemoteConfigValues() {
        diComponent.remoteConfiguration.checkRemoteConfig { fetchSuccessfully -> }
    }

    private fun moveDecision() {
        if (diComponent.sharedPrefManager.isFirstTimeEntrance) {
            firstTimeEnter()
        } else {
            secondTimeEnter()
        }
    }

    private fun firstTimeEnter() {
        launchWhenResumed {
            try {
                val action =
                    StartupFragmentDirections.actionStartupFragmentToStartupLanguageFragment()
                navigateTo(R.id.startupFragment, action)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun secondTimeEnter() {
        launchWhenResumed {
            try {
                (activity as? StartupActivity)?.moveActivity()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}