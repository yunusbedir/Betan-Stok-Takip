package com.betan.betanstoktakip.presentation.main

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
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
        setupToolbar()
        setupNavController()
        setupListeners()
    }

    override fun collectData() {
        // Veri toplama işlemleri burada yapılabilir
    }

    private fun setupListeners() {
        binding.textViewLogout.click {
            viewModel.invoke(MainContract.Action.Logout)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {

            setNavigationIcon(R.drawable.ic_back_button)
            setNavigationOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

        }

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.addProductFragment,
                R.id.showAllProductFragment,
                R.id.showProductFragment,
                R.id.showStockFragment,
                R.id.shoppingCartFragment
            )
        )

        // Toolbar ile navController'ı bağla
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        // Fragmentlarda başlık değiştirilmesinin engellenmesi için:
        navController.addOnDestinationChangedListener { _, _, _ ->
            binding.toolbar.title = "Betan Stok Takip"
        }
    }

    // NavController setup
    private fun setupNavController() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // BottomNavigationView ile navController'ı bağla
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}
