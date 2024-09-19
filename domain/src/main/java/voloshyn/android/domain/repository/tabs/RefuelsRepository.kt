package voloshyn.android.domain.repository.tabs

import voloshyn.android.domain.models.tabs.Refuel

interface RefuelsRepository {

    /** In database i think it will be good to save a Refuel as from dialog and also in runtime create a RefuelLog and save it with the same id*/

    suspend fun addNew(refuel: Refuel)

    suspend fun edit()
    suspend fun delete()
    suspend fun refuelLogs()
}

