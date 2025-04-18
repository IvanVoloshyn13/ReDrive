package com.example.redrive.core

/**
 * Contract for fragments to follow a consistent lifecycle-driven structure.
 */
interface FragmentContract {
    /** Initialize and configure view components (e.g., adapters, toolbars). */
    fun initViews() = Unit

    /** Attach click and input listeners. */
    fun attachListeners() = Unit

    /** Observe ViewModel flows and render state changes. */
    fun observeViewModel() = Unit

    /** Render UI based on a given state object (optional). */
    fun renderUi() = Unit
}