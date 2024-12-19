package voloshyn.android.data.repository.logs

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import voloshyn.android.data.di.DispatcherIo
import voloshyn.android.data.repository.RefuelsProvider
import voloshyn.android.domain.models.logs.RefuelLog
import voloshyn.android.domain.models.logs.VehicleWithRefuels
import voloshyn.android.domain.repository.RefuelLogsRepository
import javax.inject.Inject

class RefuelLogsRepositoryImpl @Inject constructor(
    @DispatcherIo private val ioDispatcher: CoroutineDispatcher,
    private val refuelsProvider: RefuelsProvider
) : RefuelLogsRepository,
    RefuelsProvider by refuelsProvider{

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getVehicleWithRefuels(): Flow<VehicleWithRefuels> {
//        return currentVehicleProvider.currentVehicle.flatMapLatest { vehicle ->
//            refuelsProvider.refuelsForVehicle(vehicle.id).map { refuels ->
             //   VehicleWithRefuels(vehicle, refuels, RefuelUnits())
                TODO()
       //     }
       // }.flowOn(ioDispatcher)
    }

    override fun lastRefuel(): Flow<RefuelLog> {
        TODO("Not yet implemented")
    }


//    dataStringResourceProvider.fromResToText(
//    payments.formatToScale(),
//    R.string.value_pln
//    )

}
