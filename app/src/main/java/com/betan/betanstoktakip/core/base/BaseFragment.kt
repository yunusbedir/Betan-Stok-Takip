package com.betan.betanstoktakip.core.base

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
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

    protected fun setKeyboardStateChangedListener(changedListener: (Boolean) -> Unit) {
        keyBoardStateChangedListener = changedListener
    }

    abstract fun setupViews(savedInstanceState: Bundle?)
    abstract fun collectData()

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

    protected fun <T> collect(flow: Flow<T>, invoke: (result: T) -> Unit) {
        lifecycleScope.launch {
            flow.collectLatest {
                invoke.invoke(it)
            }
        }
    }
}
