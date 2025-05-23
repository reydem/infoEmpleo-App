// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/core/di/NetworkModule.kt
package com.example.infoempleo.core.di


import com.example.infoempleo.login.data.network.LoginClient
import com.example.infoempleo.vacantes.data.network.VacantesApi        // ojo, aquí importa tu API de vacantes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // 1) Interceptor para ver peticiones/respuestas en Logcat
    @Provides @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
      HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
      }

    // 2) Cliente OkHttp al que le inyectamos el interceptor
    @Provides @Singleton
    fun provideOkHttpClient(
      logging: HttpLoggingInterceptor
    ): OkHttpClient =
      OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // 3) Retrofit usando el OkHttpClient anterior
    @Provides @Singleton
    fun provideRetrofit(
      client: OkHttpClient
    ): Retrofit =
      Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5000/")  // tu URL local/emulador
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 4) Client de login
    @Provides @Singleton
    fun provideLoginClient(
      retrofit: Retrofit
    ): LoginClient =
      retrofit.create(LoginClient::class.java)

    // 5) Client de vacantes (igual que el de login)
    @Provides @Singleton
    fun provideVacantesApi(
      retrofit: Retrofit
    ): VacantesApi =
      retrofit.create(VacantesApi::class.java)
}