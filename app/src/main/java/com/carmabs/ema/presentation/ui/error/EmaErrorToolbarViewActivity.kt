package com.carmabs.ema.presentation.ui.error

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.carmabs.ema.R
import com.carmabs.ema.android.databinding.EmaActivityFragmentBinding
import com.carmabs.ema.android.databinding.EmaToolbarActivityBinding
import com.carmabs.ema.android.extension.dpToPx
import com.carmabs.ema.android.extension.getColor
import com.carmabs.ema.android.extension.getFormattedString
import com.carmabs.ema.android.ui.EmaActivity
import com.carmabs.ema.android.viewmodel.EmaAndroidViewModel
import com.carmabs.ema.core.state.EmaExtraData
import com.carmabs.ema.presentation.injection.activityInjection
import org.kodein.di.DI
import org.kodein.di.instance
import kotlin.math.roundToInt


/**
 *
 * Activity that inherits from EmaActivity, overrideTheme is false, so we take EmaTheme, for that reason
 * status bar in this screen has EmaTheme colorPrimaryDark color
 * We can override toolbar background
 *
 *
 **/
class EmaErrorToolbarViewActivity : EmaActivity<EmaErrorToolbarState, EmaErrorToolbarViewModel, EmaErrorNavigator.Navigation>() {

    override val navGraph: Int = R.navigation.navigation_ema_error

    override fun provideFixedToolbarTitle(): String = getString(R.string.error_toolbar_title)

    override val androidViewModelSeed: EmaAndroidViewModel<EmaErrorToolbarViewModel> by instance<EmaAndroidErrorToolbarViewModel>()

    override val navigator: EmaErrorNavigator by instance()

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
            titleMarginStart = resources.getDimension(R.dimen.space_medium).roundToInt().dpToPx(context)

        }
    }

    override fun EmaToolbarActivityBinding.onStateNormal(data: EmaErrorToolbarState) {
        checkToolbarVisibility(data)
    }

    private fun checkToolbarVisibility(data: EmaErrorToolbarState) {
        if (data.visibility)
            showToolbar()
        else
            hideToolbar()
    }

    override fun EmaToolbarActivityBinding.onSingleEvent(data: EmaExtraData) {
        Toast.makeText(this@EmaErrorToolbarViewActivity, R.string.error_user_created.getFormattedString(this@EmaErrorToolbarViewActivity, data.extraData as Int), Toast.LENGTH_SHORT).show()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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