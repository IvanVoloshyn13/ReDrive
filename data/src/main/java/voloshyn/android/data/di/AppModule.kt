package voloshyn.android.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import voloshyn.android.data.repository.tabs.logs.DataStringResProviderImpl
import voloshyn.android.data.repository.tabs.logs.DataStringResourceProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDataStringProvider(@ApplicationContext context: Context): DataStringResourceProvider {
        return DataStringResProviderImpl(context)
    }
}