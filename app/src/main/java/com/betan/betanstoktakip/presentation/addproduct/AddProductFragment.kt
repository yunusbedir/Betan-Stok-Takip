package com.betan.betanstoktakip.presentation.addproduct

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.betan.betanstoktakip.MainContract
import com.betan.betanstoktakip.R
import com.betan.betanstoktakip.core.base.BaseFragment
import com.betan.betanstoktakip.core.extensions.click
import com.betan.betanstoktakip.core.extensions.orEmpty
import com.betan.betanstoktakip.core.extensions.setupWithFirestoreBrands
import com.betan.betanstoktakip.core.extensions.toDoubleOrZero
import com.betan.betanstoktakip.core.extensions.toIntOrZero
import com.betan.betanstoktakip.core.helper.viewLifecycleLazy
import com.betan.betanstoktakip.databinding.FragmentAddProductBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sn.biometric.Biometric
import com.sn.biometric.BiometricListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductFragment : BaseFragment<FragmentAddProductBinding>(
    bindingInflater = FragmentAddProductBinding::inflate
) {

    private val viewModel: AddProductViewModel by viewModels()

    private val biometric: Biometric by viewLifecycleLazy {
        Biometric(requireContext())
    }

    private val biometricListener = object : BiometricListener {
        override fun onFingerprintAuthenticationSuccess() {
            viewModel.invoke(
                AddProductContract.Action.AddProduct(
                    barcode = binding.editTextBarcode.text.orEmpty(),
                    name = binding.editTextName.text.orEmpty(),
                    brandName = "",
                    stockAmount = binding.editTextStockAmount.text.orEmpty().toIntOrZero(),
                    purchasePrice = binding.editTextPurchasePrice.text.orEmpty().toDoubleOrZero(),
                    salePrice = binding.editTextSalePrice.text.orEmpty().toDoubleOrZero(),
                )
            )
            MainContract.biometricAuthenticationSucceeded = true
        }

        override fun onFingerprintAuthenticationFailure(
            errorMessage: String,
            errorCode: Int
        ) {

        }

    }

    override fun onResume() {
        super.onResume()
        biometric.subscribe(biometricListener)
    }

    override fun onDestroyView() {
        biometric.unSubscribe()
        super.onDestroyView()
    }

    override fun setupViews(savedInstanceState: Bundle?) {
        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {
            buttonAddProduct.click {
                if (MainContract.biometricAuthenticationSucceeded) {
                    viewModel.invoke(
                        AddProductContract.Action.AddProduct(
                            barcode = binding.editTextBarcode.text.orEmpty(),
                            name = binding.editTextName.text.orEmpty(),
                            brandName = "",
                            stockAmount = binding.editTextStockAmount.text.orEmpty().toIntOrZero(),
                            purchasePrice = binding.editTextPurchasePrice.text.orEmpty()
                                .toDoubleOrZero(),
                            salePrice = binding.editTextSalePrice.text.orEmpty().toDoubleOrZero(),
                        )
                    )
                } else {
                    biometric.showDialog(
                        requireActivity(),
                        Biometric.DialogStrings(
                            "Kullanıcı doğrulama",
                            "Parmak izi ve ya pin kodunu gir.",
                            ""
                        )
                    )
                }
            }
            textInputBarcode.setEndIconOnClickListener {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
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
            binding.editTextBarcode.setText(it)
        }
    }

    override fun collectData() {
        collect(viewModel.failState, ::collectFailState)
        collect(viewModel.eventState, ::collectEventState)
    }

    private fun collectEventState(event: AddProductContract.Event) {
        when (event) {
            AddProductContract.Event.AddedProduct -> {
                context?.let { safeContext ->
                    MaterialAlertDialogBuilder(safeContext)
                        .setTitle("Ürün Eklendi")
                        .setIcon(R.drawable.ic_success)
                        .setMessage("Ekran temizlensin mi?")
                        .setPositiveButton("Temizlensin") { dialog, _ ->
                            clearAll()
                            dialog.dismiss()
                        }.setNegativeButton("Hayır") { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }
            }
        }
    }

    private fun clearAll() {
        with(binding) {
            editTextBarcode.setText("")
            editTextName.setText("")
            editTextBrandName.setText("")
            editTextStockAmount.setText("1")
            editTextPurchasePrice.setText("")
            editTextSalePrice.setText("")
        }
    }
}