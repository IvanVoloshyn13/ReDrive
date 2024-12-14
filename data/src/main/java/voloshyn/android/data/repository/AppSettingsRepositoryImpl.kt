package voloshyn.android.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import voloshyn.android.data.R
import voloshyn.android.data.localeStorage.room.dao.SettingsDao
import voloshyn.android.data.localeStorage.room.entities.SettingsEntity
import voloshyn.android.data.valueId
import voloshyn.android.domain.models.ItemSetting
import voloshyn.android.domain.models.AppSettings
import voloshyn.android.domain.models.SettingType
import voloshyn.android.domain.repository.AppSettingsRepository
import javax.inject.Inject

class AppSettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsDao: SettingsDao,
    private val user: AppCurrentUserRepository
) : AppSettingsRepository {

    override suspend fun saveAppSettings(appSettings: AppSettings) {
        val userId = user.user.id
        settingsDao.insertOrUpdate(SettingsEntity.toEntity(appSettings, userId))
    }

    override fun observeAppSettings(): Flow<AppSettings> {
        val userId = user.user.id
        return settingsDao.getSettingsByUserId(userId).map { settingsEntity ->
            return@map if (settingsEntity == null) {
                settingsDao.insertOrUpdate(defaultSettingsEntity(userId))
                defaultSettingsEntity(userId).toSettings()
            } else {
                settingsEntity.toSettings()
            }
        }
    }

    override fun getItemUnits(type: SettingType): Array<out String> {
        return when (type) {
            SettingType.Currency -> getLocalizedStringArray(R.array.currencies)
            SettingType.Capacity -> getLocalizedStringArray(R.array.capacities)
            SettingType.Distance -> getLocalizedStringArray(R.array.distances)
            SettingType.AvgConsumption -> getLocalizedStringArray(R.array.avg_consumption)
            SettingType.FormatOfDate -> getLocalizedStringArray(R.array.date_formats)
            SettingType.Default -> emptyArray<String>()
        }
    }


    private fun SettingsEntity.toSettings(): AppSettings {
        val currency = getLocalizedSettingsFromArray(R.array.currencies, this.currency.valueId())
        val capacity = getLocalizedSettingsFromArray(R.array.capacities, this.capacity.valueId())
        val distance = getLocalizedSettingsFromArray(R.array.distances, this.distance.valueId())
        val avgConsumption =
            getLocalizedSettingsFromArray(R.array.avg_consumption, this.avgConsumption.valueId())
        val formatOfDate =
            getLocalizedSettingsFromArray(R.array.date_formats, this.formatOfDate.valueId())

        return AppSettings(
            currency = ItemSetting(id = currency.id, valueUnit = currency.valueUnit, name = currency.name, type = SettingType.Currency),
            capacity = ItemSetting(id = capacity.id, valueUnit = capacity.valueUnit, name = capacity.name, type = SettingType.Capacity),
            distance = ItemSetting(id = distance.id, valueUnit = distance.valueUnit, name = distance.name, type = SettingType.Distance),
            avgConsumption = ItemSetting(
                id = avgConsumption.id,
                valueUnit = avgConsumption.valueUnit,
                name = avgConsumption.name,
                type = SettingType.AvgConsumption
            ),
            dateFormat = ItemSetting(
                id = formatOfDate.id,
                valueUnit = formatOfDate.valueUnit,
                name = formatOfDate.name,
                type = SettingType.FormatOfDate
            )
        )
    }

    private fun getLocalizedSettingsFromArray(arrayId: Int, valueId: String): ItemSetting {
        val localizedArray = getLocalizedStringArray(arrayId)
        val localizedValue = localizedArray.firstOrNull { it.valueId() == valueId } ?: ""
        val parts = localizedValue.split("|")
        val id = parts[0]
        val unit = parts[1]
        val name = getLocalizedName(arrayId)
        return ItemSetting(id = id, valueUnit = unit, name = name)
    }

    private fun getLocalizedStringArray(arrayResId: Int): Array<out String> {
        return context.resources.getStringArray(arrayResId)
    }

    private fun getLocalizedName(arrayId: Int): String {
        return when (arrayId) {
            R.array.currencies -> context.resources.getString(R.string.currency)
            R.array.capacities -> context.resources.getString(R.string.unit_of_capacity)
            R.array.avg_consumption -> context.resources.getString(R.string.avg_consumption)
            R.array.distances -> context.resources.getString(R.string.unit_of_distance)
            R.array.date_formats -> context.resources.getString(R.string.date_format)
            else -> throw IllegalStateException()
        }
    }

    private fun defaultSettingsEntity(userId: String): SettingsEntity {
        return SettingsEntity(
            id = 0,
            userId = userId,
            avgConsumption = getLocalizedStringArray(R.array.avg_consumption)[0],
            currency = getLocalizedStringArray(R.array.currencies)[0],
            distance = getLocalizedStringArray(R.array.distances)[0],
            capacity = getLocalizedStringArray(R.array.capacities)[0],
            formatOfDate = getLocalizedStringArray(R.array.date_formats)[0]
        )
    }

}