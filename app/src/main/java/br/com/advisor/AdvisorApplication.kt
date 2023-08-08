package br.com.advisor

import android.app.Application
import br.com.advisor.di.AdviceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AdvisorApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        inject()
    }

    private fun inject() {
        startKoin {
            androidLogger()
            androidContext(this@AdvisorApplication)
            modules(AdviceModule.getModules())
        }
    }
}