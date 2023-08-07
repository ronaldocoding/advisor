package br.com.advisor.di

import br.com.advisor.data.AdviceRepositoryImpl
import br.com.advisor.data.api.AdviceSlipApi
import br.com.advisor.domain.repository.AdviceRepository
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

    fun getModules() = listOf(dataModule)
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
