package com.betan.betanstoktakip.presentation.showstock.showSellFragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.betan.betanstoktakip.core.base.BaseFragment
import com.betan.betanstoktakip.databinding.FragmentShowSellBinding
import com.betan.betanstoktakip.domain.model.ShowSellModel
import com.betan.betanstoktakip.presentation.showstock.ShowStockViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowSellFragment : BaseFragment<FragmentShowSellBinding>(
    bindingInflater = FragmentShowSellBinding::inflate
) {
    private val viewModel: ShowStockViewModel by viewModels()
    private val salesList = arrayListOf<ShowSellModel>()
    private lateinit var showSellAdapter: ShowSellAdapter

    override fun setupViews(savedInstanceState: Bundle?) {
        setupRecyclerView()
        initObserver()}



    private fun setupRecyclerView() {
        showSellAdapter = ShowSellAdapter(salesList) // Adapter'i başlat
        binding.recyclerView.apply {
            adapter = showSellAdapter
            layoutManager = LinearLayoutManager(requireContext()) // Dikey listeleme
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.getSalesExportLiveData.observe(viewLifecycleOwner) { result ->
            println(" Ürünler: ${result.size}") // Log ile kontrol et

            salesList.clear()
            salesList.addAll(result.filterNotNull()) // Null olanları filtrele
            showSellAdapter.notifyDataSetChanged() // RecyclerView'i güncelle
        }
    }

    override fun collectData() {
        collect(viewModel.failState, ::collectFailState)
    }


}