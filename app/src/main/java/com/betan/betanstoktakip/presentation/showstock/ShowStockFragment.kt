package com.betan.betanstoktakip.presentation.showstock

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.betan.betanstoktakip.MainContract
import com.betan.betanstoktakip.core.base.BaseFragment
import com.betan.betanstoktakip.core.extensions.orEmpty
import com.betan.betanstoktakip.core.extensions.toDoubleOrZero
import com.betan.betanstoktakip.core.extensions.toIntOrZero
import com.betan.betanstoktakip.core.helper.viewLifecycleLazy
import com.betan.betanstoktakip.databinding.FragmentShowStockBinding
import com.betan.betanstoktakip.presentation.addproduct.AddProductContract
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