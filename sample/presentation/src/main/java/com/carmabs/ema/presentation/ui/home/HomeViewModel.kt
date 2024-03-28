package com.carmabs.ema.presentation.ui.home

import com.carmabs.domain.model.Role
import com.carmabs.domain.model.User
import com.carmabs.domain.usecase.GetUserFriendsUseCase
import com.carmabs.ema.core.broadcast.backBroadcastId
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.presentation.base.BaseViewModel
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationViewModel

class HomeViewModel(
    private val getUserFriendsUseCase: GetUserFriendsUseCase,
    initialDataState: HomeState
) : BaseViewModel<HomeState, HomeAction, HomeNavigationEvent>(initialDataState) {

    private lateinit var user: User
    override fun onStateCreated(initializer: EmaInitializer?) {
        sideEffect {
            when (val homeInitializer = initializer as HomeInitializer) {
                is HomeInitializer.HomeUser -> {
                    user  = homeInitializer.user
                    val friends = getUserFriendsUseCase(GetUserFriendsUseCase.Input(homeInitializer.user))
                    updateState {
                        copy(
                            userData = when(user.role) {
                                Role.ADMIN -> HomeState.UserData.Admin(
                                    user.name,
                                    user.surname
                                )
                                Role.BASIC -> HomeState.UserData.Basic
                            },
                            userList = userList.toMutableList().apply {
                                addAll(friends)
                            }
                        )
                    }
                }
            }
        }
    }
    override fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.ProfileClicked -> {
                onActionCreateProfileClicked()
            }
        }
    }

    private fun onActionCreateProfileClicked() {
        navigate(HomeNavigationEvent.ProfileClicked(user))
    }

    override fun onBroadcastListenerSetup() {
        registerBackBroadcastListener(ProfileCreationViewModel::class.backBroadcastId) {
            val user = it as User
            setBackBroadcastData(user)
            updateState {
                copy(userList = userList.toMutableList().apply {
                    add(user)
                })
            }
        }
    }
}
