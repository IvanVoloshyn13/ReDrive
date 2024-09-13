package voloshyn.android.redrive.presentation.tabs.redrive

import voloshyn.android.domain.models.tabs.redrive.Vehicle

sealed interface RedriveIntents {
    class OnCurrentVehicle(val vehicle: Vehicle) : RedriveIntents
}