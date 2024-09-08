package voloshyn.android.domain.models.tabs.redrive

import java.io.Serializable


data class Vehicle(
    val id: Long,
    val name: String,
    val currentMileage: Int
):Serializable {
    companion object {
        val NULL = emptyList<Vehicle>()
    }
}
