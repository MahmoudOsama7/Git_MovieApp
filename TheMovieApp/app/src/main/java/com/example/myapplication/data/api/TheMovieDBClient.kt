package com.example.myapplication.data.api
import com.example.myapplication.data.pojo.MovieDetails
import com.example.myapplication.data.pojo.MovieResponse
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TheMovieDBClient {
    companion object
    {
        const val FIRST_PAGE=1
        const val POSTS_PER_PAGE=20
        private const val API_KEY = "ecbbdd15ebc92cd950aa05bcd6872e17"
        private const val BASE_URL = "https://api.themoviedb.org/3/"
        const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342/"
//this is the base_url to get any poster link
//to use it for ex to get spider man poster : https://image.tmdb.org/t/p/w342/nogV4th2P5QWYvQIMiWHj4CFLU9.jpg
//creating a object TheMovieDBClient which contain a method that generates the retrofit api we will use
//however we'll build an interceptor by using okHTTP library as to be the client used by the retrofit api


        private val theMovieDBInterface: TheMovieDBInterface
        private val theMovieDBInterface1: TheMovieDBInterface
        private val retrofit: Retrofit
        private val retrofit1:Retrofit
        private val interceptor: Interceptor
        private val okHTTPClient: OkHttpClient

        init {

            //creating OKHTTP Interceptor
            interceptor = Interceptor { chain ->
                val url =
                    chain.request().url().newBuilder().addQueryParameter("api_key", API_KEY).build()
                val request = chain.request().newBuilder().url(url).build()
                return@Interceptor chain.proceed(request)
            }

            //initializing okHTTPClient object
            okHTTPClient = OkHttpClient.Builder().addInterceptor(interceptor).connectTimeout(60, TimeUnit.SECONDS).build()

            //initializing the retrofit object
            retrofit = Retrofit.Builder().client(okHTTPClient).baseUrl(BASE_URL).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build()

            //initializing the Retrofit API interface
            theMovieDBInterface = retrofit.create(TheMovieDBInterface::class.java)

            //initializing the retrofit object
            retrofit1 = Retrofit.Builder().client(okHTTPClient).baseUrl(BASE_URL).addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build()

            //initializing the Retrofit API interface
            theMovieDBInterface1 = retrofit1.create(TheMovieDBInterface::class.java)
        }

        fun getMovieDetails(movieId: Int): Single<MovieDetails> {
            return theMovieDBInterface.getMovieDetails(movieId)
        }
        fun getPopularMovieList(page:Int):Single<MovieResponse>
        {
            return theMovieDBInterface1.getPopularMovie(page)
        }
        fun getUpcomingMovieList(page:Int):Single<MovieResponse>
        {
            return theMovieDBInterface1.getUpcomingMovie(page)
        }
        fun getTopRatedMovieList(page:Int):Single<MovieResponse>
        {
            return theMovieDBInterface1.getTopRatedMovies(page)
        }
    }
}