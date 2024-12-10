package voloshyn.android.data.repository

import kotlinx.coroutines.flow.Flow
import voloshyn.android.domain.models.Vehicle

interface CurrentVehicleProvider {
    val currentVehicle: Flow<Vehicle>
}