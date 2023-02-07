package com.carmabs.ema.presentation.ui.backdata;

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.carmabs.ema.R
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.databinding.EmaToolbarActivityBinding
import com.carmabs.ema.android.ui.EmaToolbarFragmentActivity
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.base.BaseActivity
import org.koin.android.ext.android.inject

/**
 *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 * <p>
 * Date: 2019-11-07
 */

class EmaBackToolbarActivity : BaseActivity<EmaBackToolbarState, EmaBackToolbarViewModel, EmaBackNavigator.Navigation>() {

    override val navGraph: Int = R.navigation.navigation_ema_back

    override fun provideFixedToolbarTitle(): String? = null

    override val viewModelSeed: EmaBackToolbarViewModel by inject()

    override val navigator: EmaBackNavigator by inject()

    override fun provideAndroidViewModel(): EmaAndroidViewModel<EmaBackToolbarViewModel> {
        return injectDirect()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.apply {
            setBackgroundColor(ContextCompat.getColor(this@EmaBackToolbarActivity, R.color.colorPrimary))
        }
    }

    override fun EmaToolbarActivityBinding.onStateNormal(data: EmaBackToolbarState) {

    }
}