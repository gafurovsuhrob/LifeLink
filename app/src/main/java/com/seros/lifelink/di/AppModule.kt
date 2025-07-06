package com.seros.lifelink.di

import com.seros.lifelink.ui.login.LoginViewModel
import com.seros.lifelink.ui.main.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel() }
    viewModel { LoginViewModel() }
}