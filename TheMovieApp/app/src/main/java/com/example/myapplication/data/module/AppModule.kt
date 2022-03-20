package com.example.myapplication.data.module

import com.example.myapplication.data.api.TheMovieDBClient
import com.example.myapplication.data.api.TheMovieDBInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideRetrofitClient():Retrofit{
        val interceptor = Interceptor { chain ->
            val url =
                chain.request().url.newBuilder().addQueryParameter("api_key",
                    TheMovieDBClient.API_KEY
                ).build()
            val request = chain.request().newBuilder().url(url).build()
            return@Interceptor chain.proceed(request)}
        val okHTTPClient = OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(60, TimeUnit.SECONDS).build()
        return Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/").client(okHTTPClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
    @Singleton
    @Provides
    fun provideRetrofitInterface(retrofit: Retrofit):TheMovieDBInterface{
        return retrofit.create(TheMovieDBInterface::class.java)
    }
}