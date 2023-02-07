package com.carmabs.ema.presentation.base

import com.carmabs.ema.android.ui.EmaActivity
import com.carmabs.ema.core.navigator.EmaNavigationState
import com.carmabs.ema.core.state.EmaBaseState
import com.carmabs.ema.core.viewmodel.EmaViewModel
import com.carmabs.ema.presentation.injection.activityInjection


/**
 * Base Activity. OverrideTheme -> True, the theme is overriden by AppTheme
 * Injection is provided
 *
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo</a>
 */

abstract class BaseActivity<S : EmaBaseState, VM : EmaViewModel<S, D>, D : EmaNavigationState> : EmaActivity<S,VM,D>() {

    override fun injectActivityModule(kodein: DI.MainBuilder): DI.Module? = activityInjection(this)

    //True if you want to set the Application theme to activity, otherwise it will take EmaTheme.
    //False by default -> EmaTheme
    override val overrideTheme: Boolean = true
}