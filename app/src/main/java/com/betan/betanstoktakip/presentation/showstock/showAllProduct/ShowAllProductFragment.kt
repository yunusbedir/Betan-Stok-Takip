package com.betan.betanstoktakip.presentation.showstock.showAllProduct

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.betan.betanstoktakip.core.base.BaseFragment
import com.betan.betanstoktakip.databinding.FragmentShowAllProductBinding
import com.betan.betanstoktakip.domain.model.ProductModel
import com.betan.betanstoktakip.presentation.showstock.ShowStockViewModel
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class ShowAllProductFragment : BaseFragment<FragmentShowAllProductBinding>(
    bindingInflater = FragmentShowAllProductBinding::inflate
) {
    private val viewModel: ShowStockViewModel by viewModels()
    private val productList = arrayListOf<ProductModel>()
    private lateinit var productAdapter: AllProductAdapter // Adapter'i tanımladık

    override fun setupViews(savedInstanceState: Bundle?) {
        setupRecyclerView() // RecyclerView ayarla
        initObserver() // LiveData observer ekle
        viewModel.getAllProduct() // Verileri getir
    }

    private fun setupRecyclerView() {
        productAdapter = AllProductAdapter(productList) // Adapter'i başlat
        binding.recyclerView.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext()) // Dikey listeleme
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.getAllProductsLiveData.observe(viewLifecycleOwner) { result ->
            println(" Ürünler: ${result.size}") // Log ile kontrol et

            productList.clear()
            productList.addAll(result.filterNotNull()) // Null olanları filtrele
            productAdapter.notifyDataSetChanged() // RecyclerView'i güncelle
        }
    }

    override fun collectData() {
        collect(viewModel.failState, ::collectFailState)
    }
}
