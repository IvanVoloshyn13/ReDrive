package voloshyn.android.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import voloshyn.android.data.repository.tabs.logs.CurrentVehicleProvider
import voloshyn.android.data.repository.tabs.logs.RefuelsProvider
import voloshyn.android.data.repository.tabs.refuel.RefuelRepositoryImpl
import voloshyn.android.data.repository.tabs.vehicles.VehiclesRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindRefuelsProvider(repository: RefuelRepositoryImpl): RefuelsProvider

    @Binds
    fun bindCurrentVehicleProvider(repository: VehiclesRepositoryImpl): CurrentVehicleProvider


}