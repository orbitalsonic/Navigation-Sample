package com.orbitalsonic.navigationsample.presentation.home

import com.orbitalsonic.navigationsample.R
import com.orbitalsonic.navigationsample.databinding.FragmentHomeBinding
import com.orbitalsonic.navigationsample.helpers.navigation.navigateTo
import com.orbitalsonic.navigationsample.presentation.base.fragments.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override fun onViewCreated() {
        setupClicks()
    }

    private fun setupClicks(){
        binding.btnSettings.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSettingsFragment()
            navigateTo(R.id.homeFragment,action)
        }
    }
}