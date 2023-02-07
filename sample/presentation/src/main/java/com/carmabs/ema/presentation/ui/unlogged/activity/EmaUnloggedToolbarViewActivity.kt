package com.carmabs.ema.presentation.ui.unlogged.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.carmabs.ema.R
import com.carmabs.ema.android.databinding.EmaToolbarActivityBinding
import com.carmabs.ema.android.di.injectDirect
import com.carmabs.ema.android.extension.getColor
import com.carmabs.ema.android.extension.getFormattedString
import com.carmabs.ema.android.ui.EmaActivity
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.injection.activityInjection
import com.carmabs.ema.presentation.ui.unlogged.EmaAndroidUnloggedToolbarViewModel
import com.carmabs.ema.presentation.ui.unlogged.EmaUnloggedNavigator
import com.carmabs.ema.presentation.ui.unlogged.EmaUnloggedToolbarState
import com.carmabs.ema.presentation.ui.unlogged.EmaUnloggedToolbarViewModel

import org.koin.android.ext.android.inject
import kotlin.math.roundToInt


/**
 *
 * Activity that inherits from EmaActivity, overrideTheme is false, so we take EmaTheme, for that reason
 * status bar in this screen has EmaTheme colorPrimaryDark color
 * We can override toolbar background
 *
 *
 **/
class EmaUnloggedToolbarViewActivity : EmaActivity<EmaUnloggedToolbarState, EmaUnloggedToolbarViewModel, EmaUnloggedNavigator.Navigation>() {

    override val navGraph: Int = R.navigation.navigation_ema_unlogged

    override fun provideFixedToolbarTitle(): String = getString(R.string.unlogged_toolbar_title)

    override fun provideAndroidViewModel(): EmaAndroidViewModel<EmaUnloggedToolbarViewModel> {
        return EmaAndroidUnloggedToolbarViewModel(injectDirect())
    }

    override val navigator: EmaUnloggedNavigator by inject()

    override fun injectActivityModule(kodein: DI.MainBuilder): DI.Module = activityInjection(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureToolbar()
    }

    private fun configureToolbar() {

        //With EMA activity you can customize the toolbar
        toolbar.apply {
            val whiteColor = android.R.color.white.getColor(context)
            setBackgroundColor(R.color.colorPrimary.getColor(context))
            logo = getDrawable(R.drawable.ic_error_toolbar)
            setTitleTextColor(whiteColor)
            titleMarginStart = resources.getDimension(R.dimen.space_medium).roundToInt()

        }
    }

    override fun EmaToolbarActivityBinding.onStateNormal(data: EmaUnloggedToolbarState) {
        checkToolbarVisibility(data)
    }

    private fun checkToolbarVisibility(data: EmaUnloggedToolbarState) {
        if (data.visibility)
            showToolbar()
        else
            hideToolbar()
    }

    override fun EmaToolbarActivityBinding.onSingleEvent(data: EmaExtraData) {
        Toast.makeText(this@EmaUnloggedToolbarViewActivity, R.string.unlogged_user_created.getFormattedString(this@EmaUnloggedToolbarViewActivity, data.extraData as Int), Toast.LENGTH_SHORT).show()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_error, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.menu_action_error_hide_toolbar -> {
                vm.onActionMenuHideToolbar()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}