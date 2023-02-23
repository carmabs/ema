package com.carmabs.domain.manager

import com.carmabs.domain.model.User
import com.carmabs.ema.core.model.EmaText

/**
 *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Date: 2019-11-08
 */

interface ResourceManager {
    fun getCongratulations(name: String): EmaText
    fun getErrorTitle(): EmaText
    fun getErrorLogin(): EmaText
    fun getErrorLoginUserEmpty(): EmaText
    fun getErrorLoginPasswordEmpty(): EmaText
    fun getHomeAdminTitle(admin: User): EmaText
    fun getHomeUserTitle(): EmaText
    fun getUserImage(): Int
    fun getAdminImage(): Int
    fun getCreateUserMessage(): EmaText
    fun getCreateUserBasicTitle(): EmaText
    fun getCreateUserAdminTitle(): EmaText
    fun getDoYouWantToExitTitleCreationUserTitle(): EmaText
    fun getDoYouWantToExitTitleCreationUserMessage(): EmaText
    fun getExitImage():Int
}