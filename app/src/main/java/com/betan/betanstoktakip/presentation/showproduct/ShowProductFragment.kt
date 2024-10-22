package com.betan.betanstoktakip.presentation.showproduct

import android.Manifest
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.betan.betanstoktakip.R
import com.betan.betanstoktakip.core.base.BaseFragment
import com.betan.betanstoktakip.core.extensions.click
import com.betan.betanstoktakip.core.extensions.hideKeyboard
import com.betan.betanstoktakip.core.extensions.orEmpty
import com.betan.betanstoktakip.core.extensions.putMoneyDots
import com.betan.betanstoktakip.core.extensions.toMoney
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
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (EditorInfo.IME_ACTION_SEARCH == actionId) {
                viewModel.invoke(
                    ShowProductContract.Action.GetProduct(
                        editTextSearch.text.orEmpty()
                    )
                )
                hideKeyboard()
            }
            EditorInfo.IME_ACTION_SEARCH == actionId
        }

        buttonPlus.click {
            viewModel.invoke(ShowProductContract.Action.PlusCartProduct)
        }

        buttonMinus.click {
            viewModel.invoke(ShowProductContract.Action.MinusCartProduct)
        }

        buttonAddToCart.click {
            viewModel.invoke(
                ShowProductContract.Action.AddToCart
            )
        }
    }

    override fun permissionResult(permission: String, isGranted: Boolean) {
        super.permissionResult(permission, isGranted)
        if (permission == Manifest.permission.CAMERA && isGranted) {
            startBarcodeActivity()
        }
    }

    override fun barcodeActivityResult(barcode: String?) {
        super.barcodeActivityResult(barcode)
        barcode?.let {
            binding.editTextSearch.setText(it)
            viewModel.invoke(ShowProductContract.Action.GetProduct(it))
        }
    }

    private fun collectUiState(uiState: ShowProductContract.UiState) {
        with(binding) {
            textViewEmpty.isVisible = uiState.name.isEmpty()
            containerResult.isVisible = uiState.name.isNotEmpty()

            textViewAmount.text = uiState.amount.toString()
            textViewTitle.text = uiState.name
            textViewPrice.text = uiState.oneAmountPrice.toMoney()
            textViewStockCount.text = getString(R.string.text_amount, uiState.stockAmount.toString())
            editTextTotalPrice.setText(uiState.totalPrice.toMoney())

            editTextTotalPrice.isVisible = uiState.isEnableAddToCart
            containerAmount.isVisible = uiState.isEnableAddToCart
            buttonAddToCart.isEnabled = uiState.isEnableAddToCart
        }
    }
}