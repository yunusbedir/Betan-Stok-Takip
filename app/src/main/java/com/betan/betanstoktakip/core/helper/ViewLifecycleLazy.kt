package com.betan.betanstoktakip.core.helper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * A lazy property that gets cleaned up when the fragment's view is destroyed.
 *
 * Accessing this variable while the fragment's view is destroyed will throw IllegalStateException.
 */
fun <T : Any> Fragment.viewLifecycleLazy(initializer: (View) -> T): Lazy<T> =
    ViewLifecycleLazy(this, initializer)

private class ViewLifecycleLazy<T : Any>(
    private val fragment: Fragment,
    private val initializer: (View) -> T
) : Lazy<T>, LifecycleEventObserver {
    private var cached: T? = null

    override val value: T
        get() {
            return cached ?: run {
                val newValue = initializer(fragment.requireView())
                cached = newValue
                fragment.viewLifecycleOwner.lifecycle.addObserver(this)
                newValue
            }
        }

    override fun isInitialized() = cached != null

    override fun toString() = cached.toString()

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            cached = null
        }
    }
}