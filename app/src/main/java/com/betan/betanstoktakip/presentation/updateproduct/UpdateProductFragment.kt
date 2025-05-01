package com.betan.betanstoktakip.presentation.updateproduct

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.betan.betanstoktakip.core.base.BaseFragment
import com.betan.betanstoktakip.core.extensions.orEmpty
import com.betan.betanstoktakip.databinding.FragmentUpdateProductBinding
import com.betan.betanstoktakip.domain.model.ProductModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateProductFragment : BaseFragment<FragmentUpdateProductBinding>(
    FragmentUpdateProductBinding::inflate
) {

    private val updateProductViewModel: UpdateProductViewModel by viewModels()


    private val args: UpdateProductFragmentArgs by navArgs()
    private val product
        get() = args.product

    override fun setupViews(savedInstanceState: Bundle?) {
        updateProductViewModel.invoke(UpdateProductContract.Action.LoadProduct(product.barcode.orEmpty()))

        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonUpdateProduct.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Ürünü Güncelle")
                .setMessage("Ürünü güncellemek istediğinize emin misiniz, Yapılan değişiklikler geri alınmaz?")
                .setNegativeButton("Hayır") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Evet") { dialog, _ ->
                    val updatedProduct = ProductModel(
                        barcode = binding.editTextBarcode.text.orEmpty(),
                        name = binding.editTextName.text.toString(),
                        brandName = binding.editTextBrandName.text.toString(),
                        stockAmount = binding.editTextStockAmount.text.toString().toIntOrNull() ?: 0,
                        salePrice = binding.editTextSalePrice.text.toString().toDoubleOrNull() ?: 0.0,
                        purchasePrice = binding.editTextPurchasePrice.text.toString().toDoubleOrNull() ?: 0.0
                    )

                    updateProductViewModel.invoke(UpdateProductContract.Action.UpdateProduct(updatedProduct))
                    dialog.dismiss()
                }
                .show()
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
