package com.betan.betanstoktakip.presentation.showSellFragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.betan.betanstoktakip.core.base.BaseFragment
import com.betan.betanstoktakip.databinding.FragmentShowSellBinding
import com.betan.betanstoktakip.domain.model.SoldProductsModel
import com.betan.betanstoktakip.presentation.showstock.ShowStockContract
import com.betan.betanstoktakip.presentation.showstock.ShowStockViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowSellFragment : BaseFragment<FragmentShowSellBinding>(
    bindingInflater = FragmentShowSellBinding::inflate
) {
    private val viewModel: ShowStockViewModel by viewModels()
    private val salesList = arrayListOf<SoldProductsModel>()
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
        viewModel.getSalesLiveData.observe(viewLifecycleOwner) { result ->
            println("Satışlar: ${result.size}") // Kontrol logu
            salesList.clear()
            salesList.addAll(result.filterNotNull())
            showSellAdapter.notifyDataSetChanged()

            val totalSalesAmount = salesList.sumOf { sale ->
                sale.items.sumOf { item ->
                    (item.salePrice ?: 0.0) * (item.amount ?: 0)
                }
            }


            binding.textViewTotalSellAmount.text = "Toplam Satış Tutarı: %.2f ₺".format(totalSalesAmount)
        }


        viewModel.invoke(ShowStockContract.Action.GetSales)
    }

    override fun collectData() {
        collect(viewModel.failState, ::collectFailState)
    }


}