package com.betan.betanstoktakip.presentation.showstock

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.betan.betanstoktakip.MainContract
import com.betan.betanstoktakip.R
import com.betan.betanstoktakip.core.base.BaseFragment
import com.betan.betanstoktakip.core.extensions.click
import com.betan.betanstoktakip.core.extensions.orEmpty
import com.betan.betanstoktakip.core.extensions.setupWithFirestoreBrands
import com.betan.betanstoktakip.core.extensions.toDoubleOrZero
import com.betan.betanstoktakip.core.extensions.toIntOrZero
import com.betan.betanstoktakip.core.helper.viewLifecycleLazy
import com.betan.betanstoktakip.databinding.FragmentShowStockBinding
import com.betan.betanstoktakip.domain.firebase.FirebaseCollections
import com.betan.betanstoktakip.domain.model.BrandModel
import com.betan.betanstoktakip.domain.model.ProductModel
import com.betan.betanstoktakip.presentation.addproduct.AddProductContract
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import com.sn.biometric.Biometric
import com.sn.biometric.BiometricListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowStockFragment : BaseFragment<FragmentShowStockBinding>(
    bindingInflater = FragmentShowStockBinding::inflate
) {

    private val viewModel: ShowStockViewModel by viewModels()

    private val biometric: Biometric by viewLifecycleLazy {
        Biometric(requireContext())
    }
    private var brandList= arrayListOf<String>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            initObserver()
            viewModel.getBrand()

            binding.buttonShowAllProduct.setOnClickListener {
                findNavController().navigate(R.id.showAllProductFragment)
            }
            binding.buttonShowSales.setOnClickListener {
                findNavController().navigate(R.id.showSellFragment)
            }
            binding.buttonExportProduct.setOnClickListener {
                findNavController().navigate(R.id.exportProductFragment)
            }
            binding.buttonImportProduct.setOnClickListener {
                findNavController().navigate(R.id.importProductFragment)
            }
            binding.buttonFindProduct.setOnClickListener {
                
            }

        }
    }
    private val biometricListener = object : BiometricListener {
        override fun onFingerprintAuthenticationSuccess() {
            viewModel.invoke(ShowStockContract.Action.GetStock)
            MainContract.biometricAuthenticationSucceeded = true
        }

        override fun onFingerprintAuthenticationFailure(
            errorMessage: String,
            errorCode: Int
        ) {

        }

    }

    private fun initObserver(){
        viewModel.getBrandLiveData.observe(viewLifecycleOwner){ result->
            brandList.clear()
            result.forEach{ brand->
                brandList.add(brand?.brandName.toString())
            }
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
        if (MainContract.biometricAuthenticationSucceeded) {
            viewModel.invoke(ShowStockContract.Action.GetStock)
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

    override fun collectData() {
        collect(viewModel.failState, ::collectFailState)
    }

}