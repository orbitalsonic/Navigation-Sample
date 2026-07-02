package com.orbitalsonic.navigationsample.presentation.extras.language.ui

import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.orbitalsonic.navigationsample.R
import com.orbitalsonic.navigationsample.common.koin.DiComponent
import com.orbitalsonic.navigationsample.common.repository.LanguageRepository
import com.orbitalsonic.navigationsample.databinding.FragmentLanguageBinding
import com.orbitalsonic.navigationsample.helpers.listener.RapidSafeListener.setOnRapidClickSafeListener
import com.orbitalsonic.navigationsample.helpers.locale.applyLanguage
import com.orbitalsonic.navigationsample.helpers.navigation.popFrom
import com.orbitalsonic.navigationsample.helpers.ui.hideKeyboard
import com.orbitalsonic.navigationsample.presentation.base.fragments.BaseFragment
import com.orbitalsonic.navigationsample.presentation.extras.language.adapter.AdapterLanguage
import com.orbitalsonic.navigationsample.presentation.extras.language.model.LanguageItem
import com.orbitalsonic.navigationsample.storage.provider.DpLanguages

class LanguageFragment :
    BaseFragment<FragmentLanguageBinding>(FragmentLanguageBinding::inflate) {

    private val dpLanguages by lazy { DpLanguages() }
    private val sharedPrefManager by lazy { DiComponent().sharedPrefManager }

    private val adapterLanguage by lazy {
        AdapterLanguage { languageItem ->
            onLanguageSelected(languageItem)
        }
    }

    private var selectedLanguage: LanguageItem? = null

    override fun onViewCreated() {
        initRecyclerView()
        setupClicks()
        setupListener()
    }

    private fun setupClicks() {
        binding.btnBack.setOnRapidClickSafeListener {
            popFrom(R.id.languageFragment)
        }

        binding.btnApply.setOnRapidClickSafeListener {
            onApplyClick()
        }
    }

    private fun setupListener() {
        binding.etSearchText.apply {
            doAfterTextChanged {
                searchLanguage(it.toString())
            }
        }
    }

    private fun initRecyclerView() {
        binding.languageRecyclerview.adapter = adapterLanguage
        loadLanguages()
    }

    private fun loadLanguages() {
        val list = dpLanguages.getLanguagesList(sharedPrefManager.appLanguageCode)
        adapterLanguage.submitList(list)
    }

    private fun onApplyClick() {
        selectedLanguage?.let {
            if (sharedPrefManager.appLanguageCode != it.languageCode) {

                sharedPrefManager.appLanguageCode = it.languageCode

                activity?.hideKeyboard()

                applyLanguage(sharedPrefManager.appLanguageCode)
            } else {
                popFrom(R.id.languageFragment)
            }
        } ?: run {
            popFrom(R.id.languageFragment)
        }
    }

    private fun onLanguageSelected(languageItem: LanguageItem) {

        selectedLanguage = languageItem

        val updatedList = dpLanguages.getLanguagesList(languageItem.languageCode)
        adapterLanguage.submitList(updatedList)
    }

    private fun searchLanguage(query: String) {
        LanguageRepository.searchLanguage(query, sharedPrefManager.appLanguageCode) {
            try {
                if (it.isEmpty()) {
                    binding.tvNothingFound.visibility = View.VISIBLE
                } else {
                    binding.tvNothingFound.visibility = View.GONE
                }
                adapterLanguage.submitList(it)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

}