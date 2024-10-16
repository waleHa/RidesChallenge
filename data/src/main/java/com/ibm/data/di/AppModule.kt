package com.ibm.data.di

import com.ibm.data.remote.VehicleApi
import com.ibm.data.repository.VehicleRepositoryImpl
import com.ibm.domain.repository.VehicleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideVehicleApi(): VehicleApi {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder()
            .baseUrl("https://random-data-api.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VehicleApi::class.java)
    }

    @Provides
    fun provideVehicleRepository(api: VehicleApi): VehicleRepository {
        return VehicleRepositoryImpl(api)
    }
}

