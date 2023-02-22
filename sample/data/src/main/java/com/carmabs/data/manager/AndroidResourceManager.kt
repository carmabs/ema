package com.carmabs.data.manager

import com.carmabs.domain.manager.ResourceManager
import com.carmabs.domain.model.User
import com.carmabs.ema.core.model.EmaText
import com.carmabs.ema.sample.data.R

/**
 *<p>
 * Copyright (c) 2020, Carmabs. All rights reserved.
 * </p>
 *
 * @author <a href=“mailto:apps.carmabs@gmail.com”>Carlos Mateo Benito</a>
 *
 * Date: 2019-11-08
 */

class AndroidResourceManager : ResourceManager {

    override fun getResultErrorFillName(): EmaText {
        return EmaText.id(R.string.back_result_fill_name)
    }

    override fun getResultErrorFillSurname(): EmaText {
        return EmaText.id(R.string.back_result_fill_surname)
    }

    override fun getHomeUserToolbarTitle(): EmaText {
        return EmaText.id(R.string.user_toolbar_title)
    }

    override fun getCongratulations(name:String): EmaText {
        return EmaText.id(R.string.home_welcome,name)
    }

    override fun getNumberPeopleAdded(number: Int): EmaText {
        return EmaText.id(R.string.user_number_people,number)
    }

    override fun getErrorTitle(): EmaText {
        return EmaText.id(R.string.general_error_title)
    }
    override fun getErrorLogin(): EmaText {
        return EmaText.id(R.string.login_error_fail)
    }

    override fun getErrorLoginUserEmpty(): EmaText {
        return EmaText.id(R.string.login_error_user_empty)
    }

    override fun getErrorLoginPasswordEmpty(): EmaText {
        return EmaText.id(R.string.login_error_password_empty)
    }

    override fun getHomeAdminTitle(admin: User): EmaText {
        return EmaText.id(R.string.home_admin_title,admin.name,admin.surname)
    }

    override fun getHomeUserTitle(): EmaText {
        return EmaText.id(R.string.home_user_title)
    }

    override fun getUserImage():Int{
        return R.drawable.ic_user
    }

    override fun getAdminImage():Int{
        return R.drawable.ic_admin
    }

    override fun getCreateUserAdminTitle():EmaText{
        return EmaText.Id(R.string.profile_creation_user_admin_title)
    }

    override fun getCreateUserBasicTitle():EmaText{
        return EmaText.Id(R.string.profile_creation_user_basic_title);
    }

    override fun getCreateUserMessage():EmaText{
        return EmaText.Id(R.string.profile_creation_user_message);
    }
}