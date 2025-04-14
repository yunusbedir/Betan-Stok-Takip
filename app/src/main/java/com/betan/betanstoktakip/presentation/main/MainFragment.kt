package com.betan.betanstoktakip.presentation.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        // Toolbar'ı Fragment içinde kullanabilmek için Activity'ye set et
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)

        // Toolbar'ın geri butonunu ayarla
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }


        // Navigation Controller ve AppBar Configuration bağlantısı
        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
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

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.toolbar.setNavigationIcon(R.drawable.ic_back_button)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        // Fragment başlığını sabit tut
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
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mainFragment,
                R.id.loginFragment,
                R.id.addProductFragment,
                R.id.showStockFragment,
                R.id.shoppingCartFragment,
                R.id.showProductFragment -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                }

                else -> {
                    binding.bottomNavigationView.visibility = View.GONE
                }
            }
        }
    }
}
