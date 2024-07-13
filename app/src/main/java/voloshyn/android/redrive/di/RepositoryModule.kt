package voloshyn.android.redrive.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import voloshyn.android.data.repository.OnBoardRepositoryImpl
import voloshyn.android.domain.repository.OnBoardRepository

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {

    @Binds
    @ViewModelScoped
     fun bindOnBoardRepository(repository: OnBoardRepositoryImpl): OnBoardRepository
}