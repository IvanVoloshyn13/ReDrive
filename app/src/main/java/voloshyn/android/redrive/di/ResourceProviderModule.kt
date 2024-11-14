package voloshyn.android.redrive.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import voloshyn.android.redrive.presentation.tabs.logs.StringResProviderImpl
import voloshyn.android.redrive.presentation.tabs.logs.StringResourceProvider

@Module
@InstallIn(ViewModelComponent::class)
class ResourceProviderModule {

    @Provides
    @ViewModelScoped
    fun provideStringResProvider(@ApplicationContext context: Context): StringResourceProvider {
        return StringResProviderImpl(context)
    }

}