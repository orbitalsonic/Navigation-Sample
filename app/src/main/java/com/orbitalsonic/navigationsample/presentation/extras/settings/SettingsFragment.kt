package com.orbitalsonic.navigationsample.presentation.extras.settings

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import com.orbitalsonic.navigationsample.BuildConfig
import com.orbitalsonic.navigationsample.R
import com.orbitalsonic.navigationsample.common.koin.DiComponent
import com.orbitalsonic.navigationsample.databinding.FragmentSettingsBinding
import com.orbitalsonic.navigationsample.helpers.listener.RapidSafeListener.setOnRapidClickSafeListener
import com.orbitalsonic.navigationsample.helpers.navigation.navigateTo
import com.orbitalsonic.navigationsample.helpers.navigation.popFrom
import com.orbitalsonic.navigationsample.helpers.settings.bugReport
import com.orbitalsonic.navigationsample.helpers.settings.feedback
import com.orbitalsonic.navigationsample.helpers.settings.privacyPolicy
import com.orbitalsonic.navigationsample.helpers.settings.rateUs
import com.orbitalsonic.navigationsample.helpers.settings.shareApp
import com.orbitalsonic.navigationsample.helpers.settings.termsAndConditions
import com.orbitalsonic.navigationsample.presentation.base.fragments.BaseFragment

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val diComponent by lazy { DiComponent() }

    override fun onViewCreated() {
        initValues()
        setupClicks()
    }

    @SuppressLint("SetTextI18n")
    private fun initValues() {
        binding.mtvVersion.text = "V " + BuildConfig.VERSION_NAME
        val isCheck = diComponent.sharedPrefManager.isAppDarkModeEnabled == 0
        binding.darkModeSwitch.isChecked = isCheck
    }

    private fun setupClicks() {
        binding.apply {
            btnBack.setOnRapidClickSafeListener { popFrom(R.id.settingsFragment) }
            btnLanguage.setOnRapidClickSafeListener { onLanguage() }
            btnShare.setOnRapidClickSafeListener { onShareThisApp() }
            btnRate.setOnRapidClickSafeListener { onRateApp() }
            btnPrivacy.setOnRapidClickSafeListener { onPrivacyPolicy() }
            btnReportBug.setOnRapidClickSafeListener { onReportBug() }
            btnFeedback.setOnRapidClickSafeListener { onFeedback() }
            btnTerms.setOnRapidClickSafeListener { onTermsCondition() }

            binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
                setDarkMode(isChecked)
            }
        }
    }

    private fun deviceInfo(): String {
        val stringBuilder = StringBuilder()

        stringBuilder.append("Please mention issue...: \n\n\n\n")

        // Device Info
        stringBuilder.append("Device Info \n")
        stringBuilder.append("Device: ${Build.DEVICE} \n")
        stringBuilder.append("Device Model: ${Build.MODEL} \n")
        stringBuilder.append("Device BRAND: ${Build.BRAND} \n")
        stringBuilder.append("Device MANUFACTURER: ${Build.MANUFACTURER} \n")
        stringBuilder.append("Version Name: ${BuildConfig.VERSION_NAME} \n")
        stringBuilder.append("Version Code: ${BuildConfig.VERSION_CODE} \n")


        return stringBuilder.toString()
    }

    private fun onLanguage(){
        val action = SettingsFragmentDirections.actionSettingsFragmentToLanguageFragment()
        navigateTo(R.id.settingsFragment,action)
    }

    private fun setDarkMode(isDark: Boolean) {
        diComponent.sharedPrefManager.isAppDarkModeEnabled =
            if (isDark) 0 else 1

        AppCompatDelegate.setDefaultNightMode(
            if (isDark)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun onRateApp() {
        activity.rateUs()
    }

    private fun onShareThisApp() {
        activity.shareApp()
    }

    private fun onPrivacyPolicy() {
        activity.privacyPolicy()
    }

    private fun onTermsCondition() {
        activity.termsAndConditions()
    }

    private fun onReportBug() {
        activity.bugReport(deviceInfo())
    }

    private fun onFeedback() {
        activity.feedback()
    }
}