package com.betan.betanstoktakip.presentation.saleshistories

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.betan.betanstoktakip.core.base.BaseFragment
import com.betan.betanstoktakip.core.extensions.toMoney
import com.betan.betanstoktakip.core.helper.viewLifecycleLazy
import com.betan.betanstoktakip.databinding.FragmentSalesHistoriesBinding
import com.betan.betanstoktakip.presentation.saleshistories.adapter.SalesHistoriesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SalesHistoriesFragment : BaseFragment<FragmentSalesHistoriesBinding>(
    bindingInflater = FragmentSalesHistoriesBinding::inflate
) {

    private val viewModel: SalesHistoriesViewModel by viewModels()

    private val salesHistoriesAdapter: SalesHistoriesAdapter by viewLifecycleLazy {
        SalesHistoriesAdapter()
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        binding.recyclerViewSalesHistories.adapter = salesHistoriesAdapter
        viewModel.invoke(SalesHistoriesContract.Action.GetSalesHistories)
    }

    override fun collectData() {
        collect(viewModel.uiState, ::collectUiState)
    }

    private fun collectUiState(uiState: SalesHistoriesContract.UiState) {
        salesHistoriesAdapter.submitList(uiState.items)
        binding.editTextTotalPrice.setText(uiState.totalNormalPrice.toMoney())
        binding.editTextTotalPaidPrice.setText(uiState.totalPaidPrice.toMoney())
    }
}