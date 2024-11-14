package voloshyn.android.domain.models

data class AppUnitSettings(
    val currency: Currency,
    val unitOfDistance: UnitOfDistance,
    val unitOfCapacity: UnitOfCapacity,
)



@JvmInline
value class Currency(val currency:String)

//TODO move the values to the @res folder in data model




enum class UnitOfDistance{
    Kilometers,Miles
}

enum class UnitOfCapacity{
 Liters, Gallons
}