package com.example.firebase.remoteDataSource.di


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

private const val DATABASE_URL =
    "https://redrive-bceda-default-rtdb.europe-west1.firebasedatabase.app/"

private const val VEHICLES = "vehicles"
private const val REFUELS = "refuels"

@Module
@InstallIn(SingletonComponent::class)
class FbDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabaseInstance() = FirebaseDatabase.getInstance(DATABASE_URL)

    @Provides
    @Singleton
    @VehicleReference
    fun provideVehiclesReference(database: FirebaseDatabase): DatabaseReference {
        return database.getReference(VEHICLES)
    }

    @Provides
    @Singleton
    @RefuelsReference
    fun provideRefuelsReference(database: FirebaseDatabase): DatabaseReference {
        return database.getReference(REFUELS)
    }

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class UserReference

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class RefuelsReference

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class VehicleReference
