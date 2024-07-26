package voloshyn.android.data.di

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @Provides
    fun providesFirebaseApp(@ApplicationContext context: Context) =
        FirebaseApp.initializeApp(context)!!

    @Provides
    fun provideFirebaseAuth() = Firebase.auth
}