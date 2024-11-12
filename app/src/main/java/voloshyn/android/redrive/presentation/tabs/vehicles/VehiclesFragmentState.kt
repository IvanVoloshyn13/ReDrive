package voloshyn.android.redrive.presentation.tabs.vehicles

import androidx.annotation.StringRes
import voloshyn.android.app.R
import voloshyn.android.domain.models.Vehicle

data class VehiclesFragmentState(
    val isLoading: Boolean = false,
    val vehicles: List<Vehicle> = ArrayList<Vehicle>(),
    val error: Boolean = false,
    @StringRes val errorMessage: Int = R.string.empty_string
)