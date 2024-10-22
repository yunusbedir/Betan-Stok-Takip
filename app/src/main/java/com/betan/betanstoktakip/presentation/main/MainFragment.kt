package com.betan.betanstoktakip.presentation.main

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.betan.betanstoktakip.R
import com.betan.betanstoktakip.core.base.BaseFragment
import com.betan.betanstoktakip.core.extensions.click
import com.betan.betanstoktakip.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(
    bindingInflater = FragmentMainBinding::inflate
) {

    private val viewModel: MainViewModel by viewModels()

    override fun setupViews(savedInstanceState: Bundle?) {
        setupNavController()
        setupListeners()
    }

    override fun collectData() {

    }

    private fun setupListeners() {
        binding.textViewLogout.click {
            viewModel.invoke(MainContract.Action.Logout)
        }
    }

    private fun setupNavController() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

    }
}