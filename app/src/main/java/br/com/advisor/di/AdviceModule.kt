package br.com.advisor.di

import br.com.advisor.data.AdviceRepositoryImpl
import br.com.advisor.data.api.AdviceSlipApi
import br.com.advisor.domain.repository.AdviceRepository
import br.com.advisor.domain.usecase.GetAdviceUseCase
import br.com.advisor.presentation.viewmodel.MainViewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AdviceModule {

    private val dataModule = module {
        single { provideRetrofit() }
        single { provideAdviceSlipApi(retrofit = get()) }
        factory<AdviceRepository> {
            AdviceRepositoryImpl(api = get())
        }
    }

    private val domainModule = module {
        factory { GetAdviceUseCase(repository = get()) }
    }

    private val presentationModule = module {
        factory { MainViewModel(useCase = get()) }
    }

    fun getModules() = listOf(dataModule, domainModule, presentationModule)
}

fun provideAdviceSlipApi(retrofit: Retrofit): AdviceSlipApi {
    return retrofit.create(AdviceSlipApi::class.java)
}

private fun provideRetrofit(): Retrofit {
    return Retrofit.Builder().run {
        addConverterFactory(GsonConverterFactory.create())
        baseUrl("https://api.adviceslip.com/")
        build()
    }
}
