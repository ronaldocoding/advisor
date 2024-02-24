package br.com.advisor.di

import br.com.advisor.data.AdviceRepositoryImpl
import br.com.advisor.data.api.AdviceSlipApi
import br.com.advisor.domain.repository.AdviceRepository
import br.com.advisor.domain.usecase.GetAdviceUseCase
import br.com.advisor.presentation.viewmodel.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AdviceModule {

    private val dataModule = module {
        single { provideOkHttpClient() }
        single { provideRetrofit(client = get()) }
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

private fun provideRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder().run {
        addConverterFactory(GsonConverterFactory.create())
        client(client)
        baseUrl("https://api.adviceslip.com/")
        build()
    }
}

private fun provideOkHttpClient() = OkHttpClient
    .Builder()
    .addNetworkInterceptor(
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    )
    .build()

