package com.example.myapplication.data.di

import com.example.myapplication.data.api.Constants
import com.example.myapplication.data.api.Constants.BASE_URL
import com.example.myapplication.data.api.TheMovieDBInterface
import com.example.myapplication.data.pojo.MovieDetails
import com.example.myapplication.data.pojo.MovieResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
/**
 * the algorithm is that in MovieDataSource the class MovieRepository is injected to found here under annotation inject of class
 * MovieRepository
 * then the compiler will found that we need to inject another variable that is theMovieDBInterface
 * will go to appModule and will find it but will find that we need to inject another thing that is retrofit to use it in theMovieDBInterface
 * will inject it and then go back to initialize the theMovieDBInterface
 * then will go back to use the instance of theMovieDBInterface to call the method getPopularMovieList
 * that returns the theMovieDBInterface.getPopularMovie(key) that is why theMovieDBInterface needed to be injected
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideRetrofitClient():Retrofit{
        val interceptor =
            Interceptor { chain ->
            val url =
                chain.request().url.newBuilder().addQueryParameter("api_key",
                    Constants.API_KEY
                ).build()
            val request = chain.request().newBuilder().url(url).build()
            return@Interceptor chain.proceed(request)}
        val okHTTPClient = OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(60, TimeUnit.SECONDS).build()
        return Retrofit.Builder().baseUrl(BASE_URL).client(okHTTPClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }
    @Singleton
    @Provides
    fun provideRetrofitInterface(retrofit: Retrofit):TheMovieDBInterface{
        return retrofit.create(TheMovieDBInterface::class.java)
    }
    @Singleton
    @Provides
    fun provideCompositeDisposable():CompositeDisposable{
        return CompositeDisposable()
    }
}

class MovieRepository
@Inject
constructor(private val theMovieDBInterface: TheMovieDBInterface)
{
    fun getPopularMovieList(key:Int): Single<MovieResponse> {
        return theMovieDBInterface.getPopularMovie(key)
    }
    fun getTopRatedMovie(key:Int): Single<MovieResponse> {
        return theMovieDBInterface.getTopRatedMovies(key)
    }
    fun getUpComingMovie(key:Int): Single<MovieResponse> {
        return theMovieDBInterface.getUpcomingMovie(key)
    }
    fun getMovieDetails(key:Int): Single<MovieDetails> {
        return theMovieDBInterface.getMovieDetails(key)
    }
}