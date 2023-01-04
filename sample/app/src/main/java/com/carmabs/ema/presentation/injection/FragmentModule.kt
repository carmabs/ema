package com.carmabs.ema.presentation.injection

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.carmabs.ema.android.ui.dialog.EmaAndroidDialogProvider
import com.carmabs.ema.presentation.DIALOG_TAG_LOADING
import com.carmabs.ema.presentation.DIALOG_TAG_SIMPLE
import com.carmabs.ema.presentation.dialog.loading.LoadingDialogProvider
import com.carmabs.ema.presentation.dialog.simple.SimpleDialogProvider
import com.carmabs.ema.presentation.ui.backdata.creation.EmaAndroidBackUserCreationViewModel
import com.carmabs.ema.presentation.ui.backdata.creation.EmaBackUserCreationViewModel
import com.carmabs.ema.presentation.ui.backdata.userlist.EmaAndroidBackUserViewModel
import com.carmabs.ema.presentation.ui.backdata.userlist.EmaBackUserViewModel
import com.carmabs.ema.presentation.ui.unlogged.EmaUnloggedViewModel
import com.carmabs.ema.presentation.ui.home.EmaAndroidHomeViewModel
import com.carmabs.ema.presentation.ui.home.EmaHomeViewModel
import com.carmabs.ema.presentation.ui.user.EmaAndroidUserViewModel
import com.carmabs.ema.presentation.ui.user.EmaUserViewModel


import org.koin.android.ext.android.inject
import org.kodein.di.provider
import org.kodein.di.singleton

/**
 *  *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Created by: Carlos Mateo Benito on 20/1/19.
 */

fun fragmentInjection(fragment: Fragment) = DI.Module(name = "FragmentModule") {

    bind<Fragment>() with singleton { fragment }

    bind<FragmentManager>() with singleton { fragment.requireActivity().supportFragmentManager }

    bind<EmaAndroidDialogProvider>(tag = DIALOG_TAG_SIMPLE) with provider { SimpleDialogProvider(instance()) }

    bind<EmaAndroidDialogProvider>(tag = DIALOG_TAG_LOADING) with provider { LoadingDialogProvider(instance()) }

    bind<EmaHomeViewModel>() with singleton { EmaHomeViewModel(instance(),instance()) }

    bind<EmaUserViewModel>() with singleton { EmaUserViewModel(instance()) }

    bind<EmaBackUserViewModel>() with singleton { EmaBackUserViewModel() }

    bind<EmaBackUserCreationViewModel>() with singleton { EmaBackUserCreationViewModel(instance()) }

    bind<EmaUnloggedViewModel>() with singleton { EmaUnloggedViewModel() }

    bind<EmaAndroidHomeViewModel>() with singleton { EmaAndroidHomeViewModel(instance()) }

    bind<EmaAndroidUserViewModel>() with singleton { EmaAndroidUserViewModel(instance()) }

    bind<EmaAndroidBackUserViewModel>() with singleton { EmaAndroidBackUserViewModel(instance()) }

    bind<EmaAndroidBackUserCreationViewModel>() with singleton { EmaAndroidBackUserCreationViewModel(instance()) }
}