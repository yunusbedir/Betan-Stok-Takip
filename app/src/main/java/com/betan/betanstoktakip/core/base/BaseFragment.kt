package com.betan.betanstoktakip.core.base

import android.Manifest
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.betan.betanstoktakip.presentation.barcode.BarcodeScannerActivity
import com.betan.betanstoktakip.presentation.barcode.BarcodeScannerContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment<VBinding : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VBinding
) : Fragment() {

    protected lateinit var binding: VBinding

    private var isKeyboardVisible: Boolean = false

    private var keyBoardStateChangedListener: ((Boolean) -> Unit)? = null
    private val onGlobalLayoutListener: () -> Unit = {
        val r = Rect()
        view?.let { rootView ->
            rootView.getWindowVisibleDisplayFrame(r)
            val screenHeight: Int = rootView.getRootView().getHeight()
            val keypadHeight = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) { // Klavyenin açıldığını tespit etme
                if (!isKeyboardVisible) {
                    isKeyboardVisible = true
                    keyBoardStateChangedListener?.invoke(true)
                }
            } else {
                if (isKeyboardVisible) {
                    isKeyboardVisible = false
                    keyBoardStateChangedListener?.invoke(false)
                }
            }
        }
    }

    protected val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            permissionResult(Manifest.permission.CAMERA, it)
        }

    private val barcodeActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            barcodeActivityResult(
                barcode = it.data?.getStringExtra(BarcodeScannerContract.ARG_BARCODE)
            )
        }

    protected fun setKeyboardStateChangedListener(changedListener: (Boolean) -> Unit) {
        keyBoardStateChangedListener = changedListener
    }

    abstract fun setupViews(savedInstanceState: Bundle?)
    abstract fun collectData()
    open fun permissionResult(permission: String, isGranted: Boolean) {

    }

    open fun barcodeActivityResult(barcode: String?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingInflater.invoke(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        view.getViewTreeObserver()?.addOnGlobalLayoutListener(onGlobalLayoutListener)
        collectData()
        setupViews(savedInstanceState)
    }

    override fun onDestroyView() {
        view?.getViewTreeObserver()?.removeOnGlobalLayoutListener(onGlobalLayoutListener)
        super.onDestroyView()
    }

    protected fun startBarcodeActivity() {
        context?.let { safeContext ->
            barcodeActivityResultLauncher.launch(Intent(safeContext, BarcodeScannerActivity::class.java))
        }
    }

    protected fun <T> collect(flow: Flow<T>, invoke: (result: T) -> Unit) {
        lifecycleScope.launch {
            flow.collectLatest {
                invoke.invoke(it)
            }
        }
    }

    protected open fun collectFailState(fail: String) {
        context?.let { safeContext ->
            AlertDialog.Builder(safeContext)
                .setCancelable(true)
                .setTitle("Uyarı")
                .setMessage(fail)
                .setPositiveButton(
                    "Tamam"
                ) { _, _ ->
                }
                .create()
                .show()
        }
    }
}
