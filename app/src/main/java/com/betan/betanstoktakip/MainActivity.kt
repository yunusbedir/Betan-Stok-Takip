package com.betan.betanstoktakip

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.betan.betanstoktakip.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    }

    private val navController: NavController
        get() = navHostFragment.navController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Firebase.auth.addAuthStateListener {
            setupNavigation()
        }
        setupNavigation()

    }


    private fun setupNavigation() {
        if (Firebase.auth.currentUser != null && navController.graph.id != R.navigation.nav_graph_main_fragment) {
            navController.setGraph(R.navigation.nav_graph_main_fragment, null)
        } else if (navController.graph.id != R.id.nav_graph_auth) {
            navController.setGraph(R.navigation.nav_graph_auth, null)
        }
    }
}