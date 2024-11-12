package voloshyn.android.data.repository.tabs.logs

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.models.Vehicle

interface CurrentVehicleProvider {
    val currentVehicle: Flow<Vehicle>
}