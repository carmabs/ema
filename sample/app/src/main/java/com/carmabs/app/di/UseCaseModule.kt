package com.carmabs.app.di

import com.carmabs.domain.usecase.GetUserFriendsUseCase
import com.carmabs.domain.usecase.LoginUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { LoginUseCase(get()) }
    factory { GetUserFriendsUseCase(get()) }
}