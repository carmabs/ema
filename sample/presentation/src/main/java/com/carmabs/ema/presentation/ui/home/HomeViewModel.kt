package com.carmabs.ema.presentation.ui.home

import com.carmabs.domain.manager.ResourceManager
import com.carmabs.domain.model.Role
import com.carmabs.domain.model.User
import com.carmabs.domain.usecase.GetUserFriendsUseCase
import com.carmabs.ema.core.extension.resultId
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.presentation.base.BaseViewModel
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationViewModel
import com.carmabs.ema.presentation.ui.profile.onboarding.ProfileOnBoardingInitializer

class HomeViewModel(
    private val getUserFriendsUseCase: GetUserFriendsUseCase,
    private val resourceManager: ResourceManager
) : BaseViewModel<HomeState, HomeDestination>() {

    private var admin: User? = null
    override suspend fun onCreateState(initializer: EmaInitializer?): HomeState {
        return when (val homeInitializer = initializer as HomeInitializer) {
            is HomeInitializer.Admin -> {
                admin = homeInitializer.admin
                HomeState(
                    userRole = Role.ADMIN,
                    listName = resourceManager.getHomeAdminTitle(homeInitializer.admin),
                    userList = emptyList()
                )
            }
            HomeInitializer.BasicUser -> {
                val friends = getUserFriendsUseCase.execute(Unit)
                HomeState(
                    userRole = Role.BASIC,
                    listName = resourceManager.getHomeUserTitle(),
                    userList = friends
                )
            }
        }
    }


    fun onActionCreateProfileClicked() {
        navigate(
            HomeDestination.Profile()
                .setInitializer(
                    ProfileOnBoardingInitializer.Default(
                        admin ?: throw IllegalStateException("Admin cannot be null")
                    )
                )
        )
    }

    override fun onResultListenerSetup() {
        addOnResultListener(ProfileCreationViewModel::class.resultId()) {
            val user = it as User
            updateToNormalState {
                copy(userList = userList.toMutableList().apply {
                    add(user)
                })
            }
        }
    }
}
