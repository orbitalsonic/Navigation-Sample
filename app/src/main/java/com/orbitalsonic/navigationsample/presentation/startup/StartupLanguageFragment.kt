package com.orbitalsonic.navigationsample.presentation.startup

import com.orbitalsonic.navigationsample.common.koin.DiComponent
import com.orbitalsonic.navigationsample.databinding.FragmentStartupLanguageBinding
import com.orbitalsonic.navigationsample.helpers.listener.RapidSafeListener.setOnRapidClickSafeListener
import com.orbitalsonic.navigationsample.helpers.locale.applyLanguage
import com.orbitalsonic.navigationsample.presentation.base.fragments.BaseFragment
import com.orbitalsonic.navigationsample.presentation.extras.language.adapter.AdapterLanguage
import com.orbitalsonic.navigationsample.presentation.extras.language.model.LanguageItem
import com.orbitalsonic.navigationsample.storage.provider.DpLanguages

class StartupLanguageFragment :
    BaseFragment<FragmentStartupLanguageBinding>(FragmentStartupLanguageBinding::inflate) {

    private val dpLanguages by lazy { DpLanguages() }
    private val diComponent by lazy { DiComponent() }

    private val adapterLanguage by lazy {
        AdapterLanguage { languageItem ->
            onLanguageSelected(languageItem)
        }
    }

    private var selectedLanguage: LanguageItem? = null

    override fun onViewCreated() {
        initRecyclerView()
        setupClicks()
    }

    private fun initRecyclerView() {
        binding.languageRecyclerview.adapter = adapterLanguage
        loadLanguages()
    }

    private fun loadLanguages() {
        val list = dpLanguages.getLanguagesList(diComponent.sharedPrefManager.appLanguageCode)
        adapterLanguage.submitList(list)
    }

    private fun onLanguageSelected(languageItem: LanguageItem) {
        selectedLanguage = languageItem

        val updatedList = dpLanguages.getLanguagesList(languageItem.languageCode)
        adapterLanguage.submitList(updatedList)
    }

    private fun setupClicks() {
        binding.btnContinue.setOnRapidClickSafeListener {
            diComponent.sharedPrefManager.isFirstTimeEntrance = false

            if (diComponent.sharedPrefManager.appLanguageCode != "en") {
                applyLanguage(diComponent.sharedPrefManager.appLanguageCode)
            }

            (activity as? StartupActivity)?.moveActivity()
        }
    }

}