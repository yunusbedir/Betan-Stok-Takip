package com.betan.betanstoktakip.presentation.main

import android.os.Bundle
import com.betan.betanstoktakip.core.base.BaseFragment
import com.betan.betanstoktakip.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(
    bindingInflater = FragmentMainBinding::inflate
) {
    override fun setupViews(savedInstanceState: Bundle?) {
    }

    override fun collectData() {

    }
}