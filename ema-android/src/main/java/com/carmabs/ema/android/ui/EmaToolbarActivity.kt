package com.carmabs.ema.android.ui

import android.animation.Animator
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.viewbinding.ViewBinding
import com.carmabs.ema.android.extension.checkVisibility
import com.carmabs.ema.core.constants.FLOAT_ONE
import com.carmabs.ema.core.constants.FLOAT_ZERO
import com.carmabs.ema.core.navigator.EmaNavigationEvent
import com.carmabs.ema.core.state.EmaDataState
import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.google.android.material.appbar.AppBarLayout

/**
 *
 * Base activity to bind and unbind view model
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

abstract class EmaToolbarActivity<B : ViewBinding, S : EmaDataState, VM : EmaViewModel<S, D>, D : EmaNavigationEvent> :
    EmaActivity<B, S, VM, D>() {

    /**
     * Title for toolbar. If it is null the label xml tag in navigation layout is set for the toolbar
     * title, otherwise this title will be set for all fragments inside this activity.
     */
    protected open fun provideFixedToolbarTitle(): String? = null

    /**
     * Setup the toolbar
     * @param savedInstanceState for activity recreation
     */
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setupActionBars()
    }

    /**
     * The toobar for the activity
     */
    protected val toolbar: Toolbar by lazy {
        binding.provideToolbar()
    }

    /**
     * The toolbar container for the activity
     */
    protected val toolbarLayout: AppBarLayout by lazy {
        binding.provideToolbarLayout()
    }


    /**
     * Request the action bar setup
     */
    private fun setupActionBars() {
        setupToolbar(toolbar)
    }

    abstract fun B.provideToolbar(): Toolbar

    abstract fun B.provideToolbarLayout(): AppBarLayout

    /**
     * Find the toolbar and its container for the activity. The toolbar must have the
     * id=@+id/emaToolbar. The toolbar contaienr [AppBarLayout] must have the id=@+ìd/emaAppBarLayout
     */
    private fun setupToolbar(toolbar: Toolbar) {

        setSupportActionBar(toolbar)
        (navigator as? com.carmabs.ema.android.navigation.EmaNavControllerNavigator)?.navController?.also {
            setupActionBarWithNavController(it)
            it.addOnDestinationChangedListener { _, destination, _ ->
                setToolbarTitle(provideFixedToolbarTitle())
            }
        }

    }

    protected fun setToolbarTitle(title: String?) {
        supportActionBar?.title =
            title ?: provideFixedToolbarTitle()
                    ?: (navigator as? com.carmabs.ema.android.navigation.EmaNavControllerNavigator)?.navController?.currentDestination?.label
    }

    /**
     * Hides the toolbar
     */
    protected fun hideToolbar(gone: Boolean = true, animate: Boolean = true) {
        if (animate) {
            toolbarLayout.animate().apply {
                alpha(FLOAT_ZERO)
                setListener(object : Animator.AnimatorListener {

                    override fun onAnimationEnd(p0: Animator) {
                        toolbarLayout.visibility = checkVisibility(false, gone)
                    }

                    override fun onAnimationStart(p0: Animator) {

                    }

                    override fun onAnimationRepeat(p0: Animator) {

                    }

                    override fun onAnimationCancel(p0: Animator) {

                    }

                })
            }
        } else {
            toolbarLayout.visibility = if (gone) View.GONE else View.INVISIBLE
        }
    }

    /**
     * Show the toolbar
     */
    protected fun showToolbar(animate: Boolean = true) {
        if (animate) {
            toolbarLayout.animate().apply {
                alpha(FLOAT_ONE)
                setListener(object : Animator.AnimatorListener {

                    override fun onAnimationStart(p0: Animator) {
                        toolbarLayout.visibility = View.VISIBLE
                    }

                    override fun onAnimationEnd(p0: Animator) {

                    }

                    override fun onAnimationCancel(p0: Animator) {

                    }

                    override fun onAnimationRepeat(p0: Animator) {

                    }
                })
            }
        } else {
            toolbarLayout.visibility = View.VISIBLE
        }
    }
}