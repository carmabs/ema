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


    override fun getCongratulations(name:String): EmaText {
        return EmaText.empty()//EmaText.id(R.string.home_welcome,name)
    }

    override fun getErrorTitle(): EmaText {
        return EmaText.empty()//EmaText.id(R.string.general_error_title)
    }
    override fun getErrorLogin(): EmaText {
        return EmaText.empty()//EmaText.id(R.string.login_error_fail)
    }

    override fun getErrorLoginUserEmpty(): EmaText {
        return EmaText.empty()//EmaText.id(R.string.login_error_user_empty)
    }

    override fun getErrorLoginPasswordEmpty(): EmaText {
        return EmaText.empty()//EmaText.id(R.string.login_error_password_empty)
    }

    override fun getHomeAdminTitle(admin: User): EmaText {
        return EmaText.empty()//EmaText.id(R.string.home_admin_title,admin.name,admin.surname)
    }

    override fun getHomeUserTitle(): EmaText {
        return EmaText.empty()//EmaText.id(R.string.home_user_title)
    }

    override fun getUserImage():Int{
        return 0//R.drawable.ic_user
    }

    override fun getAdminImage():Int{
        return 0//R.drawable.ic_admin
    }

    override fun getCreateUserAdminTitle():EmaText{
        return EmaText.empty()//EmaText.Id(R.string.profile_creation_user_admin_title)
    }

    override fun getCreateUserBasicTitle():EmaText{
        return EmaText.empty()//EmaText.Id(R.string.profile_creation_user_basic_title);
    }

    override fun getCreateUserMessage():EmaText{
        return EmaText.empty()//EmaText.Id(R.string.profile_creation_user_message);
    }

    override fun getDoYouWantToExitTitleCreationUserTitle(): EmaText {
        return EmaText.empty()//EmaText.id(R.string.profile_creation_user_exit_title)
    }

    override fun getDoYouWantToExitTitleCreationUserMessage(): EmaText {
        return EmaText.empty()//EmaText.id(R.string.profile_creation_user_exit_message)
    }

    override fun getExitImage(): Int {
        return 0//R.drawable.ic_exit
    }
}