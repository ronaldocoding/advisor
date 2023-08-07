package br.com.advisor.di

import br.com.advisor.data.AdviceRepositoryImpl
import br.com.advisor.domain.repository.AdviceRepository
import org.junit.Before
import org.junit.Test
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.test.check.checkModules

class AdviceModuleTest {

    private lateinit var koin: KoinApplication

    @Before
    fun setUp() {
        koin = startKoin {
            modules(AdviceModule.getModules())
        }
    }

    @Test
    fun `GIVEN an AdviceModule WHEN call getModules THEN all definitions must exist`() {
        koin.checkModules()
    }

    @Test
    fun `GIVEN an AdviceModule WHEN resolve AdviceRepository THEN it should return AdviceRepositoryImpl with the correct dependencies`() {
        koin.checkModules {
            val adviceRepository: AdviceRepository = koin.get()
            assert(adviceRepository is AdviceRepositoryImpl)
        }
    }
}