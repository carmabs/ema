package com.carmabs.ema.presentation.ui.home

import com.carmabs.domain.model.User
import com.carmabs.domain.usecase.GetUserFriendsUseCase
import com.carmabs.ema.core.broadcast.broadcastId
import com.carmabs.ema.core.initializer.EmaInitializer
import com.carmabs.ema.presentation.base.BaseViewModel
import com.carmabs.ema.presentation.ui.profile.creation.ProfileCreationViewModel

class HomeViewModel(
    private val getUserFriendsUseCase: GetUserFriendsUseCase,
    initialDataState: HomeState
) : BaseViewModel<HomeState, HomeAction, HomeNavigationEvent>(initialDataState) {

    private var admin: User? = null
    override fun onStateCreated(initializer: EmaInitializer?) {
        sideEffect {
            val friends = getUserFriendsUseCase(Unit)
            when (val homeInitializer = initializer as HomeInitializer) {
                is HomeInitializer.Admin -> {
                    admin = homeInitializer.admin
                    updateState {
                        copy(
                            userData = HomeState.UserData.Admin(
                                homeInitializer.admin.name,
                                homeInitializer.admin.surname
                            ),
                            userList = friends
                        )
                    }
                }

                HomeInitializer.BasicUser -> {

                    updateState {
                        copy(
                            userData = HomeState.UserData.Basic,
                            userList = friends
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
        navigate(
            HomeNavigationEvent.ProfileClicked(
                admin ?: throw IllegalStateException("Admin cannot be null")
            )
        )
    }

    override fun onBroadcastListenerSetup() {
        addOnBroadcastListener(ProfileCreationViewModel::class.broadcastId) {
            val user = it as User
            updateState {
                copy(userList = userList.toMutableList().apply {
                    add(user)
                })
            }
        }
    }
}
