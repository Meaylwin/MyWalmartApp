package com.mywalmartapp.di.module

import com.mywalmartapp.data.datasource.CategoryApiService
import com.mywalmartapp.data.datasource.ProductApiService
import com.mywalmartapp.di.common.Constants.Companion.BASE_URL_CATEGORIES
import com.mywalmartapp.di.common.Constants.Companion.BASE_URL_PRODUCTS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideProductApiService(): ProductApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_PRODUCTS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryApiService(): CategoryApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_CATEGORIES)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CategoryApiService::class.java)
    }
}