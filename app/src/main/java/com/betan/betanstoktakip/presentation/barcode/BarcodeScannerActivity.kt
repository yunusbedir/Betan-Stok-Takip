package com.betan.betanstoktakip.presentation.barcode

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.betan.betanstoktakip.R
import com.betan.betanstoktakip.core.camera.BarcodeAnalyzer
import com.betan.betanstoktakip.databinding.ActivityBarcodeScannerBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.mlkit.vision.barcode.common.Barcode
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class BarcodeScannerActivity : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var binding: ActivityBarcodeScannerBinding
    private var showedDialog: Boolean = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                startCamera()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cameraExecutor = Executors.newSingleThreadExecutor()
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            // Image analyzer
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, BarcodeAnalyzer(
                        barcodeListener = { barcodes ->
                            if (showedDialog.not()) {
                                if (barcodes.size > 1) {
                                    showSelectBarcodeDialog(barcodes)
                                } else if (barcodes.isNotEmpty()) {
                                    finishAndResult(barcodes.firstOrNull()?.rawValue.orEmpty())
                                }
                            }
                        }
                    ))
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )

            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun finishAndResult(barcode: String) {
        Intent().apply {
            putExtra(BarcodeScannerContract.ARG_BARCODE, barcode)
            setResult(RESULT_OK, this)
            finish()
        }
    }


    private fun showSelectBarcodeDialog(barcodes: List<Barcode>) {
        showedDialog = true
        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice)
        arrayAdapter.addAll(
            barcodes.mapNotNull { it.rawValue }
        )
        var selectedBarcode = ""
        MaterialAlertDialogBuilder(this)
            .setTitle("Ürün Barcode'u Seç")
            .setAdapter(arrayAdapter) { _, which ->
                selectedBarcode = arrayAdapter.getItem(which).orEmpty()
            }.setPositiveButton(R.string.ok) { dialog, _ ->
                if (selectedBarcode.isNotEmpty()) {
                    finishAndResult(selectedBarcode)
                    dialog.dismiss()
                }
            }
            .setOnDismissListener {
                showedDialog = false
            }.show()
    }
}