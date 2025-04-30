package com.betan.betanstoktakip.presentation.showproduct.updateproduct

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.betan.betanstoktakip.R
import com.betan.betanstoktakip.core.base.BaseFragment
import com.betan.betanstoktakip.databinding.FragmentUpdateProductBinding
import com.betan.betanstoktakip.domain.model.ProductModel
import com.betan.betanstoktakip.presentation.showproduct.ShowProductContract
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateProductFragment : BaseFragment<FragmentUpdateProductBinding>(
    FragmentUpdateProductBinding::inflate
) {

    private val updateProductViewModel: UpdateProductViewModel by viewModels()


    private val args: UpdateProductFragmentArgs by navArgs()
    private val product = args.product

    override fun setupViews(savedInstanceState: Bundle?) {
        updateProductViewModel.invoke(UpdateProductContract.Action.LoadProduct(product.barcode.orEmpty()))

        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonUpdateProduct.setOnClickListener {
            val updatedProduct = ProductModel(
                name = binding.editTextName.text.toString(),
                brandName = binding.editTextBrandName.text.toString(),
                stockAmount = binding.editTextStockAmount.text.toString().toIntOrNull() ?: 0,
                salePrice = binding.editTextSalePrice.text.toString().toDoubleOrNull() ?: 0.0,
                purchasePrice = binding.editTextPurchasePrice.text.toString().toDoubleOrNull() ?: 0.0
            )

            updateProductViewModel.invoke(UpdateProductContract.Action.UpdateProduct(updatedProduct))
        }
    }

    override fun collectData() {
        collect(updateProductViewModel.successState) {
            showSnackbar("Ürün başarıyla güncellendi.")
            findNavController().popBackStack()
        }

        collect(updateProductViewModel.uiState, ::collectUiState)

        collect(updateProductViewModel.failState) { errorMessage ->
            showSnackbar(errorMessage ?: "Bir hata oluştu.")
        }
    }

    private fun collectUiState(uiState: UpdateProductContract.UiState) {
        with(binding) {
            editTextBarcode.setText(uiState.barcode)
            editTextBrandName.setText(uiState.brandName)
            editTextName.setText(uiState.name)
            editTextStockAmount.setText(uiState.stockAmount.toString())
            editTextSalePrice.setText(uiState.salePrice.toString())
            editTextPurchasePrice.setText(uiState.purchasePrice.toString())
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}
