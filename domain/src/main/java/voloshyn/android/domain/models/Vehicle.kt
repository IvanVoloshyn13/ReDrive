package voloshyn.android.domain.models

import java.io.Serializable


data class Vehicle(
    val id: Long,
    val name: String,
    val currentMileage: Int
) : Serializable {
    companion object {
        val NULL = emptyList<Vehicle>()
        val DEFAULT_VEHICLE = Vehicle(id = 0L, name = "", currentMileage = 0)
    }
}
