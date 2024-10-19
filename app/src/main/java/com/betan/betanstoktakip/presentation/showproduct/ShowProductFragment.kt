package com.betan.betanstoktakip.presentation.showproduct

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.betan.betanstoktakip.R
import com.betan.betanstoktakip.core.base.BaseFragment
import com.betan.betanstoktakip.core.extensions.click
import com.betan.betanstoktakip.core.extensions.orEmpty
import com.betan.betanstoktakip.databinding.FragmentShowProductBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowProductFragment : BaseFragment<FragmentShowProductBinding>(
    bindingInflater = FragmentShowProductBinding::inflate
) {

    private val viewModel: ShowProductViewModel by viewModels()

    override fun setupViews(savedInstanceState: Bundle?) {
        setupListeners()
    }

    override fun collectData() {
        collect(viewModel.uiState, ::collectUiState)
        collect(viewModel.failState, ::collectFailState)
    }

    private fun setupListeners() = with(binding) {
        textInputLayoutSearch.setEndIconOnClickListener {
            // show barcode screen
        }

        editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (EditorInfo.IME_ACTION_SEARCH == actionId) {
                viewModel.invoke(
                    ShowProductContract.Action.GetProduct(
                        editTextSearch.text.orEmpty()
                    )
                )
            }
            EditorInfo.IME_ACTION_SEARCH == actionId
        }

        editTextAmount.doAfterTextChanged { text ->
            val amount = text.toString().toIntOrNull()
            if (amount != viewModel.uiState.value.amount) {
                viewModel.invoke(ShowProductContract.Action.ChangedAmount(amount))
            }
        }

        buttonAddToCart.click {
            viewModel.invoke(
                ShowProductContract.Action.AddToCart
            )
        }
    }

    override fun collectFailState(fail: String) {
        super.collectFailState(fail)
        binding.textViewEmpty.isVisible = true
        binding.containerResult.isVisible = false
    }

    private fun collectUiState(uiState: ShowProductContract.UiState) {
        with(binding) {
            textViewEmpty.isVisible = uiState.name.isEmpty()
            containerResult.isVisible = uiState.name.isNotEmpty()

            editTextAmount.setText(uiState.amount.toString())
            textViewTitle.text = uiState.name
            textViewPrice.text = getString(R.string.text_price, uiState.oneAmountPrice.toString())
            textViewStockCount.text = getString(R.string.text_amount, uiState.amount.toString())
            textViewTotalPrice.text = getString(R.string.text_price, uiState.totalPrice.toString())
        }
    }
}