package com.betan.betanstoktakip.presentation.showAllProduct

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
    private val viewModel: ShowAllProductViewModel by viewModels()
    private val productList = arrayListOf<ProductModel>()
    private lateinit var productAdapter: AllProductAdapter

    override fun setupViews(savedInstanceState: Bundle?) {
        setupRecyclerView()
        initObserver()
        viewModel.getAllProduct()
    }

    private fun setupRecyclerView() {
        productAdapter = AllProductAdapter(productList)
        binding.recyclerView.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initObserver() {
        viewModel.getAllProductsLiveData.observe(viewLifecycleOwner) { result ->
            println(" Ürünler: ${result.size}")

            productList.clear()
            productList.addAll(result.filterNotNull())
            productAdapter.notifyDataSetChanged()
        }
    }

    override fun collectData() {
        collect(viewModel.failState, ::collectFailState)
    }
}
