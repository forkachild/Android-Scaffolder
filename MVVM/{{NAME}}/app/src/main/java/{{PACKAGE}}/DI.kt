package {{PACKAGE}}

import {{PACKAGE}}.data.local.TokenStore
import {{PACKAGE}}.data.remote.CloudRepository
import {{PACKAGE}}.data.remote.Repository
import {{PACKAGE}}.data.remote.TokenInterceptor
import {{PACKAGE}}.ui.login.LoginViewModel
import {{PACKAGE}}.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { TokenStore(get()) }
    single { TokenInterceptor(get()) }
    single<Repository> { CloudRepository(get(), get()) }

    viewModel { MainViewModel(get()) }
    viewModel { LoginViewModel(get()) }

}