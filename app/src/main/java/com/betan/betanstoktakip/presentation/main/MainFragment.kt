package com.betan.betanstoktakip.presentation.main

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.betan.betanstoktakip.R
import com.betan.betanstoktakip.core.base.BaseFragment
import com.betan.betanstoktakip.core.extensions.click
import com.betan.betanstoktakip.core.helper.viewLifecycleLazy
import com.betan.betanstoktakip.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(
    bindingInflater = FragmentMainBinding::inflate
) {

    private val viewModel: MainViewModel by viewModels()

    private val navHostFragment: NavHostFragment by viewLifecycleLazy {
        childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    }

    private val navController: NavController
        get() = navHostFragment.navController

    private val destinationChangedListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            listOf(
                R.id.showProductFragment,
                R.id.shoppingCartFragment,
                R.id.addProductFragment,
                R.id.showStockFragment
            ).contains(destination.id).let {
                binding.bottomNavigationView.isVisible = it
                binding.textViewLogout.isVisible = it
                binding.viewBack.isVisible = it.not()
            }
        }

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
        binding.viewBack.click {
            navController.navigateUp()
        }
        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

    private fun setupNavController() {
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onDestroyView() {
        navController.removeOnDestinationChangedListener(destinationChangedListener)
        super.onDestroyView()
    }
}