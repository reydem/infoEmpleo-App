// /webapps/infoEmpleo-App-android/InfoEmpleo/app/src/main/java/com/example/infoempleo/di/network/NetworkModule.kt
package com.example.infoempleo.di

import com.example.infoempleo.login.data.network.LoginClient
import com.example.infoempleo.vacantes.data.network.VacantesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.example.infoempleo.di.network.AuthInterceptor

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Provides @Singleton
  fun provideLoggingInterceptor(): HttpLoggingInterceptor =
    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

  @Provides @Singleton
  fun provideOkHttpClient(
    authInterceptor: AuthInterceptor,
    logging: HttpLoggingInterceptor
  ): OkHttpClient =
    OkHttpClient.Builder()
      .addInterceptor(authInterceptor)
      .addInterceptor(logging)
      .build()

  @Provides @Singleton
  fun provideRetrofit(client: OkHttpClient): Retrofit =
    Retrofit.Builder()
      .baseUrl("http://10.0.2.2:5000/")
      .client(client)
      .addConverterFactory(GsonConverterFactory.create())
      .build()

  @Provides @Singleton
  fun provideVacantesApi(retrofit: Retrofit): VacantesApi =
    retrofit.create(VacantesApi::class.java)
  
  // ← Aquí, el binding que faltaba:
  @Provides @Singleton
  fun provideLoginClient(retrofit: Retrofit): LoginClient =
    retrofit.create(LoginClient::class.java)
}
