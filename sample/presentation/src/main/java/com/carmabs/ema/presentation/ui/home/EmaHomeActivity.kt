package com.carmabs.ema.presentation.ui.home

import android.widget.Toast
import com.carmabs.ema.R
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.databinding.EmaToolbarActivityBinding
import com.carmabs.ema.android.extension.getFormattedString
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.extension.HOUR_FORMAT_HHMM
import com.carmabs.ema.core.extension.toDateFormat
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.base.BaseActivity
import org.koin.android.ext.android.inject

/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Created by: Carlos Mateo Benito on 20/1/19.
 */
class EmaHomeActivity : BaseActivity<EmaHomeToolbarState,EmaHomeToolbarViewModel,EmaHomeNavigator.Navigation>() {

    override val navGraph: Int = R.navigation.navigation_ema_home

    override fun provideFixedToolbarTitle(): String = getString(R.string.home_toolbar_title)

    /**
     * Variable used to enable the theme used in manifest. Otherwise it will use the EmaTheme,
     * It is set as true by default.
     */
    override val overrideTheme: Boolean = false

    override val navigator: EmaHomeNavigator by inject()

    override fun EmaToolbarActivityBinding.onStateNormal(data: EmaHomeToolbarState) {
        setToolbarTitle(data.toolbarTitle)

    }

    override fun EmaToolbarActivityBinding.onSingleEvent(data: EmaExtraData) {
        when(data.type){
             EmaExtraData.DEFAULT_ID -> {
                 (data.extraData as? Pair<*, *>)?.also {
                     (it.second as? Long)?.also { timestamp ->
                         try {
                             val date = timestamp.toDateFormat(HOUR_FORMAT_HHMM)
                             Toast.makeText(this@EmaHomeActivity,
                                     R.string.home_last_user.getFormattedString(this@EmaHomeActivity,it.first,date),
                                     Toast.LENGTH_SHORT).show()
                             return
                         } catch (e: Exception) { }
                     }

                 }
             }
        }
    }

   

    override fun provideAndroidViewModel(): EmaAndroidViewModel<EmaHomeToolbarViewModel> {
        return injectDirect()
    }
}